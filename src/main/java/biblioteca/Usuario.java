package biblioteca;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un usuario registrado en el sistema de biblioteca.
 * Gestiona la información personal del usuario y los préstamos de libros.
 *
 * Un usuario puede tener un número limitado de libros prestados simultáneamente.
 *
 * @author Carlos Martínez
 * @version 1.1
 */
public class Usuario {

    /** Número máximo de libros que un usuario puede tener prestados */
    public static final int MAX_PRESTAMOS = 3;

    /** Identificador único del usuario */
    private String id;

    /** Nombre completo del usuario */
    private String nombre;

    /** Correo electrónico del usuario */
    private String email;

    /** Lista que almacena los ISBN de los libros prestados */
    private List<String> prestamosActivos;

    /**
     * Constructor de la clase Usuario.
     * Inicializa los datos básicos del usuario y crea la lista de préstamos vacía.
     *
     * @param id Identificador único (no puede ser null ni vacío)
     * @param nombre Nombre del usuario (no puede ser null ni vacío)
     * @param email Correo electrónico del usuario
     * @throws IllegalArgumentException si el id o el nombre son inválidos
     */
    public Usuario(String id, String nombre, String email) {

        // Validación del ID (no puede ser null ni vacío)
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de usuario no puede ser null o vacío.");
        }

        // Validación del nombre (no puede ser null ni vacío)
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede ser null o vacío.");
        }

        // Asignación de valores a los atributos
        this.id = id;
        this.nombre = nombre;
        this.email = email;

        // Inicialización de la lista de préstamos
        this.prestamosActivos = new ArrayList<>();
    }

    /**
     * Obtiene el identificador del usuario.
     *
     * @return ID del usuario
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene el nombre del usuario.
     *
     * @return nombre completo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     *
     * @return email del usuario
     */
    public String getEmail() {
        return email;
    }

    /**
     * Devuelve una copia de la lista de préstamos activos.
     * Se devuelve una copia para evitar que se modifique desde fuera.
     *
     * @return lista de ISBN de los libros prestados
     */
    public List<String> getPrestamosActivos() {
        return new ArrayList<>(prestamosActivos);
    }

    /**
     * Obtiene el número de préstamos activos.
     *
     * @return cantidad de libros prestados
     */
    public int getNumeroPrestamos() {
        return prestamosActivos.size();
    }

    /**
     * Añade un nuevo préstamo al usuario.
     * Solo se añade si:
     * - No ha alcanzado el límite máximo
     * - No tiene ya ese libro
     *
     * @param isbn ISBN del libro
     * @return true si se añade correctamente, false si no se puede añadir
     */
    public boolean añadirPrestamo(String isbn) {

        // Comprobar si ha alcanzado el límite máximo
        if (prestamosActivos.size() >= MAX_PRESTAMOS) {
            return false;
        }

        // Comprobar si ya tiene ese libro
        if (prestamosActivos.contains(isbn)) {
            return false;
        }

        // Añadir el préstamo
        return prestamosActivos.add(isbn);
    }

    /**
     * Elimina un préstamo del usuario (devolución del libro).
     *
     * @param isbn ISBN del libro a devolver
     * @return true si se eliminó correctamente, false si no existía
     */
    public boolean eliminarPrestamo(String isbn) {
        return prestamosActivos.remove(isbn);
    }

    /**
     * Indica si el usuario puede pedir más libros.
     *
     * @return true si no ha alcanzado el límite, false si ya está en el máximo
     */
    public boolean puedePedir() {
        return prestamosActivos.size() < MAX_PRESTAMOS;
    }

    /**
     * Comprueba si el usuario tiene un libro concreto prestado.
     *
     * @param isbn ISBN del libro
     * @return true si el libro está en la lista de préstamos
     */
    public boolean tienePrestado(String isbn) {
        return prestamosActivos.contains(isbn);
    }

    /**
     * Indica cuántos préstamos más puede realizar el usuario.
     *
     * @return número de préstamos disponibles
     */
    public int prestamosDisponibles() {
        return MAX_PRESTAMOS - prestamosActivos.size();
    }

    /**
     * Devuelve los préstamos en formato texto.
     *
     * @return lista de préstamos o mensaje si no tiene
     */
    public String mostrarPrestamos() {
        if (prestamosActivos.isEmpty()) {
            return "No tiene préstamos activos.";
        }
        return "Préstamos: " + prestamosActivos;
    }

    /**
     * Devuelve una representación en texto del usuario.
     * Útil para mostrar información en consola o logs.
     *
     * @return String con datos del usuario
     */
    @Override
    public String toString() {
        return "Usuario[id=" + id +
                ", nombre=" + nombre +
                ", prestamos=" + prestamosActivos.size() +
                "/" + MAX_PRESTAMOS + "]";
    }
}
