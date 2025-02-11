package unit.GestioneSede;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import it.unisa.application.model.dao.ProfessionistaDAO;
import it.unisa.application.model.dao.SedeDAO;
import it.unisa.application.model.entity.Professionista;
import it.unisa.application.model.entity.Sede;
import it.unisa.application.sottosistemi.GestioneSede.service.GestioneProfessionistaService;
import org.junit.jupiter.api.*;
import org.mockito.*;
import unit.DAO.DatabaseSetupForTest;

import java.util.Arrays;
import java.util.List;

@DisplayName("Test per il servizio GestioneProfessionistaService")
public class GestioneProfessionistaServiceTest {

    private GestioneProfessionistaService gestioneProfessionistaService;

    @Mock
    private ProfessionistaDAO professionistaDAOMock;

    @Mock
    private SedeDAO sedeDAOMock;
    @BeforeAll
    static void globalSetup() {
        DatabaseSetupForTest.configureH2DataSource();
        System.out.println("Datasource H2 configurato per i test.");
    }
    @BeforeEach
    void setUp() {
        // Inizializza i mock
        MockitoAnnotations.openMocks(this);

        // Crea l'oggetto del servizio con i mock già iniettati tramite il costruttore
        gestioneProfessionistaService = new GestioneProfessionistaService(professionistaDAOMock, sedeDAOMock);
    }

    @Test
    @DisplayName("Recupero sede per ID - Successo")
    void testGetSedeByIdSuccess() {
        System.out.println("Test: Recupero sede per ID - Successo");

        int sedeId = 1;
        // Creiamo un oggetto Sede mock con il costruttore corretto
        Sede mockSede = new Sede("Indirizzo Test", "Sede Test", "Città Test", sedeId);
        System.out.println("Dati sedeId: " + sedeId);
        System.out.println("Dati sede mock: " + mockSede);

        // Definiamo il comportamento del mock
        when(sedeDAOMock.getSedeById(sedeId)).thenReturn(mockSede);

        // Chiamata al servizio
        Sede result = gestioneProfessionistaService.getSedeById(sedeId);

        // Verifica il risultato
        assertNotNull(result, "La sede non dovrebbe essere null.");
        assertEquals(sedeId, result.getId(), "L'ID della sede dovrebbe essere uguale a quello fornito.");
        assertEquals("Sede Test", result.getNome(), "Il nome della sede dovrebbe corrispondere a quello mockato.");
        verify(sedeDAOMock).getSedeById(sedeId);
        System.out.println("Esito: Sede recuperata con successo.\n");
    }

    @Test
    @DisplayName("Assunzione professionista - Successo")
    void testAssumiProfessionistaSuccess() {
        System.out.println("Test: Assunzione professionista - Successo");

        // Creiamo un professionista fittizio con il costruttore corretto
        Professionista professionista = new Professionista(1, "professionista1", 1, Arrays.asList());
        System.out.println("Dati professionista: " + professionista);

        // Definiamo il comportamento del mock
        doNothing().when(professionistaDAOMock).insertProfessionista(professionista);

        // Chiamata al servizio
        gestioneProfessionistaService.assumiProfessionista(professionista);

        // Verifica che il metodo insertProfessionista sia stato chiamato correttamente
        verify(professionistaDAOMock).insertProfessionista(professionista);
        System.out.println("Esito: Professionista assunto con successo.\n");
    }

    @Test
    @DisplayName("Rimozione professionista - Successo")
    void testRimuoviProfessionistaSuccess() {
        System.out.println("Test: Rimozione professionista - Successo");

        int professionistaId = 1;
        // Comportamento mock: rimuovere il professionista con successo
        when(professionistaDAOMock.rimuoviProfessionista(professionistaId)).thenReturn(true);

        // Chiamata al servizio
        String result = gestioneProfessionistaService.rimuoviProfessionista(professionistaId);

        // Verifica che il risultato sia quello atteso
        assertEquals("successo", result, "La rimozione del professionista dovrebbe essere riuscita.");

        // Verifica che il metodo del mock sia stato invocato
        verify(professionistaDAOMock).rimuoviProfessionista(professionistaId);
        System.out.println("Esito: Professionista rimosso con successo.\n");
    }


    @Test
    @DisplayName("Rimozione professionista - Fallimento (prenotazioni attive)")
    void testRimuoviProfessionistaFailure() {
        System.out.println("Test: Rimozione professionista - Fallimento (prenotazioni attive)");

        int professionistaId = 1;
        // Comportamento mock: fallimento della rimozione per prenotazioni attive
        when(professionistaDAOMock.rimuoviProfessionista(professionistaId)).thenReturn(false);

        // Chiamata al servizio
        String result = gestioneProfessionistaService.rimuoviProfessionista(professionistaId);

        // Verifica che il risultato sia quello atteso
        assertEquals("Errore durante la rimozione del professionista o ci sono prenotazioni attive.", result);

        // Verifica che il metodo del mock sia stato invocato
        verify(professionistaDAOMock).rimuoviProfessionista(professionistaId);
        System.out.println("Esito: Errore durante la rimozione del professionista.\n");
    }




    @Test
    @DisplayName("Recupero professionisti per sede")
    void testGetProfessionistiBySede() {
        System.out.println("Test: Recupero professionisti per sede");

        int sedeId = 1;
        // Creiamo una lista di professionisti mock
        List<Professionista> professionistiMock = Arrays.asList(
                new Professionista(1, "professionista1", sedeId, Arrays.asList()),
                new Professionista(2, "professionista2", sedeId, Arrays.asList())
        );
        System.out.println("Dati sedeId: " + sedeId);
        System.out.println("Professionisti mock: " + professionistiMock);

        // Definiamo il comportamento del mock
        when(professionistaDAOMock.getProfessionistiBySede(sedeId)).thenReturn(professionistiMock);

        // Chiamata al servizio
        List<Professionista> result = gestioneProfessionistaService.getProfessionistiBySede(sedeId);

        // Verifica il risultato
        assertNotNull(result);
        assertEquals(2, result.size(), "Dovrebbero esserci 2 professionisti per la sede.");
        verify(professionistaDAOMock).getProfessionistiBySede(sedeId);
        System.out.println("Esito: Professionisti recuperati con successo.\n");
    }
}