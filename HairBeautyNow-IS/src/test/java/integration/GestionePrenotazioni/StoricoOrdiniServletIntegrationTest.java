package integration.GestionePrenotazioni;

import it.unisa.application.model.entity.Prenotazione;
import it.unisa.application.model.entity.UtenteAcquirente;
import it.unisa.application.sottosistemi.GestionePrenotazioni.view.StoricoOrdiniServlet;
import it.unisa.application.sottosistemi.GestionePrenotazioni.service.StoricoOrdiniService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import unit.DAO.DatabaseSetupForTest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

public class StoricoOrdiniServletIntegrationTest {

    private StoricoOrdiniServlet servlet;

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private HttpSession sessionMock;

    @Mock
    private RequestDispatcher dispatcherMock;

    @Mock
    private StoricoOrdiniService storicoOrdiniServiceMock;

    @BeforeAll
    static void setupDatabase() {
        // Configura il database, se necessario
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() throws Exception {
        // Inizializzazione dei mock con MockitoAnnotations
        MockitoAnnotations.openMocks(this);

        servlet = new StoricoOrdiniServlet();

        // Iniettare i mock nei campi della servlet
        injectMock("storicoOrdiniService", storicoOrdiniServiceMock);

        // Mock per sessione e request dispatcher
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/storicoOrdini.jsp")).thenReturn(dispatcherMock);
    }

    @Test
    void testDoGet_userNotLoggedIn() throws Exception {
        // Simula un utente non loggato
        when(sessionMock.getAttribute("user")).thenReturn(null);

        // Invoca il metodo doGet tramite riflessione
        System.out.println("Test: Utente non loggato");
        invokeDoGet();

        // Verifica che l'utente venga reindirizzato alla pagina di login
        verify(responseMock).sendRedirect(requestMock.getContextPath() + "/loginPage");
    }

    @Test
    void testDoGet_invalidUsername() throws Exception {
        // Simula un utente loggato senza username valido
        UtenteAcquirente user = mock(UtenteAcquirente.class);
        when(user.getUsername()).thenReturn("");
        when(sessionMock.getAttribute("user")).thenReturn(user);

        // Invoca il metodo doGet tramite riflessione
        System.out.println("Test: Username non valido per l'utente");
        invokeDoGet();

        // Verifica che venga mostrato un errore
        System.out.println("Impostato errorMessage: 'Errore: l'utente non ha uno username valido.'");
        verify(requestMock).setAttribute("errorMessage", "Errore: l'utente non ha uno username valido.");
        verify(dispatcherMock).forward(requestMock, responseMock);
    }

    @Test
    void testDoGet_noOrders() throws Exception {
        // Simula un utente loggato con username valido
        UtenteAcquirente user = mock(UtenteAcquirente.class);
        when(user.getUsername()).thenReturn("validUser");
        when(sessionMock.getAttribute("user")).thenReturn(user);

        // Simula che il servizio non ritorni alcuna prenotazione
        when(storicoOrdiniServiceMock.getPrenotazioniByUsername("validUser")).thenReturn(new ArrayList<>());

        // Invoca il metodo doGet tramite riflessione
        System.out.println("Test: Nessuna prenotazione trovata per l'utente 'validUser'");
        invokeDoGet();

        // Verifica che venga impostato un messaggio di errore
        System.out.println("Impostato errorMessage: 'Nessuna prenotazione trovata.'");
        verify(requestMock).setAttribute("errorMessage", "Nessuna prenotazione trovata.");
        verify(dispatcherMock).forward(requestMock, responseMock);
    }

    @Test
    void testDoGet_withOrders() throws Exception {
        // Simula un utente loggato con username valido
        UtenteAcquirente user = mock(UtenteAcquirente.class);
        when(user.getUsername()).thenReturn("validUser");
        when(sessionMock.getAttribute("user")).thenReturn(user);

        // Crea delle prenotazioni di esempio
        List<Prenotazione> prenotazioni = new ArrayList<>();
        Prenotazione p1 = new Prenotazione();
        p1.setServizioName("Servizio 1");
        p1.setProfessionistaId(1);
        prenotazioni.add(p1);

        // Simula il comportamento del servizio
        when(storicoOrdiniServiceMock.getPrenotazioniByUsername("validUser")).thenReturn(prenotazioni);
        when(storicoOrdiniServiceMock.getPrezzoByServizio("Servizio 1")).thenReturn(100.0);
        when(storicoOrdiniServiceMock.getIndirizzoBySedeId(1)).thenReturn("Indirizzo Professionista 1");

        // Invoca il metodo doGet tramite riflessione
        System.out.println("Test: Utente 'validUser' con prenotazioni");
        System.out.println("Prenotazioni: " + prenotazioni);
        invokeDoGet();

        // Verifica che le prenotazioni siano state aggiunte come attributi
        System.out.println("Impostato attributo 'prenotazioni': " + prenotazioni);
        verify(requestMock).setAttribute("prenotazioni", prenotazioni);
        verify(requestMock).setAttribute("indirizzo", "Indirizzo Professionista 1");

        // Verifica che venga effettuato il forward alla pagina storicoOrdini.jsp
        verify(dispatcherMock).forward(requestMock, responseMock);
    }

    // Metodo per invocare doGet tramite riflessione
    private void invokeDoGet() throws Exception {
        Method doGetMethod = StoricoOrdiniServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(servlet, requestMock, responseMock);
    }

    // Metodo per iniettare i mock nei campi della servlet
    private void injectMock(String fieldName, Object mockInstance) throws Exception {
        var field = StoricoOrdiniServlet.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(servlet, mockInstance);
    }
}
