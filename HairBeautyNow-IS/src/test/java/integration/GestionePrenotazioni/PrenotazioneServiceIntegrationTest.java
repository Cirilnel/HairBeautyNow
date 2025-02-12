package integration.GestionePrenotazioni;

import it.unisa.application.model.dao.SedeDAO;
import it.unisa.application.model.entity.Sede;
import it.unisa.application.sottosistemi.GestionePrenotazioni.service.PrenotazioneService;
import org.junit.jupiter.api.*;
import org.mockito.*;
import unit.DAO.DatabaseSetupForTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PrenotazioneServiceIntegrationTest {

    @Mock
    private SedeDAO sedeDAOMock;

    @InjectMocks
    private PrenotazioneService prenotazioneService;

    @BeforeAll
    static void setupDatabase() {
        // Configura il database, se necessario
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        // Inizializza i mock
        MockitoAnnotations.openMocks(this);

        // Assicurati che prenotazioneService utilizzi il mock di SedeDAO
       // prenotazioneService = new PrenotazioneService(sedeDAOMock);
    }

    @Test
    @DisplayName("TC01: Recupera tutte le sedi")
    void testGetAllSedi() {
        // Simuliamo il recupero di tutte le sedi dal DAO
        List<Sede> sediMock = Arrays.asList(
                new Sede("Via Roma 1", "Sede 1", "Città 1", 1),
                new Sede("Via Milano 2", "Sede 2", "Città 2", 2)
        );

        // Configura il mock
        when(sedeDAOMock.getAllSedi()).thenReturn(sediMock);

        // Chiamata al servizio
        List<Sede> sedi = prenotazioneService.getAllSedi();

        // Verifica che i dati siano corretti
        assertNotNull(sedi);
        assertEquals(2, sedi.size());
        assertEquals("Città 1", sedi.get(0).getCitta());
        assertEquals("Città 2", sedi.get(1).getCitta());

        // Stampa i dati coinvolti nel test
        System.out.println("Sedi recuperate: ");
        sedi.forEach(sede -> System.out.println("Indirizzo: " + sede.getIndirizzo() + ", Nome: " + sede.getNome() + ", Città: " + sede.getCitta()));

        // Verifica che il mock sia stato invocato correttamente
        verify(sedeDAOMock, times(1)).getAllSedi();
    }

    @Test
    void testGetSediByCitta() {
        // Simuliamo il comportamento del DAO per "Città1"
        List<Sede> sediCitta1 = Arrays.asList(
                new Sede("Via Roma 1", "Sede 1", "Città 1", 1)
        );

        // Configura il mock
        when(sedeDAOMock.getSediByCitta("Città1")).thenReturn(sediCitta1);

        // Chiamata al servizio
        List<Sede> sedi = prenotazioneService.getSediByCitta("Città1");

        // Stampa i dati coinvolti nel test
        System.out.println("Sedi per Città1: ");
        sedi.forEach(sede -> System.out.println("Indirizzo: " + sede.getIndirizzo() + ", Nome: " + sede.getNome() + ", Città: " + sede.getCitta()));

        // Logga i dati per il debug
        System.out.println("Sedi nella città: " + sedi.size());

        // Verifica che il mock sia stato chiamato correttamente
        verify(sedeDAOMock, times(1)).getSediByCitta("Città1");

        // Verifica che i dati siano corretti
        assertNotNull(sedi);
        assertEquals(1, sedi.size()); // Test per la città con 1 sede
        assertEquals("Città 1", sedi.get(0).getCitta());
    }

    @Test
    @DisplayName("TC03: Recupera tutte le città disponibili")
    void testGetCittaDisponibili() {
        List<Sede> sediMock = Arrays.asList(
                new Sede("Via Roma 1", "Sede 1", "Città 1", 1),
                new Sede("Via Milano 2", "Sede 2", "Città 2", 2)
        );

        // Chiamata al servizio
        Set<String> cittaDisponibili = prenotazioneService.getCittaDisponibili(sediMock);

        // Stampa le città coinvolte nel test
        System.out.println("Città disponibili: ");
        cittaDisponibili.forEach(citta -> System.out.println("Città: " + citta));

        // Verifica che le città siano correttamente estratte
        assertNotNull(cittaDisponibili);
        assertEquals(2, cittaDisponibili.size());
        assertTrue(cittaDisponibili.contains("Città 1"));
        assertTrue(cittaDisponibili.contains("Città 2"));
    }
}
