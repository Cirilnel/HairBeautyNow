package unit.DAO;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.dao.ProfessionistaDAO;
import it.unisa.application.model.entity.Professionista;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProfessionistaDAOTest {

    private ProfessionistaDAO professionistaDAO;

    @BeforeAll
    static void globalSetup() {
        // Configura l'H2 in-memory DB tramite la classe DatabaseSetupForTest
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        // Istanzia il DAO
        professionistaDAO = new ProfessionistaDAO();

        // Popola il database con i dati di base necessari per i test
        populateDatabase();
    }

    /**
     * Ripulisce e popola il database con dati minimi necessari per testare ProfessionistaDAO.
     * In particolare, assicura che esista l'utente "userTest" prima di inserire una prenotazione.
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

                INSERT INTO sede (id, indirizzo, nome, città)
                VALUES (2, 'Via Milano 200', 'SedeSeconda', 'Milano');

                -- Inseriamo alcuni professionisti
                INSERT INTO professionista (id, nome, sedeId)
                VALUES (10, 'Valeria', 1);

                INSERT INTO professionista (id, nome, sedeId)
                VALUES (11, 'Giulia', 1);

                INSERT INTO professionista (id, nome, sedeId)
                VALUES (12, 'Sofia', 2);

                -- Inseriamo fasce orarie per il professionista con ID=10
                INSERT INTO fascia_oraria (professionista_id, giorno, fascia, disponibile)
                VALUES (10, '2025-02-10', '08:00-08:30', TRUE),
                       (10, '2025-02-10', '08:30-09:00', TRUE);

                -- Inseriamo alcuni Servizi
                INSERT INTO Servizio (nome, prezzo, tipo, descrizione)
                VALUES ('TaglioCapelli', 15.50, 'Hair styling', 'Taglio base'),
                       ('Manicure', 20.00, 'Nail care', 'Cura unghie e mani');

                -- Inseriamo un UtenteAcquirente per evitare errori di FK su Prenotazione
                INSERT INTO UtenteAcquirente (username, email, password, nome, cognome, citta)
                VALUES ('userTest', 'test@example.com', 'password123', 'Test', 'User', 'Napoli');

                -- Inseriamo una Prenotazione su professionista ID=12 (ha 1 prenotazione)
                INSERT INTO Prenotazione (servizioName, professionistaId, prezzo, data, username)
                VALUES ('Manicure', 12, 20.00, '2025-02-15 10:00:00', 'userTest');
            """;
            stmt.execute(setupScript);

        } catch (Exception e) {
            throw new RuntimeException("Errore nel setup del database per i test", e);
        }
    }

    @Test
    @DisplayName("testGetProfessionistaById - Recupero nome professionista tramite ID")
    void testGetProfessionistaById() {
        System.out.println("Recupero nome professionista tramite ID");
        // Professionista con ID=10 -> "Mario Rossi"
        String nome = professionistaDAO.getProfessionistaById(10);
        System.out.println("Nome recuperato per professionista con ID=10: " + nome);

        assertNotNull(nome, "Il nome del professionista non dovrebbe essere null");
        assertEquals("Valeria", nome, "Il nome dovrebbe corrispondere a 'Valeria'");
    }

    @Test
    @DisplayName("testGetIndirizzoBySedeId - Recupero indirizzo da sedeId")
    void testGetIndirizzoBySedeId() {
        System.out.println(" Recupero indirizzo da sedeId");
        // Sede ID=1 -> "Via Roma 100"
        String indirizzo = professionistaDAO.getIndirizzoBySedeId(1);
        System.out.println("Indirizzo recuperato per sedeId=1: " + indirizzo);

        assertNotNull(indirizzo, "L'indirizzo non dovrebbe essere null");
        assertEquals("Via Roma 100", indirizzo, "L'indirizzo dovrebbe essere 'Via Roma 100'");
    }

    @Test
    @DisplayName("testGetProfessionistiBySede - Recupero sedeId da professionistaId")
    void testGetProfessionistiBySede() {
        System.out.println("Recupero sedeId da professionistaId");
        // Per la sedeId=1, ci aspettiamo ID=10 e ID=11
        List<Professionista> professionistiSede1 = professionistaDAO.getProfessionistiBySede(1);
        System.out.println("Professionisti trovati per sedeId=1: " + professionistiSede1);
        assertEquals(2, professionistiSede1.size(), "Dovrebbero esserci 2 professionisti con sedeId=1");

        // Per la sedeId=2, ci aspettiamo ID=12
        List<Professionista> professionistiSede2 = professionistaDAO.getProfessionistiBySede(2);
        System.out.println("Professionisti trovati per sedeId=2: " + professionistiSede2);
        assertEquals(1, professionistiSede2.size(), "Dovrebbe esserci 1 professionista con sedeId=2");
    }

    @Test
    @DisplayName("testGetSedeIdByProfessionistaId - Recupero sedeId da professionistaId")
    void testGetSedeIdByProfessionistaId() {
        System.out.println("Recupero sedeId da professionistaId");
        // Professionista ID=10 ha sedeId=1
        int sedeId = professionistaDAO.getSedeIdByProfessionistaId(10);
        System.out.println("sedeId per professionistaId=10: " + sedeId);

        assertEquals(1, sedeId, "Il sedeId dovrebbe essere 1 per professionista ID=10");
    }

    @Test
    @DisplayName("testRimuoviFasceOrarie - Rimozione fasce orarie di un professionista")
    void testRimuoviFasceOrarie() {
        System.out.println("Rimozione fasce orarie di un professionista");
        // Professionista ID=10 ha 2 fasce orarie
        boolean result = professionistaDAO.rimuoviFasceOrarie(10);
        System.out.println("Risultato rimozione fasce orarie per ID=10: " + result);

        assertTrue(result, "Dovrebbe restituire true perché esistono fasce orarie da rimuovere");
    }

    @Test
    @DisplayName("testRimuoviProfessionista - Rimozione di un professionista (senza prenotazioni)")
    void testRimuoviProfessionista() {
        System.out.println(" Rimozione di un professionista (senza prenotazioni)");
        // Professionista ID=10 non ha prenotazioni, quindi la rimozione dovrebbe andare a buon fine
        boolean removeResult = professionistaDAO.rimuoviProfessionista(10);
        System.out.println("Risultato rimozione professionista ID=10: " + removeResult);

        assertTrue(removeResult, "La rimozione dovrebbe avere successo se non ha prenotazioni");
    }

    @Test
    @DisplayName("testInsertProfessionista - Inserimento di un nuovo professionista + fasce")
    void testInsertProfessionista() {
        System.out.println("Inserimento di un nuovo professionista + fasce");

        // Creiamo un professionista
        Professionista nuovo = new Professionista();
        nuovo.setNome("Alessandra");
        nuovo.setSedeId(1);

        // Inseriamo
        professionistaDAO.insertProfessionista(nuovo);
        System.out.println("Professionista inserito: " + nuovo);

        // Cerchiamo tra i professionisti di sedeId=1
        List<Professionista> professionistiSede1 = professionistaDAO.getProfessionistiBySede(1);
        System.out.println("Dopo l'inserimento, professionisti nella sede 1: " + professionistiSede1);

        // Verifichiamo la presenza del nuovo professionista
        boolean found = professionistiSede1.stream()
                .anyMatch(p -> "Alessandra".equals(p.getNome()));
        assertTrue(found, "Dovrebbe esistere un professionista con nome 'Alessandra'");
    }

    @Test
    @DisplayName("testGetPrezzoByServizio - Recupero prezzo da servizioName")
    void testGetPrezzoByServizio() {
        System.out.println("Recupero prezzo da servizioName");
        // Nel setup, "TaglioCapelli" = 15.50, "Manicure" = 20.00
        double prezzoTaglio = professionistaDAO.getPrezzoByServizio("TaglioCapelli");
        System.out.println("Prezzo del servizio 'TaglioCapelli': " + prezzoTaglio);
        assertEquals(15.50, prezzoTaglio, 0.01, "Il prezzo dovrebbe essere 15.50");

        double prezzoManicure = professionistaDAO.getPrezzoByServizio("Manicure");
        System.out.println("Prezzo del servizio 'Manicure': " + prezzoManicure);
        assertEquals(20.00, prezzoManicure, 0.01, "Il prezzo dovrebbe essere 20.00");
    }

    @Test
    @DisplayName("testHasPrenotazioni - Verifica se un professionista ha prenotazioni")
    void testHasPrenotazioni() {
        System.out.println("Verifica se un professionista ha prenotazioni");
        // Professionista ID=12 ha 1 prenotazione inserita
        boolean hasPrenotazioni12 = professionistaDAO.hasPrenotazioni(12);
        System.out.println("Professionista ID=12 ha prenotazioni? " + hasPrenotazioni12);
        assertTrue(hasPrenotazioni12, "Dovrebbe avere almeno 1 prenotazione");

        // Professionista ID=10 non ne ha
        boolean hasPrenotazioni10 = professionistaDAO.hasPrenotazioni(10);
        System.out.println("Professionista ID=10 ha prenotazioni? " + hasPrenotazioni10);
        assertFalse(hasPrenotazioni10, "Non dovrebbe avere prenotazioni");
    }
}