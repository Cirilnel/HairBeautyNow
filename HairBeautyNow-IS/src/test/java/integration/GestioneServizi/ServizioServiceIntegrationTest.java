package integration.GestioneServizi;

import it.unisa.application.model.dao.ServizioDAO;
import it.unisa.application.model.entity.Servizio;
import it.unisa.application.sottosistemi.GestioneServizi.service.ServizioService;
import org.junit.jupiter.api.*;
import org.mockito.*;
import unit.DAO.DatabaseSetupForTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class ServizioServiceIntegrationTest {

    @Mock
    private ServizioDAO servizioDAOMock;

    private ServizioService servizioService;

    @BeforeAll
    static void setupDatabase() {
        // Configura il database, se necessario
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        servizioService = new ServizioService(servizioDAOMock);  // Servizio con il mock del DAO
    }

    @Test
    @DisplayName("TC01.1: Get All Servizi - Successo")
    void testGetAllServiziSuccess() {
        // Prepara i dati di test
        Servizio servizio1 = new Servizio("Servizio A", 100.0, "Tipo A", "Descrizione A");
        Servizio servizio2 = new Servizio("Servizio B", 150.0, "Tipo B", "Descrizione B");
        List<Servizio> servizi = List.of(servizio1, servizio2);

        // Configura il mock per ottenere tutti i servizi
        when(servizioDAOMock.getAll()).thenReturn(servizi);

        System.out.println("\nGet All Servizi - Successo");

        // Chiamata al metodo del servizio
        List<Servizio> risultato = servizioService.getAllServizi();

        // Verifica che i servizi siano stati recuperati correttamente
        assertNotNull(risultato, "La lista dei servizi non dovrebbe essere null.");
        assertEquals(2, risultato.size(), "La lista dovrebbe contenere 2 servizi.");
        risultato.forEach(s -> System.out.println(s.getNome() + " - " + s.getDescrizione()));

        verify(servizioDAOMock, times(1)).getAll();
    }

    @Test
    @DisplayName("TC01.2: Get Prezzo By Nome - Successo")
    void testGetPrezzoByNomeSuccess() {
        String nomeServizio = "Servizio A";
        Servizio servizio = new Servizio(nomeServizio, 100.0, "Tipo A", "Descrizione A");

        // Configura il mock per ottenere un servizio per nome
        when(servizioDAOMock.getByNome(nomeServizio)).thenReturn(servizio);

        System.out.println("\nGet Prezzo By Nome - Successo");

        // Chiamata al metodo del servizio
        double risultato = servizioService.getPrezzoByNome(nomeServizio);

        // Verifica che il prezzo restituito sia corretto
        assertEquals(100.0, risultato, "Il prezzo dovrebbe essere 100.0.");

        verify(servizioDAOMock, times(1)).getByNome(nomeServizio);
    }

    @Test
    @DisplayName("TC01.3: Get Prezzo By Nome - Servizio Non Esistente")
    void testGetPrezzoByNomeServiceNotFound() {
        String nomeServizio = "Servizio Non Esistente";

        // Configura il mock per restituire null quando il servizio non esiste
        when(servizioDAOMock.getByNome(nomeServizio)).thenReturn(null);

        System.out.println("\nGet Prezzo By Nome - Servizio Non Esistente");

        // Chiamata al metodo del servizio
        double risultato = servizioService.getPrezzoByNome(nomeServizio);

        // Verifica che il prezzo restituito sia 0.0 se il servizio non è trovato
        assertEquals(0.0, risultato, "Il prezzo dovrebbe essere 0.0 se il servizio non esiste.");

        verify(servizioDAOMock, times(1)).getByNome(nomeServizio);
    }

    @Test
    @DisplayName("TC01.4: Get Servizi Per Tipo - Successo")
    void testGetServiziPerTipoSuccess() {
        // Prepara i dati di test
        Servizio servizio1 = new Servizio("Servizio A", 100.0, "Tipo A", "Descrizione A");
        Servizio servizio2 = new Servizio("Servizio B", 150.0, "Tipo A", "Descrizione B");
        Servizio servizio3 = new Servizio("Servizio C", 200.0, "Tipo B", "Descrizione C");

        Map<String, List<Servizio>> serviziPerTipo = new HashMap<>();
        serviziPerTipo.put("Tipo A", List.of(servizio1, servizio2));
        serviziPerTipo.put("Tipo B", List.of(servizio3));

        // Configura il mock per ottenere i servizi raggruppati per tipo
        when(servizioDAOMock.getServiziPerTipo()).thenReturn(serviziPerTipo);

        System.out.println("\nGet Servizi Per Tipo - Successo");

        // Chiamata al metodo del servizio
        Map<String, List<Servizio>> risultato = servizioService.getServiziPerTipo();

        // Verifica che i servizi siano stati recuperati correttamente per tipo
        assertNotNull(risultato, "La mappa dei servizi per tipo non dovrebbe essere null.");
        assertEquals(2, risultato.size(), "La mappa dovrebbe contenere 2 tipi di servizi.");
        risultato.forEach((tipo, servizi) ->
                System.out.println("Tipo: " + tipo + " - Servizi: " + servizi.size())
        );

        verify(servizioDAOMock, times(1)).getServiziPerTipo();
    }
}
