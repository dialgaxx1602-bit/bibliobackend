package pe.edu.upeu.BiblioBackend.dto;

import java.math.BigDecimal;

public record LibroResponseDTO(
        Long id,
        String titulo,
        String autor,
        String isbn,
        BigDecimal costoReposicion,
        Integer stock,
        Boolean estado,
        Long generoId,
        String generoNombre
) {}
