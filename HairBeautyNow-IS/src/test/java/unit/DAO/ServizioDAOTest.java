package unit.DAO;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.dao.ServizioDAO;
import it.unisa.application.model.entity.Servizio;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ServizioDAOTest {

    private ServizioDAO servizioDAO;

    @BeforeAll
    static void globalSetup() {
        // Configura l'H2 in-memory DB
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        servizioDAO = new ServizioDAO();
        // Pulisce la tabella "Servizi" prima di ogni test
        try (Connection conn = DataSourceSingleton.getInstance().getConnection()) {
            conn.createStatement().execute("DELETE FROM Servizio");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        // Pulisce la tabella "Servizi" dopo ogni test
        try (Connection conn = DataSourceSingleton.getInstance().getConnection()) {
            conn.createStatement().execute("DELETE FROM Servizio");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("testGetAllServizi - Recupero di tutti i servizi")
    void testGetAllServizi() {
        System.out.println("Recupero di tutti i servizi");

        // Inserisce alcuni servizi
        Servizio servizio1 = new Servizio("Massaggio", 50.0, "Benessere", "Massaggio rilassante");
        Servizio servizio2 = new Servizio("Taglio Capelli", 20.0, "Estetica", "Taglio capelli uomo");

        servizioDAO.insert(servizio1);
        servizioDAO.insert(servizio2);

        // Recupera tutti i servizi
        List<Servizio> tuttiIServizi = servizioDAO.getAll();
        System.out.println("Elenco servizi recuperati: " + tuttiIServizi);

        // Verifica che i servizi siano stati inseriti correttamente
        assertEquals(2, tuttiIServizi.size(), "Dovrebbero esserci 2 servizi in totale");
        assertTrue(tuttiIServizi.stream().anyMatch(s -> s.getNome().equals("Massaggio")), "Il servizio Massaggio dovrebbe essere presente");
        assertTrue(tuttiIServizi.stream().anyMatch(s -> s.getNome().equals("Taglio Capelli")), "Il servizio Taglio Capelli dovrebbe essere presente");
    }



    @Test
    @DisplayName("testInsertServizio - Inserimento servizio")
    void testInsertServizio() {
        System.out.println("Inserimento di un servizio");
        Servizio servizio = new Servizio("Pedicure", 30.0, "Estetica", "Trattamento piedi");
        System.out.println("Servizio da inserire: " + servizio);

        // Inserisce il servizio
        servizioDAO.insert(servizio);

        // Verifica che il servizio sia stato effettivamente inserito
        List<Servizio> servizi = servizioDAO.getAll();
        assertTrue(servizi.stream().anyMatch(s -> s.getNome().equals("Pedicure")), "Il servizio Pedicure dovrebbe essere presente");
    }



}
