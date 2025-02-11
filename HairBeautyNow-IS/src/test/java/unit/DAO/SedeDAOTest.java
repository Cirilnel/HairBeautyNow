package unit.DAO;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.dao.SedeDAO;
import it.unisa.application.model.entity.Sede;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SedeDAOTest {

    private SedeDAO sedeDAO;

    @BeforeAll
    static void globalSetup() {
        // Configura l'H2 in-memory DB
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        sedeDAO = new SedeDAO();
        // Pulisce la tabella "sede" prima di ogni test
        try (Connection conn = DataSourceSingleton.getInstance().getConnection()) {
            conn.createStatement().execute("DELETE FROM sede");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        // Pulisce la tabella "sede" dopo ogni test
        try (Connection conn = DataSourceSingleton.getInstance().getConnection()) {
            conn.createStatement().execute("DELETE FROM sede");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Test
    @DisplayName("testGetSedeById - Recupero sede tramite ID")
    void testGetSedeById() {
        System.out.println("Recupero sede tramite ID");
        // Inserisce una sede di prova
        Sede sedeInserita = new Sede("Via Test 2", "Salone Test Due", "Milano", 0);
        int generatedID = sedeDAO.insertSedeAndReturnID(sedeInserita);
        System.out.println("Sede inserita con ID: " + generatedID);

        // Recupera la sede
        Sede sedeRecuperata = sedeDAO.getSedeById(generatedID);
        System.out.println("Sede recuperata dal DB: " + sedeRecuperata);

        // Verifiche
        assertNotNull(sedeRecuperata, "La sede recuperata non dovrebbe essere null");
        assertEquals("Via Test 2", sedeRecuperata.getIndirizzo());
        assertEquals("Salone Test Due", sedeRecuperata.getNome());
        assertEquals("Milano", sedeRecuperata.getCitta());
    }
    @Test
    @DisplayName("testGetAllSedi - Recupero di tutte le sedi")
    void testGetAllSedi() {
        System.out.println("Recupero di tutte le sedi");
        // Inserisce un paio di sedi
        Sede sede1 = new Sede("Via Uno", "Salone Uno", "Roma", 0);
        Sede sede2 = new Sede("Via Due", "Salone Due", "Napoli", 0);

        int id1 = sedeDAO.insertSedeAndReturnID(sede1);
        int id2 = sedeDAO.insertSedeAndReturnID(sede2);

        System.out.println("Inserite sedi con ID: " + id1 + " e " + id2);

        // Recupera tutte le sedi
        List<Sede> tutteLeSedi = sedeDAO.getAllSedi();
        System.out.println("Elenco sedi recuperate: " + tutteLeSedi);

        assertEquals(2, tutteLeSedi.size(), "Dovrebbero esserci 2 sedi in totale");
    }

    @Test
    @DisplayName("testInsertSedeAndReturnID - Inserimento sede e recupero ID")
    void testInsertSedeAndReturnID() {
        System.out.println("Inserimento sede e recupero ID");
        Sede sede = new Sede("Via Test 1", "Salone Test Uno", "Roma", 0);
        System.out.println("Sede da inserire: " + sede);

        int generatedID = sedeDAO.insertSedeAndReturnID(sede);
        System.out.println("ID generato dopo l'inserimento: " + generatedID);

        assertTrue(generatedID > 0, "L'ID generato dovrebbe essere maggiore di 0");
    }


    @Test
    @DisplayName("testGetSediByCitta - Recupero sedi filtrate per città")
    void testGetSediByCitta() {
        System.out.println("Recupero sedi filtrate per città");
        // Inserisce sedi in città diverse
        Sede sedeRoma1 = new Sede("Via Roma 1", "Salone Roma 1", "Roma", 0);
        Sede sedeRoma2 = new Sede("Via Roma 2", "Salone Roma 2", "Roma", 0);
        Sede sedeMilano = new Sede("Via Milano", "Salone Milano", "Milano", 0);

        sedeDAO.insertSedeAndReturnID(sedeRoma1);
        sedeDAO.insertSedeAndReturnID(sedeRoma2);
        sedeDAO.insertSedeAndReturnID(sedeMilano);

        System.out.println("Inserite sedi a Roma e Milano:");

        List<Sede> sediRoma = sedeDAO.getSediByCitta("Roma");
        System.out.println("Sedi trovate per la città di Roma: " + sediRoma);
        assertEquals(2, sediRoma.size(), "Dovrebbero esserci 2 sedi a Roma");

        List<Sede> sediMilanoResult = sedeDAO.getSediByCitta("Milano");
        System.out.println("Sedi trovate per la città di Milano: " + sediMilanoResult);
        assertEquals(1, sediMilanoResult.size(), "Dovrebbe esserci 1 sede a Milano");

    }

}