package integration.GestioneCatena;

import it.unisa.application.sottosistemi.GestioneCatena.view.CreaGestoreServlet;
import it.unisa.application.sottosistemi.GestioneCatena.service.GestioneGestoreService;
import it.unisa.application.model.entity.UtenteGestoreSede;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import unit.DAO.DatabaseSetupForTest;

import static org.mockito.Mockito.*;

public class CreaGestoreServletIntegrationTest {

    private CreaGestoreServlet creaGestoreServlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private HttpSession sessionMock;
    private RequestDispatcher dispatcherMock;
    private GestioneGestoreService gestioneGestoreService;

    @BeforeAll
    static void setupDatabase() {
        // Configura il database, se necessario
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() throws Exception {
        // Inizializza la servlet e i mock
        creaGestoreServlet = new CreaGestoreServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
        dispatcherMock = mock(RequestDispatcher.class);

        // Inizializza il servizio
        gestioneGestoreService = mock(GestioneGestoreService.class);

        // Inietta il mock del servizio tramite il costruttore
        creaGestoreServlet = new CreaGestoreServlet(gestioneGestoreService);
    }

    @Test
    void testDoGet() throws Exception {
        // Mock del RequestDispatcher per la pagina di creazione gestore
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/creaGestore.jsp")).thenReturn(dispatcherMock);

        // Chiamata al metodo doGet() della servlet
        var doGetMethod = CreaGestoreServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(creaGestoreServlet, requestMock, responseMock);

        // Verifica che il forward sia stato fatto alla JSP di creazione gestore
        verify(dispatcherMock).forward(requestMock, responseMock);
    }

    @Test
    void testDoPostSuccess() throws Exception {
        // Mock dei parametri della richiesta
        when(requestMock.getParameter("usernameUGS")).thenReturn("gestore123");
        when(requestMock.getParameter("password")).thenReturn("password123");
        when(requestMock.getSession()).thenReturn(sessionMock);  // IMPORTANTE: Associare la sessione mockata

        // Simulazione dell'oggetto che verrà passato alla service class
        UtenteGestoreSede nuovoGestore = new UtenteGestoreSede("gestore123", "password123", 1);

        // Simulazione del servizio che indica che il gestore è stato creato con successo
        when(gestioneGestoreService.creaGestore(any(UtenteGestoreSede.class))).thenReturn(true);

        // Stampa dei parametri e del risultato del mock
        System.out.println("Parametri request: usernameUGS = " + requestMock.getParameter("usernameUGS"));
        System.out.println("Parametri request: password = " + requestMock.getParameter("password"));
        System.out.println("Gestore creato: " + nuovoGestore);

        // Esegui la chiamata alla servlet
        var doPostMethod = CreaGestoreServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(creaGestoreServlet, requestMock, responseMock);

        // Stampa il risultato dell'attributo della sessione
        verify(sessionMock).setAttribute(eq("gestoreCreato"), eq("ok"));
        System.out.println("Attributo sessione 'gestoreCreato' impostato a 'ok'");

        // Verifica che la servlet abbia fatto il redirect alla pagina homeCatena
        verify(responseMock).sendRedirect(requestMock.getContextPath() + "/homeCatena");
        System.out.println("Redirect alla pagina: " + requestMock.getContextPath() + "/homeCatena");
    }

    @Test
    void testDoPostFailure() throws Exception {
        // Parametri errati per la creazione del gestore
        when(requestMock.getParameter("usernameUGS")).thenReturn("gestore123");
        when(requestMock.getParameter("password")).thenReturn("");

        // Crea un mock per il servizio che simula un fallimento
        when(gestioneGestoreService.creaGestore(any())).thenReturn(false);

        // Stampa i parametri di richiesta
        System.out.println("Parametri request: usernameUGS = " + requestMock.getParameter("usernameUGS"));
        System.out.println("Parametri request: password = " + requestMock.getParameter("password"));

        // Esegui la chiamata alla servlet
        var doPostMethod = CreaGestoreServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(creaGestoreServlet, requestMock, responseMock);

        // Verifica il redirect con il messaggio di errore
        verify(responseMock).sendRedirect(requestMock.getContextPath() + "/creaGestore?errore=Tutti i campi sono obbligatori");
        System.out.println("Redirect con errore: Tutti i campi sono obbligatori");
    }

    @Test
    void testDoPostUsernameTaken() throws Exception {
        // Crea un UtenteGestoreSede con un nome utente già esistente
        UtenteGestoreSede nuovoGestore = new UtenteGestoreSede("gestore123", "password123", 1);

        // Mock del fallimento della creazione per un username già preso
        when(gestioneGestoreService.creaGestore(nuovoGestore)).thenReturn(false);

        // Mock dei parametri della richiesta
        when(requestMock.getParameter("usernameUGS")).thenReturn("gestore123");
        when(requestMock.getParameter("password")).thenReturn("password123");

        // Stampa i parametri della richiesta
        System.out.println("Parametri request: usernameUGS = " + requestMock.getParameter("usernameUGS"));
        System.out.println("Parametri request: password = " + requestMock.getParameter("password"));

        // Esegui la chiamata alla servlet
        var doPostMethod = CreaGestoreServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(creaGestoreServlet, requestMock, responseMock);

        // Verifica il redirect con il messaggio di errore
        verify(responseMock).sendRedirect(requestMock.getContextPath() + "/creaGestore?errore=Username gi&agrave; in uso");
        System.out.println("Redirect con errore: Username già in uso");
    }
}
