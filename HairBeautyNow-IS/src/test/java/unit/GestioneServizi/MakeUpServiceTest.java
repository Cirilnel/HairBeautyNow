package unit.GestioneServizi;

import it.unisa.application.model.dao.ServizioDAO;
import it.unisa.application.model.entity.Servizio;
import it.unisa.application.sottosistemi.GestioneServizi.service.MakeUpService;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

@DisplayName("Test per MakeUpService")
public class MakeUpServiceTest {

    private MakeUpService makeUpService;
    private ServizioDAO servizioDAOMock;

    @BeforeEach
    void setUp() {
        // Crea il mock del ServizioDAO
        servizioDAOMock = mock(ServizioDAO.class);
        // Passa il mock del ServizioDAO al costruttore del MakeUpService
        makeUpService = new MakeUpService(servizioDAOMock);
    }

    @Test
    @DisplayName("Recupero tutti i servizi con descrizione - Successo")
    void testGetAllServiziWithDescription() {
        System.out.println("Test: Recupero tutti i servizi con descrizione - Successo");

        // Prepara i dati mockati
        List<Servizio> serviziMock = new ArrayList<>();
        Servizio servizio1 = new Servizio("Taglio", 20.0, "Haircut", "Taglio capelli base");
        Servizio servizio2 = new Servizio("Colore", 40.0, "Haircolor", "Trattamento di colorazione");
        serviziMock.add(servizio1);
        serviziMock.add(servizio2);

        // Stampa dei dati mockati
        System.out.println("Servizi mockati:");
        for (Servizio servizio : serviziMock) {
            System.out.println(servizio);
        }

        // Simula il comportamento del DAO per il metodo getAllWithDescription
        when(servizioDAOMock.getAllWithDescription()).thenReturn(serviziMock);

        // Chiamata al servizio
        List<Servizio> result = makeUpService.getAllServiziWithDescription();

        // Stampa dei risultati del servizio
        System.out.println("Servizi restituiti dal servizio:");
        for (Servizio servizio : result) {
            System.out.println(servizio);
        }

        // Verifica il risultato
        assertNotNull(result);
        assertEquals(2, result.size(), "Dovrebbero esserci 2 servizi.");
        assertEquals("Taglio", result.get(0).getNome(), "Il nome del primo servizio dovrebbe essere 'Taglio'.");
        assertEquals("Colore", result.get(1).getNome(), "Il nome del secondo servizio dovrebbe essere 'Colore'.");
    }

    @Test
    @DisplayName("Recupero prezzo servizio per nome - Successo")
    void testGetPrezzoByNome() {
        System.out.println("Test: Recupero prezzo servizio per nome - Successo");

        // Prepara il dato mockato
        Servizio servizioMock = new Servizio("Taglio", 20.0, "Haircut", "Taglio capelli base");

        // Stampa dei dati mockati
        System.out.println("Servizio mockato:");
        System.out.println(servizioMock);

        // Simula il comportamento del DAO per il metodo getByNome
        when(servizioDAOMock.getByNome("Taglio")).thenReturn(servizioMock);

        // Chiamata al servizio
        double prezzo = makeUpService.getPrezzoByNome("Taglio");

        // Stampa del risultato
        System.out.println("Prezzo restituito dal servizio per 'Taglio': " + prezzo);

        // Verifica il risultato
        assertEquals(20.0, prezzo, "Il prezzo del servizio 'Taglio' dovrebbe essere 20.0.");
    }

    @Test
    @DisplayName("Recupero servizi per tipo - Successo")
    void testGetServiziPerTipo() {
        System.out.println("Test: Recupero servizi per tipo - Successo");

        // Prepara i dati mockati
        List<Servizio> serviziMock = new ArrayList<>();
        Servizio servizio1 = new Servizio("Taglio", 20.0, "Haircut", "Taglio capelli base");
        Servizio servizio2 = new Servizio("Colore", 40.0, "Haircolor", "Trattamento di colorazione");
        serviziMock.add(servizio1);
        serviziMock.add(servizio2);

        // Stampa dei dati mockati
        System.out.println("Servizi mockati per tipo:");
        for (Servizio servizio : serviziMock) {
            System.out.println(servizio);
        }

        // Simula il comportamento del DAO per il metodo getServiziPerTipo
        when(servizioDAOMock.getServiziPerTipo()).thenReturn(mockRaggruppamentoPerTipo(serviziMock));

        // Chiamata al servizio
        Map<String, List<Servizio>> serviziPerTipo = makeUpService.getServiziPerTipo();

        // Stampa dei risultati del servizio
        System.out.println("Servizi raggruppati per tipo:");
        for (Map.Entry<String, List<Servizio>> entry : serviziPerTipo.entrySet()) {
            System.out.println("Tipo: " + entry.getKey());
            for (Servizio servizio : entry.getValue()) {
                System.out.println(servizio);
            }
        }

        // Verifica il risultato
        assertNotNull(serviziPerTipo);
        assertEquals(2, serviziPerTipo.size(), "Dovrebbero esserci 2 tipi di servizi.");
        assertTrue(serviziPerTipo.containsKey("Haircut"), "Dovrebbe esserci un servizio di tipo 'Haircut'.");
        assertTrue(serviziPerTipo.containsKey("Haircolor"), "Dovrebbe esserci un servizio di tipo 'Haircolor'.");
    }

    // Metodo ausiliario per simulare un gruppo di servizi per tipo
    private Map<String, List<Servizio>> mockRaggruppamentoPerTipo(List<Servizio> servizi) {
        Map<String, List<Servizio>> serviziPerTipo = new HashMap<>();
        for (Servizio servizio : servizi) {
            serviziPerTipo.computeIfAbsent(servizio.getTipo(), k -> new ArrayList<>()).add(servizio);
        }
        return serviziPerTipo;
    }
}
