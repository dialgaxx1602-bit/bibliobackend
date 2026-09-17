package pe.edu.upeu.BiblioBackend.exception.dto;

import java.util.Map;

public record ErrorResponseDTO(
        String timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> validationErrors
) {}
