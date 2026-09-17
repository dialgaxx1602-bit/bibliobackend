package pe.edu.upeu.BiblioBackend.dto;

public record GeneroResponseDTO(
        Long id,
        String nombre,
        String descripcion,
        Boolean estado
) {}
