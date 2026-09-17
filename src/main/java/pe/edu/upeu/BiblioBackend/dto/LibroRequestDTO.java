package pe.edu.upeu.BiblioBackend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record LibroRequestDTO(
        @NotBlank(message = "El titulo es obligatorio")
        @Size(min = 3, max = 150, message = "El titulo debe tener entre 3 y 150 caracteres")
        String titulo,

        @NotBlank(message = "El autor es obligatorio")
        @Size(min = 3, max = 120, message = "El autor debe tener entre 3 y 120 caracteres")
        String autor,

        @NotBlank(message = "El ISBN es obligatorio")
        @Pattern(regexp = "^(\\d{10}|\\d{13})$", message = "El ISBN debe tener 10 o 13 digitos")
        String isbn,

        @NotNull(message = "El costo de reposicion es obligatorio")
        @DecimalMin(value = "0.01", message = "El costo de reposicion debe ser mayor o igual a 0.01")
        BigDecimal costoReposicion,

        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock no puede ser negativo")
        Integer stock,

        @NotNull(message = "El estado es obligatorio")
        Boolean estado,

        @NotNull(message = "El genero es obligatorio")
        @Positive(message = "El ID del genero debe ser positivo")
        Long generoId
) {}
