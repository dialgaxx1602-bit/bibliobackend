package pe.edu.upeu.BiblioBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.BiblioBackend.entity.Socio;

public interface SocioRepository extends JpaRepository<Socio, Long> {
    boolean existsByDniAndIdNot(String dni, Long id);
    boolean existsByDni(String dni);
}
