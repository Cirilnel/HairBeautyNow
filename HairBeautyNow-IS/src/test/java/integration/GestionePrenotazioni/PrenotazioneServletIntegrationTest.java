package integration.GestionePrenotazioni;

import it.unisa.application.sottosistemi.GestionePrenotazioni.view.PrenotazioneServlet;
import it.unisa.application.model.entity.Sede;
import it.unisa.application.model.entity.UtenteAcquirente;
import it.unisa.application.sottosistemi.GestionePrenotazioni.service.PrenotazioneService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unit.DAO.DatabaseSetupForTest;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

public class PrenotazioneServletIntegrationTest {
    private PrenotazioneServlet servlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private HttpSession sessionMock;
    private RequestDispatcher dispatcherMock;
    private PrenotazioneService prenotazioneServiceMock;
    @BeforeAll
    static void setupDatabase() {
        // Configura il database, se necessario
        DatabaseSetupForTest.configureH2DataSource();
    }
    @BeforeEach
    void setUp() throws Exception {
        servlet = new PrenotazioneServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
        dispatcherMock = mock(RequestDispatcher.class);
        prenotazioneServiceMock = mock(PrenotazioneService.class);

        // Mock per i metodi di PrenotazioneService
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/saloni.jsp")).thenReturn(dispatcherMock);

        // Iniettare il mock di PrenotazioneService
        injectMock("prenotazioneService", prenotazioneServiceMock);
    }

    @Test
    void testDoPost() throws Exception {
        // Imposta i parametri della richiesta
        String servizioSelezionato = "Massaggio";
        String cittaSelezionata = "Napoli";

        when(requestMock.getParameter("servizio")).thenReturn(servizioSelezionato);
        when(requestMock.getParameter("citta")).thenReturn(cittaSelezionata);

        // Mock della sessione utente
        UtenteAcquirente user = new UtenteAcquirente("user123", "email@test.com", "password", "Mario", "Rossi", "Napoli");
        when(sessionMock.getAttribute("user")).thenReturn(user);

        // Mock dei metodi PrenotazioneService
        List<Sede> sediMock = Arrays.asList(
                new Sede("Via Roma 1", "Salone 1", "Napoli", 1),
                new Sede("Via Napoli 5", "Salone 2", "Napoli", 2)
        );
        Set<String> cittaDisponibiliMock = Set.of("Napoli", "Roma", "Milano");

        when(prenotazioneServiceMock.getSediByCitta(cittaSelezionata)).thenReturn(sediMock);
        when(prenotazioneServiceMock.getCittaDisponibili(any())).thenReturn(cittaDisponibiliMock);

        // Stampa i dati ricevuti nella richiesta
        System.out.println("Dati ricevuti nella richiesta:");
        System.out.println("Servizio Selezionato: " + servizioSelezionato);
        System.out.println("Città Selezionata: " + cittaSelezionata);

        // Invoca il metodo doPost tramite riflessione
        invokeDoPost();

        // Verifica che i dati siano stati salvati nella sessione
        verify(sessionMock).setAttribute("servizioPrenotato", servizioSelezionato);

        // Verifica che i metodi di PrenotazioneService siano stati chiamati correttamente
        verify(prenotazioneServiceMock).getSediByCitta(cittaSelezionata);
        verify(prenotazioneServiceMock).getCittaDisponibili(any());

        // Verifica che la servlet faccia il forward alla pagina saloni.jsp
        verify(requestMock).getRequestDispatcher("/WEB-INF/jsp/saloni.jsp");
        verify(dispatcherMock).forward(requestMock, responseMock);

        // Stampa le sedi e le città disponibili
        System.out.println("Sedi disponibili per la città di " + cittaSelezionata + ":");
        sediMock.forEach(sede -> System.out.println(sede));

        System.out.println("Città disponibili:");
        cittaDisponibiliMock.forEach(System.out::println);
    }

    // Metodo per invocare doPost con riflessione
    private void invokeDoPost() throws Exception {
        Method doPostMethod = PrenotazioneServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(servlet, requestMock, responseMock);
    }

    private void injectMock(String fieldName, Object mockInstance) throws Exception {
        // Inietta il mock nel campo della servlet
        var field = PrenotazioneServlet.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(servlet, mockInstance);
    }
}
