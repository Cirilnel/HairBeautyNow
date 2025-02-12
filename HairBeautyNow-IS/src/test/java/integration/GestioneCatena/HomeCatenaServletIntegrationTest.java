package integration.GestioneCatena;

import it.unisa.application.sottosistemi.GestioneCatena.view.HomeCatenaServlet;
import it.unisa.application.model.dao.SedeDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import unit.DAO.DatabaseSetupForTest;

import static org.mockito.Mockito.*;

public class HomeCatenaServletIntegrationTest {

    private HomeCatenaServlet homeCatenaServlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private HttpSession sessionMock;
    private RequestDispatcher dispatcherMock;
    private SedeDAO sedeDAO;

    @BeforeAll
    static void setupDatabase() {
        // Configura il database se necessario, simile alla configurazione di test H2
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() throws Exception {
        // Inizializza la servlet e i mock
        homeCatenaServlet = new HomeCatenaServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
        dispatcherMock = mock(RequestDispatcher.class);

        // Inizializza il DAO (se è necessario per il test)
        sedeDAO = mock(SedeDAO.class);

        // Puoi iniettare il mock del DAO se la servlet dipende da esso.
        // homeCatenaServlet.setSedeDAO(sedeDAO);
    }

    @Test
    void testDoGet() throws Exception {
        // Mock della richiesta per il forward alla pagina homeCatena.jsp
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/homeCatena.jsp")).thenReturn(dispatcherMock);

        // Stampa dei dati della richiesta per il debug
        System.out.println("Request Mock: " + requestMock);
        System.out.println("Request URI: " + requestMock.getRequestURI());

        // Stampa della sessione mockata
        when(requestMock.getSession()).thenReturn(sessionMock);
        System.out.println("Session Mock: " + sessionMock);

        // Chiamata al metodo doGet() della servlet
        var doGetMethod = HomeCatenaServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(homeCatenaServlet, requestMock, responseMock);

        // Verifica che il forward sia stato fatto alla JSP homeCatena.jsp
        verify(dispatcherMock).forward(requestMock, responseMock);

        // Stampa dei dati coinvolti nel forwarding
        System.out.println("Forwarding to: /WEB-INF/jsp/homeCatena.jsp");
        System.out.println("Request Attributes: " + requestMock.getAttributeNames());
    }

    // Aggiungi altri test se necessario per la logica di gestione, per esempio per l'interazione con SedeDAO
}
