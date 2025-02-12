package integration.GestioneCatena;

import it.unisa.application.sottosistemi.GestioneCatena.view.AssegnaGestoreServlet;
import it.unisa.application.model.dao.SedeDAO;
import it.unisa.application.model.dao.UtenteGestoreSedeDAO;
import it.unisa.application.model.entity.Sede;
import it.unisa.application.model.entity.UtenteGestoreSede;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unit.DAO.DatabaseSetupForTest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class AssegnaGestoreServletIntegrationTest {
    private AssegnaGestoreServlet assegnaGestoreServlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private HttpSession sessionMock;
    private RequestDispatcher dispatcherMock;
    private UtenteGestoreSedeDAO utenteGestoreSedeDAOMock;
    private SedeDAO sedeDAOMock;

    @BeforeAll
    static void setupDatabase() {
        // Configura il database se necessario
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        assegnaGestoreServlet = new AssegnaGestoreServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
        dispatcherMock = mock(RequestDispatcher.class);
        utenteGestoreSedeDAOMock = mock(UtenteGestoreSedeDAO.class);
        sedeDAOMock = mock(SedeDAO.class);

        Field fieldGestoreDAO = AssegnaGestoreServlet.class.getDeclaredField("utenteGestoreSedeDAO");
        fieldGestoreDAO.setAccessible(true);
        fieldGestoreDAO.set(assegnaGestoreServlet, utenteGestoreSedeDAOMock);

        Field fieldSedeDAO = AssegnaGestoreServlet.class.getDeclaredField("sedeDAO");
        fieldSedeDAO.setAccessible(true);
        fieldSedeDAO.set(assegnaGestoreServlet, sedeDAOMock);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/assegnaGestore.jsp")).thenReturn(dispatcherMock);
    }

    @Test
    void testDoGetGestoriSenzaSede() throws Exception {
        List<UtenteGestoreSede> gestoriSenzaSede = new ArrayList<>();
        gestoriSenzaSede.add(new UtenteGestoreSede("gestore1", "password", null));

        when(utenteGestoreSedeDAOMock.getGestoriSenzaSede()).thenReturn(gestoriSenzaSede);

        System.out.println("Test: testDoGetGestoriSenzaSede - Gestori senza sede: " + gestoriSenzaSede);

        invokeDoGet();

        verify(requestMock).setAttribute("gestoriSenzaSede", gestoriSenzaSede);
        verify(dispatcherMock).forward(requestMock, responseMock);
    }

    @Test
    void testDoPostAssegnaNuovaSede() throws Exception {
        Sede nuovaSede = new Sede("Via Roma 10", "Nome Sede", "Roma", 0);
        when(sessionMock.getAttribute("nuovaSede")).thenReturn(nuovaSede);
        when(requestMock.getParameter("usernameUGS")).thenReturn("gestore1");
        when(sedeDAOMock.insertSedeAndReturnID(nuovaSede)).thenReturn(1);

        System.out.println("Test: testDoPostAssegnaNuovaSede - Nuova sede: " + nuovaSede);
        System.out.println("Gestore selezionato: gestore1");

        invokeDoPost();

        verify(utenteGestoreSedeDAOMock).assegnaSede("gestore1", 1);
        verify(sessionMock).removeAttribute("nuovaSede");
        verify(responseMock).sendRedirect(requestMock.getContextPath() + "/homeCatena?successo=ok");
    }

    @Test
    void testDoPostLicenziaGestoreEAssegnaNuovo() throws Exception {
        when(sessionMock.getAttribute("sedeIDLicenziato")).thenReturn(1);
        when(sessionMock.getAttribute("usernameLicenziato")).thenReturn("vecchioGestore");
        when(requestMock.getParameter("usernameUGS")).thenReturn("nuovoGestore");

        System.out.println("Test: testDoPostLicenziaGestoreEAssegnaNuovo");
        System.out.println("Vecchio gestore licenziato: vecchioGestore");
        System.out.println("Nuovo gestore assegnato: nuovoGestore");
        System.out.println("Sede ID: 1");

        invokeDoPost();

        verify(utenteGestoreSedeDAOMock).licenziaGestore("vecchioGestore");
        verify(utenteGestoreSedeDAOMock).assegnaSede("nuovoGestore", 1);
        verify(responseMock).sendRedirect(requestMock.getContextPath() + "/homeCatena");
    }

    private void invokeDoGet() throws Exception {
        Method doGetMethod = AssegnaGestoreServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(assegnaGestoreServlet, requestMock, responseMock);
    }

    private void invokeDoPost() throws Exception {
        Method doPostMethod = AssegnaGestoreServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(assegnaGestoreServlet, requestMock, responseMock);
    }
}
