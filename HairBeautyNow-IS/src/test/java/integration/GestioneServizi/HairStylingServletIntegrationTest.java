package integration.GestioneServizi;

import it.unisa.application.model.dao.ServizioDAO;
import it.unisa.application.model.entity.Servizio;
import it.unisa.application.model.entity.UtenteAcquirente;
import it.unisa.application.sottosistemi.GestioneServizi.view.HairStylingServlet;
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

public class HairStylingServletIntegrationTest {

    @Mock
    private MakeUpService makeUpServiceMock;

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private HttpSession sessionMock;

    private HairStylingServlet hairStylingServlet;

    @BeforeAll
    static void setupDatabase() {
        // Configura il database se necessario
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        hairStylingServlet = new HairStylingServlet();

        // Usa la riflessione per ottenere il campo makeUpService e impostarlo
        Field makeUpServiceField = HairStylingServlet.class.getDeclaredField("makeUpService");
        makeUpServiceField.setAccessible(true);
        makeUpServiceField.set(hairStylingServlet, makeUpServiceMock);
    }

    // Metodo per invocare doGet con riflessione


    @Test
    @DisplayName("TC01: HairStylingServlet - Utente Non Loggato, Redirezione a Login")
    void testDoGetUserNotLoggedIn() throws Exception {
        // Simula l'assenza dell'utente nella sessione
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("user")).thenReturn(null);

        System.out.println("\nHairStylingServlet - Utente Non Loggato, Redirezione a Login");

        // Stampa lo stato della sessione
        System.out.println("Stato della sessione (utente): " + sessionMock.getAttribute("user"));

        // Chiamata al metodo doGet attraverso il mock
        invokeDoGet();

        // Verifica che venga invocato il reindirizzamento alla pagina di login
        verify(responseMock).sendRedirect(requestMock.getContextPath() + "/loginPage");
    }


    @Test
    @DisplayName("TC02: HairStylingServlet - Utente Loggato, Visualizzazione Servizi Hair Styling")
    void testDoGetUserLoggedIn() throws Exception {
        // Simula l'utente loggato
        UtenteAcquirente userMock = mock(UtenteAcquirente.class);
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("user")).thenReturn(userMock);

        // Prepara i dati per il mock del servizio
        Servizio servizio1 = new Servizio("Servizio A", 100.0, "hair styling", "Descrizione Servizio A");
        Servizio servizio2 = new Servizio("Servizio B", 150.0, "hair styling", "Descrizione Servizio B");
        List<Servizio> serviziHairStyling = List.of(servizio1, servizio2);
        Map<String, List<Servizio>> serviziPerTipoMock = Map.of("hair styling", serviziHairStyling);

        // Configura il mock per ottenere i servizi di "hair styling"
        when(makeUpServiceMock.getServiziPerTipo()).thenReturn(serviziPerTipoMock);

        // Mock del RequestDispatcher
        RequestDispatcher dispatcherMock = mock(RequestDispatcher.class);
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/hairstyling.jsp")).thenReturn(dispatcherMock);

        System.out.println("\nHairStylingServlet - Utente Loggato, Visualizzazione Servizi Hair Styling");

        // Stampa i dati coinvolti
        System.out.println("Servizi Mockati per Hair Styling:");
        for (Servizio servizio : serviziHairStyling) {
            System.out.println(servizio.getNome() + " - Prezzo: " + servizio.getPrezzo() + " - Descrizione: " + servizio.getDescrizione());
        }

        // Chiamata al metodo doGet attraverso il mock
        invokeDoGet();

        // Verifica che il settaggio dell'attributo abbia avuto luogo correttamente
        verify(requestMock).setAttribute("serviziPerTipo", Map.of("hair styling", serviziHairStyling));

        // Verifica che il forward venga invocato sulla risposta
        verify(dispatcherMock).forward(requestMock, responseMock);
    }


    private void invokeDoGet() throws Exception {
        Method doGetMethod = HairStylingServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(hairStylingServlet, requestMock, responseMock);
    }
}
