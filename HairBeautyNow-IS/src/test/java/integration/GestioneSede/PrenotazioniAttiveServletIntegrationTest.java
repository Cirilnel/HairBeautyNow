package integration.GestioneSede;

import it.unisa.application.sottosistemi.GestioneSede.view.PrenotazioniAttiveServlet;
import it.unisa.application.sottosistemi.GestionePrenotazioni.service.PrenotazioneService;
import it.unisa.application.model.entity.Prenotazione;
import it.unisa.application.model.entity.UtenteGestoreSede;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import unit.DAO.DatabaseSetupForTest;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class PrenotazioniAttiveServletIntegrationTest {

    private PrenotazioniAttiveServlet prenotazioniAttiveServlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private HttpSession sessionMock;
    private RequestDispatcher dispatcherMock;
    private PrenotazioneService prenotazioneServiceMock;

    @BeforeAll
    static void setupDatabase() {
        // Configura il database per i test
        DatabaseSetupForTest.configureH2DataSource();
    }



    @BeforeEach
    void setUp() {
        prenotazioniAttiveServlet = new PrenotazioniAttiveServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
        dispatcherMock = mock(RequestDispatcher.class);

        // Create a mock PrintWriter
        PrintWriter writerMock = mock(PrintWriter.class);

        // Mock the response.getWriter() to return the mocked writer
        try {
            when(responseMock.getWriter()).thenReturn(writerMock);
        } catch (IOException e) {
            e.printStackTrace(); // Handle IOException (though it should not occur in this case)
        }

        // Mock PrenotazioneService
        prenotazioneServiceMock = mock(PrenotazioneService.class);

        // Inject the mock PrenotazioneService into the servlet using reflection
        try {
            Field field = PrenotazioniAttiveServlet.class.getDeclaredField("gestionePrenotazioniService");
            field.setAccessible(true);
            field.set(prenotazioniAttiveServlet, prenotazioneServiceMock);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        // Mock session behavior
        when(requestMock.getSession()).thenReturn(sessionMock);
    }




    @Test
    void testDoGetSuccess() throws Exception {
        // Mock dell'utente loggato (UtenteGestoreSede)
        UtenteGestoreSede utente = new UtenteGestoreSede("gestoreSede", "passwordSede", 1);
        when(sessionMock.getAttribute("user")).thenReturn(utente);
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/prenotazioniAttive.jsp")).thenReturn(dispatcherMock);

        // Mock della lista delle prenotazioni attive
        List<Prenotazione> prenotazioniAttive = new ArrayList<>();
        LocalDateTime dataPrenotazione = LocalDateTime.of(2025, 2, 11, 14, 30);
        prenotazioniAttive.add(new Prenotazione(1, "Servizio Test", 1, dataPrenotazione, "Giovanni Rossi", 100.50));
        when(prenotazioneServiceMock.getPrenotazioniAttive(1)).thenReturn(prenotazioniAttive);

        // Chiamata al metodo doGet() della servlet
        invokeDoGet();

        // Verifica che la lista delle prenotazioni attive sia stata impostata correttamente
        verify(requestMock).setAttribute("prenotazioniAttive", prenotazioniAttive);
        verify(dispatcherMock).forward(requestMock, responseMock);
    }

    
    @Test
    void testDoGetRedirectToLoginWhenNotAuthenticated() throws Exception {
        // Caso in cui l'utente non è autenticato
        when(sessionMock.getAttribute("user")).thenReturn(null);
        when(requestMock.getContextPath()).thenReturn("/app");

        // Chiamata al metodo doGet() della servlet
        invokeDoGet();

        // Verifica che venga effettuato il redirect alla pagina di login
        verify(responseMock).sendRedirect("/app/loginPage");
    }


    @Test
    void testDoGetFailureSQLException() throws Exception {
        // Mock the user
        UtenteGestoreSede utente = new UtenteGestoreSede("gestoreSede", "passwordSede", 1);
        when(sessionMock.getAttribute("user")).thenReturn(utente);

        // Simulate a SQLException
        when(prenotazioneServiceMock.getPrenotazioniAttive(1)).thenThrow(new SQLException("Errore SQL"));

        // Verify that getWriter() is being called
        PrintWriter writerMock = responseMock.getWriter();
        System.out.println("Writer mock: " + writerMock);  // This will let you verify if the mock is invoked

        // Call the doGet method
        invokeDoGet();

        // Verify the response
        verify(responseMock).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(writerMock).write("Errore durante il recupero delle prenotazioni");
    }


    private void invokeDoGet() throws Exception {
        Method doGetMethod = PrenotazioniAttiveServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(prenotazioniAttiveServlet, requestMock, responseMock);
    }
}
