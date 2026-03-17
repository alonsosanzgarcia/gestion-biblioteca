package biblioteca;

import java.util.List;
import java.util.Scanner;

/**
 * Clase principal del Sistema de Gestión de Biblioteca.
 * Proporciona una interfaz de consola interactiva para gestionar préstamos,
 * devoluciones y consultas del catálogo.
 *
 * <p>Al ejecutar este programa se muestra un menú con las siguientes opciones:</p>
 * <ul>
 *   <li>Registrar libros y usuarios</li>
 *   <li>Realizar préstamos y devoluciones</li>
 *   <li>Consultar libros disponibles</li>
 *   <li>Calcular multas por retraso</li>
 * </ul>
 *
 * @author Ana García y Carlos Martínez
 * @version 1.0
 * @since 2024-01-15
 */
public class Main {

    /** Instancia del gestor de biblioteca para las operaciones */
    private static GestorBiblioteca gestor = new GestorBiblioteca();

    /** Scanner para lectura de entrada del usuario por consola */
    private static Scanner scanner = new Scanner(System.in);

    /**
     * Método principal que inicia la aplicación de gestión de biblioteca.
     * Carga datos de ejemplo y lanza el menú interactivo.
     *
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        cargarDatosEjemplo();
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   SISTEMA DE GESTIÓN DE BIBLIOTECA   ║");
        System.out.println("╚══════════════════════════════════════╝");

        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            int opcion = leerEnteroSeguro();
            switch (opcion) {
                case 1: mostrarLibrosDisponibles(); break;
                case 2: realizarPrestamo(); break;
                case 3: realizarDevolucion(); break;
                case 4: calcularMulta(); break;
                case 5: registrarLibro(); break;
                case 6: registrarUsuario(); break;
                case 0: salir = true; System.out.println("¡Hasta pronto!"); break;
                default: System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
        scanner.close();
    }

    /**
     * Muestra el menú principal de opciones en la consola.
     */
    private static void mostrarMenu() {
        System.out.println("\n--- MENÚ PRINCIPAL ---");
        System.out.println("1. Ver libros disponibles");
        System.out.println("2. Realizar préstamo");
        System.out.println("3. Realizar devolución");
        System.out.println("4. Calcular multa por retraso");
        System.out.println("5. Registrar nuevo libro");
        System.out.println("6. Registrar nuevo usuario");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }

    /**
     * Muestra en consola la lista de libros disponibles para préstamo.
     */
    private static void mostrarLibrosDisponibles() {
        List<Libro> disponibles = gestor.obtenerLibrosDisponibles();
        if (disponibles.isEmpty()) {
            System.out.println("No hay libros disponibles en este momento.");
        } else {
            System.out.println("\n--- LIBROS DISPONIBLES ---");
            for (Libro libro : disponibles) {
                System.out.println(libro);
            }
        }
    }

    /**
     * Gestiona el proceso interactivo de préstamo de un libro a un usuario.
     * Solicita el ID del usuario y el ISBN del libro, y muestra el resultado.
     */
    private static void realizarPrestamo() {
        System.out.print("ID de usuario: ");
        String idUsuario = scanner.nextLine().trim();
        System.out.print("ISBN del libro: ");
        String isbn = scanner.nextLine().trim();

        int resultado = gestor.realizarPrestamo(idUsuario, isbn);
        switch (resultado) {
            case  0: System.out.println("✓ Préstamo realizado con éxito."); break;
            case -1: System.out.println("✗ Usuario no encontrado."); break;
            case -2: System.out.println("✗ Libro no encontrado en catálogo."); break;
            case -3: System.out.println("✗ El usuario ha alcanzado el límite de préstamos (3)."); break;
            case -4: System.out.println("✗ No hay copias disponibles del libro."); break;
        }
    }

    /**
     * Gestiona el proceso interactivo de devolución de un libro.
     */
    private static void realizarDevolucion() {
        System.out.print("ID de usuario: ");
        String idUsuario = scanner.nextLine().trim();
        System.out.print("ISBN del libro: ");
        String isbn = scanner.nextLine().trim();

        if (gestor.realizarDevolucion(idUsuario, isbn)) {
            System.out.println("✓ Devolución registrada correctamente.");
        } else {
            System.out.println("✗ No se pudo registrar la devolución. Verifique los datos.");
        }
    }

    /**
     * Solicita los días de retraso y muestra el cálculo de la multa correspondiente.
     */
    private static void calcularMulta() {
        System.out.print("Días de retraso: ");
        int dias = leerEnteroSeguro();
        double multa = gestor.calcularMulta(dias);
        if (multa < 0) {
            System.out.println("✗ Número de días inválido.");
        } else {
            System.out.printf("Multa calculada: %.2f €%n", multa);
        }
    }

    /**
     * Permite registrar un nuevo libro en el catálogo mediante entrada por consola.
     */
    private static void registrarLibro() {
        System.out.print("Título: ");
        String titulo = scanner.nextLine().trim();
        System.out.print("Autor: ");
        String autor = scanner.nextLine().trim();
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine().trim();
        System.out.print("Número de copias: ");
        int copias = leerEnteroSeguro();

        try {
            Libro libro = new Libro(titulo, autor, isbn, copias);
            if (gestor.registrarLibro(libro)) {
                System.out.println("✓ Libro registrado correctamente.");
            } else {
                System.out.println("✗ Ya existe un libro con ese ISBN.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    /**
     * Permite registrar un nuevo usuario en el sistema mediante entrada por consola.
     */
    private static void registrarUsuario() {
        System.out.print("ID de usuario: ");
        String id = scanner.nextLine().trim();
        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        try {
            Usuario usuario = new Usuario(id, nombre, email);
            if (gestor.registrarUsuario(usuario)) {
                System.out.println("✓ Usuario registrado correctamente.");
            } else {
                System.out.println("✗ Ya existe un usuario con ese ID.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    /**
     * Lee un número entero de la entrada estándar de forma segura.
     * Si la entrada no es un número válido, devuelve -99.
     *
     * @return El entero leído, o -99 si la entrada no es válida
     */
    private static int leerEnteroSeguro() {
        try {
            String linea = scanner.nextLine().trim();
            return Integer.parseInt(linea);
        } catch (NumberFormatException e) {
            return -99;
        }
    }

    /**
     * Carga un conjunto de datos de ejemplo en el sistema para demostración.
     * Incluye libros y usuarios predefinidos.
     */
    private static void cargarDatosEjemplo() {
        gestor.registrarLibro(new Libro("Don Quijote de la Mancha", "Miguel de Cervantes", "978-84-376-0494-7", 3));
        gestor.registrarLibro(new Libro("Cien años de soledad", "Gabriel García Márquez", "978-84-376-0495-4", 2));
        gestor.registrarLibro(new Libro("La sombra del viento", "Carlos Ruiz Zafón", "978-84-08-04366-8", 1));
        gestor.registrarLibro(new Libro("El nombre de la rosa", "Umberto Eco", "978-84-350-1928-9", 2));

        gestor.registrarUsuario(new Usuario("U001", "María López", "maria@email.com"));
        gestor.registrarUsuario(new Usuario("U002", "Juan Pérez", "juan@email.com"));
        gestor.registrarUsuario(new Usuario("U003", "Laura Sánchez", "laura@email.com"));
    }
}
