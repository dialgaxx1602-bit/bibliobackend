package pe.edu.upeu.BiblioBackend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DetallePrestamoRequestDTO(
        @NotNull(message = "El libroId es obligatorio")
        @Positive(message = "El libroId debe ser positivo")
        Long libroId,

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        Integer cantidad
) {}
