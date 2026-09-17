package pe.edu.upeu.BiblioBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.BiblioBackend.entity.DetallePrestamo;

public interface DetallePrestamoRepository extends JpaRepository<DetallePrestamo, Long> {
    boolean existsByLibroId(Long libroId);
}
