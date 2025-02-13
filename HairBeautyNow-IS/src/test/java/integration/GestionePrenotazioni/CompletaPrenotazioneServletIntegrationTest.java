package integration.GestionePrenotazioni;

import it.unisa.application.model.dao.MetodoDiPagamentoDAO;
import it.unisa.application.model.entity.*;
import it.unisa.application.sottosistemi.GestionePrenotazioni.service.FasciaOrariaService;
import it.unisa.application.sottosistemi.GestionePrenotazioni.service.PrenotazioneService;
import it.unisa.application.sottosistemi.GestioneServizi.service.ServizioService;
import it.unisa.application.sottosistemi.utilities.PagamentoFactory;
import it.unisa.application.sottosistemi.utilities.PagamentoStrategy;
import it.unisa.application.sottosistemi.GestionePrenotazioni.view.CompletaPrenotazioneServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import unit.DAO.DatabaseSetupForTest;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

        // Configura i mock per la sessione e il dispatcher
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/metodoPagamento.jsp")).thenReturn(dispatcherMock);

        // Inietta i mock nei campi della servlet
        injectMock("prenotazioneService", prenotazioneServiceMock);
        injectMock("fasciaOrariaService", fasciaOrariaServiceMock);
        injectMock("servizioService", servizioServiceMock);
        injectMock("metodoDiPagamentoDAO", metodoDiPagamentoDAOMock);
    }

    @Test
    void testDoPost() throws Exception {
        // Configura i dati della sessione
        String giornoString = "2025-02-15";
        String orario = "10:00-12:00";
        int professionistaId = 1;
        String servizioName = "Taglio di capelli";
        double prezzo = 30.0;
        UtenteAcquirente utente = new UtenteAcquirente("username", "email@example.com", "password", "Nome", "Cognome", "Città");

        when(sessionMock.getAttribute("giorno")).thenReturn(giornoString);
        when(sessionMock.getAttribute("orario")).thenReturn(orario);
        when(sessionMock.getAttribute("professionistaId")).thenReturn(String.valueOf(professionistaId));
        when(sessionMock.getAttribute("servizioPrenotato")).thenReturn(servizioName);
        when(sessionMock.getAttribute("user")).thenReturn(utente);

        // Configura i parametri della richiesta per il metodo di pagamento
        when(requestMock.getParameter("metodoPagamento")).thenReturn("visa");
        when(requestMock.getParameter("numeroCarta")).thenReturn("1234567890123456");
        when(requestMock.getParameter("cvv")).thenReturn("123");
        when(requestMock.getParameter("scadenza")).thenReturn("12/25");
        when(requestMock.getParameter("indirizzo")).thenReturn("Via Roma 1");

        // Configura il comportamento del servizio per il prezzo
        when(servizioServiceMock.getPrezzoByNome(servizioName)).thenReturn(prezzo);

        // Configura il comportamento del DAO per il metodo di pagamento
        when(metodoDiPagamentoDAOMock.getMetodoDiPagamentoByUsername(utente.getUsername())).thenReturn(null);

        // Configura il comportamento del servizio per la fascia oraria
        LocalDate giorno = LocalDate.parse(giornoString, DateTimeFormatter.ISO_LOCAL_DATE);
        FasciaOraria fasciaOraria = new FasciaOraria(1, professionistaId, giorno, orario, true);
        when(fasciaOrariaServiceMock.getFasciaOraria(professionistaId, giorno, orario)).thenReturn(fasciaOraria);

        // Crea l'oggetto Prenotazione con gli stessi valori
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setServizioName(servizioName);
        prenotazione.setProfessionistaId(professionistaId);
        prenotazione.setPrezzo(prezzo);
        prenotazione.setData(LocalDateTime.of(2025, 2, 15, 10, 0));  // Assicurati che la data sia uguale
        prenotazione.setUsername(utente.getUsername());  // Usa l'utente dalla sessione

        // Stampa i valori coinvolti
        System.out.println("Test doPost - Dati coinvolti:");
        System.out.println("Data prenotazione: " + giornoString);
        System.out.println("Orario prenotazione: " + orario);
        System.out.println("Professionista ID: " + professionistaId);
        System.out.println("Servizio: " + servizioName);
        System.out.println("Prezzo: " + prezzo);
        System.out.println("Utente: " + utente.getUsername());
        System.out.println("Metodo di pagamento: " + "visa");
        System.out.println("Numero carta: " + "1234567890123456");
        System.out.println("CVV: " + "123");
        System.out.println("Scadenza carta: " + "12/25");
        System.out.println("Indirizzo: " + "Via Roma 1");

        // Invoca il metodo doPost tramite riflessione
        invokeDoPost();

        // Stampa i risultati per il metodo di pagamento e prenotazione
        System.out.println("Verifica che il metodo di pagamento sia stato aggiunto...");
        verify(metodoDiPagamentoDAOMock).addMetodoDiPagamento(any(MetodoDiPagamento.class));

        // Verifica che la prenotazione sia stata aggiunta
        System.out.println("Verifica che la prenotazione sia stata aggiunta...");
        verify(prenotazioneServiceMock, times(1)).addPrenotazione(argThat(prenotazioneArg -> {
            System.out.println("Prenotazione inserita: " + prenotazioneArg);
            return prenotazioneArg.getServizioName().equals(prenotazione.getServizioName()) &&
                    prenotazioneArg.getProfessionistaId() == prenotazione.getProfessionistaId() &&
                    prenotazioneArg.getPrezzo() == prenotazione.getPrezzo() &&
                    prenotazioneArg.getData().equals(prenotazione.getData()) &&
                    prenotazioneArg.getUsername().equals(prenotazione.getUsername());
        }));

        // Verifica che la fascia oraria sia stata aggiornata
        System.out.println("Verifica che la fascia oraria sia stata aggiornata...");
        verify(fasciaOrariaServiceMock).updateFasciaOraria(fasciaOraria);

        // Verifica che la risposta sia stata reindirizzata a index.jsp
        System.out.println("Verifica che la risposta sia stata reindirizzata a index.jsp...");
        verify(responseMock).sendRedirect("index.jsp");
    }


    // Metodo per invocare doPost con riflessione
    private void invokeDoPost() throws Exception {
        Method doPostMethod = CompletaPrenotazioneServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(servlet, requestMock, responseMock);
    }

    // Metodo per iniettare i mock nei campi della servlet
    private void injectMock(String fieldName, Object mockInstance) throws Exception {
        var field = CompletaPrenotazioneServlet.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(servlet, mockInstance);
    }
}
