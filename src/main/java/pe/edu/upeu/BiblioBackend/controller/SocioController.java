package pe.edu.upeu.BiblioBackend.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.BiblioBackend.dto.SocioRequestDTO;
import pe.edu.upeu.BiblioBackend.dto.SocioResponseDTO;
import pe.edu.upeu.BiblioBackend.service.service.SocioService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/socios")
public class SocioController {

    private final SocioService socioService;

    public SocioController(SocioService socioService) {
        this.socioService = socioService;
    }

    @GetMapping
    public ResponseEntity<List<SocioResponseDTO>> listarSocios() {
        return ResponseEntity.ok(socioService.readAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SocioResponseDTO> obtenerSocio(@PathVariable Long id) {
        return ResponseEntity.ok(socioService.read(id));
    }

    @PostMapping
    public ResponseEntity<SocioResponseDTO> registrarSocio(@Valid @RequestBody SocioRequestDTO requestDTO) {
        return new ResponseEntity<>(socioService.create(requestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SocioResponseDTO> actualizarSocio(@PathVariable Long id, @Valid @RequestBody SocioRequestDTO requestDTO) {
        return ResponseEntity.ok(socioService.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSocio(@PathVariable Long id) {
        socioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
