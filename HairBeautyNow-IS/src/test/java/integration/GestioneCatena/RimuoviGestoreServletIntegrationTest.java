package integration.GestioneCatena;

import it.unisa.application.sottosistemi.GestioneCatena.view.RimuoviGestoreServlet;
import it.unisa.application.model.dao.UtenteGestoreSedeDAO;
import it.unisa.application.model.entity.UtenteGestoreSede;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import unit.DAO.DatabaseSetupForTest;

import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class RimuoviGestoreServletIntegrationTest {

    private RimuoviGestoreServlet rimuoviGestoreServlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private HttpSession sessionMock;
    private RequestDispatcher dispatcherMock;
    private UtenteGestoreSedeDAO utenteGestoreSedeDAOMock;

    @BeforeAll
    static void setupDatabase() {
        // Configure the database if necessary (mocking or real database setup)
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() throws Exception {
        // Initialize the servlet and the mocks
        rimuoviGestoreServlet = new RimuoviGestoreServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
        dispatcherMock = mock(RequestDispatcher.class);

        // Initialize the DAO mock
        utenteGestoreSedeDAOMock = mock(UtenteGestoreSedeDAO.class);

        // Use reflection to inject the mock DAO into the servlet
        Field daoField = RimuoviGestoreServlet.class.getDeclaredField("utenteGestoreSedeDAO");
        daoField.setAccessible(true);
        daoField.set(rimuoviGestoreServlet, utenteGestoreSedeDAOMock);
    }

    @Test
    void testDoGet() throws Exception {
        // Prepare mock data for gestori con sede
        List<UtenteGestoreSede> gestoriConSede = Arrays.asList(
                new UtenteGestoreSede("gestore1", "password1", 1),
                new UtenteGestoreSede("gestore2", "password2", 2)
        );

        // Mock the DAO to return the list of gestori con sede
        when(utenteGestoreSedeDAOMock.getGestoriConSede()).thenReturn(gestoriConSede);
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/rimuoviGestore.jsp")).thenReturn(dispatcherMock);

        // Print the list of gestoriConSede for visibility
        System.out.println("Gestori with Sede: " + gestoriConSede);

        // Call the doGet method
        var doGetMethod = RimuoviGestoreServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(rimuoviGestoreServlet, requestMock, responseMock);

        // Verify that the request was forwarded to the correct JSP
        verify(dispatcherMock).forward(requestMock, responseMock);
        // Verify that the gestori list was set as a request attribute
        verify(requestMock).setAttribute(eq("gestoriConSede"), eq(gestoriConSede));
    }

    @Test
    void testDoPostSuccess() throws Exception {
        // Prepare test data for the selected gestore
        UtenteGestoreSede gestoreLicenziato = new UtenteGestoreSede("gestore1", "password1", 1);

        // Mock DAO behavior for finding the gestore by username
        when(utenteGestoreSedeDAOMock.getByUsername("gestore1")).thenReturn(gestoreLicenziato);
        when(requestMock.getParameter("usernameUGS")).thenReturn("gestore1");
        when(requestMock.getSession()).thenReturn(sessionMock);

        // Print the data for the selected gestore
        System.out.println("Gestore to be removed: " + gestoreLicenziato);

        // Call the doPost method
        var doPostMethod = RimuoviGestoreServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(rimuoviGestoreServlet, requestMock, responseMock);

        // Verify that the session was set with the correct data
        verify(sessionMock).setAttribute(eq("sedeIDLicenziato"), eq(gestoreLicenziato.getSedeID()));
        verify(sessionMock).setAttribute(eq("usernameLicenziato"), eq(gestoreLicenziato.getUsernameUGS()));
        // Verify the redirection to the "/assegnaGestore" page
        verify(responseMock).sendRedirect(requestMock.getContextPath() + "/assegnaGestore");
    }

    @Test
    void testDoPostGestoreNotFound() throws Exception {
        // Mock DAO behavior for the case where the gestore is not found
        when(utenteGestoreSedeDAOMock.getByUsername("gestoreNotFound")).thenReturn(null);
        when(requestMock.getParameter("usernameUGS")).thenReturn("gestoreNotFound");

        // Print the invalid gestore case
        System.out.println("Gestore not found: gestoreNotFound");

        // Call the doPost method
        var doPostMethod = RimuoviGestoreServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(rimuoviGestoreServlet, requestMock, responseMock);

        // Verify the redirection with the error message
        verify(responseMock).sendRedirect(requestMock.getContextPath() + "/rimuoviGestore?errore=Gestore non trovato.");
    }

    @Test
    void testDoPostInvalidGestore() throws Exception {
        // Test with invalid gestore (empty or null username)
        when(requestMock.getParameter("usernameUGS")).thenReturn("");

        // Print the invalid gestore case
        System.out.println("Invalid Gestore case: Empty username");

        // Call the doPost method
        var doPostMethod = RimuoviGestoreServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(rimuoviGestoreServlet, requestMock, responseMock);

        // Verify the redirection with the error message
        verify(responseMock).sendRedirect(requestMock.getContextPath() + "/rimuoviGestore?errore=Gestore non valido.");
    }

    @Test
    void testDoGetWithError() throws Exception {
        // Disabilitare temporaneamente il logger per evitare qualsiasi stampa di log
        Logger globalLogger = Logger.getLogger(RimuoviGestoreServlet.class.getName());
        globalLogger.setLevel(Level.OFF); // Disabilita tutti i log

        // Sopprimere System.err per evitare la stampa della traccia dello stack
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                // Sopprimiamo l'output
            }
        }));

        // Simula un errore durante il recupero dei gestori
        when(utenteGestoreSedeDAOMock.getGestoriConSede()).thenThrow(new RuntimeException("Database error"));

        try {
            // Esegui la chiamata
            var doGetMethod = RimuoviGestoreServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
            doGetMethod.setAccessible(true);

            // Simuliamo un errore che viene gestito dalla servlet
            try {
                doGetMethod.invoke(rimuoviGestoreServlet, requestMock, responseMock);
            } catch (Exception e) {
                // Gestiamo l'eccezione per evitare che venga stampata
                assertTrue(e.getCause() instanceof RuntimeException);
                assertEquals("Database error", e.getCause().getMessage());
            }

            // Verifica che la redirezione avvenga con il messaggio di errore
            verify(responseMock).sendRedirect(requestMock.getContextPath() + "/rimuoviGestore?errore=Impossibile recuperare i gestori con sede.");
        } finally {
            // Ripristina System.err dopo il test
            System.setErr(originalErr);
        }
    }



}
