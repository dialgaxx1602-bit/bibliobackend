package pe.edu.upeu.BiblioBackend.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.BiblioBackend.dto.DetallePrestamoRequestDTO;
import pe.edu.upeu.BiblioBackend.dto.DetallePrestamoResponseDTO;
import pe.edu.upeu.BiblioBackend.dto.PrestamoRequestDTO;
import pe.edu.upeu.BiblioBackend.dto.PrestamoResponseDTO;
import pe.edu.upeu.BiblioBackend.entity.DetallePrestamo;
import pe.edu.upeu.BiblioBackend.entity.Libro;
import pe.edu.upeu.BiblioBackend.entity.Prestamo;
import pe.edu.upeu.BiblioBackend.entity.Socio;
import pe.edu.upeu.BiblioBackend.enums.EstadoPrestamo;
import pe.edu.upeu.BiblioBackend.exception.RecursoNoEncontradoException;
import pe.edu.upeu.BiblioBackend.exception.ReglaNegocioException;
import pe.edu.upeu.BiblioBackend.repository.LibroRepository;
import pe.edu.upeu.BiblioBackend.repository.PrestamoRepository;
import pe.edu.upeu.BiblioBackend.repository.SocioRepository;
import pe.edu.upeu.BiblioBackend.service.service.PrestamoService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrestamoServiceImpl implements PrestamoService {

    private static final Logger log = LoggerFactory.getLogger(PrestamoServiceImpl.class);

    private final PrestamoRepository prestamoRepository;
    private final SocioRepository socioRepository;
    private final LibroRepository libroRepository;

    public PrestamoServiceImpl(PrestamoRepository prestamoRepository, SocioRepository socioRepository, LibroRepository libroRepository) {
        this.prestamoRepository = prestamoRepository;
        this.socioRepository = socioRepository;
        this.libroRepository = libroRepository;
    }

    @Override
    @Transactional
    public PrestamoResponseDTO registrar(PrestamoRequestDTO requestDTO) {
        long start = System.currentTimeMillis();
        log.info("Iniciando registrar Prestamo para socioId: {}", requestDTO.socioId());

        Socio socio = socioRepository.findById(requestDTO.socioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Socio no encontrado con id: " + requestDTO.socioId()));

        if (!Boolean.TRUE.equals(socio.getEstado())) {
            throw new ReglaNegocioException("El socio no esta activo o se encuentra suspendido");
        }

        Prestamo prestamo = new Prestamo();
        prestamo.setSocio(socio);
        prestamo.setEstado(EstadoPrestamo.REGISTRADO);
        prestamo.setFechaDevolucionPrevista(LocalDate.now().plusDays(7));
        
        BigDecimal totalValorizado = BigDecimal.ZERO;

        for (DetallePrestamoRequestDTO dDTO : requestDTO.detalles()) {
            Libro libro = libroRepository.findById(dDTO.libroId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Libro no encontrado con id: " + dDTO.libroId()));

            if (!Boolean.TRUE.equals(libro.getEstado())) {
                throw new ReglaNegocioException("El libro " + libro.getTitulo() + " no esta activo");
            }

            if (libro.getStock() < dDTO.cantidad()) {
                throw new ReglaNegocioException("Stock insuficiente para " + libro.getTitulo() + ". Disponible: " + libro.getStock() + ", solicitado: " + dDTO.cantidad());
            }

            libro.setStock(libro.getStock() - dDTO.cantidad());
            libroRepository.save(libro);

            DetallePrestamo detalle = new DetallePrestamo();
            detalle.setLibro(libro);
            detalle.setCantidad(dDTO.cantidad());
            detalle.setCostoUnitario(libro.getCostoReposicion());
            
            BigDecimal subtotal = libro.getCostoReposicion().multiply(BigDecimal.valueOf(dDTO.cantidad()));
            detalle.setSubtotal(subtotal);
            
            prestamo.agregarDetalle(detalle);
            totalValorizado = totalValorizado.add(subtotal);
        }

        prestamo.setTotalValorizado(totalValorizado);
        prestamo = prestamoRepository.save(prestamo);

        log.info("Fin registrar Prestamo. Id: {}, Tiempo: {} ms", prestamo.getId(), (System.currentTimeMillis() - start));
        return mapToResponseDTO(prestamo);
    }

    @Override
    @Transactional
    public PrestamoResponseDTO devolver(Long id) {
        long start = System.currentTimeMillis();
        log.info("Iniciando devolver Prestamo por Id: {}", id);

        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Prestamo no encontrado con id: " + id));

        if (prestamo.getEstado() != EstadoPrestamo.REGISTRADO) {
            throw new ReglaNegocioException("Solo se puede devolver un prestamo en estado REGISTRADO");
        }

        prestamo.setEstado(EstadoPrestamo.DEVUELTO);
        prestamo.setFechaDevolucionReal(LocalDate.now());

        for (DetallePrestamo detalle : prestamo.getDetalles()) {
            Libro libro = detalle.getLibro();
            libro.setStock(libro.getStock() + detalle.getCantidad());
            libroRepository.save(libro);
        }

        prestamo = prestamoRepository.save(prestamo);
        
        log.info("Fin devolver Prestamo. Id: {}, Tiempo: {} ms", prestamo.getId(), (System.currentTimeMillis() - start));
        return mapToResponseDTO(prestamo);
    }

    @Override
    @Transactional
    public PrestamoResponseDTO anular(Long id) {
        long start = System.currentTimeMillis();
        log.info("Iniciando anular Prestamo por Id: {}", id);

        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Prestamo no encontrado con id: " + id));

        if (prestamo.getEstado() != EstadoPrestamo.REGISTRADO) {
            throw new ReglaNegocioException("Solo se puede anular un prestamo en estado REGISTRADO");
        }

        prestamo.setEstado(EstadoPrestamo.ANULADO);

        for (DetallePrestamo detalle : prestamo.getDetalles()) {
            Libro libro = detalle.getLibro();
            libro.setStock(libro.getStock() + detalle.getCantidad());
            libroRepository.save(libro);
        }

        prestamo = prestamoRepository.save(prestamo);
        
        log.info("Fin anular Prestamo. Id: {}, Tiempo: {} ms", prestamo.getId(), (System.currentTimeMillis() - start));
        return mapToResponseDTO(prestamo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoResponseDTO> listarTodos() {
        long start = System.currentTimeMillis();
        log.info("Iniciando listarTodos Prestamos");

        List<Prestamo> prestamos = prestamoRepository.findAll();
        List<PrestamoResponseDTO> response = prestamos.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

        log.info("Fin listarTodos Prestamos. Cantidad: {}, Tiempo: {} ms", response.size(), (System.currentTimeMillis() - start));
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public PrestamoResponseDTO buscarPorId(Long id) {
        long start = System.currentTimeMillis();
        log.info("Iniciando buscarPorId Prestamo: {}", id);

        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Prestamo no encontrado con id: " + id));

        log.info("Fin buscarPorId Prestamo. Id: {}, Tiempo: {} ms", prestamo.getId(), (System.currentTimeMillis() - start));
        return mapToResponseDTO(prestamo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoResponseDTO> buscar(Long socioId, EstadoPrestamo estado, LocalDateTime desde, LocalDateTime hasta, String ordenarPor, String direccion) {
        long start = System.currentTimeMillis();
        log.info("Iniciando buscar Prestamos con filtros combinados");

        if (desde != null && hasta != null && desde.isAfter(hasta)) {
            throw new ReglaNegocioException("La fecha desde no puede ser posterior a la fecha hasta");
        }

        String sortProperty = (ordenarPor != null && !ordenarPor.isEmpty()) ? ordenarPor : "fecha";
        if (!List.of("id", "fecha", "totalValorizado", "estado").contains(sortProperty)) {
            throw new ReglaNegocioException("Campo de ordenamiento no permitido: " + sortProperty);
        }

        Sort.Direction sortDirection = (direccion != null && direccion.equalsIgnoreCase("asc")) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(sortDirection, sortProperty);

        List<Prestamo> prestamos = prestamoRepository.buscarPrestamos(socioId, estado, desde, hasta, sort);
        List<PrestamoResponseDTO> response = prestamos.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

        log.info("Fin buscar Prestamos. Cantidad: {}, Tiempo: {} ms", response.size(), (System.currentTimeMillis() - start));
        return response;
    }

    private PrestamoResponseDTO mapToResponseDTO(Prestamo prestamo) {
        return new PrestamoResponseDTO(
                prestamo.getId(),
                prestamo.getFecha(),
                prestamo.getFechaDevolucionPrevista(),
                prestamo.getFechaDevolucionReal(),
                prestamo.getEstado().name(),
                prestamo.getSocio().getId(),
                prestamo.getSocio().getNombres() + " " + prestamo.getSocio().getApellidos(),
                prestamo.getTotalValorizado(),
                prestamo.getDetalles().stream().map(this::mapToDetalleDTO).collect(Collectors.toList())
        );
    }

    private DetallePrestamoResponseDTO mapToDetalleDTO(DetallePrestamo detalle) {
        return new DetallePrestamoResponseDTO(
                detalle.getLibro().getId(),
                detalle.getLibro().getTitulo(),
                detalle.getCantidad(),
                detalle.getCostoUnitario(),
                detalle.getSubtotal()
        );
    }
}
