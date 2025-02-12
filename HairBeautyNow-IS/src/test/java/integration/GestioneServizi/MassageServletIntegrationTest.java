package integration.GestioneServizi;

import it.unisa.application.model.dao.ServizioDAO;
import it.unisa.application.model.entity.Servizio;
import it.unisa.application.model.entity.UtenteAcquirente;
import it.unisa.application.sottosistemi.GestioneServizi.view.MassageServlet;
import it.unisa.application.sottosistemi.GestioneServizi.service.MakeUpService;
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

public class MassageServletIntegrationTest {

    @Mock
    private MakeUpService makeUpServiceMock;

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private HttpSession sessionMock;

    private MassageServlet massageServlet;

    @BeforeAll
    static void setupDatabase() {
        // Configura il database se necessario
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        massageServlet = new MassageServlet();

        // Usa la riflessione per ottenere il campo makeUpService e impostarlo
        Field makeUpServiceField = MassageServlet.class.getDeclaredField("makeUpService");
        makeUpServiceField.setAccessible(true);
        makeUpServiceField.set(massageServlet, makeUpServiceMock);
    }

    // Metodo per invocare doGet con riflessione
    private void invokeDoGet() throws Exception {
        Method doGetMethod = MassageServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(massageServlet, requestMock, responseMock);
    }

    @Test
    @DisplayName("TC01: MassageServlet - Utente Non Loggato, Redirezione a Login")
    void testDoGetUserNotLoggedIn() throws Exception {
        // Simula l'assenza dell'utente nella sessione
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("user")).thenReturn(null);

        System.out.println("\nMassageServlet - Utente Non Loggato, Redirezione a Login");

        // Stampa lo stato della sessione
        System.out.println("Stato della sessione (utente): " + sessionMock.getAttribute("user"));

        // Chiamata al metodo doGet attraverso il mock
        invokeDoGet();

        // Verifica che venga invocato il reindirizzamento alla pagina di login
        verify(responseMock).sendRedirect(requestMock.getContextPath() + "/loginPage");
    }

    @Test
    @DisplayName("TC02: MassageServlet - Utente Loggato, Visualizzazione Servizi Massaggi")
    void testDoGetUserLoggedIn() throws Exception {
        // Simula l'utente loggato
        UtenteAcquirente userMock = mock(UtenteAcquirente.class);
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("user")).thenReturn(userMock);

        // Prepara i dati per il mock del servizio
        Servizio servizio1 = new Servizio("Massaggio A", 100.0, "massaggi", "Descrizione Massaggio A");
        Servizio servizio2 = new Servizio("Massaggio B", 150.0, "massaggi", "Descrizione Massaggio B");
        List<Servizio> serviziMassaggi = List.of(servizio1, servizio2);
        Map<String, List<Servizio>> serviziPerTipoMock = Map.of("massaggi", serviziMassaggi);

        // Configura il mock per ottenere i servizi di "massaggi"
        when(makeUpServiceMock.getServiziPerTipo()).thenReturn(serviziPerTipoMock);

        // Mock del RequestDispatcher
        RequestDispatcher dispatcherMock = mock(RequestDispatcher.class);
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/massage.jsp")).thenReturn(dispatcherMock);

        System.out.println("\nMassageServlet - Utente Loggato, Visualizzazione Servizi Massaggi");

        // Stampa i dati coinvolti
        System.out.println("Servizi Mockati per Massaggi:");
        for (Servizio servizio : serviziMassaggi) {
            System.out.println(servizio.getNome() + " - Prezzo: " + servizio.getPrezzo() + " - Descrizione: " + servizio.getDescrizione());
        }

        // Chiamata al metodo doGet attraverso il mock
        invokeDoGet();

        // Verifica che il settaggio dell'attributo abbia avuto luogo correttamente
        verify(requestMock).setAttribute("serviziPerTipo", Map.of("massaggi", serviziMassaggi));

        // Verifica che il forward venga invocato sulla risposta
        verify(dispatcherMock).forward(requestMock, responseMock);
    }
}
