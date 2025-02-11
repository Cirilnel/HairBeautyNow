package unit.GestioneCatena;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import it.unisa.application.model.dao.UtenteGestoreSedeDAO;
import it.unisa.application.model.entity.UtenteGestoreSede;
import it.unisa.application.sottosistemi.GestioneCatena.service.GestioneGestoreService;
import org.junit.jupiter.api.*;
import unit.DAO.DatabaseSetupForTest;

import java.util.*;

@DisplayName("Test per il servizio GestioneGestoreService")
public class GestioneGestoreServiceTest {

    private GestioneGestoreService gestioneGestoreService;
    private UtenteGestoreSedeDAO utenteGestoreSedeDAOMock;
    @BeforeAll
    static void globalSetup() {
        DatabaseSetupForTest.configureH2DataSource();
        System.out.println("Datasource H2 configurato per i test.");
    }
    @BeforeEach
    void setUp() {
        // Mock del DAO
        utenteGestoreSedeDAOMock = mock(UtenteGestoreSedeDAO.class);

        // Inizializzazione del servizio con il DAO mockato tramite costruttore
        gestioneGestoreService = new GestioneGestoreService(utenteGestoreSedeDAOMock);
    }

    @Test
    @DisplayName("Creazione di un nuovo gestore - Successo")
    void testCreaGestoreSuccess() {
        System.out.println("Test: Creazione di un nuovo gestore - Successo");

        // Prepara il dato mockato con il parametro sedeID
        UtenteGestoreSede nuovoGestore = new UtenteGestoreSede("john_doe", "password123", 1);

        // Stampa del dato mockato
        System.out.println("Dati mockati per il test 'Creazione di un nuovo gestore':");
        System.out.println("Username: " + nuovoGestore.getUsernameUGS());
        System.out.println("SedeID: " + nuovoGestore.getSedeID());

        // Simula il comportamento del DAO per il metodo insert
        when(utenteGestoreSedeDAOMock.insert(nuovoGestore)).thenReturn(true);

        // Chiamata al servizio
        boolean result = gestioneGestoreService.creaGestore(nuovoGestore);

        // Stampa del risultato
        System.out.println("Risultato del test 'Creazione di un nuovo gestore': " + result);

        // Verifica il risultato
        assertTrue(result, "Il gestore dovrebbe essere creato con successo.");
    }

    @Test
    @DisplayName("Assegnazione sede a un gestore - Successo")
    void testAssegnaSedeSuccess() {
        System.out.println("Test: Assegnazione sede a un gestore - Successo");

        // Dati per il test
        String usernameUGS = "john_doe";
        int sedeID = 1;

        // Simula il comportamento del DAO per il metodo assegnaSede
        doNothing().when(utenteGestoreSedeDAOMock).assegnaSede(usernameUGS, sedeID);

        // Chiamata al servizio
        gestioneGestoreService.assegnaSede(usernameUGS, sedeID);

        // Verifica che il metodo sia stato chiamato
        verify(utenteGestoreSedeDAOMock, times(1)).assegnaSede(usernameUGS, sedeID);
        System.out.println("Risultato del test 'Assegnazione sede a un gestore': Assegnata sede ID " + sedeID + " al gestore " + usernameUGS);
    }

    @Test
    @DisplayName("Licenziamento di un gestore - Successo")
    void testLicenziaGestoreSuccess() {
        System.out.println("Test: Licenziamento di un gestore - Successo");

        // Dati per il test
        String usernameUGS = "john_doe";

        // Simula il comportamento del DAO per il metodo licenziaGestore
        doNothing().when(utenteGestoreSedeDAOMock).licenziaGestore(usernameUGS);

        // Chiamata al servizio
        gestioneGestoreService.licenziaGestore(usernameUGS);

        // Verifica che il metodo sia stato chiamato
        verify(utenteGestoreSedeDAOMock, times(1)).licenziaGestore(usernameUGS);
        System.out.println("Risultato del test 'Licenziamento di un gestore': Licenziato gestore " + usernameUGS);
    }

    @Test
    @DisplayName("Recupero gestori senza sede - Successo")
    void testGetGestoriSenzaSedeSuccess() {
        System.out.println("Test: Recupero gestori senza sede - Successo");

        // Prepara i dati mockati con sedeID
        List<UtenteGestoreSede> gestoriSenzaSedeMock = new ArrayList<>();
        gestoriSenzaSedeMock.add(new UtenteGestoreSede("john_doe", "password123", null));
        gestoriSenzaSedeMock.add(new UtenteGestoreSede("jane_doe", "password456", null));

        // Stampa dei dati mockati
        System.out.println("Dati mockati per il test 'Recupero gestori senza sede':");
        for (UtenteGestoreSede gestore : gestoriSenzaSedeMock) {
            System.out.println("Username: " + gestore.getUsernameUGS());
        }

        // Simula il comportamento del DAO per il metodo getGestoriSenzaSede
        when(utenteGestoreSedeDAOMock.getGestoriSenzaSede()).thenReturn(gestoriSenzaSedeMock);

        // Chiamata al servizio
        List<UtenteGestoreSede> result = gestioneGestoreService.getGestoriSenzaSede();

        // Stampa del risultato
        System.out.println("Risultato del test 'Recupero gestori senza sede':");
        for (UtenteGestoreSede gestore : result) {
            System.out.println("Username: " + gestore.getUsernameUGS());
        }

        // Verifica il risultato
        assertNotNull(result);
        assertEquals(2, result.size(), "Dovrebbero esserci 2 gestori senza sede.");
    }

    @Test
    @DisplayName("Recupero gestori con sede - Successo")
    void testGetGestoriConSedeSuccess() {
        System.out.println("Test: Recupero gestori con sede - Successo");

        // Prepara i dati mockati con sedeID
        List<UtenteGestoreSede> gestoriConSedeMock = new ArrayList<>();
        gestoriConSedeMock.add(new UtenteGestoreSede("john_doe", "password123", 1));
        gestoriConSedeMock.add(new UtenteGestoreSede("jane_doe", "password456", 2));

        // Stampa dei dati mockati
        System.out.println("Dati mockati per il test 'Recupero gestori con sede':");
        for (UtenteGestoreSede gestore : gestoriConSedeMock) {
            System.out.println("Username: " + gestore.getUsernameUGS());
        }

        // Simula il comportamento del DAO per il metodo getGestoriConSede
        when(utenteGestoreSedeDAOMock.getGestoriConSede()).thenReturn(gestoriConSedeMock);

        // Chiamata al servizio
        List<UtenteGestoreSede> result = gestioneGestoreService.getGestoriConSede();

        // Stampa del risultato
        System.out.println("Risultato del test 'Recupero gestori con sede':");
        for (UtenteGestoreSede gestore : result) {
            System.out.println("Username: " + gestore.getUsernameUGS());
        }

        // Verifica il risultato
        assertNotNull(result);
        assertEquals(2, result.size(), "Dovrebbero esserci 2 gestori con sede.");
    }
}
