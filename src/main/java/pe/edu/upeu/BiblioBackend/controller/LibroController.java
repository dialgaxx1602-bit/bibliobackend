package pe.edu.upeu.BiblioBackend.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.BiblioBackend.dto.LibroRequestDTO;
import pe.edu.upeu.BiblioBackend.dto.LibroResponseDTO;
import pe.edu.upeu.BiblioBackend.service.service.LibroService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/libros")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping
    public ResponseEntity<List<LibroResponseDTO>> listarLibros() {
        return ResponseEntity.ok(libroService.readAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> obtenerLibro(@PathVariable Long id) {
        return ResponseEntity.ok(libroService.read(id));
    }

    @PostMapping
    public ResponseEntity<LibroResponseDTO> registrarLibro(@Valid @RequestBody LibroRequestDTO requestDTO) {
        return new ResponseEntity<>(libroService.create(requestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> actualizarLibro(@PathVariable Long id, @Valid @RequestBody LibroRequestDTO requestDTO) {
        return ResponseEntity.ok(libroService.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLibro(@PathVariable Long id) {
        libroService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
