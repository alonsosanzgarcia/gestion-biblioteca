package biblioteca;

/**
 * Clase que representa un libro en el sistema de gestión de biblioteca.
 * Contiene información básica del libro como título, autor, ISBN y disponibilidad.
 *
 * @author Ana García (Componente 1)
 * @version 1.0
 * @since 2024-01-15
 */
public class Libro {

    /** Título del libro */
    private String titulo;

    /** Autor del libro */
    private String autor;

    /** Código ISBN único del libro */
    private String isbn;

    /** Número de copias disponibles en la biblioteca */
    private int copiasDisponibles;

    /** Número total de copias del libro */
    private int copiasTotal;

    /**
     * Constructor que inicializa un libro con todos sus atributos.
     *
     * @param titulo Título del libro (no puede ser null ni vacío)
     * @param autor Nombre del autor del libro
     * @param isbn Código ISBN del libro (formato estándar)
     * @param copiasTotal Número total de copias disponibles en la biblioteca
     * @throws IllegalArgumentException si el título o ISBN son null o vacíos,
     *         o si copiasTotal es negativo
     */
    public Libro(String titulo, String autor, String isbn, int copiasTotal) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede ser null o vacío.");
        }
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("El ISBN no puede ser null o vacío.");
        }
        if (copiasTotal < 0) {
            throw new IllegalArgumentException("El número de copias no puede ser negativo.");
        }
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.copiasTotal = copiasTotal;
        this.copiasDisponibles = copiasTotal;
    }

    /**
     * Obtiene el título del libro.
     *
     * @return El título del libro
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Obtiene el autor del libro.
     *
     * @return El nombre del autor
     */
    public String getAutor() {
        return autor;
    }

    /**
     * Obtiene el código ISBN del libro.
     *
     * @return El ISBN del libro
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Obtiene el número de copias disponibles para préstamo.
     *
     * @return Número de copias actualmente disponibles
     */
    public int getCopiasDisponibles() {
        return copiasDisponibles;
    }

    /**
     * Obtiene el número total de copias del libro.
     *
     * @return Número total de copias
     */
    public int getCopiasTotal() {
        return copiasTotal;
    }

    /**
     * Decrementa el número de copias disponibles al realizar un préstamo.
     *
     * @return true si el préstamo fue posible (había copias disponibles), false en caso contrario
     */
    public boolean prestar() {
        if (copiasDisponibles > 0) {
            copiasDisponibles--;
            return true;
        }
        return false;
    }

    /**
     * Incrementa el número de copias disponibles al devolver un libro.
     *
     * @return true si la devolución fue válida (no supera el total), false si no
     */
    public boolean devolver() {
        if (copiasDisponibles < copiasTotal) {
            copiasDisponibles++;
            return true;
        }
        return false;
    }

    /**
     * Comprueba si hay alguna copia disponible para préstamo.
     *
     * @return true si hay al menos una copia disponible, false en caso contrario
     */
    public boolean estaDisponible() {
        return copiasDisponibles > 0;
    }

    /**
     * Devuelve una representación en cadena de texto del libro.
     *
     * @return String con el formato: "[ISBN] Título - Autor (X copias disponibles)"
     */
    @Override
    public String toString() {
        return "[" + isbn + "] " + titulo + " - " + autor + " (" + copiasDisponibles + " copias disponibles)";
    }
}
