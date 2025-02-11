package integration.GestioneRegistrazione;

import it.unisa.application.sottosistemi.GestioneRegistrazione.view.RegisterServlet;
import it.unisa.application.sottosistemi.GestioneRegistrazione.service.UtenteAcquirenteService;
import it.unisa.application.model.entity.UtenteAcquirente;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import unit.DAO.DatabaseSetupForTest;

import static org.mockito.Mockito.*;

public class RegisterServletIntegrationTest {

    private RegisterServlet registerServlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private HttpSession sessionMock;
    private RequestDispatcher dispatcherMock;
    private UtenteAcquirenteService utenteAcquirenteService;

    @BeforeAll
    static void setupDatabase() {
        // Configura il database, se necessario
        DatabaseSetupForTest.configureH2DataSource();
    }


    @BeforeEach
    void setUp() throws Exception {
        // Inizializza la servlet e i mock
        registerServlet = new RegisterServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
        dispatcherMock = mock(RequestDispatcher.class);

        // Inizializza il servizio
        utenteAcquirenteService = mock(UtenteAcquirenteService.class);
        var field = RegisterServlet.class.getDeclaredField("utenteAcquirenteService");
        field.setAccessible(true);
        field.set(registerServlet, utenteAcquirenteService);

    }

    @Test
    void testDoGet() throws Exception {
        // Mock del RequestDispatcher per la pagina di registrazione
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/registerPage.jsp")).thenReturn(dispatcherMock);

        // Chiamata al metodo doGet() della servlet
        System.out.println("Invocazione del metodo doGet() della servlet...");
        var doGetMethod = RegisterServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(registerServlet, requestMock, responseMock);

        // Verifica che il forward sia stato fatto alla JSP di registrazione
        System.out.println("Verifica che il forward sia stato fatto alla pagina registerPage.jsp");
        verify(dispatcherMock).forward(requestMock, responseMock);
    }

    @Test
    void testDoPostSuccess() throws Exception {
        // Parametri validi per la registrazione
        System.out.println("Test con parametri validi per la registrazione:");
        System.out.println("Email: test@example.com");
        System.out.println("Password: Password123!");
        System.out.println("Nome: Mario");
        System.out.println("Cognome: Rossi");
        System.out.println("Username: mario123");
        System.out.println("Città: Roma");

        when(requestMock.getParameter("email")).thenReturn("test@example.com");
        when(requestMock.getParameter("password")).thenReturn("Password123!");
        when(requestMock.getParameter("nome")).thenReturn("Mario");
        when(requestMock.getParameter("cognome")).thenReturn("Rossi");
        when(requestMock.getParameter("username")).thenReturn("mario123");
        when(requestMock.getParameter("citta")).thenReturn("Roma");

        // Mock del RequestDispatcher per il successivo forwarding alla pagina di login
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/loginPage.jsp")).thenReturn(dispatcherMock);

        // Simula una registrazione riuscita
        when(utenteAcquirenteService.createUser(any(UtenteAcquirente.class))).thenReturn(true);

        // Chiamata al metodo doPost() della servlet
        System.out.println("Invocazione del metodo doPost() per la registrazione...");
        var doPostMethod = RegisterServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(registerServlet, requestMock, responseMock);

        // Verifica che il forward sia stato fatto alla pagina di login
        System.out.println("Verifica che il forward sia stato fatto alla pagina loginPage.jsp");
        verify(dispatcherMock).forward(requestMock, responseMock);
    }

    @Test
    void testDoPostFailure() throws Exception {
        // Parametri non validi per la registrazione
        System.out.println("Test con parametri non validi per la registrazione:");
        System.out.println("Email: invalid-email");
        System.out.println("Password: 123");
        System.out.println("Nome: Mario");
        System.out.println("Cognome: Rossi");
        System.out.println("Username: mario123");
        System.out.println("Città: Roma");

        when(requestMock.getParameter("email")).thenReturn("invalid-email");
        when(requestMock.getParameter("password")).thenReturn("123");
        when(requestMock.getParameter("nome")).thenReturn("Mario");
        when(requestMock.getParameter("cognome")).thenReturn("Rossi");
        when(requestMock.getParameter("username")).thenReturn("mario123");
        when(requestMock.getParameter("citta")).thenReturn("Roma");

        // Mock del RequestDispatcher per il forwarding alla pagina di registrazione in caso di errore
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/registerPage.jsp")).thenReturn(dispatcherMock);

        // Simula una registrazione fallita
        when(utenteAcquirenteService.createUser(any(UtenteAcquirente.class))).thenReturn(false);

        // Chiamata al metodo doPost() della servlet
        System.out.println("Invocazione del metodo doPost() per la registrazione fallita...");
        var doPostMethod = RegisterServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(registerServlet, requestMock, responseMock);

        // Verifica che il forward sia stato fatto alla pagina di errore
        System.out.println("Verifica che il forward sia stato fatto alla pagina registerPage.jsp con un messaggio di errore");
        verify(requestMock).setAttribute(eq("errorMessage"), anyString());
        verify(dispatcherMock).forward(requestMock, responseMock);
    }


}
