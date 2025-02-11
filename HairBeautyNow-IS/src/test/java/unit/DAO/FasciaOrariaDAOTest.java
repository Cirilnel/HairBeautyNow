package unit.DAO;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.dao.FasciaOrariaDAO;
import it.unisa.application.model.entity.FasciaOraria;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FasciaOrariaDAOTest {

    private FasciaOrariaDAO fasciaOrariaDAO;

    @BeforeAll
    static void globalSetup() {
        // Configura l'H2 in-memory DB tramite la classe DatabaseSetupForTest
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        // Istanzia il DAO
        fasciaOrariaDAO = new FasciaOrariaDAO();

        // Popola il database con i dati di base necessari per i test
        populateDatabase();
    }

    /**
     * Ripulisce e popola il database con dati minimi necessari per testare FasciaOrariaDAO.
     * Assicura che esistano alcune fasce orarie e professionisti.
     */
    private void populateDatabase() {
        try (Connection conn = DataSourceSingleton.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {

            // 1) Elimina i dati esistenti per avere un ambiente pulito
            String cleanupScript = """
            DELETE FROM fascia_oraria;
            DELETE FROM professionista;
            DELETE FROM sede;
        """;
            stmt.execute(cleanupScript);

            // 2) Inserisci i dati minimi di esempio
            String setupScript = """
            -- Inseriamo una sede
            INSERT INTO sede (id, indirizzo, nome, città)
            VALUES (1, 'Via Roma 100', 'SedeTest', 'Napoli');

            -- Inseriamo alcuni professionisti associati a una sede
            INSERT INTO professionista (id, nome, sedeId)
            VALUES (10, 'Valeria', 1);

            -- Inseriamo alcune fasce orarie per il professionista
            INSERT INTO fascia_oraria (professionista_id, giorno, fascia, disponibile)
            VALUES (10, '2025-02-15', '09:00-10:00', TRUE),
                   (10, '2025-02-15', '10:00-11:00', FALSE),
                   (10, '2025-02-16', '09:00-10:00', TRUE);
        """;
            stmt.execute(setupScript);

        } catch (Exception e) {
            throw new RuntimeException("Errore nel setup del database per i test", e);
        }
    }


    @Test
    @DisplayName("testGetFasciaByProfessionistaAndGiorno - Recupero fascia oraria per professionista e giorno")
    void testGetFasciaByProfessionistaAndGiorno() throws Exception {
        System.out.println("Recupero fascia oraria per professionista e giorno");

        // Recuperiamo una fascia oraria
        FasciaOraria fascia = fasciaOrariaDAO.getFasciaByProfessionistaAndGiorno(10, LocalDate.of(2025, 2, 15), "09:00-10:00");
        System.out.println("Fascia oraria trovata: " + fascia);

        assertNotNull(fascia, "La fascia oraria non dovrebbe essere nulla");
        assertEquals(10, fascia.getProfessionistaId(), "L'ID del professionista dovrebbe essere 10");
        assertEquals(LocalDate.of(2025, 2, 15), fascia.getGiorno(), "Il giorno dovrebbe essere 2025-02-15");
        assertEquals("09:00-10:00", fascia.getFascia(), "La fascia oraria dovrebbe essere 09:00-10:00");
        assertTrue(fascia.isDisponibile(), "La fascia oraria dovrebbe essere disponibile");
    }

    @Test
    @DisplayName("testGetFasceOrarieByProfessionista - Recupero tutte le fasce orarie di un professionista")
    void testGetFasceOrarieByProfessionista() throws Exception {
        System.out.println("Recupero tutte le fasce orarie di un professionista");

        // Recuperiamo tutte le fasce orarie per il professionista con ID 10
        List<FasciaOraria> fasceOrarie = fasciaOrariaDAO.getFasceOrarieByProfessionista(10);
        System.out.println("Fasce orarie trovate: " + fasceOrarie);

        assertNotNull(fasceOrarie, "La lista delle fasce orarie non dovrebbe essere nulla");
        assertEquals(3, fasceOrarie.size(), "Dovrebbero esserci 3 fasce orarie per il professionista con ID 10");
    }

    @Test
    @DisplayName("testAggiornaFasciaOraria - Aggiornamento fascia oraria")
    void testAggiornaFasciaOraria() throws Exception {
        System.out.println("Aggiornamento fascia oraria");

        // Recuperiamo una fascia oraria
        FasciaOraria fascia = fasciaOrariaDAO.getFasciaByProfessionistaAndGiorno(10, LocalDate.of(2025, 2, 15), "09:00-10:00");
        System.out.println("Fascia oraria prima dell'aggiornamento: " + fascia);

        // Aggiorniamo la fascia oraria
        fascia.setDisponibile(false);
        boolean result = fasciaOrariaDAO.aggiornaFasciaOraria(fascia);
        System.out.println("Risultato aggiornamento: " + result);

        // Verifichiamo che la fascia oraria sia stata aggiornata
        FasciaOraria fasciaAggiornata = fasciaOrariaDAO.getFasciaByProfessionistaAndGiorno(10, LocalDate.of(2025, 2, 15), "09:00-10:00");
        assertNotNull(fasciaAggiornata, "La fascia oraria aggiornata non dovrebbe essere nulla");
        assertFalse(fasciaAggiornata.isDisponibile(), "La fascia oraria dovrebbe essere non disponibile dopo l'aggiornamento");
    }

    @Test
    @DisplayName("testGetFasciaOraria - Recupero fascia oraria per professionista, giorno e fascia")
    void testGetFasciaOraria() throws Exception {
        System.out.println("Recupero fascia oraria per professionista, giorno e fascia");

        // Recuperiamo una fascia oraria specifica
        FasciaOraria fascia = fasciaOrariaDAO.getFasciaOraria(10, LocalDate.of(2025, 2, 15), "09:00-10:00");
        System.out.println("Fascia oraria trovata: " + fascia);

        assertNotNull(fascia, "La fascia oraria non dovrebbe essere nulla");
        assertEquals("09:00-10:00", fascia.getFascia(), "La fascia oraria dovrebbe essere 09:00-10:00");
    }
}
