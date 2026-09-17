package pe.edu.upeu.BiblioBackend.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.BiblioBackend.dto.LibroRequestDTO;
import pe.edu.upeu.BiblioBackend.dto.LibroResponseDTO;
import pe.edu.upeu.BiblioBackend.entity.Genero;
import pe.edu.upeu.BiblioBackend.entity.Libro;
import pe.edu.upeu.BiblioBackend.exception.RecursoNoEncontradoException;
import pe.edu.upeu.BiblioBackend.exception.ReglaNegocioException;
import pe.edu.upeu.BiblioBackend.repository.DetallePrestamoRepository;
import pe.edu.upeu.BiblioBackend.repository.GeneroRepository;
import pe.edu.upeu.BiblioBackend.repository.LibroRepository;
import pe.edu.upeu.BiblioBackend.service.service.LibroService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibroServiceImpl implements LibroService {

    private static final Logger log = LoggerFactory.getLogger(LibroServiceImpl.class);

    private final LibroRepository libroRepository;
    private final GeneroRepository generoRepository;
    private final DetallePrestamoRepository detallePrestamoRepository;

    public LibroServiceImpl(LibroRepository libroRepository, GeneroRepository generoRepository, DetallePrestamoRepository detallePrestamoRepository) {
        this.libroRepository = libroRepository;
        this.generoRepository = generoRepository;
        this.detallePrestamoRepository = detallePrestamoRepository;
    }

    @Override
    @Transactional
    public LibroResponseDTO create(LibroRequestDTO requestDTO) {
        long start = System.currentTimeMillis();
        log.info("Iniciando create Libro: {}", requestDTO.titulo());

        if (libroRepository.existsByIsbn(requestDTO.isbn())) {
            throw new ReglaNegocioException("El ISBN del libro no puede repetirse");
        }

        Genero genero = generoRepository.findById(requestDTO.generoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Genero no encontrado con id: " + requestDTO.generoId()));

        Libro libro = new Libro();
        libro.setTitulo(requestDTO.titulo());
        libro.setAutor(requestDTO.autor());
        libro.setIsbn(requestDTO.isbn());
        libro.setCostoReposicion(requestDTO.costoReposicion());
        libro.setStock(requestDTO.stock());
        libro.setEstado(requestDTO.estado());
        libro.setGenero(genero);

        libro = libroRepository.save(libro);

        log.info("Fin create Libro. Id: {}, Tiempo: {} ms", libro.getId(), (System.currentTimeMillis() - start));
        return mapToResponseDTO(libro);
    }

    @Override
    @Transactional(readOnly = true)
    public LibroResponseDTO read(Long id) {
        long start = System.currentTimeMillis();
        log.info("Iniciando read Libro por Id: {}", id);

        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Libro no encontrado con id: " + id));

        log.info("Fin read Libro. Id: {}, Tiempo: {} ms", libro.getId(), (System.currentTimeMillis() - start));
        return mapToResponseDTO(libro);
    }

    @Override
    @Transactional
    public LibroResponseDTO update(Long id, LibroRequestDTO requestDTO) {
        long start = System.currentTimeMillis();
        log.info("Iniciando update Libro por Id: {}", id);

        if (libroRepository.existsByIsbnAndIdNot(requestDTO.isbn(), id)) {
            throw new ReglaNegocioException("El ISBN del libro no puede repetirse");
        }

        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Libro no encontrado con id: " + id));

        Genero genero = generoRepository.findById(requestDTO.generoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Genero no encontrado con id: " + requestDTO.generoId()));

        libro.setTitulo(requestDTO.titulo());
        libro.setAutor(requestDTO.autor());
        libro.setIsbn(requestDTO.isbn());
        libro.setCostoReposicion(requestDTO.costoReposicion());
        libro.setStock(requestDTO.stock());
        libro.setEstado(requestDTO.estado());
        libro.setGenero(genero);

        libro = libroRepository.save(libro);

        log.info("Fin update Libro. Id: {}, Tiempo: {} ms", libro.getId(), (System.currentTimeMillis() - start));
        return mapToResponseDTO(libro);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        long start = System.currentTimeMillis();
        log.info("Iniciando delete Libro por Id: {}", id);

        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Libro no encontrado con id: " + id));

        if (detallePrestamoRepository.existsByLibroId(id)) {
            throw new ReglaNegocioException("No se permite eliminar un libro que tenga detalles de prestamo asociados");
        }

        libroRepository.delete(libro);
        log.info("Fin delete Libro. Id: {}, Tiempo: {} ms", id, (System.currentTimeMillis() - start));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LibroResponseDTO> readAll() {
        long start = System.currentTimeMillis();
        log.info("Iniciando readAll Libros");

        List<Libro> libros = libroRepository.findAll();
        List<LibroResponseDTO> response = libros.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

        log.info("Fin readAll Libros. Cantidad: {}, Tiempo: {} ms", response.size(), (System.currentTimeMillis() - start));
        return response;
    }

    private LibroResponseDTO mapToResponseDTO(Libro libro) {
        return new LibroResponseDTO(
                libro.getId(),
                libro.getTitulo(),
                libro.getAutor(),
                libro.getIsbn(),
                libro.getCostoReposicion(),
                libro.getStock(),
                libro.getEstado(),
                libro.getGenero().getId(),
                libro.getGenero().getNombre()
        );
    }
}
