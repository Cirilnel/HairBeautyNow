package integration.GestioneServizi;

import it.unisa.application.model.entity.Servizio;
import it.unisa.application.sottosistemi.GestioneServizi.view.ServizioServlet;
import it.unisa.application.sottosistemi.GestioneServizi.service.ServizioService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import unit.DAO.DatabaseSetupForTest;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServizioServletIntegrationTest {

    @Mock
    private ServizioService servizioServiceMock;

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private HttpSession sessionMock;

    private ServizioServlet servizioServlet;

    @BeforeAll
    static void setupDatabase() {
        // Configura il database se necessario
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        servizioServlet = new ServizioServlet();

        // Usa la riflessione per ottenere il campo servizioService e impostarlo
        Field servizioServiceField = ServizioServlet.class.getDeclaredField("servizioService");
        servizioServiceField.setAccessible(true);
        servizioServiceField.set(servizioServlet, servizioServiceMock);
    }

    // Metodo per invocare doGet con riflessione
    private void invokeDoGet() throws Exception {
        Method doGetMethod = ServizioServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(servizioServlet, requestMock, responseMock);
    }

    @Test
    @DisplayName("TC01: ServizioServlet - Visualizzazione Servizi")
    void testDoGet() throws Exception {
        // Prepara i dati per il mock del servizio
        Servizio servizio1 = new Servizio("Servizio A", 100.0, "make up", "Descrizione Servizio A");
        Servizio servizio2 = new Servizio("Servizio B", 150.0, "hair styling", "Descrizione Servizio B");
        List<Servizio> listaServizi = List.of(servizio1, servizio2);

        Map<String, List<Servizio>> serviziPerTipoMock = Map.of(
                "make up", List.of(servizio1),
                "hair styling", List.of(servizio2)
        );

        // Configura il mock per ottenere tutti i servizi e i servizi raggruppati per tipo
        when(servizioServiceMock.getAllServizi()).thenReturn(listaServizi);
        when(servizioServiceMock.getServiziPerTipo()).thenReturn(serviziPerTipoMock);

        // Mock del RequestDispatcher
        RequestDispatcher dispatcherMock = mock(RequestDispatcher.class);
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/servizi.jsp")).thenReturn(dispatcherMock);

        System.out.println("\nServizioServlet - Visualizzazione Servizi");

        // Stampa i dati coinvolti
        System.out.println("Servizi Mockati:");
        for (Servizio servizio : listaServizi) {
            System.out.println(servizio.getNome() + " - Prezzo: " + servizio.getPrezzo() + " - Descrizione: " + servizio.getDescrizione());
        }

        // Chiamata al metodo doGet attraverso il mock
        invokeDoGet();

        // Verifica che il settaggio dell'attributo abbia avuto luogo correttamente
        verify(requestMock).setAttribute("serviziPerTipo", serviziPerTipoMock);

        // Verifica che il forward venga invocato sulla risposta
        verify(dispatcherMock).forward(requestMock, responseMock);
    }
}
