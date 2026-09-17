package pe.edu.upeu.BiblioBackend.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.BiblioBackend.dto.GeneroRequestDTO;
import pe.edu.upeu.BiblioBackend.dto.GeneroResponseDTO;
import pe.edu.upeu.BiblioBackend.service.service.GeneroService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/generos")
public class GeneroController {

    private final GeneroService generoService;

    public GeneroController(GeneroService generoService) {
        this.generoService = generoService;
    }

    @GetMapping
    public ResponseEntity<List<GeneroResponseDTO>> listarGeneros() {
        return ResponseEntity.ok(generoService.readAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneroResponseDTO> obtenerGenero(@PathVariable Long id) {
        return ResponseEntity.ok(generoService.read(id));
    }

    @PostMapping
    public ResponseEntity<GeneroResponseDTO> registrarGenero(@Valid @RequestBody GeneroRequestDTO requestDTO) {
        return new ResponseEntity<>(generoService.create(requestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneroResponseDTO> actualizarGenero(@PathVariable Long id, @Valid @RequestBody GeneroRequestDTO requestDTO) {
        return ResponseEntity.ok(generoService.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarGenero(@PathVariable Long id) {
        generoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
