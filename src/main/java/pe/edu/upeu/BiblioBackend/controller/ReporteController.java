package pe.edu.upeu.BiblioBackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upeu.BiblioBackend.dto.reporte.LibroMasPrestadoDTO;
import pe.edu.upeu.BiblioBackend.dto.reporte.PrestamoPorGeneroDTO;
import pe.edu.upeu.BiblioBackend.service.service.ReporteService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/prestamos-por-genero")
    public ResponseEntity<List<PrestamoPorGeneroDTO>> reportePrestamosPorGenero(
            @RequestParam(required = false) LocalDate desde,
            @RequestParam(required = false) LocalDate hasta
    ) {
        return ResponseEntity.ok(reporteService.prestamosPorGenero(desde, hasta));
    }

    @GetMapping("/libros-mas-prestados")
    public ResponseEntity<List<LibroMasPrestadoDTO>> reporteLibrosMasPrestados(
            @RequestParam(required = false) LocalDate desde,
            @RequestParam(required = false) LocalDate hasta
    ) {
        return ResponseEntity.ok(reporteService.librosMasPrestados(desde, hasta));
    }
}
