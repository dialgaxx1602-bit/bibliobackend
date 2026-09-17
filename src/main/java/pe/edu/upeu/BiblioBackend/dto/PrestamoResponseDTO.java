package pe.edu.upeu.BiblioBackend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record PrestamoResponseDTO(
        Long id,
        LocalDateTime fecha,
        LocalDate fechaDevolucionPrevista,
        LocalDate fechaDevolucionReal,
        String estado,
        Long socioId,
        String socioNombre,
        BigDecimal totalValorizado,
        List<DetallePrestamoResponseDTO> detalles
) {}
