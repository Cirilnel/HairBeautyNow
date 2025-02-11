package integration.GestioneAccesso;

import it.unisa.application.sottosistemi.GestioneAccesso.view.LogoutServlet;
import it.unisa.application.sottosistemi.GestioneAccesso.service.UtenteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import unit.DAO.DatabaseSetupForTest;

import static org.mockito.Mockito.*;

public class LogoutServletIntegrationTest {

    private LogoutServlet logoutServlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private HttpSession sessionMock;
    private UtenteService utenteService;

    @BeforeAll
    static void setupDatabase() {
        // Configura il database se necessario
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() throws Exception {
        // Inizializza la servlet e i mock
        logoutServlet = new LogoutServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);

        // Inizializza il servizio
        utenteService = mock(UtenteService.class);

        // Inietta il mock del servizio nella servlet
        logoutServlet.setUtenteService(utenteService);
    }

    @Test
    void testDoGetLogoutSuccess() throws Exception {
        // Simula l'utente autenticato
        String utenteAutenticato = "utenteAutenticato";
        when(sessionMock.getAttribute("user")).thenReturn(utenteAutenticato);
        when(requestMock.getSession()).thenReturn(sessionMock);

        // Stampa per il dato simulato
        System.out.println("Simulato utente autenticato: " + utenteAutenticato);

        // Simula il comportamento del metodo logout
        doNothing().when(utenteService).logout(requestMock);

        // Chiamata al metodo doGet() della servlet
        var doGetMethod = LogoutServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(logoutServlet, requestMock, responseMock);

        // Verifica che il metodo logout() sia stato invocato
        verify(utenteService).logout(requestMock);

        // Stampa per verificare il comportamento del logout
        System.out.println("Metodo logout invocato con richiesta: " + requestMock);

        // Verifica che la sessione venga invalidata
        verify(sessionMock).invalidate();

        // Stampa per il comportamento della sessione
        System.out.println("Sessione invalidata: " + sessionMock);

        // Verifica che il reindirizzamento alla home sia stato effettuato
        verify(responseMock).sendRedirect(requestMock.getContextPath() + "/home");

        // Stampa per il comportamento del reindirizzamento
        System.out.println("Reindirizzato alla home con path: " + requestMock.getContextPath());

        // Stampa di conferma
        System.out.println("Test di logout: utente disconnesso e reindirizzato alla home.");
    }

    @Test
    void testDoGetLogoutNoUser() throws Exception {
        // Simula l'assenza di un utente autenticato
        when(sessionMock.getAttribute("user")).thenReturn(null);
        when(requestMock.getSession()).thenReturn(sessionMock);

        // Stampa per l'assenza di utente
        System.out.println("Nessun utente autenticato. La sessione non ha un attributo 'user'.");


        // Chiamata al metodo doGet() della servlet
        var doGetMethod = LogoutServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(logoutServlet, requestMock, responseMock);

        // Verifica che il metodo logout() non venga invocato se non c'è utente autenticato
        verify(utenteService, times(0)).logout(requestMock);

        // Stampa per verificare che il metodo logout() non è stato invocato
        System.out.println("Metodo logout non invocato (assenza utente).");

        // Verifica che la sessione venga invalidata anche quando non c'è un utente
        verify(sessionMock, times(1)).invalidate();  // La sessione viene sempre invalidata in caso di logout

        // Stampa per la sessione invalidata
        System.out.println("Sessione invalidata (indipendentemente dall'utente): " + sessionMock);

        // Verifica che il reindirizzamento alla home sia comunque eseguito
        verify(responseMock).sendRedirect(requestMock.getContextPath() + "/home");

        // Stampa per il comportamento del reindirizzamento
        System.out.println("Reindirizzato alla home con path: " + requestMock.getContextPath());

        // Stampa di conferma
        System.out.println("Test senza utente autenticato: sessione invalidata, reindirizzato alla home.");
    }
}
