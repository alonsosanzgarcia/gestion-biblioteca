package biblioteca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase principal que gestiona las operaciones de una biblioteca digital.
 * Proporciona funcionalidad para registrar libros y usuarios, gestionar préstamos
 * y devoluciones, y consultar el catálogo.
 *
 * <p>Esta clase contiene el método {@link #realizarPrestamo(String, String)} cuya
 * complejidad ciclomática la hace idónea para pruebas de caja blanca (camino básico),
 * así como métodos con rangos de valores aptos para particiones equivalentes.</p>
 *
 * @author Ana García y Carlos Martínez
 * @version 1.0
 * @since 2024-01-15
 * @see Libro
 * @see Usuario
 */
public class GestorBiblioteca {

    /** Catálogo de libros indexado por ISBN */
    private Map<String, Libro> catalogo;

    /** Registro de usuarios indexado por ID de usuario */
    private Map<String, Usuario> usuarios;

    /**
     * Construye un nuevo gestor de biblioteca con catálogo y registro de usuarios vacíos.
     */
    public GestorBiblioteca() {
        this.catalogo = new HashMap<>();
        this.usuarios = new HashMap<>();
    }

    /**
     * Registra un nuevo libro en el catálogo de la biblioteca.
     * Si ya existe un libro con el mismo ISBN, no se añade y se devuelve false.
     *
     * @param libro Objeto {@link Libro} a añadir al catálogo
     * @return true si el libro fue añadido correctamente, false si ya existía
     * @throws IllegalArgumentException si el parámetro libro es null
     */
    public boolean registrarLibro(Libro libro) {
        if (libro == null) {
            throw new IllegalArgumentException("El libro no puede ser null.");
        }
        if (catalogo.containsKey(libro.getIsbn())) {
            return false;
        }
        catalogo.put(libro.getIsbn(), libro);
        return true;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * No permite usuarios duplicados (mismo ID).
     *
     * @param usuario Objeto {@link Usuario} a registrar
     * @return true si el usuario fue registrado, false si ya existía con ese ID
     * @throws IllegalArgumentException si el usuario es null
     */
    public boolean registrarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser null.");
        }
        if (usuarios.containsKey(usuario.getId())) {
            return false;
        }
        usuarios.put(usuario.getId(), usuario);
        return true;
    }

    /**
     * Realiza el préstamo de un libro a un usuario.
     *
     * <p><b>Flujo de decisión (para prueba del camino básico):</b></p>
     * <ol>
     *   <li>Comprueba si el usuario existe en el sistema.</li>
     *   <li>Comprueba si el libro existe en el catálogo.</li>
     *   <li>Comprueba si el usuario puede solicitar más préstamos.</li>
     *   <li>Comprueba si el libro tiene copias disponibles.</li>
     *   <li>Si todo es correcto, registra el préstamo.</li>
     * </ol>
     *
     * @param idUsuario ID del usuario que solicita el préstamo
     * @param isbn ISBN del libro que se desea tomar prestado
     * @return Código de resultado:
     *         <ul>
     *           <li>0 = Préstamo realizado con éxito</li>
     *           <li>-1 = Usuario no encontrado</li>
     *           <li>-2 = Libro no encontrado en catálogo</li>
     *           <li>-3 = Usuario ha alcanzado el límite de préstamos</li>
     *           <li>-4 = No hay copias disponibles del libro</li>
     *         </ul>
     */
    public int realizarPrestamo(String idUsuario, String isbn) {
        // Nodo 1: Verificar usuario
        Usuario usuario = usuarios.get(idUsuario);
        if (usuario == null) {                          // Decisión 1
            return -1;                                  // Camino A: usuario no existe
        }

        // Nodo 2: Verificar libro
        Libro libro = catalogo.get(isbn);
        if (libro == null) {                            // Decisión 2
            return -2;                                  // Camino B: libro no existe
        }

        // Nodo 3: Verificar límite de préstamos del usuario
        if (!usuario.puedePedir()) {                    // Decisión 3
            return -3;                                  // Camino C: límite alcanzado
        }

        // Nodo 4: Verificar disponibilidad del libro
        if (!libro.estaDisponible()) {                  // Decisión 4
            return -4;                                  // Camino D: sin copias
        }

        // Nodo 5: Realizar el préstamo
        libro.prestar();
        usuario.añadirPrestamo(isbn);
        return 0;                                       // Camino E: éxito
    }

    /**
     * Procesa la devolución de un libro por parte de un usuario.
     *
     * @param idUsuario ID del usuario que devuelve el libro
     * @param isbn ISBN del libro que se devuelve
     * @return true si la devolución se realizó correctamente, false en cualquier caso de error
     *         (usuario no existe, libro no existe, el usuario no tenía ese libro prestado)
     */
    public boolean realizarDevolucion(String idUsuario, String isbn) {
        Usuario usuario = usuarios.get(idUsuario);
        if (usuario == null) {
            return false;
        }

        Libro libro = catalogo.get(isbn);
        if (libro == null) {
            return false;
        }

        boolean eliminado = usuario.eliminarPrestamo(isbn);
        if (!eliminado) {
            return false;
        }

        libro.devolver();
        return true;
    }

    /**
     * Calcula la multa por retraso en la devolución de un libro.
     * La política de multas es la siguiente:
     * <ul>
     *   <li>0 días de retraso: sin multa</li>
     *   <li>1–7 días: 0.20€ por día</li>
     *   <li>8–30 días: 0.50€ por día</li>
     *   <li>Más de 30 días: 1.00€ por día</li>
     * </ul>
     *
     * @param diasRetraso Número de días de retraso en la devolución
     * @return Importe de la multa en euros, o -1.0 si los días son negativos (valor inválido)
     */
    public double calcularMulta(int diasRetraso) {
        if (diasRetraso < 0) {
            return -1.0;
        }
        if (diasRetraso == 0) {
            return 0.0;
        }
        if (diasRetraso <= 7) {
            return diasRetraso * 0.20;
        }
        if (diasRetraso <= 30) {
            return diasRetraso * 0.50;
        }
        return diasRetraso * 1.00;
    }

    /**
     * Obtiene la lista completa de libros disponibles en el catálogo.
     *
     * @return Lista de libros disponibles (con al menos una copia); lista vacía si no hay ninguno
     */
    public List<Libro> obtenerLibrosDisponibles() {
        List<Libro> disponibles = new ArrayList<>();
        for (Libro libro : catalogo.values()) {
            if (libro.estaDisponible()) {
                disponibles.add(libro);
            }
        }
        return disponibles;
    }

    /**
     * Busca un libro en el catálogo por su ISBN.
     *
     * @param isbn ISBN del libro buscado
     * @return El objeto {@link Libro} si existe, null en caso contrario
     */
    public Libro buscarLibro(String isbn) {
        return catalogo.get(isbn);
    }

    /**
     * Busca un usuario registrado en el sistema por su ID.
     *
     * @param id ID del usuario buscado
     * @return El objeto {@link Usuario} si existe, null en caso contrario
     */
    public Usuario buscarUsuario(String id) {
        return usuarios.get(id);
    }

    /**
     * Devuelve el número total de libros registrados en el catálogo.
     *
     * @return Tamaño del catálogo
     */
    public int getTotalLibros() {
        return catalogo.size();
    }

    /**
     * Devuelve el número total de usuarios registrados en el sistema.
     *
     * @return Número de usuarios registrados
     */
    public int getTotalUsuarios() {
        return usuarios.size();
    }
}
