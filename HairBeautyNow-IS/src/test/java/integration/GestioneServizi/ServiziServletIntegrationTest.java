package integration.GestioneServizi;

import it.unisa.application.sottosistemi.GestioneServizi.view.ServiziServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

public class ServiziServletIntegrationTest {

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private RequestDispatcher dispatcherMock;

    private ServiziServlet serviziServlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        serviziServlet = new ServiziServlet();
    }

    @Test
    @DisplayName("TC01: ServiziServlet - Reindirizzamento a Servizi")
    void testDoGet() throws Exception {
        // Configura il mock per il RequestDispatcher
        when(requestMock.getRequestDispatcher("/servizi")).thenReturn(dispatcherMock);

        // Stampa i dati coinvolti nel test
        System.out.println("Invocazione di doGet su ServiziServlet:");
        System.out.println("RequestMock: " + requestMock);
        System.out.println("ResponseMock: " + responseMock);
        System.out.println("RequestDispatcherMock: " + dispatcherMock);

        // Usa la riflessione per invocare il metodo doGet
        invokeDoGet();

        // Stampa il risultato della chiamata al forward
        System.out.println("Verifica della chiamata a forward:");
        verify(dispatcherMock).forward(requestMock, responseMock);

        // Verifica che il RequestDispatcher venga invocato correttamente per inoltrare la richiesta
        verify(requestMock).getRequestDispatcher("/servizi");
    }

    // Metodo per invocare doGet tramite riflessione
    private void invokeDoGet() throws Exception {
        Method doGetMethod = ServiziServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true); // Rende il metodo accessibile se è privato
        doGetMethod.invoke(serviziServlet, requestMock, responseMock); // Invoca il metodo su 'serviziServlet'
    }
}
