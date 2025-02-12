package integration.GestionePrenotazioni;

import it.unisa.application.model.dao.PrenotazioneDAO;
import it.unisa.application.model.dao.ProfessionistaDAO;
import it.unisa.application.model.entity.Prenotazione;
import it.unisa.application.sottosistemi.GestionePrenotazioni.service.StoricoOrdiniService;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StoricoOrdiniServiceIntegrationTest {

    @Mock
    private PrenotazioneDAO prenotazioneDAOMock;

    @Mock
    private ProfessionistaDAO professionistaDAOMock;

    @InjectMocks
    private StoricoOrdiniService storicoOrdiniService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        storicoOrdiniService = new StoricoOrdiniService(prenotazioneDAOMock, professionistaDAOMock);
    }

    @Test
    @DisplayName("TC01: Recupera prenotazioni per un dato username")
    void testGetPrenotazioniByUsername() throws SQLException {
        // Mock delle prenotazioni per l'utente "mario.rossi"
        List<Prenotazione> prenotazioniMock = Arrays.asList(
                new Prenotazione(1, "Taglio", 5, LocalDateTime.of(2025, 2, 15, 10, 0), "mario.rossi", 25.0),
                new Prenotazione(2, "Colore", 7, LocalDateTime.of(2025, 2, 16, 15, 0), "mario.rossi", 50.0)
        );

        // Configura il mock per il metodo DAO
        when(prenotazioneDAOMock.getPrenotazioniByUsername("mario.rossi")).thenReturn(prenotazioniMock);

        // Chiamata al servizio
        List<Prenotazione> prenotazioni = storicoOrdiniService.getPrenotazioniByUsername("mario.rossi");

        // Debug: Stampa prenotazioni
        System.out.println("Prenotazioni per mario.rossi:");
        prenotazioni.forEach(p -> System.out.println(p.toString()));

        // Verifica
        assertNotNull(prenotazioni);
        assertEquals(2, prenotazioni.size());

        Prenotazione p1 = prenotazioni.get(0);
        assertEquals(1, p1.getId());
        assertEquals("Taglio", p1.getServizioName());
        assertEquals(5, p1.getProfessionistaId());
        assertEquals(LocalDateTime.of(2025, 2, 15, 10, 0), p1.getData());
        assertEquals("mario.rossi", p1.getUsername());
        assertEquals(25.0, p1.getPrezzo());

        Prenotazione p2 = prenotazioni.get(1);
        assertEquals(2, p2.getId());
        assertEquals("Colore", p2.getServizioName());
        assertEquals(7, p2.getProfessionistaId());
        assertEquals(LocalDateTime.of(2025, 2, 16, 15, 0), p2.getData());
        assertEquals("mario.rossi", p2.getUsername());
        assertEquals(50.0, p2.getPrezzo());

        verify(prenotazioneDAOMock, times(1)).getPrenotazioniByUsername("mario.rossi");
    }

    @Test
    @DisplayName("TC02: Recupera il prezzo di un servizio")
    void testGetPrezzoByServizio() {
        // Mock del prezzo del servizio "Taglio"
        when(professionistaDAOMock.getPrezzoByServizio("Taglio")).thenReturn(25.0);

        // Chiamata al servizio
        double prezzo = storicoOrdiniService.getPrezzoByServizio("Taglio");

        // Debug
        System.out.println("Prezzo per Taglio: " + prezzo);

        // Verifica
        assertEquals(25.0, prezzo);
        verify(professionistaDAOMock, times(1)).getPrezzoByServizio("Taglio");
    }

    @Test
    @DisplayName("TC03: Recupera l'indirizzo di una sede")
    void testGetIndirizzoBySedeId() {
        // Mock dell'indirizzo per sedeId = 1
        when(professionistaDAOMock.getIndirizzoBySedeId(1)).thenReturn("Via Roma 10, Napoli");

        // Chiamata al servizio
        String indirizzo = storicoOrdiniService.getIndirizzoBySedeId(1);

        // Debug
        System.out.println("Indirizzo per sede ID 1: " + indirizzo);

        // Verifica
        assertEquals("Via Roma 10, Napoli", indirizzo);
        verify(professionistaDAOMock, times(1)).getIndirizzoBySedeId(1);
    }
}
