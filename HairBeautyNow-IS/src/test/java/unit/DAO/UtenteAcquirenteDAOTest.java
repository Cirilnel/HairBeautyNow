package unit.DAO;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.dao.UtenteAcquirenteDAO;
import it.unisa.application.model.entity.UtenteAcquirente;
import it.unisa.application.sottosistemi.utilities.PasswordUtils;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class UtenteAcquirenteDAOTest {

    private UtenteAcquirenteDAO utenteAcquirenteDAO;

    @BeforeAll
    static void globalSetup() {
        // Configura l'H2 in-memory DB tramite la classe DatabaseSetupForTest
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        // Istanzia il DAO
        utenteAcquirenteDAO = new UtenteAcquirenteDAO();

        // Popola il database con i dati di base necessari per i test
        populateDatabase();
    }

    /**
     * Ripulisce e popola il database con dati minimi necessari per testare UtenteAcquirenteDAO.
     * Assicura che esista almeno un utente acquirente.
     */
    private void populateDatabase() {
        try (Connection conn = DataSourceSingleton.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {

            // 1) Elimina i dati esistenti per avere un ambiente pulito
            String cleanupScript = """
            DELETE FROM UtenteAcquirente;
        """;
            stmt.execute(cleanupScript);

            // 2) Inserisci un utente acquirente di esempio con la password hashata
            String hashedPassword = PasswordUtils.hashPassword("hashedPassword"); // Usa hashPassword qui

            String setupScript = String.format("""
            INSERT INTO UtenteAcquirente (username, email, password, nome, cognome, citta)
            VALUES ('userTest', 'test@example.com', '%s', 'Test', 'User', 'Napoli');
        """, hashedPassword);
            stmt.execute(setupScript);

        } catch (Exception e) {
            throw new RuntimeException("Errore nel setup del database per i test", e);
        }
    }


    @Test
    @DisplayName("testInsertUtenteAcquirente - Inserimento di un nuovo utente acquirente")
    void testInsertUtenteAcquirente() {
        System.out.println("Aggiunta di un nuovo utente acquirente");

        // Crea un nuovo utente
        UtenteAcquirente utente = new UtenteAcquirente("newUser", "newuser@example.com", "newPassword", "New", "User", "Roma");

        // Inserisce l'utente nel database
        boolean result = utenteAcquirenteDAO.insert(utente);
        System.out.println("Inserimento risultato: " + result);
        assertTrue(result, "L'utente dovrebbe essere inserito correttamente nel database");
    }

    @Test
    @DisplayName("testGetByUsernameAndPassword - Recupero utente con username e password corretti, errati e inesistenti")
    void testGetByUsernameAndPassword() {
        System.out.println("Recupero utente tramite username e password");

        // Prepara la password hashata
        String hashedPassword = PasswordUtils.hashPassword("hashedPassword");
        System.out.println("Password hashata: " + hashedPassword);

        // Test con credenziali corrette
        UtenteAcquirente utenteCorretto = utenteAcquirenteDAO.getByUsernameAndPassword("userTest", "hashedPassword");
        System.out.println("Utente trovato con credenziali corrette: " + utenteCorretto);
        assertNotNull(utenteCorretto, "L'utente dovrebbe essere trovato con la password corretta");
        assertEquals("userTest", utenteCorretto.getUsername(), "L'username dell'utente dovrebbe essere 'userTest'");

        // Test con password errata (devi usare l'hashing anche qui)
        String wrongPasswordHash = PasswordUtils.hashPassword("wrongPassword");
        UtenteAcquirente utenteErrato = utenteAcquirenteDAO.getByUsernameAndPassword("userTest", wrongPasswordHash);
        System.out.println("Utente trovato con password errata: " + utenteErrato);
        assertNull(utenteErrato, "Non dovrebbe essere trovato nessun utente con la password errata");

        // Test con username inesistente
        UtenteAcquirente utenteInesistente = utenteAcquirenteDAO.getByUsernameAndPassword("nonExistentUser", hashedPassword);
        System.out.println("Utente trovato con username inesistente: " + utenteInesistente);
        assertNull(utenteInesistente, "Non dovrebbe essere trovato nessun utente con username inesistente");
    }
}
