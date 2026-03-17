package biblioteca;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas JUnit 5 para {@link GestorBiblioteca}.
 *
 * <p>Incluye pruebas derivadas de:</p>
 * <ul>
 *   <li><b>Caja blanca (camino básico)</b>: pruebas del método {@link GestorBiblioteca#realizarPrestamo(String, String)}
 *       que cubren todos los caminos independientes del grafo de flujo.</li>
 *   <li><b>Caja negra (particiones equivalentes)</b>: pruebas del método {@link GestorBiblioteca#calcularMulta(int)}
 *       que cubren todas las particiones: días negativos, cero, 1-7, 8-30 y más de 30.</li>
 * </ul>
 *
 * @author Ana García y Carlos Martínez
 * @version 1.0
 * @since 2024-01-15
 */
@DisplayName("Pruebas del Gestor de Biblioteca")
class GestorBibliotecaTest {
    

    /** Gestor de biblioteca bajo prueba */
    private GestorBiblioteca gestor;

    /** Libro de prueba con 2 copias */
    private Libro libro;

    /** Usuario de prueba sin préstamos */
    private Usuario usuario;

    /**
     * Configuración previa a cada prueba.
     * Inicializa un gestor limpio con un libro y un usuario registrados.
     */
    @BeforeEach
    void setUp() {
        gestor = new GestorBiblioteca();
        libro = new Libro("Título Test", "Autor Test", "ISBN-001", 2);
        usuario = new Usuario("U001", "Usuario Test", "test@email.com");
        gestor.registrarLibro(libro);
        gestor.registrarUsuario(usuario);
    }

    // ====================================================================
    // PRUEBAS DE CAJA BLANCA — CAMINO BÁSICO (realizarPrestamo)
    // ====================================================================

    /**
     * CB-1: Camino A — El usuario no existe en el sistema.
     * Nodo de decisión 1 → rama verdadera (usuario == null).
     * Resultado esperado: -1
     */
    @Test
    @DisplayName("CB-1: Préstamo con usuario inexistente → -1")
    void testPrestamo_UsuarioNoExiste() {
        int resultado = gestor.realizarPrestamo("INEXISTENTE", "ISBN-001");
        assertEquals(-1, resultado, "Debe retornar -1 si el usuario no existe");
    }

    /**
     * CB-2: Camino B — El usuario existe pero el libro no está en el catálogo.
     * Nodo de decisión 1 → falsa. Nodo de decisión 2 → verdadera (libro == null).
     * Resultado esperado: -2
     */
    @Test
    @DisplayName("CB-2: Préstamo con libro inexistente → -2")
    void testPrestamo_LibroNoExiste() {
        int resultado = gestor.realizarPrestamo("U001", "ISBN-INEXISTENTE");
        assertEquals(-2, resultado, "Debe retornar -2 si el libro no existe");
    }

    /**
     * CB-3: Camino C — Usuario y libro existen, pero el usuario ha alcanzado el límite de préstamos.
     * Nodo de decisión 3 → verdadera (!usuario.puedePedir()).
     * Resultado esperado: -3
     */
    @Test
    @DisplayName("CB-3: Préstamo cuando el usuario tiene el límite alcanzado → -3")
    void testPrestamo_LimitePrestamosAlcanzado() {
        // Registrar libros adicionales para llenar el límite
        gestor.registrarLibro(new Libro("Libro2", "Autor", "ISBN-002", 1));
        gestor.registrarLibro(new Libro("Libro3", "Autor", "ISBN-003", 1));
        gestor.registrarLibro(new Libro("Libro4", "Autor", "ISBN-004", 1));

        gestor.realizarPrestamo("U001", "ISBN-001");
        gestor.realizarPrestamo("U001", "ISBN-002");
        gestor.realizarPrestamo("U001", "ISBN-003");

        int resultado = gestor.realizarPrestamo("U001", "ISBN-004");
        assertEquals(-3, resultado, "Debe retornar -3 si el usuario alcanzó el límite de préstamos");
    }

    /**
     * CB-4: Camino D — Usuario y libro existen, usuario puede pedir, pero no hay copias disponibles.
     * Nodo de decisión 4 → verdadera (!libro.estaDisponible()).
     * Resultado esperado: -4
     */
    @Test
    @DisplayName("CB-4: Préstamo de libro sin copias disponibles → -4")
    void testPrestamo_SinCopiasDisponibles() {
        Libro libroSinCopias = new Libro("Sin Copias", "Autor", "ISBN-SC", 1);
        gestor.registrarLibro(libroSinCopias);

        // Registrar segundo usuario para agotar la única copia
        Usuario usuario2 = new Usuario("U002", "Otro Usuario", "otro@email.com");
        gestor.registrarUsuario(usuario2);
        gestor.realizarPrestamo("U002", "ISBN-SC");

        int resultado = gestor.realizarPrestamo("U001", "ISBN-SC");
        assertEquals(-4, resultado, "Debe retornar -4 si no hay copias disponibles");
    }

    /**
     * CB-5: Camino E — Todos los nodos de decisión toman la rama falsa. Préstamo exitoso.
     * Recorre el camino completo hasta el nodo final de éxito.
     * Resultado esperado: 0
     */
    @Test
    @DisplayName("CB-5: Préstamo exitoso en condiciones normales → 0")
    void testPrestamo_Exitoso() {
        int resultado = gestor.realizarPrestamo("U001", "ISBN-001");
        assertEquals(0, resultado, "Debe retornar 0 en un préstamo exitoso");
        assertEquals(1, usuario.getNumeroPrestamos());
        assertEquals(1, libro.getCopiasDisponibles());
    }

    // ====================================================================
    // PRUEBAS DE CAJA NEGRA — PARTICIONES EQUIVALENTES (calcularMulta)
    // ====================================================================

    /**
     * PE-1: Partición inválida — días negativos.
     * Clase: diasRetraso &lt; 0. Valor representativo: -5.
     * Resultado esperado: -1.0 (valor de error)
     */
    @Test
    @DisplayName("PE-1: Multa con días negativos → -1.0 (error)")
    void testMulta_DiasNegativos() {
        double resultado = gestor.calcularMulta(-5);
        assertEquals(-1.0, resultado, 0.001, "Días negativos deben devolver -1.0");
    }

    /**
     * PE-2: Partición válida — sin retraso (0 días).
     * Clase: diasRetraso == 0. Valor representativo: 0.
     * Resultado esperado: 0.0 (sin multa)
     */
    @Test
    @DisplayName("PE-2: Multa con 0 días de retraso → 0.0 €")
    void testMulta_CeroDias() {
        double resultado = gestor.calcularMulta(0);
        assertEquals(0.0, resultado, 0.001, "Sin retraso la multa debe ser 0.0");
    }

    /**
     * PE-3: Partición válida — retraso leve (1 a 7 días), tarifa 0.20€/día.
     * Valor representativo: 5 días → 5 × 0.20 = 1.00€
     */
    @Test
    @DisplayName("PE-3: Multa con 5 días de retraso → 1.00 €")
    void testMulta_RetrasoBajo_5dias() {
        double resultado = gestor.calcularMulta(5);
        assertEquals(1.00, resultado, 0.001, "5 días × 0.20€ = 1.00€");
    }

    /**
     * PE-3b: Frontera superior de la partición de 1-7 días.
     * Valor en frontera: 7 días → 7 × 0.20 = 1.40€
     */
    @Test
    @DisplayName("PE-3b: Multa con 7 días de retraso (frontera) → 1.40 €")
    void testMulta_Frontera7dias() {
        double resultado = gestor.calcularMulta(7);
        assertEquals(1.40, resultado, 0.001, "7 días × 0.20€ = 1.40€");
    }

    /**
     * PE-4: Partición válida — retraso medio (8 a 30 días), tarifa 0.50€/día.
     * Valor representativo: 15 días → 15 × 0.50 = 7.50€
     */
    @Test
    @DisplayName("PE-4: Multa con 15 días de retraso → 7.50 €")
    void testMulta_RetrasoMedio_15dias() {
        double resultado = gestor.calcularMulta(15);
        assertEquals(7.50, resultado, 0.001, "15 días × 0.50€ = 7.50€");
    }

    /**
     * PE-4b: Frontera de partición: 8 días (primer valor de tarifa media).
     * Valor: 8 × 0.50 = 4.00€
     */
    @Test
    @DisplayName("PE-4b: Multa con 8 días (frontera baja tarifa media) → 4.00 €")
    void testMulta_Frontera8dias() {
        double resultado = gestor.calcularMulta(8);
        assertEquals(4.00, resultado, 0.001, "8 días × 0.50€ = 4.00€");
    }

    /**
     * PE-5: Partición válida — retraso grave (más de 30 días), tarifa 1.00€/día.
     * Valor representativo: 45 días → 45 × 1.00 = 45.00€
     */
    @Test
    @DisplayName("PE-5: Multa con 45 días de retraso → 45.00 €")
    void testMulta_RetrasoGrave_45dias() {
        double resultado = gestor.calcularMulta(45);
        assertEquals(45.00, resultado, 0.001, "45 días × 1.00€ = 45.00€");
    }

    /**
     * PE-5b: Frontera de partición: 31 días (primer valor de tarifa máxima).
     * Valor: 31 × 1.00 = 31.00€
     */
    @Test
    @DisplayName("PE-5b: Multa con 31 días (frontera alta) → 31.00 €")
    void testMulta_Frontera31dias() {
        double resultado = gestor.calcularMulta(31);
        assertEquals(31.00, resultado, 0.001, "31 días × 1.00€ = 31.00€");
    }

    // ====================================================================
    // PRUEBAS ADICIONALES DE INTEGRACIÓN
    // ====================================================================

    /**
     * Verifica que la devolución de un libro incrementa correctamente las copias disponibles.
     */
    @Test
    @DisplayName("INT-1: Devolución correcta incrementa copias disponibles")
    void testDevolucion_Correcta() {
        gestor.realizarPrestamo("U001", "ISBN-001");
        assertEquals(1, libro.getCopiasDisponibles());

        boolean resultado = gestor.realizarDevolucion("U001", "ISBN-001");

        assertTrue(resultado);
        assertEquals(2, libro.getCopiasDisponibles());
        assertEquals(0, usuario.getNumeroPrestamos());
    }

    /**
     * Verifica que no se puede registrar un libro con ISBN duplicado.
     */
    @Test
    @DisplayName("INT-2: Registro de libro duplicado devuelve false")
    void testRegistrarLibro_Duplicado() {
        Libro duplicado = new Libro("Otro Título", "Otro Autor", "ISBN-001", 5);
        boolean resultado = gestor.registrarLibro(duplicado);
        assertFalse(resultado, "No se debe registrar un libro con ISBN duplicado");
        assertEquals(1, gestor.getTotalLibros());
    }

    /**
     * Verifica que la creación de un libro con título vacío lanza excepción.
     */
    @Test
    @DisplayName("INT-3: Libro con título vacío lanza IllegalArgumentException")
    void testLibro_TituloVacio() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Libro("", "Autor", "ISBN-999", 1);
        });
    }
}
