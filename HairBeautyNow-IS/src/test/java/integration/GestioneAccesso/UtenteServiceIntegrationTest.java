package integration.GestioneAccesso;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import it.unisa.application.model.dao.UtenteAcquirenteDAO;
import it.unisa.application.model.dao.UtenteGestoreCatenaDAO;
import it.unisa.application.model.dao.UtenteGestoreSedeDAO;
import it.unisa.application.model.entity.UtenteAcquirente;
import it.unisa.application.model.entity.UtenteGestoreCatena;
import it.unisa.application.model.entity.UtenteGestoreSede;
import it.unisa.application.sottosistemi.GestioneAccesso.service.UtenteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.*;
import org.mockito.*;

public class UtenteServiceIntegrationTest {

    @Mock
    private UtenteAcquirenteDAO utenteAcquirenteDAOMock;

    @Mock
    private UtenteGestoreCatenaDAO utenteGestoreCatenaDAOMock;

    @Mock
    private UtenteGestoreSedeDAO utenteGestoreSedeDAOMock;

    private UtenteService utenteService;

    @BeforeAll
    static void globalSetup() {
        System.out.println("=== Configurazione iniziale per UtenteService ===");
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        utenteService = new UtenteService(utenteAcquirenteDAOMock, utenteGestoreCatenaDAOMock, utenteGestoreSedeDAOMock);
    }

    @AfterEach
    void tearDown() {
        System.out.println("=== Pulizia dopo ogni test ===\n");
    }

    @Test
    @DisplayName("TC01.1: Login Utente Acquirente - Successo")
    void testLoginAcquirenteSuccess() {
        String username = "user123";
        String password = "password123";
        UtenteAcquirente utenteAcquirente = new UtenteAcquirente(username, "test@example.com", password, "Nome", "Cognome", "Roma");

        when(utenteAcquirenteDAOMock.getByUsernameAndPassword(username, password)).thenReturn(utenteAcquirente);

        System.out.println("\n--- TEST LOGIN UTENTE ACQUIRENTE ---");
        System.out.println("Dati di input:");
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);

        Object result = utenteService.login(username, password);

        System.out.println("Risultato ottenuto: " + result);

        assertNotNull(result, "L'utente dovrebbe essere autenticato con successo.");
        assertTrue(result instanceof UtenteAcquirente, "L'oggetto restituito deve essere un UtenteAcquirente.");
        assertEquals(username, ((UtenteAcquirente) result).getUsername(), "Lo username deve corrispondere.");

        verify(utenteAcquirenteDAOMock, times(1)).getByUsernameAndPassword(username, password);
    }

    @Test
    @DisplayName("TC01.2: Login Gestore Catena - Successo")
    void testLoginGestoreCatenaSuccess() {
        String username = "gestoreCatena";
        String password = "passwordCatena";
        UtenteGestoreCatena gestoreCatena = new UtenteGestoreCatena(password, username);

        when(utenteGestoreCatenaDAOMock.getByUsername(username, password)).thenReturn(gestoreCatena);

        System.out.println("\n--- TEST LOGIN GESTORE CATENA ---");
        System.out.println("Dati di input:");
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);

        Object result = utenteService.login(username, password);

        System.out.println("Risultato ottenuto: " + result);

        assertNotNull(result, "Il gestore catena dovrebbe essere autenticato con successo.");
        assertTrue(result instanceof UtenteGestoreCatena, "L'oggetto restituito deve essere un UtenteGestoreCatena.");
        assertEquals(username, ((UtenteGestoreCatena) result).getUsername(), "Lo username deve corrispondere.");

        verify(utenteGestoreCatenaDAOMock, times(1)).getByUsername(username, password);
    }

    @Test
    @DisplayName("TC01.3: Login Gestore Sede - Successo")
    void testLoginGestoreSedeSuccess() {
        String username = "gestoreSede";
        String password = "passwordSede";
        Integer sedeID = 1;
        UtenteGestoreSede gestoreSede = new UtenteGestoreSede(username, password, sedeID);

        when(utenteGestoreSedeDAOMock.getByUsername(username, password)).thenReturn(gestoreSede);

        System.out.println("\n--- TEST LOGIN GESTORE SEDE ---");
        System.out.println("Dati di input:");
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        System.out.println("Sede ID: " + sedeID);

        Object result = utenteService.login(username, password);

        System.out.println("Risultato ottenuto: " + result);

        assertNotNull(result, "Il gestore sede dovrebbe essere autenticato con successo.");
        assertTrue(result instanceof UtenteGestoreSede, "L'oggetto restituito deve essere un UtenteGestoreSede.");
        assertEquals(username, ((UtenteGestoreSede) result).getUsernameUGS(), "Lo username deve corrispondere.");

        verify(utenteGestoreSedeDAOMock, times(1)).getByUsername(username, password);
    }

    @Test
    @DisplayName("TC01.4: Login Fallito - Nessun utente trovato")
    void testLoginFailure() {
        String username = "utenteNonEsistente";
        String password = "passwordErrata";

        when(utenteAcquirenteDAOMock.getByUsernameAndPassword(username, password)).thenReturn(null);
        when(utenteGestoreCatenaDAOMock.getByUsername(username, password)).thenReturn(null);
        when(utenteGestoreSedeDAOMock.getByUsername(username, password)).thenReturn(null);

        System.out.println("\n--- TEST LOGIN FALLITO ---");
        System.out.println("Dati di input:");
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);

        Object result = utenteService.login(username, password);

        System.out.println("Risultato ottenuto: " + result);

        assertNull(result, "Se nessun utente è trovato, il metodo deve restituire null.");

        verify(utenteAcquirenteDAOMock, times(1)).getByUsernameAndPassword(username, password);
        verify(utenteGestoreCatenaDAOMock, times(1)).getByUsername(username, password);
        verify(utenteGestoreSedeDAOMock, times(1)).getByUsername(username, password);
    }

    @Test
    @DisplayName("TC02.1: Logout - Successo")
    void testLogoutSuccess() {
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);

        when(requestMock.getSession()).thenReturn(sessionMock);

        System.out.println("\n--- TEST LOGOUT ---");

        utenteService.logout(requestMock);

        System.out.println("Logout eseguito con successo.");

        verify(sessionMock, times(1)).invalidate();
    }
}
