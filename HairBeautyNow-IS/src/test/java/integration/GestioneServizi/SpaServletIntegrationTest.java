package integration.GestioneServizi;

import it.unisa.application.model.dao.ServizioDAO;
import it.unisa.application.model.entity.Servizio;
import it.unisa.application.model.entity.UtenteAcquirente;
import it.unisa.application.sottosistemi.GestioneServizi.view.SpaServlet;
import it.unisa.application.sottosistemi.GestioneServizi.service.MakeUpService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static org.mockito.Mockito.*;

public class SpaServletIntegrationTest {

    @Mock
    private MakeUpService makeUpServiceMock;

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private HttpSession sessionMock;

    private SpaServlet spaServlet;

    @BeforeAll
    static void setupDatabase() {
        // Configura il database se necessario
        // DatabaseSetupForTest.configureH2DataSource();  // Se necessario per configurare il DB prima dei test
    }

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        spaServlet = new SpaServlet();

        // Usa la riflessione per ottenere il campo makeUpService e impostarlo
        Field makeUpServiceField = SpaServlet.class.getDeclaredField("makeUpService");
        makeUpServiceField.setAccessible(true);
        makeUpServiceField.set(spaServlet, makeUpServiceMock);
    }

    // Metodo per invocare doGet con riflessione
    private void invokeDoGet() throws Exception {
        Method doGetMethod = SpaServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(spaServlet, requestMock, responseMock);
    }

    @Test
    @DisplayName("TC01: SpaServlet - Utente Non Loggato, Redirezione a Login")
    void testDoGetUserNotLoggedIn() throws Exception {
        // Simula l'assenza dell'utente nella sessione
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("user")).thenReturn(null);

        System.out.println("\nSpaServlet - Utente Non Loggato, Redirezione a Login");

        // Stampa lo stato della sessione
        System.out.println("Stato della sessione (utente): " + sessionMock.getAttribute("user"));

        // Chiamata al metodo doGet attraverso il mock
        invokeDoGet();

        // Verifica che venga invocato il reindirizzamento alla pagina di login
        verify(responseMock).sendRedirect(requestMock.getContextPath() + "/loginPage");
    }

    @Test
    @DisplayName("TC02: SpaServlet - Utente Loggato, Visualizzazione Servizi Spa")
    void testDoGetUserLoggedIn() throws Exception {
        // Simula l'utente loggato
        UtenteAcquirente userMock = mock(UtenteAcquirente.class);
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("user")).thenReturn(userMock);

        // Prepara i dati per il mock del servizio
        Servizio servizio1 = new Servizio("Servizio A", 100.0, "spa", "Descrizione Servizio A");
        Servizio servizio2 = new Servizio("Servizio B", 150.0, "spa", "Descrizione Servizio B");
        List<Servizio> serviziSpa = List.of(servizio1, servizio2);
        Map<String, List<Servizio>> serviziPerTipoMock = Map.of("spa", serviziSpa);

        // Configura il mock per ottenere i servizi di "spa"
        when(makeUpServiceMock.getServiziPerTipo()).thenReturn(serviziPerTipoMock);

        // Mock del RequestDispatcher
        RequestDispatcher dispatcherMock = mock(RequestDispatcher.class);
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/spa.jsp")).thenReturn(dispatcherMock);

        System.out.println("\nSpaServlet - Utente Loggato, Visualizzazione Servizi Spa");

        // Stampa i dati coinvolti
        System.out.println("Servizi Mockati per Spa:");
        for (Servizio servizio : serviziSpa) {
            System.out.println(servizio.getNome() + " - Prezzo: " + servizio.getPrezzo() + " - Descrizione: " + servizio.getDescrizione());
        }

        // Chiamata al metodo doGet attraverso il mock
        invokeDoGet();

        // Verifica che il settaggio dell'attributo abbia avuto luogo correttamente
        verify(requestMock).setAttribute("serviziPerTipo", Map.of("spa", serviziSpa));

        // Verifica che il forward venga invocato sulla risposta
        verify(dispatcherMock).forward(requestMock, responseMock);
    }
}
