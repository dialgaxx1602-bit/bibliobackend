package pe.edu.upeu.BiblioBackend.enums;

public enum EstadoPrestamo {
    REGISTRADO, // prestamo vigente, los ejemplares estan fuera de la biblioteca
    DEVUELTO,   // el socio devolvio los ejemplares
    ANULADO     // prestamo cancelado por error de registro
}
