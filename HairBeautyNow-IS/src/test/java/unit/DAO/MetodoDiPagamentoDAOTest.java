package unit.DAO;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.dao.MetodoDiPagamentoDAO;
import it.unisa.application.model.entity.MetodoDiPagamento;
import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class MetodoDiPagamentoDAOTest {

    private MetodoDiPagamentoDAO metodoDiPagamentoDAO;

    @BeforeAll
    static void globalSetup() {
        // Configura l'H2 in-memory DB tramite la classe DatabaseSetupForTest
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        // Istanzia il DAO
        metodoDiPagamentoDAO = new MetodoDiPagamentoDAO();

        // Popola il database con i dati di base necessari per i test
        populateDatabase();
    }

    /**
     * Ripulisce e popola il database con dati minimi necessari per testare MetodoDiPagamentoDAO.
     * Assicura che esistano alcuni metodi di pagamento e utenti.
     */
    private void populateDatabase() {
        try (Connection conn = DataSourceSingleton.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {

            // 1) Elimina i dati esistenti per avere un ambiente pulito
            String cleanupScript = """
                DELETE FROM MetodoDiPagamento;
                DELETE FROM UtenteAcquirente;
            """;
            stmt.execute(cleanupScript);

            // 2) Inserisci i dati minimi di esempio
            String setupScript = """
                -- Inseriamo un UtenteAcquirente
                INSERT INTO UtenteAcquirente (username, email, password, nome, cognome, citta)
                VALUES ('userTest', 'test@example.com', 'password123', 'Test', 'User', 'Napoli');

                -- Inseriamo un metodo di pagamento per l'utente
                INSERT INTO MetodoDiPagamento (nCarta, dataScadenza, nomeIntestatario, cvv, indirizzo, username, metodoPagamento, email)
                VALUES ('1234567812345678', '2025-12-31', 'Mario Rossi', 123, 'Via Roma 100', 'userTest', 'credito', 'test@example.com');
            """;
            stmt.execute(setupScript);

        } catch (Exception e) {
            throw new RuntimeException("Errore nel setup del database per i test", e);
        }
    }

    @Test
    @DisplayName("testMetodoDiPagamentoEsistente - Verifica se il metodo di pagamento esiste per l'utente")
    void testMetodoDiPagamentoEsistente() throws SQLException {
        System.out.println("Verifica se il metodo di pagamento esiste per l'utente");

        // Verifica che il metodo di pagamento esista
        boolean exists = metodoDiPagamentoDAO.metodoDiPagamentoEsistente("userTest", "credito");
        System.out.println("Metodo di pagamento esistente: " + exists);
        assertTrue(exists, "Il metodo di pagamento dovrebbe esistere per l'utente 'userTest'");
    }
/*
    @Test
    @DisplayName("testAddMetodoDiPagamento - Aggiunta di un nuovo metodo di pagamento")
    void testAddMetodoDiPagamento() throws SQLException {
        System.out.println("Aggiunta di un nuovo metodo di pagamento");

        // Creiamo un nuovo metodo di pagamento
        MetodoDiPagamento metodo = new MetodoDiPagamento();
        metodo.setnCarta("8765432187654321");
       // metodo.setDataScadenza(Date.valueOf("2026-11-30"));
        metodo.setNomeIntestatario("Giuseppe Verdi");
        metodo.setCvv(456);
        metodo.setIndirizzo("Via Milano 200");
        metodo.setUsername("userTest");
        metodo.setMetodoPagamento("credito");
        metodo.setEmail("giuseppe@example.com");

        // Aggiungiamo il metodo di pagamento
        metodoDiPagamentoDAO.addMetodoDiPagamento(metodo);
        System.out.println("Metodo di pagamento aggiunto: " + metodo);

        // Verifica che il metodo di pagamento sia stato aggiunto (verifica con getMetodoDiPagamentoByUsername)
        MetodoDiPagamento retrievedMetodo = metodoDiPagamentoDAO.getMetodoDiPagamentoByUsername("userTest");
        System.out.println("Metodo di pagamento per 'userTest': " + retrievedMetodo);

        assertNotNull(retrievedMetodo, "Il metodo di pagamento dovrebbe essere recuperato per l'utente 'userTest'");
        assertEquals("8765432187654321", retrievedMetodo.getnCarta(), "Il numero della carta dovrebbe essere corretto");
        assertEquals("Giuseppe Verdi", retrievedMetodo.getNomeIntestatario(), "Il nome intestatario dovrebbe essere corretto");
    }

*/

  /*  @Test
    @DisplayName("testUpdateMetodoDiPagamento - Aggiornamento di un metodo di pagamento")
    void testUpdateMetodoDiPagamento() throws SQLException {
        System.out.println("Aggiornamento di un metodo di pagamento");

        // Creiamo un metodo di pagamento per l'aggiornamento
        MetodoDiPagamento metodo = new MetodoDiPagamento();
        metodo.setnCarta("1234567812345678");
      //  metodo.setDataScadenza(Date.valueOf("2025-12-31"));
        metodo.setNomeIntestatario("Mario Rossi");
        metodo.setCvv(123);
        metodo.setIndirizzo("Via Roma 100");
        metodo.setUsername("userTest");
        metodo.setMetodoPagamento("credito");
        metodo.setEmail("test@example.com");

        // Aggiungiamo il metodo di pagamento
        metodoDiPagamentoDAO.addMetodoDiPagamento(metodo);

        // Aggiorniamo i dati del metodo di pagamento
        metodo.setIndirizzo("Via Napoli 200");
        metodo.setEmail("updated@example.com");

        // Aggiorniamo il metodo di pagamento
        metodoDiPagamentoDAO.updateMetodoDiPagamento(metodo);
        System.out.println("Metodo di pagamento aggiornato: " + metodo);

        // Recuperiamo il metodo di pagamento per 'userTest' e verifichiamo l'aggiornamento
        MetodoDiPagamento updatedMetodo = metodoDiPagamentoDAO.getMetodoDiPagamentoByUsername("userTest");
        assertNotNull(updatedMetodo, "Il metodo di pagamento dovrebbe essere recuperato dopo l'aggiornamento");
        assertEquals("Via Napoli 200", updatedMetodo.getIndirizzo(), "L'indirizzo del metodo di pagamento dovrebbe essere aggiornato");
        assertEquals("updated@example.com", updatedMetodo.getEmail(), "L'email del metodo di pagamento dovrebbe essere aggiornata");
    } */
    @Test
    @DisplayName("testGetMetodoDiPagamentoByUsername - Recupero metodo di pagamento per username")
    void testGetMetodoDiPagamentoByUsername() throws SQLException {
        System.out.println("Recupero metodo di pagamento per username");

        // Recuperiamo il metodo di pagamento per 'userTest'
        MetodoDiPagamento metodo = metodoDiPagamentoDAO.getMetodoDiPagamentoByUsername("userTest");
        System.out.println("Metodo di pagamento per 'userTest': " + metodo);

        assertNotNull(metodo, "Il metodo di pagamento non dovrebbe essere nullo");
        assertEquals("1234567812345678", metodo.getnCarta(), "Il numero della carta dovrebbe essere corretto");
        assertEquals("Mario Rossi", metodo.getNomeIntestatario(), "Il nome intestatario dovrebbe essere corretto");
    }

}
