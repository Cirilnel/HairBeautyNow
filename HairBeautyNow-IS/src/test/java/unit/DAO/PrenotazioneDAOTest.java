package unit.DAO;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.dao.PrenotazioneDAO;
import it.unisa.application.model.dao.ProfessionistaDAO;
import it.unisa.application.model.entity.Prenotazione;
import it.unisa.application.model.entity.Professionista;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PrenotazioneDAOTest {

    private PrenotazioneDAO prenotazioneDAO;
    private ProfessionistaDAO professionistaDAO;

    @BeforeAll
    static void globalSetup() {
        // Configura l'H2 in-memory DB tramite la classe DatabaseSetupForTest
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        // Istanzia i DAO
        prenotazioneDAO = new PrenotazioneDAO();
        professionistaDAO = new ProfessionistaDAO();

        // Popola il database con i dati di base necessari per i test
        populateDatabase();
    }

    /**
     * Ripulisce e popola il database con dati minimi necessari per testare PrenotazioneDAO.
     * Assicura che esistano alcune prenotazioni e professionisti.
     */
    private void populateDatabase() {
        try (Connection conn = DataSourceSingleton.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {

            // 1) Elimina i dati esistenti per avere un ambiente pulito
            String cleanupScript = """
                DELETE FROM Prenotazione;
                DELETE FROM fascia_oraria;
                DELETE FROM professionista;
                DELETE FROM sede;
                DELETE FROM Servizio;
                DELETE FROM UtenteAcquirente;
            """;
            stmt.execute(cleanupScript);

            // 2) Inserisci i dati minimi di esempio
            String setupScript = """
                -- Inseriamo alcune sedi
                INSERT INTO sede (id, indirizzo, nome, città)
                VALUES (1, 'Via Roma 100', 'SedeTest', 'Napoli');

                -- Inseriamo alcuni professionisti
                INSERT INTO professionista (id, nome, sedeId)
                VALUES (10, 'Valeria', 1);

                -- Inseriamo alcuni Servizi
                INSERT INTO Servizio (nome, prezzo, tipo, descrizione)
                VALUES ('TaglioCapelli', 15.50, 'Hair styling', 'Taglio base');

                -- Inseriamo un UtenteAcquirente
                INSERT INTO UtenteAcquirente (username, email, password, nome, cognome, citta)
                VALUES ('userTest', 'test@example.com', 'password123', 'Test', 'User', 'Napoli');

                -- Inseriamo una Prenotazione per professionista ID=10
                INSERT INTO Prenotazione (servizioName, professionistaId, data, username, prezzo)
                VALUES ('TaglioCapelli', 10, '2025-02-15 10:00:00', 'userTest', 15.50);
            """;
            stmt.execute(setupScript);

        } catch (Exception e) {
            throw new RuntimeException("Errore nel setup del database per i test", e);
        }
    }

    @Test
    @DisplayName("testAddPrenotazione - Aggiunta di una nuova prenotazione")
    void testAddPrenotazione() throws Exception {
        System.out.println("Aggiunta di una nuova prenotazione");

        // Creiamo una nuova prenotazione
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setServizioName("TaglioCapelli");
        prenotazione.setProfessionistaId(10);
        prenotazione.setData(LocalDateTime.of(2025, 2, 16, 9, 0));
        prenotazione.setUsername("userTest");
        prenotazione.setPrezzo(15.50);

        // Aggiungiamo la prenotazione
        prenotazioneDAO.addPrenotazione(prenotazione);
        System.out.println("Prenotazione aggiunta: " + prenotazione);

        // Verifichiamo che la prenotazione sia stata aggiunta
        List<Prenotazione> prenotazioni = prenotazioneDAO.getPrenotazioniByUsername("userTest");
        System.out.println("Prenotazioni per 'userTest': " + prenotazioni);
        assertEquals(2, prenotazioni.size(), "Dovrebbero esserci 2 prenotazioni per l'utente 'userTest'");
    }

    @Test
    @DisplayName("testGetPrenotazioniByUsername - Recupero prenotazioni per username")
    void testGetPrenotazioniByUsername() throws Exception {
        System.out.println("Recupero prenotazioni per username");

        // Recuperiamo le prenotazioni per "userTest"
        List<Prenotazione> prenotazioni = prenotazioneDAO.getPrenotazioniByUsername("userTest");
        System.out.println("Prenotazioni per 'userTest': " + prenotazioni);

        assertNotNull(prenotazioni, "La lista delle prenotazioni non dovrebbe essere nulla");
        assertEquals(1, prenotazioni.size(), "L'utente dovrebbe avere 1 prenotazione");
    }

    @Test
    @DisplayName("testGetPrenotazioniByProfessionisti - Recupero prenotazioni per una lista di professionisti")
    void testGetPrenotazioniByProfessionisti() throws Exception {
        System.out.println("Recupero prenotazioni per una lista di professionisti");

        // Recuperiamo i professionisti
        List<Professionista> professionisti = professionistaDAO.getProfessionistiBySede(1);
        System.out.println("Professionisti trovati: " + professionisti);

        // Recuperiamo le prenotazioni per i professionisti
        List<Prenotazione> prenotazioni = prenotazioneDAO.getPrenotazioniByProfessionisti(professionisti);
        System.out.println("Prenotazioni trovate: " + prenotazioni);
        assertEquals(1, prenotazioni.size(), "Dovrebbe esserci 1 prenotazione per i professionisti di sede 1");
    }

    @Test
    @DisplayName("testRimuoviPrenotazione - Rimozione di una prenotazione")
    void testRimuoviPrenotazione() throws Exception {
        System.out.println("Rimozione di una prenotazione");

        // Recuperiamo la prenotazione da rimuovere
        List<Prenotazione> prenotazioni = prenotazioneDAO.getPrenotazioniByUsername("userTest");
        Prenotazione prenotazione = prenotazioni.get(0);
        System.out.println("Prenotazione da rimuovere: " + prenotazione);

        // Rimuoviamo la prenotazione
        boolean result = prenotazioneDAO.rimuoviPrenotazione(prenotazione.getId());
        System.out.println("Risultato rimozione: " + result);
        assertTrue(result, "La prenotazione dovrebbe essere rimossa con successo");

        // Verifichiamo che la prenotazione sia stata rimossa
        prenotazioni = prenotazioneDAO.getPrenotazioniByUsername("userTest");
        System.out.println("Prenotazioni per 'userTest' dopo rimozione: " + prenotazioni);
        assertEquals(0, prenotazioni.size(), "L'utente non dovrebbe avere prenotazioni dopo la rimozione");
    }

    @Test
    @DisplayName("testGetPrenotazioneById - Recupero di una prenotazione per ID")
    void testGetPrenotazioneById() throws Exception {
        System.out.println("Recupero di una prenotazione per ID");

        // Recuperiamo una prenotazione tramite ID
        List<Prenotazione> prenotazioni = prenotazioneDAO.getPrenotazioniByUsername("userTest");
        Prenotazione prenotazione = prenotazioni.get(0);
        Prenotazione prenotazioneRecuperata = prenotazioneDAO.getPrenotazioneById(prenotazione.getId());
        System.out.println("Prenotazione recuperata per ID: " + prenotazioneRecuperata);

        assertNotNull(prenotazioneRecuperata, "La prenotazione recuperata non dovrebbe essere nulla");
        assertEquals(prenotazione.getId(), prenotazioneRecuperata.getId(), "Gli ID delle prenotazioni dovrebbero coincidere");
    }
}
