package pe.edu.upeu.BiblioBackend.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.BiblioBackend.dto.reporte.LibroMasPrestadoDTO;
import pe.edu.upeu.BiblioBackend.dto.reporte.PrestamoPorGeneroDTO;
import pe.edu.upeu.BiblioBackend.repository.PrestamoRepository;
import pe.edu.upeu.BiblioBackend.service.service.ReporteService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReporteServiceImpl implements ReporteService {

    private static final Logger log = LoggerFactory.getLogger(ReporteServiceImpl.class);

    private final PrestamoRepository prestamoRepository;

    public ReporteServiceImpl(PrestamoRepository prestamoRepository) {
        this.prestamoRepository = prestamoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoPorGeneroDTO> prestamosPorGenero(LocalDate desde, LocalDate hasta) {
        long start = System.currentTimeMillis();
        log.info("Iniciando reporte prestamosPorGenero");

        LocalDateTime fechaDesde = (desde != null) ? desde.atStartOfDay() : null;
        LocalDateTime fechaHasta = (hasta != null) ? hasta.atTime(LocalTime.MAX) : null;

        List<PrestamoPorGeneroDTO> reporte = prestamoRepository.reportePrestamosPorGenero(fechaDesde, fechaHasta);
        
        log.info("Fin reporte prestamosPorGenero. Filas: {}, Tiempo: {} ms", reporte.size(), (System.currentTimeMillis() - start));
        return reporte;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LibroMasPrestadoDTO> librosMasPrestados(LocalDate desde, LocalDate hasta) {
        long start = System.currentTimeMillis();
        log.info("Iniciando reporte librosMasPrestados");

        LocalDateTime fechaDesde = (desde != null) ? desde.atStartOfDay() : null;
        LocalDateTime fechaHasta = (hasta != null) ? hasta.atTime(LocalTime.MAX) : null;

        List<LibroMasPrestadoDTO> reporte = prestamoRepository.reporteLibrosMasPrestados(fechaDesde, fechaHasta);
        
        log.info("Fin reporte librosMasPrestados. Filas: {}, Tiempo: {} ms", reporte.size(), (System.currentTimeMillis() - start));
        return reporte;
    }
}
