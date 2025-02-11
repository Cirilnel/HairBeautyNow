package unit.DAO;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.dao.UtenteGestoreSedeDAO;
import it.unisa.application.model.entity.UtenteGestoreSede;
import it.unisa.application.sottosistemi.utilities.PasswordUtils;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UtenteGestoreSedeDAOTest {

    private UtenteGestoreSedeDAO utenteGestoreSedeDAO;

    @BeforeAll
    static void globalSetup() {
        // Configura l'H2 in-memory DB tramite la classe DatabaseSetupForTest
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        utenteGestoreSedeDAO = new UtenteGestoreSedeDAO();
        try (Connection conn = DataSourceSingleton.getInstance().getConnection()) {
            System.out.println("Database connected: " + conn.getMetaData().getURL());
        } catch (Exception e) {
            e.printStackTrace();
        }
        populateDatabase();
    }

    private void populateDatabase() {
        try (Connection conn = DataSourceSingleton.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {

            // 1) Elimina i dati esistenti per avere un ambiente pulito
            String cleanupScript = """
            DELETE FROM UtenteGestoreSede;
            DELETE FROM Sede;
        """;
            stmt.execute(cleanupScript);

            // 2) Inserisci le sedi necessarie per il test
            String setupSedeScript = """
            INSERT INTO Sede (id, indirizzo, nome, città)
            VALUES (1, 'Via Test 1', 'Sede1', 'Napoli'),
                   (2, 'Via Test 2', 'Sede2', 'Milano');
        """;
            stmt.execute(setupSedeScript);

            // 3) Inserisci un utente gestore sede di esempio con la password hashata
            String hashedPassword = PasswordUtils.hashPassword("hashedPassword"); // Usa hashPassword qui
            System.out.println("Hash della password da inserire: " + hashedPassword);

            String setupUtenteScript = String.format("""
            INSERT INTO UtenteGestoreSede (usernameUGS, password, sedeID)
            VALUES ('gestoreSedeTest', '%s', NULL);
        """, hashedPassword);
            stmt.execute(setupUtenteScript);

            // Verifica che l'utente sia stato inserito
            ResultSet rs = stmt.executeQuery("SELECT * FROM UtenteGestoreSede WHERE usernameUGS = 'gestoreSedeTest';");
            if (rs.next()) {
                System.out.println("Utente inserito con username: " + rs.getString("usernameUGS"));
            } else {
                System.out.println("L'utente non è stato inserito correttamente.");
            }

        } catch (Exception e) {
            throw new RuntimeException("Errore nel setup del database per i test", e);
        }
    }

    @AfterEach
    void tearDown() {
        try (Connection conn = DataSourceSingleton.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            String cleanupScript = "DELETE FROM UtenteGestoreSede;";
            stmt.execute(cleanupScript);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("testExistsByUsername - Verifica se uno username esiste nel database")
    void testExistsByUsername() {
        System.out.println("Verifica se uno username esiste nel database");

        // Test con username esistente
        boolean esiste = utenteGestoreSedeDAO.existsByUsername("gestoreSedeTest");
        assertTrue(esiste, "Lo username 'gestoreSedeTest' dovrebbe esistere nel database");

        // Test con username inesistente
        boolean nonEsiste = utenteGestoreSedeDAO.existsByUsername("nonExistentUsername");
        assertFalse(nonEsiste, "Lo username 'nonExistentUsername' non dovrebbe esistere nel database");
    }

    @Test
    @DisplayName("testInsertUtenteGestoreSede - Inserimento di un nuovo utente gestore sede")
    void testInsert() {
        String newUsername = "newGestoreSede";
        String newPassword = "newPassword";
        String newPasswordHash = PasswordUtils.hashPassword(newPassword);

        UtenteGestoreSede nuovoUtente = new UtenteGestoreSede(newUsername, newPasswordHash, null);  // Passa 'null' per sedeID
        boolean result = utenteGestoreSedeDAO.insert(nuovoUtente);
        System.out.println("Inserimento utente: " + result);
        assertTrue(result, "L'inserimento dell'utente dovrebbe riuscire");

        // Verifica che l'utente sia stato inserito
        UtenteGestoreSede utenteInserito = utenteGestoreSedeDAO.getByUsername(newUsername, newPasswordHash);
        assertNotNull(utenteInserito, "L'utente inserito dovrebbe essere trovato");
        assertEquals(newUsername, utenteInserito.getUsernameUGS(), "L'username dell'utente inserito dovrebbe essere '" + newUsername + "'");
    }

    @Test
    @DisplayName("testGetByUsername - Recupero utente gestore sede con username e password corretti, errati e inesistenti")
    void testGetByUsername() {
        System.out.println("Recupero utente gestore sede tramite username e password");

        // Prepara la password hashata
        String hashedPassword = PasswordUtils.hashPassword("hashedPassword");
        System.out.println("Password hashata: " + hashedPassword);

        // Test con credenziali corrette
        UtenteGestoreSede utenteCorretto = utenteGestoreSedeDAO.getByUsername("gestoreSedeTest", "hashedPassword");
        System.out.println("Utente trovato con credenziali corrette: " + utenteCorretto);
        assertNotNull(utenteCorretto, "L'utente dovrebbe essere trovato con la password corretta");
        assertEquals("gestoreSedeTest", utenteCorretto.getUsernameUGS(), "L'username dell'utente dovrebbe essere 'gestoreSedeTest'");

        // Test con password errata
        String wrongPasswordHash = PasswordUtils.hashPassword("wrongPassword");
        UtenteGestoreSede utenteErrato = utenteGestoreSedeDAO.getByUsername("gestoreSedeTest", wrongPasswordHash);
        System.out.println("Utente trovato con password errata: " + utenteErrato);
        assertNull(utenteErrato, "Non dovrebbe essere trovato nessun utente con la password errata");

        // Test con username inesistente
        UtenteGestoreSede utenteInesistente = utenteGestoreSedeDAO.getByUsername("nonExistentGestoreSede", hashedPassword);
        System.out.println("Utente trovato con username inesistente: " + utenteInesistente);
        assertNull(utenteInesistente, "Non dovrebbe essere trovato nessun utente con username inesistente");
    }


    @Test
    @DisplayName("testGetAll - Recupero di tutti gli utenti gestori sede")
    void testGetAll() {
        System.out.println("Recupero di tutti gli utenti gestori sede");

        // Recupera tutti gli utenti
        var utenti = utenteGestoreSedeDAO.getAll();
        assertFalse(utenti.isEmpty(), "La lista degli utenti non dovrebbe essere vuota");
    }

    @Test
    @DisplayName("testGetGestoriSenzaSede - Recupero di gestori senza sede assegnata")
    void testGetGestoriSenzaSede() {
        System.out.println("Recupero di gestori senza sede assegnata");

        // Recupera i gestori senza sede
        List<UtenteGestoreSede> gestoriSenzaSede = utenteGestoreSedeDAO.getGestoriSenzaSede();
        assertFalse(gestoriSenzaSede.isEmpty(), "La lista dei gestori senza sede non dovrebbe essere vuota");
    }

    @Test
    @DisplayName("testGetGestoriConSede - Recupero di gestori con sede assegnata")
    void testGetGestoriConSede() {
        System.out.println("Recupero di gestori con sede assegnata");

        // Assegna una sede al gestore esistente
        utenteGestoreSedeDAO.assegnaSede("gestoreSedeTest", 1);

        // Recupera i gestori con sede
        List<UtenteGestoreSede> gestoriConSede = utenteGestoreSedeDAO.getGestoriConSede();
        assertFalse(gestoriConSede.isEmpty(), "La lista dei gestori con sede non dovrebbe essere vuota");
    }

    @Test
    @DisplayName("testAssegnaSede - Assegnazione di una sede a un utente gestore sede")
    void testAssegnaSede() {
        System.out.println("Assegnazione di una sede");

        // Assegna una sede (sedeID = 1)
        utenteGestoreSedeDAO.assegnaSede("gestoreSedeTest", 1);

        // Verifica che la sede sia stata assegnata correttamente
        UtenteGestoreSede utenteAggiornato = utenteGestoreSedeDAO.getByUsername("gestoreSedeTest", "hashedPassword");
        assertNotNull(utenteAggiornato, "L'utente dovrebbe esistere");
        assertEquals(1, utenteAggiornato.getSedeID(), "La sede dell'utente dovrebbe essere 1");
    }


    @Test
    @DisplayName("testLicenziaGestore - Licenziamento di un utente gestore sede")
    void testLicenziaGestore() {
        System.out.println("Licenziamento di un utente gestore sede");

        // Licenzia il gestore (sedeID = 0)
        utenteGestoreSedeDAO.licenziaGestore("gestoreSedeTest");

        // Verifica che la sede sia stata rimossa (la sedeID dovrebbe essere 0)
        UtenteGestoreSede utenteLicenziato = utenteGestoreSedeDAO.getByUsername("gestoreSedeTest", "hashedPassword");
        assertNotNull(utenteLicenziato, "L'utente dovrebbe esistere ancora");
        assertEquals(0, utenteLicenziato.getSedeID(), "La sede dell'utente dovrebbe essere 0 dopo il licenziamento");
    }


}
