package pe.edu.upeu.BiblioBackend.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.BiblioBackend.dto.SocioRequestDTO;
import pe.edu.upeu.BiblioBackend.dto.SocioResponseDTO;
import pe.edu.upeu.BiblioBackend.entity.Socio;
import pe.edu.upeu.BiblioBackend.exception.RecursoNoEncontradoException;
import pe.edu.upeu.BiblioBackend.exception.ReglaNegocioException;
import pe.edu.upeu.BiblioBackend.repository.SocioRepository;
import pe.edu.upeu.BiblioBackend.service.service.SocioService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SocioServiceImpl implements SocioService {

    private static final Logger log = LoggerFactory.getLogger(SocioServiceImpl.class);

    private final SocioRepository socioRepository;

    public SocioServiceImpl(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    @Override
    @Transactional
    public SocioResponseDTO create(SocioRequestDTO requestDTO) {
        long start = System.currentTimeMillis();
        log.info("Iniciando create Socio con DNI: {}", requestDTO.dni());

        if (socioRepository.existsByDni(requestDTO.dni())) {
            throw new ReglaNegocioException("El DNI del socio no puede repetirse");
        }

        Socio socio = new Socio();
        socio.setDni(requestDTO.dni());
        socio.setNombres(requestDTO.nombres());
        socio.setApellidos(requestDTO.apellidos());
        socio.setEmail(requestDTO.email());
        socio.setTelefono(requestDTO.telefono());
        socio.setDireccion(requestDTO.direccion());
        socio.setEstado(requestDTO.estado());

        socio = socioRepository.save(socio);

        log.info("Fin create Socio. Id: {}, Tiempo: {} ms", socio.getId(), (System.currentTimeMillis() - start));
        return mapToResponseDTO(socio);
    }

    @Override
    @Transactional(readOnly = true)
    public SocioResponseDTO read(Long id) {
        long start = System.currentTimeMillis();
        log.info("Iniciando read Socio por Id: {}", id);

        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Socio no encontrado con id: " + id));

        log.info("Fin read Socio. Id: {}, Tiempo: {} ms", socio.getId(), (System.currentTimeMillis() - start));
        return mapToResponseDTO(socio);
    }

    @Override
    @Transactional
    public SocioResponseDTO update(Long id, SocioRequestDTO requestDTO) {
        long start = System.currentTimeMillis();
        log.info("Iniciando update Socio por Id: {}", id);

        if (socioRepository.existsByDniAndIdNot(requestDTO.dni(), id)) {
            throw new ReglaNegocioException("El DNI del socio no puede repetirse");
        }

        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Socio no encontrado con id: " + id));

        socio.setDni(requestDTO.dni());
        socio.setNombres(requestDTO.nombres());
        socio.setApellidos(requestDTO.apellidos());
        socio.setEmail(requestDTO.email());
        socio.setTelefono(requestDTO.telefono());
        socio.setDireccion(requestDTO.direccion());
        socio.setEstado(requestDTO.estado());

        socio = socioRepository.save(socio);

        log.info("Fin update Socio. Id: {}, Tiempo: {} ms", socio.getId(), (System.currentTimeMillis() - start));
        return mapToResponseDTO(socio);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        long start = System.currentTimeMillis();
        log.info("Iniciando delete Socio por Id: {}", id);

        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Socio no encontrado con id: " + id));

        socioRepository.delete(socio);
        
        log.info("Fin delete Socio. Id: {}, Tiempo: {} ms", id, (System.currentTimeMillis() - start));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SocioResponseDTO> readAll() {
        long start = System.currentTimeMillis();
        log.info("Iniciando readAll Socios");

        List<Socio> socios = socioRepository.findAll();
        List<SocioResponseDTO> response = socios.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

        log.info("Fin readAll Socios. Cantidad: {}, Tiempo: {} ms", response.size(), (System.currentTimeMillis() - start));
        return response;
    }

    private SocioResponseDTO mapToResponseDTO(Socio socio) {
        return new SocioResponseDTO(
                socio.getId(),
                socio.getDni(),
                socio.getNombres(),
                socio.getApellidos(),
                socio.getEmail(),
                socio.getTelefono(),
                socio.getDireccion(),
                socio.getEstado()
        );
    }
}
