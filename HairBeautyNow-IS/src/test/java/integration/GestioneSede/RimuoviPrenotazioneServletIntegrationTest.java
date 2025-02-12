package integration.GestioneSede;

import it.unisa.application.sottosistemi.GestioneSede.view.RimuoviPrenotazioneServlet;
import it.unisa.application.sottosistemi.GestionePrenotazioni.service.PrenotazioneService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import unit.DAO.DatabaseSetupForTest;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class RimuoviPrenotazioneServletIntegrationTest {

    private RimuoviPrenotazioneServlet rimuoviPrenotazioneServlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private HttpSession sessionMock;
    private PrenotazioneService prenotazioneServiceMock;

    @BeforeAll
    static void setupDatabase() {
        // Configura il database per i test
        DatabaseSetupForTest.configureH2DataSource();
    }
    @BeforeEach
    void setUp() {
        rimuoviPrenotazioneServlet = new RimuoviPrenotazioneServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);

        // Mock PrintWriter
        PrintWriter writerMock = mock(PrintWriter.class);
        try {
            when(responseMock.getWriter()).thenReturn(writerMock);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Mock PrenotazioneService
        prenotazioneServiceMock = mock(PrenotazioneService.class);

        // Inject the mock PrenotazioneService into the servlet using reflection
        try {
            Field field = RimuoviPrenotazioneServlet.class.getDeclaredField("gestionePrenotazioneService");
            field.setAccessible(true);
            field.set(rimuoviPrenotazioneServlet, prenotazioneServiceMock);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testDoPostSuccess() throws Exception {
        // Configura i dati per il test (ID prenotazione valido)
        String prenotazioneIdStr = "1"; // Assicurati che l'ID sia valido

        System.out.println("Parametro prenotazioneId: " + prenotazioneIdStr);

        when(requestMock.getParameter("prenotazioneId")).thenReturn(prenotazioneIdStr);
        // Mock del risultato positivo dalla logica della rimozione della prenotazione
        when(prenotazioneServiceMock.rimuoviPrenotazione(anyInt())).thenReturn("Prenotazione rimossa con successo");

        // Chiamata al metodo doPost
        invokeDoPost();

        // Verifica che lo stato della risposta sia 200
        verify(responseMock).setStatus(HttpServletResponse.SC_OK);
        verify(responseMock.getWriter()).write("Prenotazione rimossa con successo");

        // Stampare i dati della risposta
        System.out.println("Status della risposta: " + HttpServletResponse.SC_OK);
    }

    @Test
    void testDoPostMissingPrenotazioneId() throws Exception {
        // Mock della richiesta senza parametro prenotazioneId
        when(requestMock.getParameter("prenotazioneId")).thenReturn(null);

        // Chiamata al metodo doPost() della servlet
        invokeDoPost();

        // Verifica che venga restituito un errore con stato BAD_REQUEST
        verify(responseMock).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(responseMock.getWriter()).write(argumentCaptor.capture());

        // Stampare il messaggio di errore
        System.out.println("Messaggio di errore: " + argumentCaptor.getValue());

        // Verifica che la risposta contenga il messaggio "ID prenotazione mancante"
        assert argumentCaptor.getValue().contains("ID prenotazione mancante");
    }

    @Test
    void testDoPostSQLException() throws Exception {
        // Mock della richiesta con parametro valido
        when(requestMock.getParameter("prenotazioneId")).thenReturn("1");

        // Simula un errore di SQLException nel servizio
        when(prenotazioneServiceMock.rimuoviPrenotazione(1)).thenThrow(new SQLException("Errore durante la rimozione"));

        // Chiamata al metodo doPost() della servlet
        invokeDoPost();

        // Verifica che venga restituito un errore con stato INTERNAL_SERVER_ERROR
        verify(responseMock).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(responseMock.getWriter()).write(argumentCaptor.capture());

        // Stampare il messaggio di errore
        System.out.println("Messaggio di errore: " + argumentCaptor.getValue());

        // Verifica che la risposta contenga il messaggio di errore
        assert argumentCaptor.getValue().contains("Errore durante la rimozione della prenotazione");
    }

    private void invokeDoPost() throws Exception {
        Method doPostMethod = RimuoviPrenotazioneServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(rimuoviPrenotazioneServlet, requestMock, responseMock);
    }
}
