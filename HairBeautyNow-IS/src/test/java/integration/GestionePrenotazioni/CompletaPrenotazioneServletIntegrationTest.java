package integration.GestionePrenotazioni;

import it.unisa.application.sottosistemi.GestionePrenotazioni.view.CompletaPrenotazioneServlet;
import it.unisa.application.model.dao.MetodoDiPagamentoDAO;
import it.unisa.application.model.entity.FasciaOraria;
import it.unisa.application.model.entity.UtenteAcquirente;
import it.unisa.application.sottosistemi.GestionePrenotazioni.service.FasciaOrariaService;
import it.unisa.application.sottosistemi.GestionePrenotazioni.service.PrenotazioneService;
import it.unisa.application.sottosistemi.GestioneServizi.service.ServizioService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unit.DAO.DatabaseSetupForTest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import static org.mockito.Mockito.*;

public class CompletaPrenotazioneServletIntegrationTest {
    private CompletaPrenotazioneServlet servlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private HttpSession sessionMock;
    private RequestDispatcher dispatcherMock;
    private PrenotazioneService prenotazioneServiceMock;
    private FasciaOrariaService fasciaOrariaServiceMock;
    private ServizioService servizioServiceMock;
    private MetodoDiPagamentoDAO metodoDiPagamentoDAOMock;

    @BeforeAll
    static void setupDatabase() {
        // Configura il database, se necessario
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() throws Exception {
        servlet = new CompletaPrenotazioneServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
        dispatcherMock = mock(RequestDispatcher.class);
        prenotazioneServiceMock = mock(PrenotazioneService.class);
        fasciaOrariaServiceMock = mock(FasciaOrariaService.class);
        servizioServiceMock = mock(ServizioService.class);
        metodoDiPagamentoDAOMock = mock(MetodoDiPagamentoDAO.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(requestMock.getRequestDispatcher(anyString())).thenReturn(dispatcherMock);

        injectMock("prenotazioneService", prenotazioneServiceMock);
        injectMock("fasciaOrariaService", fasciaOrariaServiceMock);
        injectMock("servizioService", servizioServiceMock);
        injectMock("metodoDiPagamentoDAO", metodoDiPagamentoDAOMock);
    }

    @Test
    void testDoPostPrenotazioneSuccess() throws Exception {
        // Setup mock di sessione
        when(sessionMock.getAttribute("giorno")).thenReturn("2025-02-15");
        when(sessionMock.getAttribute("orario")).thenReturn("08:00-08:30");
        when(sessionMock.getAttribute("professionistaId")).thenReturn("1");
        when(sessionMock.getAttribute("servizioPrenotato")).thenReturn("Massaggio");
        when(sessionMock.getAttribute("user")).thenReturn(new UtenteAcquirente("user123", "email@test.com", "pass", "Mario", "Rossi", "Napoli"));
        when(servizioServiceMock.getPrezzoByNome("Massaggio")).thenReturn(50.0);

        // Stampa i valori di sessione per il debug
        System.out.println("Giorno: " + sessionMock.getAttribute("giorno"));
        System.out.println("Orario: " + sessionMock.getAttribute("orario"));
        System.out.println("Professionista ID: " + sessionMock.getAttribute("professionistaId"));
        System.out.println("Servizio Prenotato: " + sessionMock.getAttribute("servizioPrenotato"));
        System.out.println("Utente: " + sessionMock.getAttribute("user"));

        FasciaOraria fasciaOraria = new FasciaOraria(1, 1, LocalDate.of(2025, 2, 15), "08:00-08:30", true);
        when(fasciaOrariaServiceMock.getFasciaOraria(1, LocalDate.of(2025, 2, 15), "08:00-08:30"))
                .thenReturn(fasciaOraria);

        // Invoca il doPost
        invokeDoPost();

        // Verifica le interazioni e stampa i risultati
        verify(prenotazioneServiceMock).addPrenotazione(any());
        verify(fasciaOrariaServiceMock).updateFasciaOraria(any());
        verify(responseMock).sendRedirect("index.jsp");
    }

    @Test
    void testDoPostPrenotazioneFailure() throws Exception {
        when(sessionMock.getAttribute("giorno")).thenReturn(null);
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/metodoPagamento.jsp")).thenReturn(dispatcherMock);

        // Stampa i valori di sessione per il debug
        System.out.println("Giorno: " + sessionMock.getAttribute("giorno"));
        System.out.println("Orario: " + sessionMock.getAttribute("orario"));

        // Invoca il doPost
        invokeDoPost();

        // Verifica le interazioni e stampa il risultato
        verify(requestMock).setAttribute(eq("errore"), anyString());
        verify(dispatcherMock).forward(requestMock, responseMock);
    }

    // Metodo per invocare doPost con riflessione
    private void invokeDoPost() throws Exception {
        Method doPostMethod = CompletaPrenotazioneServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(servlet, requestMock, responseMock);
    }

    private void injectMock(String fieldName, Object mockInstance) throws Exception {
        Field field = CompletaPrenotazioneServlet.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(servlet, mockInstance);
    }
}
