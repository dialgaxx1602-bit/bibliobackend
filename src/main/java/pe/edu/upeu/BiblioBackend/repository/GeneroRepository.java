package pe.edu.upeu.BiblioBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.BiblioBackend.entity.Genero;

public interface GeneroRepository extends JpaRepository<Genero, Long> {
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);
    boolean existsByNombreIgnoreCase(String nombre);
}
