package unit.GestioneServizi;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import it.unisa.application.model.dao.ServizioDAO;
import it.unisa.application.model.entity.Servizio;
import it.unisa.application.sottosistemi.GestioneServizi.service.ServizioService;
import org.junit.jupiter.api.*;
import java.util.*;

@DisplayName("Test per il servizio ServizioService")
public class ServizioServiceTest {

    private ServizioService servizioService;
    private ServizioDAO servizioDAOMock;

    @BeforeEach
    void setUp() {
        // Mock del DAO
        servizioDAOMock = mock(ServizioDAO.class);

        // Inizializzazione del servizio con il DAO mockato
        servizioService = new ServizioService(servizioDAOMock);
    }

    @Test
    @DisplayName("Recupero tutti i servizi - Successo")
    void testGetAllServiziSuccess() {
        System.out.println("Test: Recupero tutti i servizi - Successo");

        // Prepara i dati mockati
        List<Servizio> serviziMock = new ArrayList<>();
        serviziMock.add(new Servizio("Taglio", 20.0, "Haircut", "Taglio capelli base"));
        serviziMock.add(new Servizio("Colore", 40.0, "Haircolor", "Trattamento di colorazione"));

        // Stampa dei dati mockati
        System.out.println("Dati mockati per il test 'Recupero tutti i servizi':");
        for (Servizio servizio : serviziMock) {
            System.out.println("Nome: " + servizio.getNome() + ", Prezzo: " + servizio.getPrezzo() + ", Tipo: " + servizio.getTipo() + ", Descrizione: " + servizio.getDescrizione());
        }

        // Simula il comportamento del DAO per il metodo getAll
        when(servizioDAOMock.getAll()).thenReturn(serviziMock);

        // Chiamata al servizio
        List<Servizio> result = servizioService.getAllServizi();

        // Verifica il risultato
        System.out.println("Risultato del test 'Recupero tutti i servizi':");
        for (Servizio servizio : result) {
            System.out.println("Nome: " + servizio.getNome() + ", Prezzo: " + servizio.getPrezzo() + ", Tipo: " + servizio.getTipo() + ", Descrizione: " + servizio.getDescrizione());
        }

        // Verifica il risultato
        assertNotNull(result);
        assertEquals(2, result.size(), "Dovrebbero esserci 2 servizi.");
        assertEquals("Taglio", result.get(0).getNome(), "Il nome del primo servizio dovrebbe essere 'Taglio'.");
        assertEquals(40.0, result.get(1).getPrezzo(), "Il prezzo del secondo servizio dovrebbe essere 40.0.");
    }

    @Test
    @DisplayName("Recupero prezzo servizio per nome - Successo")
    void testGetPrezzoByNomeSuccess() {
        System.out.println("Test: Recupero prezzo servizio per nome - Successo");

        // Prepara il dato mockato
        Servizio servizioMock = new Servizio("Taglio", 20.0, "Haircut", "Taglio capelli base");

        // Stampa del dato mockato
        System.out.println("Dati mockati per il test 'Recupero prezzo servizio per nome':");
        System.out.println("Nome: " + servizioMock.getNome() + ", Prezzo: " + servizioMock.getPrezzo() + ", Tipo: " + servizioMock.getTipo() + ", Descrizione: " + servizioMock.getDescrizione());

        // Simula il comportamento del DAO per il metodo getByNome
        when(servizioDAOMock.getByNome("Taglio")).thenReturn(servizioMock);

        // Chiamata al servizio
        double prezzo = servizioService.getPrezzoByNome("Taglio");

        // Stampa del risultato
        System.out.println("Risultato del test 'Recupero prezzo servizio per nome': Prezzo per 'Taglio' = " + prezzo);

        // Verifica il risultato
        assertEquals(20.0, prezzo, "Il prezzo del servizio 'Taglio' dovrebbe essere 20.0.");
    }

    @Test
    @DisplayName("Recupero servizi per tipo - Successo")
    void testGetServiziPerTipoSuccess() {
        System.out.println("Test: Recupero servizi per tipo - Successo");

        // Prepara i dati mockati
        List<Servizio> serviziMock = new ArrayList<>();
        serviziMock.add(new Servizio("Taglio", 20.0, "Haircut", "Taglio capelli base"));
        serviziMock.add(new Servizio("Colore", 40.0, "Haircolor", "Trattamento di colorazione"));

        // Simula il comportamento del DAO per il metodo getAll
        when(servizioDAOMock.getAll()).thenReturn(serviziMock);

        // Simula il comportamento del DAO per il metodo getServiziPerTipo
        Map<String, List<Servizio>> serviziPerTipoMock = new HashMap<>();
        List<Servizio> haircutServices = new ArrayList<>();
        List<Servizio> haircolorServices = new ArrayList<>();

        // Aggiungi i servizi ai rispettivi tipi
        for (Servizio servizio : serviziMock) {
            if ("Haircut".equals(servizio.getTipo())) {
                haircutServices.add(servizio);
            } else if ("Haircolor".equals(servizio.getTipo())) {
                haircolorServices.add(servizio);
            }
        }

        // Popola la mappa con i servizi raggruppati
        serviziPerTipoMock.put("Haircut", haircutServices);
        serviziPerTipoMock.put("Haircolor", haircolorServices);

        // Simula il comportamento del metodo getServiziPerTipo
        when(servizioDAOMock.getServiziPerTipo()).thenReturn(serviziPerTipoMock);

        // Chiamata al servizio
        Map<String, List<Servizio>> serviziPerTipo = servizioService.getServiziPerTipo();

        // Verifica che i servizi siano raggruppati per tipo
        assertNotNull(serviziPerTipo);
        assertEquals(2, serviziPerTipo.size(), "Dovrebbero esserci 2 tipi di servizi.");
        assertTrue(serviziPerTipo.containsKey("Haircut"), "Dovrebbe esserci un servizio di tipo 'Haircut'.");
        assertTrue(serviziPerTipo.containsKey("Haircolor"), "Dovrebbe esserci un servizio di tipo 'Haircolor'.");

        // Aggiungi ulteriori verifiche per i servizi raggruppati
        assertEquals(1, serviziPerTipo.get("Haircut").size(), "Dovrebbe esserci 1 servizio di tipo 'Haircut'.");
        assertEquals(1, serviziPerTipo.get("Haircolor").size(), "Dovrebbe esserci 1 servizio di tipo 'Haircolor'.");
    }

}
