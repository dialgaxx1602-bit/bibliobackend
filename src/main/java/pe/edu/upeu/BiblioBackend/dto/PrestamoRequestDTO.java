package pe.edu.upeu.BiblioBackend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record PrestamoRequestDTO(
        @NotNull(message = "El socioId es obligatorio")
        @Positive(message = "El socioId debe ser positivo")
        Long socioId,

        @NotEmpty(message = "El prestamo debe contener al menos un detalle")
        @Valid
        List<DetallePrestamoRequestDTO> detalles
) {}
