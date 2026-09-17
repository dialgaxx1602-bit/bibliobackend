package pe.edu.upeu.BiblioBackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SocioRequestDTO(
        @NotBlank(message = "El DNI es obligatorio")
        @Pattern(regexp = "^\\d{8}$", message = "El DNI debe tener 8 digitos")
        String dni,

        @NotBlank(message = "Los nombres son obligatorios")
        @Size(min = 2, max = 100, message = "Los nombres deben tener entre 2 y 100 caracteres")
        String nombres,

        @NotBlank(message = "Los apellidos son obligatorios")
        @Size(min = 2, max = 100, message = "Los apellidos deben tener entre 2 y 100 caracteres")
        String apellidos,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email debe tener un formato valido")
        @Size(max = 150, message = "El email no puede superar los 150 caracteres")
        String email,

        @Pattern(regexp = "^\\d{9}$", message = "El telefono debe tener 9 digitos")
        String telefono,

        @Size(max = 250, message = "La direccion no puede superar los 250 caracteres")
        String direccion,

        @NotNull(message = "El estado es obligatorio")
        Boolean estado
) {}
