package biblioteca;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un usuario registrado en el sistema de biblioteca.
 * Gestiona la información personal del usuario y el historial de préstamos activos.
 *
 * @author Carlos Martínez (Componente 2)
 * @version 1.0
 * @since 2024-01-15
 */
public class Usuario {

    /** Límite máximo de libros que un usuario puede tener prestados simultáneamente */
    public static final int MAX_PRESTAMOS = 3;

    /** Identificador único del usuario */
    private String id;

    /** Nombre completo del usuario */
    private String nombre;

    /** Correo electrónico del usuario */
    private String email;

    /** Lista de ISBN de los libros actualmente en préstamo */
    private List<String> prestamosActivos;

    /**
     * Construye un nuevo usuario con los datos proporcionados.
     *
     * @param id Identificador único del usuario (no puede ser null)
     * @param nombre Nombre completo del usuario
     * @param email Dirección de correo electrónico válida
     * @throws IllegalArgumentException si el id o nombre son null o vacíos
     */
    public Usuario(String id, String nombre, String email) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de usuario no puede ser null o vacío.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede ser null o vacío.");
        }
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.prestamosActivos = new ArrayList<>();
    }

    /**
     * Obtiene el identificador único del usuario.
     *
     * @return ID del usuario
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene el nombre del usuario.
     *
     * @return Nombre completo del usuario
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     *
     * @return Email del usuario
     */
    public String getEmail() {
        return email;
    }

    /**
     * Devuelve la lista de ISBNs de los préstamos activos del usuario.
     *
     * @return Lista de ISBNs en préstamo actualmente
     */
    public List<String> getPrestamosActivos() {
        return new ArrayList<>(prestamosActivos);
    }

    /**
     * Obtiene el número de préstamos activos que tiene el usuario.
     *
     * @return Cantidad de libros actualmente prestados
     */
    public int getNumeroPrestamos() {
        return prestamosActivos.size();
    }

    /**
     * Añade un préstamo a la lista de activos del usuario si no ha superado el límite.
     *
     * @param isbn ISBN del libro a añadir al préstamo
     * @return true si se pudo añadir el préstamo, false si se superó el límite o ya lo tiene
     */
    public boolean añadirPrestamo(String isbn) {
        if (prestamosActivos.size() >= MAX_PRESTAMOS) {
            return false;
        }
        if (prestamosActivos.contains(isbn)) {
            return false;
        }
        return prestamosActivos.add(isbn);
    }

    /**
     * Elimina un préstamo de la lista de activos del usuario.
     *
     * @param isbn ISBN del libro que se devuelve
     * @return true si existía el préstamo y fue eliminado, false si no existía
     */
    public boolean eliminarPrestamo(String isbn) {
        return prestamosActivos.remove(isbn);
    }

    /**
     * Comprueba si el usuario puede solicitar un nuevo préstamo.
     *
     * @return true si no ha alcanzado el límite máximo de préstamos, false en caso contrario
     */
    public boolean puedePedir() {
        return prestamosActivos.size() < MAX_PRESTAMOS;
    }

    /**
     * Representación textual del usuario para mostrar en consola o logs.
     *
     * @return String con formato: "Usuario[id=..., nombre=..., prestamos=X/3]"
     */
    @Override
    public String toString() {
        return "Usuario[id=" + id + ", nombre=" + nombre + ", prestamos=" + prestamosActivos.size() + "/" + MAX_PRESTAMOS + "]";
    }
}
