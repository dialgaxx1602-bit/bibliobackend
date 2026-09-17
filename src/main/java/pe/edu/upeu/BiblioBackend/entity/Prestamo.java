package pe.edu.upeu.BiblioBackend.entity;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.upeu.BiblioBackend.enums.EstadoPrestamo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prestamos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    private LocalDate fechaDevolucionPrevista;

    private LocalDate fechaDevolucionReal;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalValorizado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPrestamo estado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "socio_id")
    private Socio socio;

    @OneToMany(mappedBy = "prestamo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePrestamo> detalles = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.fecha == null) {
            this.fecha = LocalDateTime.now();
        }
    }

    public void agregarDetalle(DetallePrestamo d) {
        detalles.add(d);
        d.setPrestamo(this);
    }
}
