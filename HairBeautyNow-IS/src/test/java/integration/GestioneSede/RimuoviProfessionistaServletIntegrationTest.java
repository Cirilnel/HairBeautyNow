package integration.GestioneSede;

import it.unisa.application.sottosistemi.GestioneSede.view.RimuoviProfessionistaServlet;
import it.unisa.application.sottosistemi.GestioneSede.service.GestioneProfessionistaService;
import it.unisa.application.model.entity.Professionista;
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

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

public class RimuoviProfessionistaServletIntegrationTest {

    private RimuoviProfessionistaServlet rimuoviProfessionistaServlet;
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
        rimuoviProfessionistaServlet = new RimuoviProfessionistaServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
        dispatcherMock = mock(RequestDispatcher.class);

        // Mock del servizio GestioneProfessionistaService
        gestioneProfessionistaServiceMock = mock(GestioneProfessionistaService.class);

        // Iniezione del mock nella servlet (utilizzando la riflessione)
        Field field = RimuoviProfessionistaServlet.class.getDeclaredField("gestioneRimozioneProfessionistaService");
        field.setAccessible(true);
        field.set(rimuoviProfessionistaServlet, gestioneProfessionistaServiceMock);

        when(requestMock.getSession()).thenReturn(sessionMock);
    }

    @Test
    void testDoPostSuccess() throws Exception {
        // Parametro valido per rimuovere un professionista
        String professionistaIdStr = "1";
        when(requestMock.getParameter("professionistaId")).thenReturn(professionistaIdStr);
        System.out.println("Parametro professionistaId: " + professionistaIdStr);

        // Mock dell'utente loggato (UtenteGestoreSede)
        UtenteGestoreSede utenteGestoreSede = new UtenteGestoreSede("gestoreSede", "password", 1);
        when(sessionMock.getAttribute("user")).thenReturn(utenteGestoreSede);
        System.out.println("Utente loggato: " + utenteGestoreSede.getUsernameUGS());

        // Mock della Sede associata
        Sede sedeMock = new Sede("Via Roma 10", "Sede Test", "Città", 1);
        when(gestioneProfessionistaServiceMock.getSedeById(1)).thenReturn(sedeMock);
        System.out.println("Sede trovata: " + sedeMock.getNome() + ", " + sedeMock.getIndirizzo() + ", " + sedeMock.getCitta());

        // Mock del servizio di rimozione del professionista
        when(gestioneProfessionistaServiceMock.rimuoviProfessionista(1)).thenReturn("successo");
        System.out.println("Professionista rimosso con successo");

        // Mock per PrintWriter
        PrintWriter printWriterMock = mock(PrintWriter.class);
        when(responseMock.getWriter()).thenReturn(printWriterMock);

        // Chiamata al metodo doPost() della servlet
        invokeDoPost();

        // Verifica che il metodo di rimozione professionista sia stato invocato
        verify(gestioneProfessionistaServiceMock).rimuoviProfessionista(1);
        System.out.println("Metodo rimuoviProfessionista invocato");

        // Verifica che il metodo write del PrintWriter sia stato invocato per inviare la risposta
        verify(printWriterMock).write("Professionista rimosso con successo.");
        System.out.println("Risposta inviata: Professionista rimosso con successo!");
    }

    @Test
    void testDoPostFailureProfessionistaIdNonValido() throws Exception {
        // Parametro professionistaId non valido
        String professionistaIdStr = "non_un_numero";
        when(requestMock.getParameter("professionistaId")).thenReturn(professionistaIdStr);
        System.out.println("Parametro professionistaId: " + professionistaIdStr);

        // Mock dell'utente loggato (UtenteGestoreSede)
        UtenteGestoreSede utenteGestoreSede = new UtenteGestoreSede("gestoreSede", "password", 1);
        when(sessionMock.getAttribute("user")).thenReturn(utenteGestoreSede);
        System.out.println("Utente loggato: " + utenteGestoreSede.getUsernameUGS());

        // Mock per PrintWriter
        PrintWriter printWriterMock = mock(PrintWriter.class);
        when(responseMock.getWriter()).thenReturn(printWriterMock);  // Configuriamo il mock per getWriter

        // Chiamata al metodo doPost() della servlet
        invokeDoPost();  // Eseguiamo il metodo post della servlet

        // Verifica che il metodo write del PrintWriter sia stato invocato con il messaggio di errore
        verify(printWriterMock).write("ID professionista non valido");
        System.out.println("Risposta inviata: ID professionista non valido");

        // Verifica che la risposta sia stata effettivamente scritta
        verify(responseMock).getWriter(); // Controlliamo che il metodo getWriter() sia stato chiamato
        System.out.println("Risposta scritta al client.");
    }

    @Test
    void testDoPostFailureProfessionistaIdMancante() throws Exception {
        // Parametro professionistaId mancante
        when(requestMock.getParameter("professionistaId")).thenReturn(null);
        System.out.println("Parametro professionistaId mancante");

        // Mock dell'utente loggato (UtenteGestoreSede)
        UtenteGestoreSede utenteGestoreSede = new UtenteGestoreSede("gestoreSede", "password", 1);
        when(sessionMock.getAttribute("user")).thenReturn(utenteGestoreSede);
        System.out.println("Utente loggato: " + utenteGestoreSede.getUsernameUGS());

        // Mock per PrintWriter
        PrintWriter printWriterMock = mock(PrintWriter.class);
        when(responseMock.getWriter()).thenReturn(printWriterMock);

        // Chiamata al metodo doPost() della servlet
        invokeDoPost();  // Eseguiamo il metodo post della servlet

        // Verifica che il metodo write del PrintWriter sia stato invocato con il messaggio di errore
        verify(printWriterMock).write("ID professionista mancante");
        System.out.println("Risposta inviata: ID professionista mancante");

        // Verifica che la risposta sia stata effettivamente scritta
        verify(responseMock).getWriter();
        System.out.println("Risposta scritta al client.");
    }

    private void invokeDoPost() throws Exception {
        Method doPostMethod = RimuoviProfessionistaServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(rimuoviProfessionistaServlet, requestMock, responseMock);
    }

}
