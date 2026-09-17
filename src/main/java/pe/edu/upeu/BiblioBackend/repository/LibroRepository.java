package pe.edu.upeu.BiblioBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.BiblioBackend.entity.Libro;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    boolean existsByIsbnAndIdNot(String isbn, Long id);
    boolean existsByIsbn(String isbn);
    boolean existsByGeneroId(Long generoId);
}
