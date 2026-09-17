package pe.edu.upeu.BiblioBackend.dto;

import java.math.BigDecimal;

public record DetallePrestamoResponseDTO(
        Long libroId,
        String titulo,
        Integer cantidad,
        BigDecimal costoUnitario,
        BigDecimal subtotal
) {}
