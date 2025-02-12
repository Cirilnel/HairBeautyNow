package integration.GestioneCatena;

import it.unisa.application.model.dao.UtenteGestoreSedeDAO;
import it.unisa.application.model.entity.UtenteGestoreSede;
import it.unisa.application.sottosistemi.GestioneCatena.service.GestioneGestoreService;
import org.junit.jupiter.api.*;
import org.mockito.*;
import unit.DAO.DatabaseSetupForTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GestioneGestoreServiceIntegrationTest {

    @Mock
    private UtenteGestoreSedeDAO utenteGestoreSedeDAOMock;

    private GestioneGestoreService gestioneGestoreService;

    @BeforeAll
    static void setupDatabase() {
        // Configura il database, se necessario
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gestioneGestoreService = new GestioneGestoreService(utenteGestoreSedeDAOMock);
    }

    @Test
    @DisplayName("TC01.1: Creazione Gestore - Successo")
    void testCreaGestoreSuccess() {
        // Dati di input
        UtenteGestoreSede nuovoGestore = new UtenteGestoreSede("gestore1", "password", 0);

        // Configura il mock per l'inserimento del nuovo gestore
        when(utenteGestoreSedeDAOMock.insert(nuovoGestore)).thenReturn(true);

        System.out.println("\nCreazione Gestore - Successo");
        System.out.println("Dati di input:");
        System.out.println("Username: " + nuovoGestore.getUsernameUGS());

        // Chiamata al metodo del servizio
        boolean risultato = gestioneGestoreService.creaGestore(nuovoGestore);

        // Verifica che il gestore sia stato creato con successo
        assertTrue(risultato, "Il gestore dovrebbe essere creato con successo.");
        verify(utenteGestoreSedeDAOMock, times(1)).insert(nuovoGestore);
        System.out.println("Gestore creato con successo.");
    }

    @Test
    @DisplayName("TC01.2: Assegna Sede al Gestore - Successo")
    void testAssegnaSedeSuccess() {
        String usernameUGS = "gestore1";
        int sedeID = 1;

        // Configura il mock per l'assegnazione della sede al gestore
        doNothing().when(utenteGestoreSedeDAOMock).assegnaSede(usernameUGS, sedeID);

        System.out.println("\nAssegna Sede al Gestore - Successo");
        System.out.println("Gestore: " + usernameUGS + " - Sede ID: " + sedeID);

        // Chiamata al metodo del servizio
        gestioneGestoreService.assegnaSede(usernameUGS, sedeID);

        // Verifica che l'assegnazione sia stata eseguita correttamente
        verify(utenteGestoreSedeDAOMock, times(1)).assegnaSede(usernameUGS, sedeID);
        System.out.println("Sede assegnata con successo.");
    }

    @Test
    @DisplayName("TC01.3: Licenzia Gestore - Successo")
    void testLicenziaGestoreSuccess() {
        String usernameUGS = "gestore1";

        // Configura il mock per licenziare il gestore
        doNothing().when(utenteGestoreSedeDAOMock).licenziaGestore(usernameUGS);

        System.out.println("\nLicenzia Gestore - Successo");
        System.out.println("Gestore: " + usernameUGS);

        // Chiamata al metodo del servizio
        gestioneGestoreService.licenziaGestore(usernameUGS);

        // Verifica che il licenziamento sia stato eseguito correttamente
        verify(utenteGestoreSedeDAOMock, times(1)).licenziaGestore(usernameUGS);
        System.out.println("Gestore licenziato con successo.");
    }

    @Test
    @DisplayName("TC02.1: Recupera Gestori Senza Sede - Successo")
    void testGetGestoriSenzaSedeSuccess() {
        // Simuliamo una lista di gestori senza sede
        UtenteGestoreSede gestore1 = new UtenteGestoreSede("gestore1", "password", 0);
        UtenteGestoreSede gestore2 = new UtenteGestoreSede("gestore2", "password", 0);
        List<UtenteGestoreSede> gestoriSenzaSede = List.of(gestore1, gestore2);

        // Configura il mock per recuperare i gestori senza sede
        when(utenteGestoreSedeDAOMock.getGestoriSenzaSede()).thenReturn(gestoriSenzaSede);

        System.out.println("\nRecupero Gestori Senza Sede - Successo");

        // Chiamata al metodo del servizio
        List<UtenteGestoreSede> risultato = gestioneGestoreService.getGestoriSenzaSede();

        // Verifica che i gestori siano stati recuperati correttamente
        assertNotNull(risultato, "La lista dei gestori senza sede non dovrebbe essere null.");
        assertEquals(2, risultato.size(), "La lista dovrebbe contenere 2 gestori.");
        System.out.println("Gestori senza sede recuperati:");
        risultato.forEach(g -> System.out.println(g.getUsernameUGS()));

        verify(utenteGestoreSedeDAOMock, times(1)).getGestoriSenzaSede();
    }

    @Test
    @DisplayName("TC02.2: Recupera Gestori Con Sede - Successo")
    void testGetGestoriConSedeSuccess() {
        // Simuliamo una lista di gestori con sede
        UtenteGestoreSede gestore1 = new UtenteGestoreSede("gestore1", "password", 1);
        UtenteGestoreSede gestore2 = new UtenteGestoreSede("gestore2", "password", 1);
        List<UtenteGestoreSede> gestoriConSede = List.of(gestore1, gestore2);

        // Configura il mock per recuperare i gestori con sede
        when(utenteGestoreSedeDAOMock.getGestoriConSede()).thenReturn(gestoriConSede);

        System.out.println("\nRecupero Gestori Con Sede - Successo");

        // Chiamata al metodo del servizio
        List<UtenteGestoreSede> risultato = gestioneGestoreService.getGestoriConSede();

        // Verifica che i gestori siano stati recuperati correttamente
        assertNotNull(risultato, "La lista dei gestori con sede non dovrebbe essere null.");
        assertEquals(2, risultato.size(), "La lista dovrebbe contenere 2 gestori.");
        System.out.println("Gestori con sede recuperati:");
        risultato.forEach(g -> System.out.println(g.getUsernameUGS()));

        verify(utenteGestoreSedeDAOMock, times(1)).getGestoriConSede();
    }
}
