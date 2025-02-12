package integration.GestionePrenotazioni;

import it.unisa.application.sottosistemi.GestionePrenotazioni.view.OrarioServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

public class OrarioServletIntegrationTest {
    private OrarioServlet servlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private HttpSession sessionMock;
    private RequestDispatcher dispatcherMock;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new OrarioServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
        dispatcherMock = mock(RequestDispatcher.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/metodoPagamento.jsp")).thenReturn(dispatcherMock);
    }

    @Test
    void testDoPost() throws Exception {
        // Imposta i parametri della richiesta
        String giorno = "2025-02-15";
        String orario = "08:00-08:30";
        String professionistaId = "1";

        when(requestMock.getParameter("data")).thenReturn(giorno);
        when(requestMock.getParameter("orario")).thenReturn(orario);
        when(requestMock.getParameter("professionistaId")).thenReturn(professionistaId);

        // Stampa i dati della richiesta
        System.out.println("Dati ricevuti nella richiesta:");
        System.out.println("Giorno: " + giorno);
        System.out.println("Orario: " + orario);
        System.out.println("Professionista ID: " + professionistaId);

        // Invoca il metodo doPost tramite riflessione
        invokeDoPost();

        // Verifica che i dati siano stati salvati nella sessione
        verify(sessionMock).setAttribute("giorno", giorno);
        verify(sessionMock).setAttribute("orario", orario);
        verify(sessionMock).setAttribute("professionistaId", professionistaId);

        // Stampa i dati salvati nella sessione
        System.out.println("Dati salvati nella sessione:");
        System.out.println("Giorno nella sessione: " + giorno);
        System.out.println("Orario nella sessione: " + orario);
        System.out.println("Professionista ID nella sessione: " + professionistaId);

        // Verifica che la servlet faccia il forward alla pagina di pagamento
        verify(requestMock).getRequestDispatcher("/WEB-INF/jsp/metodoPagamento.jsp");
        verify(dispatcherMock).forward(requestMock, responseMock);

        // Stampa l'operazione di forward
        System.out.println("La servlet ha effettuato il forward alla pagina: /WEB-INF/jsp/metodoPagamento.jsp");
    }

    // Metodo per invocare doPost con riflessione
    private void invokeDoPost() throws Exception {
        Method doPostMethod = OrarioServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(servlet, requestMock, responseMock);
    }
}
