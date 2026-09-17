package pe.edu.upeu.BiblioBackend.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.BiblioBackend.dto.PrestamoRequestDTO;
import pe.edu.upeu.BiblioBackend.dto.PrestamoResponseDTO;
import pe.edu.upeu.BiblioBackend.enums.EstadoPrestamo;
import pe.edu.upeu.BiblioBackend.service.service.PrestamoService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @GetMapping
    public ResponseEntity<List<PrestamoResponseDTO>> listarPrestamos() {
        return ResponseEntity.ok(prestamoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrestamoResponseDTO> obtenerPrestamo(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<PrestamoResponseDTO> registrarPrestamo(@Valid @RequestBody PrestamoRequestDTO requestDTO) {
        return new ResponseEntity<>(prestamoService.registrar(requestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/devolucion")
    public ResponseEntity<PrestamoResponseDTO> devolverPrestamo(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.devolver(id));
    }

    @PutMapping("/{id}/anulacion")
    public ResponseEntity<PrestamoResponseDTO> anularPrestamo(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.anular(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<PrestamoResponseDTO>> buscarPrestamos(
            @RequestParam(required = false) Long socioId,
            @RequestParam(required = false) EstadoPrestamo estado,
            @RequestParam(required = false) LocalDateTime desde,
            @RequestParam(required = false) LocalDateTime hasta,
            @RequestParam(required = false) String ordenarPor,
            @RequestParam(required = false) String direccion
    ) {
        return ResponseEntity.ok(prestamoService.buscar(socioId, estado, desde, hasta, ordenarPor, direccion));
    }
}
