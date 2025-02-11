package unit.DAO;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.dao.UtenteGestoreCatenaDAO;
import it.unisa.application.model.entity.UtenteGestoreCatena;
import it.unisa.application.sottosistemi.utilities.PasswordUtils;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

public class UtenteGestoreCatenaDAOTest {

    private UtenteGestoreCatenaDAO utenteGestoreCatenaDAO;

    @BeforeAll
    static void globalSetup() {
        // Configura l'H2 in-memory DB tramite la classe DatabaseSetupForTest
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        utenteGestoreCatenaDAO = new UtenteGestoreCatenaDAO();
        try (Connection conn = DataSourceSingleton.getInstance().getConnection()) {
            System.out.println("Database connected: " + conn.getMetaData().getURL());
        } catch (Exception e) {
            e.printStackTrace();
        }
        populateDatabase();
    }

    /**
     * Ripulisce e popola il database con dati minimi necessari per testare UtenteGestoreCatenaDAO.
     * Assicura che esista almeno un utente gestore catena.
     */
    private void populateDatabase() {
        try (Connection conn = DataSourceSingleton.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {

            // 1) Elimina i dati esistenti per avere un ambiente pulito
            String cleanupScript = "DELETE FROM UtenteGestoreCatena;";
            stmt.execute(cleanupScript);

            // 2) Inserisci un utente gestore catena di esempio con la password hashata
            String hashedPassword = PasswordUtils.hashPassword("hashedPassword"); // Usa hashPassword qui
            System.out.println("Hash della password da inserire: " + hashedPassword);

            String setupScript = String.format("INSERT INTO UtenteGestoreCatena (username, password) VALUES ('gestoreTest', '%s');", hashedPassword);
            stmt.execute(setupScript);

            // Verifica che l'utente sia stato inserito
            ResultSet rs = stmt.executeQuery("SELECT * FROM UtenteGestoreCatena WHERE username = 'gestoreTest';");
            if (rs.next()) {
                System.out.println("Utente inserito con username: " + rs.getString("username"));
            } else {
                System.out.println("L'utente non è stato inserito correttamente.");
            }

        } catch (Exception e) {
            throw new RuntimeException("Errore nel setup del database per i test", e);
        }
    }

    @Test
    @DisplayName("testGetByUsername - Recupero utente gestore catena con username e password corretti, errati e inesistenti")
    void testGetByUsername() {
        System.out.println("Recupero utente gestore catena tramite username e password");

        // Prepara la password hashata
        String hashedPassword = PasswordUtils.hashPassword("hashedPassword");
        System.out.println("Password hashata: " + hashedPassword);

        // Test con credenziali corrette
        UtenteGestoreCatena utenteCorretto = utenteGestoreCatenaDAO.getByUsername("gestoreTest", "hashedPassword");
        System.out.println("Utente trovato con credenziali corrette: " + utenteCorretto);
        assertNotNull(utenteCorretto, "L'utente dovrebbe essere trovato con la password corretta");
        assertEquals("gestoreTest", utenteCorretto.getUsername(), "L'username dell'utente dovrebbe essere 'gestoreTest'");

        // Test con password errata (devi usare l'hashing anche qui)
        String wrongPasswordHash = PasswordUtils.hashPassword("wrongPassword");
        UtenteGestoreCatena utenteErrato = utenteGestoreCatenaDAO.getByUsername("gestoreTest", wrongPasswordHash);
        System.out.println("Utente trovato con password errata: " + utenteErrato);
        assertNull(utenteErrato, "Non dovrebbe essere trovato nessun utente con la password errata");

        // Test con username inesistente
        UtenteGestoreCatena utenteInesistente = utenteGestoreCatenaDAO.getByUsername("nonExistentGestore", hashedPassword);
        System.out.println("Utente trovato con username inesistente: " + utenteInesistente);
        assertNull(utenteInesistente, "Non dovrebbe essere trovato nessun utente con username inesistente");
    }

    @Test
    @DisplayName("testUpdateUtenteGestoreCatena - Aggiornamento della password di un utente gestore catena")
    void testUpdate() {
        String newPassword = "newPassword";
        String newPasswordHash = PasswordUtils.hashPassword(newPassword);  // Hash della nuova password
        System.out.println("Hash della nuova password: " + newPasswordHash);

        // Verifica che l'utente esista prima dell'aggiornamento
        UtenteGestoreCatena utenteEsistente = utenteGestoreCatenaDAO.getByUsername("gestoreTest", "hashedPassword");
        assertNotNull(utenteEsistente, "L'utente 'gestoreTest' dovrebbe esistere prima dell'aggiornamento");

        // Aggiorna la password
        utenteEsistente.setPassword(newPasswordHash);  // Usa la password hashata
        utenteGestoreCatenaDAO.update(utenteEsistente);

        // Verifica che l'utente sia stato aggiornato con la nuova password
        UtenteGestoreCatena utenteAggiornato = utenteGestoreCatenaDAO.getByUsername("gestoreTest", newPasswordHash);  // Confronta con l'hash
        assertNotNull(utenteAggiornato, "L'utente dovrebbe essere trovato con la password aggiornata");
    }

    @Test
    @DisplayName("testDeleteUtenteGestoreCatena - Rimozione di un utente gestore catena")
    void testDelete() {
        System.out.println("Rimozione di un utente gestore catena");

        // Rimuovi l'utente
        utenteGestoreCatenaDAO.delete("gestoreTest");
        System.out.println("Utente rimosso: gestoreTest");

        // Verifica che l'utente sia stato rimosso
        UtenteGestoreCatena utenteRimosso = utenteGestoreCatenaDAO.getByUsername("gestoreTest", "hashedPassword");
        assertNull(utenteRimosso, "L'utente dovrebbe essere rimosso dal database");
    }
}
