package integration.GestioneCatena;

import it.unisa.application.sottosistemi.GestioneCatena.view.AggiungiSedeServlet;
import it.unisa.application.sottosistemi.GestioneCatena.service.GestioneSedeService;
import it.unisa.application.sottosistemi.GestioneCatena.service.GestioneGestoreService;
import it.unisa.application.model.entity.Sede;
import it.unisa.application.model.entity.UtenteGestoreSede;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import unit.DAO.DatabaseSetupForTest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class AggiungiSedeServletIntegrationTest {

    private AggiungiSedeServlet aggiungiSedeServlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private HttpSession sessionMock;
    private RequestDispatcher dispatcherMock;
    private GestioneSedeService gestioneSedeServiceMock;
    private GestioneGestoreService gestioneGestoreServiceMock;

    @BeforeAll
    static void setupDatabase() {
        // Configura il database se necessario
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        aggiungiSedeServlet = new AggiungiSedeServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
        dispatcherMock = mock(RequestDispatcher.class);

        // Mock dei servizi GestioneSedeService e GestioneGestoreService
        gestioneSedeServiceMock = mock(GestioneSedeService.class);
        gestioneGestoreServiceMock = mock(GestioneGestoreService.class);

        // Iniezione del mock nella servlet (utilizzando la riflessione)
        Field fieldSedeService = AggiungiSedeServlet.class.getDeclaredField("gestioneSedeService");
        fieldSedeService.setAccessible(true);
        fieldSedeService.set(aggiungiSedeServlet, gestioneSedeServiceMock);

        Field fieldGestoreService = AggiungiSedeServlet.class.getDeclaredField("gestioneGestoreService");
        fieldGestoreService.setAccessible(true);
        fieldGestoreService.set(aggiungiSedeServlet, gestioneGestoreServiceMock);

        // Mock della sessione
        when(requestMock.getSession()).thenReturn(sessionMock);

        // Mock per il RequestDispatcher
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/aggiungiSede.jsp")).thenReturn(dispatcherMock);
    }

    @Test
    void testDoGetGestoriDisponibili() throws Exception {
        // Mock dei gestori senza sede
        List<UtenteGestoreSede> gestoriSenzaSede = new ArrayList<>();
        gestoriSenzaSede.add(new UtenteGestoreSede("gestore1", "password", 1));
        when(gestioneGestoreServiceMock.getGestoriSenzaSede()).thenReturn(gestoriSenzaSede);

        // Stampa i dati coinvolti nel test
        System.out.println("Testing DoGet Gestori Disponibili");
        System.out.println("Gestori disponibili: " + gestoriSenzaSede);

        // Usa invokeDoGet invece di chiamare direttamente doGet
        invokeDoGet();

        // Verifica che il dispatcher per la pagina di aggiunta della sede sia stato chiamato
        verify(dispatcherMock).forward(requestMock, responseMock);
    }

    @Test
    void testDoGetGestoriNonDisponibili() throws Exception {
        // Mock di una lista vuota di gestori senza sede
        List<UtenteGestoreSede> gestoriSenzaSede = new ArrayList<>();
        when(gestioneGestoreServiceMock.getGestoriSenzaSede()).thenReturn(gestoriSenzaSede);

        // Stampa i dati coinvolti nel test
        System.out.println("Testing DoGet Gestori Non Disponibili");
        System.out.println("Gestori disponibili: " + gestoriSenzaSede);

        // Usa invokeDoGet invece di chiamare direttamente doGet
        invokeDoGet();

        // Verifica che un errore venga settato nella request
        verify(requestMock).setAttribute("errore", "Nessun gestore disponibile per l'assegnazione della sede.");
    }

    @Test
    void testDoPostSuccess() throws Exception {
        // Parametri validi per la sede
        String indirizzo = "Via Roma 10";
        String citta = "Roma";
        when(requestMock.getParameter("indirizzo")).thenReturn(indirizzo);
        when(requestMock.getParameter("citta")).thenReturn(citta);

        // Mock della sessione
        UtenteGestoreSede utenteGestoreSede = new UtenteGestoreSede("gestoreSede", "password", 1);
        when(sessionMock.getAttribute("user")).thenReturn(utenteGestoreSede);

        // Mock del servizio GestioneGestoreService
        List<UtenteGestoreSede> gestoriSenzaSede = new ArrayList<>();
        gestoriSenzaSede.add(new UtenteGestoreSede("gestoreSede1", "password1", 1));
        when(gestioneGestoreServiceMock.getGestoriSenzaSede()).thenReturn(gestoriSenzaSede);

        // Mock della creazione della sede
        Sede nuovaSede = new Sede(indirizzo, "HairBeauty Now", citta, 0);
        when(gestioneSedeServiceMock.creaSede(nuovaSede)).thenReturn(1); // Successo nella creazione

        // Mock per il RequestDispatcher
        when(requestMock.getRequestDispatcher("/assegnaGestore")).thenReturn(dispatcherMock);

        // Stampa i dati coinvolti nel test
        System.out.println("Testing DoPost Success");
        System.out.println("Sede creata: " + nuovaSede);
        System.out.println("Gestori senza sede: " + gestoriSenzaSede);

        // Usa invokeDoPost invece di chiamare direttamente doPost
        invokeDoPost();

        // Verifica il redirect alla pagina di assegnazione gestore
        verify(responseMock).sendRedirect(requestMock.getContextPath() + "/assegnaGestore");
    }

    @Test
    void testDoPostNoGestoriDisponibili() throws Exception {
        // Parametri validi per la sede
        String indirizzo = "Via Roma 10";
        String citta = "Roma";
        when(requestMock.getParameter("indirizzo")).thenReturn(indirizzo);
        when(requestMock.getParameter("citta")).thenReturn(citta);

        // Mock della sessione
        UtenteGestoreSede utenteGestoreSede = new UtenteGestoreSede("gestoreSede", "password", 1);
        when(sessionMock.getAttribute("user")).thenReturn(utenteGestoreSede);

        // Mock di una lista vuota di gestori senza sede
        List<UtenteGestoreSede> gestoriSenzaSede = new ArrayList<>();
        when(gestioneGestoreServiceMock.getGestoriSenzaSede()).thenReturn(gestoriSenzaSede);

        // Stampa i dati coinvolti nel test
        System.out.println("Testing DoPost No Gestori Disponibili");
        System.out.println("Gestori senza sede: " + gestoriSenzaSede);

        // Usa invokeDoPost invece di chiamare direttamente doPost
        invokeDoPost();

        // Verifica che la risposta venga reindirizzata alla pagina di errore
        verify(responseMock).sendRedirect(requestMock.getContextPath() + "/aggiungiSede?errore=Nessun gestore disponibile per l'assegnazione.");
    }

    // Metodo per invocare doGet con riflessione
    private void invokeDoGet() throws Exception {
        Method doGetMethod = AggiungiSedeServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(aggiungiSedeServlet, requestMock, responseMock);
    }

    // Metodo per invocare doPost con riflessione
    private void invokeDoPost() throws Exception {
        Method doPostMethod = AggiungiSedeServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(aggiungiSedeServlet, requestMock, responseMock);
    }
}
