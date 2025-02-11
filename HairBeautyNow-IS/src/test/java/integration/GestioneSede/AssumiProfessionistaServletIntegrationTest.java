package integration.GestioneSede;

import it.unisa.application.sottosistemi.GestioneSede.view.AssumiProfessionistaServlet;
import it.unisa.application.sottosistemi.GestioneSede.service.GestioneProfessionistaService;
import it.unisa.application.model.entity.UtenteGestoreSede;
import it.unisa.application.model.entity.Professionista;
import it.unisa.application.model.entity.Sede;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import unit.DAO.DatabaseSetupForTest;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

public class AssumiProfessionistaServletIntegrationTest {

    private AssumiProfessionistaServlet assumiProfessionistaServlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private HttpSession sessionMock;
    private RequestDispatcher dispatcherMock;
    private GestioneProfessionistaService gestioneProfessionistaServiceMock;

    @BeforeAll
    static void setupDatabase() {
        // Configura il database se necessario
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        assumiProfessionistaServlet = new AssumiProfessionistaServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
        dispatcherMock = mock(RequestDispatcher.class);

        // Mock del servizio GestioneProfessionistaService
        gestioneProfessionistaServiceMock = mock(GestioneProfessionistaService.class);

        // Iniezione del mock nella servlet (utilizzando la riflessione)
        Field field = AssumiProfessionistaServlet.class.getDeclaredField("gestioneProfessionistaService");
        field.setAccessible(true);
        field.set(assumiProfessionistaServlet, gestioneProfessionistaServiceMock);

        when(requestMock.getSession()).thenReturn(sessionMock);
    }
    @Test
    void testDoPostSuccess() throws Exception {
        // Parametro valido
        String nomeProfessionista = "Giovanni Rossi";
        when(requestMock.getParameter("nome")).thenReturn(nomeProfessionista);
        System.out.println("Parametro nome professionista: " + nomeProfessionista);

        // Mock dell'utente loggato (UtenteGestoreSede)
        UtenteGestoreSede utenteGestoreSede = new UtenteGestoreSede("gestoreSede", "password", 1);
        when(sessionMock.getAttribute("user")).thenReturn(utenteGestoreSede);
        System.out.println("Utente loggato: " + utenteGestoreSede.getUsernameUGS());


        // Mock del servizio di assunzione professionista
        // Creiamo un mock della Sede, che ora richiede un indirizzo, nome, città e id
        Sede sedeMock = new Sede("Via Roma 10", "Sede Test", "Città", 1);
        when(gestioneProfessionistaServiceMock.getSedeById(1)).thenReturn(sedeMock);
        System.out.println("Sede trovata: " + sedeMock.getNome() + ", " + sedeMock.getIndirizzo() + ", " + sedeMock.getCitta());

        // Usa doNothing() perché il metodo assumiProfessionista è void
        doNothing().when(gestioneProfessionistaServiceMock).assumiProfessionista(any(Professionista.class));

        // Mock per PrintWriter
        PrintWriter printWriterMock = mock(PrintWriter.class);
        when(responseMock.getWriter()).thenReturn(printWriterMock);

        // Mock del RequestDispatcher per il forwarding alla pagina di successo
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/successPage.jsp")).thenReturn(dispatcherMock);

        // Chiamata al metodo doPost() della servlet
        invokeDoPost();

        // Verifica che il metodo assumiProfessionista sia stato invocato
        verify(gestioneProfessionistaServiceMock).assumiProfessionista(any(Professionista.class));
        System.out.println("Metodo assumiProfessionista invocato");

        // Verifica che il metodo write del PrintWriter sia stato invocato per inviare la risposta
        verify(printWriterMock).write("{\"message\":\"Professionista assunto con successo!\"}");
        System.out.println("Risposta inviata: Professionista assunto con successo!");

        // Verifica il forward alla pagina di successo
        verify(responseMock).getWriter();
        System.out.println("Risposta scritta al client.");
    }

    @Test
    void testDoPostFailureInvalidNome() throws Exception {
        // Parametro nome professionista vuoto
        String nomeProfessionista = ""; // Nome professionista non valido
        when(requestMock.getParameter("nome")).thenReturn(nomeProfessionista);
        System.out.println("Parametro nome professionista: " + nomeProfessionista);

        // Mock dell'utente loggato (UtenteGestoreSede)
        UtenteGestoreSede utenteGestoreSede = new UtenteGestoreSede("gestoreSede", "password", 1);
        when(sessionMock.getAttribute("user")).thenReturn(utenteGestoreSede);
        System.out.println("Utente loggato: " + utenteGestoreSede.getUsernameUGS());

        // Mock della Sede associata
        Sede sedeMock = new Sede("Via Roma 10", "Sede Test", "Città", 1);
        when(gestioneProfessionistaServiceMock.getSedeById(1)).thenReturn(sedeMock);
        System.out.println("Sede trovata: " + sedeMock.getNome() + ", " + sedeMock.getIndirizzo() + ", " + sedeMock.getCitta());

        // Mock del servizio assunzione professionista, ma non invocato in questo caso
        doNothing().when(gestioneProfessionistaServiceMock).assumiProfessionista(any(Professionista.class));

        // Mock per PrintWriter
        PrintWriter printWriterMock = mock(PrintWriter.class);
        when(responseMock.getWriter()).thenReturn(printWriterMock);  // Configuriamo il mock per getWriter

        // Chiamata al metodo doPost() della servlet
        invokeDoPost();  // Eseguiamo il metodo post della servlet

        // Verifica che il metodo write del PrintWriter sia stato invocato con il messaggio di errore
        verify(printWriterMock).write("{\"message\":\"Errore: Nome professionista non valido\"}");
        System.out.println("Risposta inviata: Errore: Nome professionista non valido");

        // Verifica che la risposta sia stata effettivamente scritta
        verify(responseMock).getWriter(); // Controlliamo che il metodo getWriter() sia stato chiamato
        System.out.println("Risposta scritta al client.");
    }

    private void invokeDoPost() throws Exception {
        Method doPostMethod = AssumiProfessionistaServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(assumiProfessionistaServlet, requestMock, responseMock);
    }

}
