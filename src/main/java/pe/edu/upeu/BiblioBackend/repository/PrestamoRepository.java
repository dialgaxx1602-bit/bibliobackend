package pe.edu.upeu.BiblioBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upeu.BiblioBackend.dto.reporte.LibroMasPrestadoDTO;
import pe.edu.upeu.BiblioBackend.dto.reporte.PrestamoPorGeneroDTO;
import pe.edu.upeu.BiblioBackend.entity.Prestamo;
import pe.edu.upeu.BiblioBackend.enums.EstadoPrestamo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    @Query("SELECT p FROM Prestamo p LEFT JOIN FETCH p.socio LEFT JOIN FETCH p.detalles d LEFT JOIN FETCH d.libro WHERE " +
           "(:socioId IS NULL OR p.socio.id = :socioId) AND " +
           "(:estado IS NULL OR p.estado = :estado) AND " +
           "(cast(:desde as timestamp) IS NULL OR p.fecha >= :desde) AND " +
           "(cast(:hasta as timestamp) IS NULL OR p.fecha <= :hasta)")
    List<Prestamo> buscarPrestamos(
            @Param("socioId") Long socioId,
            @Param("estado") EstadoPrestamo estado,
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta,
            org.springframework.data.domain.Sort sort
    );

    @Query("SELECT new pe.edu.upeu.BiblioBackend.dto.reporte.PrestamoPorGeneroDTO( " +
           "g.id, g.nombre, SUM(d.cantidad), SUM(d.subtotal)) " +
           "FROM Prestamo p JOIN p.detalles d JOIN d.libro l JOIN l.genero g " +
           "WHERE p.estado IN ('REGISTRADO', 'DEVUELTO') AND " +
           "(cast(:desde as timestamp) IS NULL OR p.fecha >= :desde) AND " +
           "(cast(:hasta as timestamp) IS NULL OR p.fecha <= :hasta) " +
           "GROUP BY g.id, g.nombre " +
           "ORDER BY SUM(d.cantidad) DESC")
    List<PrestamoPorGeneroDTO> reportePrestamosPorGenero(
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta
    );

    @Query("SELECT new pe.edu.upeu.BiblioBackend.dto.reporte.LibroMasPrestadoDTO( " +
           "l.id, l.titulo, g.nombre, SUM(d.cantidad), SUM(d.subtotal)) " +
           "FROM Prestamo p JOIN p.detalles d JOIN d.libro l JOIN l.genero g " +
           "WHERE p.estado IN ('REGISTRADO', 'DEVUELTO') AND " +
           "(cast(:desde as timestamp) IS NULL OR p.fecha >= :desde) AND " +
           "(cast(:hasta as timestamp) IS NULL OR p.fecha <= :hasta) " +
           "GROUP BY l.id, l.titulo, g.nombre " +
           "ORDER BY SUM(d.cantidad) DESC")
    List<LibroMasPrestadoDTO> reporteLibrosMasPrestados(
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta
    );
}
