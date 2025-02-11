package integration.GestioneAccesso;

import it.unisa.application.sottosistemi.GestioneAccesso.view.LoginServlet;
import it.unisa.application.sottosistemi.GestioneAccesso.service.UtenteService;
import it.unisa.application.model.entity.UtenteAcquirente;
import it.unisa.application.model.entity.UtenteGestoreSede;
import it.unisa.application.model.entity.UtenteGestoreCatena;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import unit.DAO.DatabaseSetupForTest;

import static org.mockito.Mockito.*;

public class LoginServletIntegrationTest {

    private LoginServlet loginServlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private HttpSession sessionMock;
    private RequestDispatcher dispatcherMock;
    private UtenteService utenteService;

    @BeforeAll
    static void setupDatabase() {
        // Configura il database, se necessario
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() throws Exception {
        // Inizializza la servlet e i mock
        loginServlet = new LoginServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
        dispatcherMock = mock(RequestDispatcher.class);

        // Inizializza il servizio
        utenteService = mock(UtenteService.class);

        // Inietta il mock del servizio tramite il setter
    }

    @Test
    void testDoGet() throws Exception {
        // Mock del RequestDispatcher per la pagina di login
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/loginPage.jsp")).thenReturn(dispatcherMock);

        // Chiamata al metodo doGet() della servlet
        var doGetMethod = LoginServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(loginServlet, requestMock, responseMock);

        // Verifica che il forward sia stato fatto alla JSP di login
        verify(dispatcherMock).forward(requestMock, responseMock);
    }

    @Test
    void testDoPostSuccessUtenteAcquirente() throws Exception {
        // Mock dell'utente (UtenteAcquirente)
        UtenteAcquirente utenteAcquirente = new UtenteAcquirente("acquirente123", "email@example.com", "AcquirentePassword!", "Mario", "Rossi", "Roma");

        // Stampa dell'utente creato per il debug
        System.out.println("Test con UtenteAcquirente:");
        System.out.println("Utente: " + utenteAcquirente);

        // Crea un mock per UtenteService
        UtenteService utenteServiceMock = mock(UtenteService.class);
        when(utenteServiceMock.login("acquirente123", "AcquirentePassword!")).thenReturn(utenteAcquirente);

        // Inietta il mock del servizio nella servlet
        LoginServlet loginServlet = new LoginServlet(utenteServiceMock);

        // Configura gli altri mock (request, response, session)
        when(requestMock.getParameter("username")).thenReturn("acquirente123");
        when(requestMock.getParameter("password")).thenReturn("AcquirentePassword!");
        when(requestMock.getSession()).thenReturn(sessionMock);

        // Esegui la chiamata alla servlet
        var doPostMethod = LoginServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(loginServlet, requestMock, responseMock);

        // Verifica le interazioni con la sessione e il reindirizzamento
        verify(sessionMock).setAttribute(eq("user"), eq(utenteAcquirente));
        verify(responseMock).sendRedirect(requestMock.getContextPath() + "/home");

        // Stampa delle informazioni sull'utente dopo il reindirizzamento
        System.out.println("Verifica che l'utente Acquirente sia stato salvato nella sessione.");
    }

    @Test
    void testDoPostFailure() throws Exception {
        // Parametri errati per il login
        System.out.println("Test con parametri errati per il login:");

        // Crea l'utente non trovato
        when(requestMock.getParameter("username")).thenReturn("nonEsistente");
        when(requestMock.getParameter("password")).thenReturn("PasswordErrata!");

        // Stampa dei parametri errati
        System.out.println("Username: nonEsistente, Password: PasswordErrata!");

        when(utenteService.login("nonEsistente", "PasswordErrata!")).thenReturn(null);
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/loginPage.jsp")).thenReturn(dispatcherMock);

        var doPostMethod = LoginServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(loginServlet, requestMock, responseMock);

        verify(requestMock).setAttribute("errorMessage", "Username o password errati!");
        verify(dispatcherMock).forward(requestMock, responseMock);

        // Stampa del messaggio di errore
        System.out.println("Messaggio di errore: Username o password errati!");
    }
}
