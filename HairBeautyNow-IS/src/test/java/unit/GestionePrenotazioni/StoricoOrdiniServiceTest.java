package unit.GestionePrenotazioni;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import it.unisa.application.model.dao.PrenotazioneDAO;
import it.unisa.application.model.dao.ProfessionistaDAO;
import it.unisa.application.model.entity.Prenotazione;
import it.unisa.application.sottosistemi.GestionePrenotazioni.service.StoricoOrdiniService;
import org.junit.jupiter.api.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@DisplayName("Test per il servizio StoricoOrdiniService")
public class StoricoOrdiniServiceTest {

    private StoricoOrdiniService storicoOrdiniService;
    private PrenotazioneDAO prenotazioneDAOMock;
    private ProfessionistaDAO professionistaDAOMock;

    @BeforeAll
    static void globalSetup() {
        // Setup del database o configurazione globale, se necessaria
        System.out.println("Datasource H2 configurato per i test.");
    }

    @BeforeEach
    void setUp() {
        // Mock dei DAO
        prenotazioneDAOMock = mock(PrenotazioneDAO.class);
        professionistaDAOMock = mock(ProfessionistaDAO.class);

        // Inizializzazione del servizio con i DAO mockati
        storicoOrdiniService = new StoricoOrdiniService(prenotazioneDAOMock, professionistaDAOMock);  // Passa i mock nei costruttori
    }

    @Test
    @DisplayName("Recupero prenotazioni per username - Successo")
    void testGetPrenotazioniByUsernameSuccess() throws SQLException {
        System.out.println("Test: Recupero prenotazioni per username - Successo");

        // Dati di input per il test
        String username = "mario.rossi";

        // Creazione di una lista di prenotazioni mockate con LocalDateTime
        LocalDateTime data1 = LocalDateTime.of(2025, 2, 11, 9, 0);
        LocalDateTime data2 = LocalDateTime.of(2025, 2, 11, 10, 0);
        Prenotazione prenotazione1 = new Prenotazione(1, "Taglio capelli", 1, data1, username, 30.0);
        Prenotazione prenotazione2 = new Prenotazione(2, "Manicure", 1, data2, username, 20.0);
        List<Prenotazione> prenotazioniMock = Arrays.asList(prenotazione1, prenotazione2);

        // Simula il comportamento del DAO per il metodo getPrenotazioniByUsername
        when(prenotazioneDAOMock.getPrenotazioniByUsername(username)).thenReturn(prenotazioniMock);

        // Chiamata al servizio
        List<Prenotazione> result = storicoOrdiniService.getPrenotazioniByUsername(username);

        // Verifica il risultato
        assertNotNull(result, "La lista delle prenotazioni non dovrebbe essere nulla.");
        assertEquals(2, result.size(), "La lista delle prenotazioni dovrebbe contenere 2 elementi.");
        assertTrue(result.contains(prenotazione1), "La lista delle prenotazioni dovrebbe contenere la prenotazione per 'Taglio capelli'.");
        assertTrue(result.contains(prenotazione2), "La lista delle prenotazioni dovrebbe contenere la prenotazione per 'Manicure'.");
        System.out.println("Risultato del test 'Recupero prenotazioni per username': " + result);
    }

    @Test
    @DisplayName("Recupero prezzo per servizio - Successo")
    void testGetPrezzoByServizioSuccess() {
        System.out.println("Test: Recupero prezzo per servizio - Successo");

        // Dati di input per il test
        String servizioName = "Taglio capelli";
        double expectedPrice = 30.0;

        // Simula il comportamento del DAO per il metodo getPrezzoByServizio
        when(professionistaDAOMock.getPrezzoByServizio(servizioName)).thenReturn(expectedPrice);

        // Chiamata al servizio
        double result = storicoOrdiniService.getPrezzoByServizio(servizioName);

        // Verifica il risultato
        assertEquals(expectedPrice, result, "Il prezzo per il servizio dovrebbe essere 30.0.");
        System.out.println("Risultato del test 'Recupero prezzo per servizio': " + result);
    }

    @Test
    @DisplayName("Recupero indirizzo per sedeId - Successo")
    void testGetIndirizzoBySedeIdSuccess() {
        System.out.println("Test: Recupero indirizzo per sedeId - Successo");

        // Dati di input per il test
        int sedeId = 1;
        String expectedAddress = "Via Roma 1, Napoli";

        // Simula il comportamento del DAO per il metodo getIndirizzoBySedeId
        when(professionistaDAOMock.getIndirizzoBySedeId(sedeId)).thenReturn(expectedAddress);

        // Chiamata al servizio
        String result = storicoOrdiniService.getIndirizzoBySedeId(sedeId);

        // Verifica il risultato
        assertEquals(expectedAddress, result, "L'indirizzo per il sedeId dovrebbe essere 'Via Roma 1, Napoli'.");
        System.out.println("Risultato del test 'Recupero indirizzo per sedeId': " + result);
    }

    @Test
    @DisplayName("Recupero prenotazioni per username - Fallimento (assenza di prenotazioni)")
    void testGetPrenotazioniByUsernameNotFound() throws SQLException {
        System.out.println("Test: Recupero prenotazioni per username - Fallimento");

        // Dati di input per il test
        String username = "non.esistente";

        // Simula il comportamento del DAO quando non trova prenotazioni
        when(prenotazioneDAOMock.getPrenotazioniByUsername(username)).thenReturn(Collections.emptyList());

        // Chiamata al servizio
        List<Prenotazione> result = storicoOrdiniService.getPrenotazioniByUsername(username);

        // Verifica che la lista delle prenotazioni sia vuota
        assertTrue(result.isEmpty(), "La lista delle prenotazioni dovrebbe essere vuota.");
        System.out.println("Risultato del test 'Recupero prenotazioni per username - Fallimento': " + result);
    }

    @Test
    @DisplayName("Recupero prezzo per servizio - Fallimento (servizio inesistente)")
    void testGetPrezzoByServizioNotFound() {
        System.out.println("Test: Recupero prezzo per servizio - Fallimento");

        // Dati di input per il test
        String servizioName = "Servizio inesistente";

        // Simula il comportamento del DAO quando il servizio non esiste
        when(professionistaDAOMock.getPrezzoByServizio(servizioName)).thenReturn(0.0);

        // Chiamata al servizio
        double result = storicoOrdiniService.getPrezzoByServizio(servizioName);

        // Verifica il risultato
        assertEquals(0.0, result, "Il prezzo per il servizio inesistente dovrebbe essere 0.0.");
        System.out.println("Risultato del test 'Recupero prezzo per servizio - Fallimento': " + result);
    }

    @Test
    @DisplayName("Recupero indirizzo per sedeId - Fallimento (sede non trovata)")
    void testGetIndirizzoBySedeIdNotFound() {
        System.out.println("Test: Recupero indirizzo per sedeId - Fallimento");

        // Dati di input per il test
        int sedeId = 999; // ID di una sede non esistente
        String expectedAddress = null;

        // Simula il comportamento del DAO quando non trova l'indirizzo
        when(professionistaDAOMock.getIndirizzoBySedeId(sedeId)).thenReturn(expectedAddress);

        // Chiamata al servizio
        String result = storicoOrdiniService.getIndirizzoBySedeId(sedeId);

        // Verifica che l'indirizzo sia null
        assertNull(result, "L'indirizzo per un sedeId inesistente dovrebbe essere null.");
        System.out.println("Risultato del test 'Recupero indirizzo per sedeId - Fallimento': " + result);
    }
}
