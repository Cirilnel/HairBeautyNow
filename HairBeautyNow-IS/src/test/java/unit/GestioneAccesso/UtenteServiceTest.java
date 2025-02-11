package unit.GestioneAccesso;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

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
import unit.DAO.DatabaseSetupForTest;

import javax.sql.DataSource;

@DisplayName("Test per UtenteService")
public class UtenteServiceTest {

    private UtenteService utenteService;

    @Mock
    private UtenteAcquirenteDAO utenteAcquirenteDAOMock;
    @Mock
    private UtenteGestoreCatenaDAO utenteGestoreCatenaDAOMock;
    @Mock
    private UtenteGestoreSedeDAO utenteGestoreSedeDAOMock;

    @BeforeAll
    static void globalSetup() {
        DatabaseSetupForTest.configureH2DataSource();
        System.out.println("Datasource H2 configurato per i test.");
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        utenteService = new UtenteService(utenteAcquirenteDAOMock, utenteGestoreCatenaDAOMock, utenteGestoreSedeDAOMock);
    }

    @Test
    @DisplayName("Login utente acquirente con credenziali corrette")
    void testLoginUtenteAcquirente() {
        System.out.println("Login utente acquirente con credenziali corrette");

        String username = "utente1";
        String password = "password123";
        UtenteAcquirente mockUtente = new UtenteAcquirente(username, "email@example.com", password, "Nome", "Cognome", "Città");
        System.out.println("Credenziali in input - Username: " + username + ", Password: " + password);
        System.out.println("Utente mock creato: " + mockUtente);

        when(utenteAcquirenteDAOMock.getByUsernameAndPassword(username, password)).thenReturn(mockUtente);

        UtenteAcquirente result = (UtenteAcquirente) utenteService.login(username, password);
        System.out.println("Risultato login: " + result);
        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }

    @Test
    @DisplayName("Login utente gestore catena con credenziali corrette")
    void testLoginUtenteGestoreCatena() {
        System.out.println("Login utente gestore catena con credenziali corrette");

        String username = "gestore_catena1";
        String password = "password123";
        UtenteGestoreCatena mockUtente = new UtenteGestoreCatena();
        mockUtente.setUsername(username);
        System.out.println("Credenziali in input - Username: " + username + ", Password: " + password);
        System.out.println("Utente mock creato: " + mockUtente);

        when(utenteGestoreCatenaDAOMock.getByUsername(username, password)).thenReturn(mockUtente);

        UtenteGestoreCatena result = (UtenteGestoreCatena) utenteService.login(username, password);
        System.out.println("Risultato login: " + result);
        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }

    @Test
    @DisplayName("Login utente gestore sede con credenziali corrette")
    void testLoginUtenteGestoreSede() {
        System.out.println("Login utente gestore sede con credenziali corrette");

        String username = "gestore_sede1";
        String password = "password123";
        UtenteGestoreSede mockUtente = new UtenteGestoreSede(username, password, 1);
        System.out.println("Credenziali in input - Username: " + username + ", Password: " + password);
        System.out.println("Utente mock creato: " + mockUtente);

        when(utenteGestoreSedeDAOMock.getByUsername(username, password)).thenReturn(mockUtente);

        UtenteGestoreSede result = (UtenteGestoreSede) utenteService.login(username, password);
        System.out.println("Risultato login: " + result);
        assertNotNull(result);
        assertEquals(username, result.getUsernameUGS());
    }

    @Test
    @DisplayName("Login fallito per utente non esistente")
    void testLoginUtenteNonTrovato() {
        System.out.println("Login fallito per utente non esistente");

        String username = "non_existent";
        String password = "password123";
        System.out.println("Credenziali in input - Username: " + username + ", Password: " + password);

        when(utenteAcquirenteDAOMock.getByUsernameAndPassword(username, password)).thenReturn(null);
        when(utenteGestoreCatenaDAOMock.getByUsername(username, password)).thenReturn(null);
        when(utenteGestoreSedeDAOMock.getByUsername(username, password)).thenReturn(null);

        Object result = utenteService.login(username, password);
        System.out.println("Risultato login: " + result);
        assertNull(result);
    }

    @Test
    @DisplayName("Logout utente - invalidazione sessione")
    void testLogout() {
        System.out.println("Logout utente - invalidazione sessione");

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpSession mockSession = mock(HttpSession.class);
        when(mockRequest.getSession()).thenReturn(mockSession);
        doNothing().when(mockSession).invalidate();

        utenteService.logout(mockRequest);
        System.out.println("Sessione invalidata.");
        verify(mockSession).invalidate();
    }
}
