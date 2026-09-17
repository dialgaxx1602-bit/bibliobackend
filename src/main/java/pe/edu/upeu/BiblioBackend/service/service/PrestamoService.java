package pe.edu.upeu.BiblioBackend.service.service;

import pe.edu.upeu.BiblioBackend.dto.PrestamoRequestDTO;
import pe.edu.upeu.BiblioBackend.dto.PrestamoResponseDTO;
import pe.edu.upeu.BiblioBackend.enums.EstadoPrestamo;

import java.time.LocalDateTime;
import java.util.List;

public interface PrestamoService {
    PrestamoResponseDTO registrar(PrestamoRequestDTO requestDTO);
    PrestamoResponseDTO devolver(Long id);
    PrestamoResponseDTO anular(Long id);
    List<PrestamoResponseDTO> listarTodos();
    PrestamoResponseDTO buscarPorId(Long id);
    List<PrestamoResponseDTO> buscar(Long socioId, EstadoPrestamo estado, LocalDateTime desde, LocalDateTime hasta, String ordenarPor, String direccion);
}
