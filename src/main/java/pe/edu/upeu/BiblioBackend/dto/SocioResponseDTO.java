package pe.edu.upeu.BiblioBackend.dto;

public record SocioResponseDTO(
        Long id,
        String dni,
        String nombres,
        String apellidos,
        String email,
        String telefono,
        String direccion,
        Boolean estado
) {}
