package pe.edu.upeu.BiblioBackend.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.BiblioBackend.dto.GeneroRequestDTO;
import pe.edu.upeu.BiblioBackend.dto.GeneroResponseDTO;
import pe.edu.upeu.BiblioBackend.entity.Genero;
import pe.edu.upeu.BiblioBackend.exception.RecursoNoEncontradoException;
import pe.edu.upeu.BiblioBackend.exception.ReglaNegocioException;
import pe.edu.upeu.BiblioBackend.repository.GeneroRepository;
import pe.edu.upeu.BiblioBackend.repository.LibroRepository;
import pe.edu.upeu.BiblioBackend.service.service.GeneroService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeneroServiceImpl implements GeneroService {

    private static final Logger log = LoggerFactory.getLogger(GeneroServiceImpl.class);

    private final GeneroRepository generoRepository;
    private final LibroRepository libroRepository;

    public GeneroServiceImpl(GeneroRepository generoRepository, LibroRepository libroRepository) {
        this.generoRepository = generoRepository;
        this.libroRepository = libroRepository;
    }

    @Override
    @Transactional
    public GeneroResponseDTO create(GeneroRequestDTO requestDTO) {
        long start = System.currentTimeMillis();
        log.info("Iniciando create Genero: {}", requestDTO.nombre());
        
        if (generoRepository.existsByNombreIgnoreCase(requestDTO.nombre())) {
            throw new ReglaNegocioException("El nombre del genero no puede repetirse");
        }

        Genero genero = new Genero();
        genero.setNombre(requestDTO.nombre());
        genero.setDescripcion(requestDTO.descripcion());
        genero.setEstado(requestDTO.estado());

        genero = generoRepository.save(genero);

        log.info("Fin create Genero. Id: {}, Tiempo: {} ms", genero.getId(), (System.currentTimeMillis() - start));
        return mapToResponseDTO(genero);
    }

    @Override
    @Transactional(readOnly = true)
    public GeneroResponseDTO read(Long id) {
        long start = System.currentTimeMillis();
        log.info("Iniciando read Genero por Id: {}", id);
        
        Genero genero = generoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Genero no encontrado con id: " + id));

        log.info("Fin read Genero. Id: {}, Tiempo: {} ms", genero.getId(), (System.currentTimeMillis() - start));
        return mapToResponseDTO(genero);
    }

    @Override
    @Transactional
    public GeneroResponseDTO update(Long id, GeneroRequestDTO requestDTO) {
        long start = System.currentTimeMillis();
        log.info("Iniciando update Genero por Id: {}", id);

        if (generoRepository.existsByNombreIgnoreCaseAndIdNot(requestDTO.nombre(), id)) {
            throw new ReglaNegocioException("El nombre del genero no puede repetirse");
        }

        Genero genero = generoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Genero no encontrado con id: " + id));

        genero.setNombre(requestDTO.nombre());
        genero.setDescripcion(requestDTO.descripcion());
        genero.setEstado(requestDTO.estado());

        genero = generoRepository.save(genero);

        log.info("Fin update Genero. Id: {}, Tiempo: {} ms", genero.getId(), (System.currentTimeMillis() - start));
        return mapToResponseDTO(genero);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        long start = System.currentTimeMillis();
        log.info("Iniciando delete Genero por Id: {}", id);

        Genero genero = generoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Genero no encontrado con id: " + id));
        
        if (libroRepository.existsByGeneroId(id)) {
            throw new ReglaNegocioException("No se permite eliminar un genero que tenga libros asociados");
        }

        generoRepository.delete(genero);
        log.info("Fin delete Genero. Id: {}, Tiempo: {} ms", id, (System.currentTimeMillis() - start));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GeneroResponseDTO> readAll() {
        long start = System.currentTimeMillis();
        log.info("Iniciando readAll Generos");

        List<Genero> generos = generoRepository.findAll();
        List<GeneroResponseDTO> response = generos.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

        log.info("Fin readAll Generos. Cantidad: {}, Tiempo: {} ms", response.size(), (System.currentTimeMillis() - start));
        return response;
    }

    private GeneroResponseDTO mapToResponseDTO(Genero genero) {
        return new GeneroResponseDTO(
                genero.getId(),
                genero.getNombre(),
                genero.getDescripcion(),
                genero.getEstado()
        );
    }
}
