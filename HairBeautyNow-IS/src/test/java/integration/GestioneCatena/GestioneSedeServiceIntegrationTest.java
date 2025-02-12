package integration.GestioneCatena;

import it.unisa.application.model.dao.SedeDAO;
import it.unisa.application.model.entity.Sede;
import it.unisa.application.sottosistemi.GestioneCatena.service.GestioneSedeService;
import org.junit.jupiter.api.*;
import org.mockito.*;
import unit.DAO.DatabaseSetupForTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GestioneSedeServiceIntegrationTest {

    @Mock
    private SedeDAO sedeDAOMock;

    private GestioneSedeService gestioneSedeService;

    @BeforeAll
    static void setupDatabase() {
        // Configura il database, se necessario
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gestioneSedeService = new GestioneSedeService(sedeDAOMock);
    }

    @Test
    @DisplayName("TC01.1: Creazione Sede - Successo")
    void testCreaSedeSuccess() {
        // Crea la nuova sede da inserire (id=0 è un esempio, l'id sarà generato dal database)
        Sede nuovaSede = new Sede("Via Roma 1", "Sede Centrale", "Roma", 0); // Il costruttore ora richiede un id

        // Configura il mock per l'inserimento della sede
        when(sedeDAOMock.insertSedeAndReturnID(nuovaSede)).thenReturn(1); // Supponiamo che l'ID della sede inserita sia 1

        System.out.println("\nCreazione Sede - Successo");
        System.out.println("Dati di input:");
        System.out.println("Indirizzo: " + nuovaSede.getIndirizzo() + ", Nome: " + nuovaSede.getNome() + ", Città: " + nuovaSede.getCitta());

        // Chiamata al metodo del servizio
        int sedeID = gestioneSedeService.creaSede(nuovaSede);

        // Verifica che il metodo ritorni un ID valido
        assertEquals(1, sedeID, "L'ID della sede dovrebbe essere 1.");
        verify(sedeDAOMock, times(1)).insertSedeAndReturnID(nuovaSede);
        System.out.println("Sede creata con successo con ID: " + sedeID);
    }

    @Test
    @DisplayName("TC01.2: Creazione Sede - Fallimento (ID non valido)")
    void testCreaSedeFailureInvalidID() {
        // Crea la nuova sede da inserire (id=0 è un esempio)
        Sede nuovaSede = new Sede("Via Roma 1", "Sede Centrale", "Roma", 0);

        // Configura il mock per l'inserimento della sede che fallisce
        when(sedeDAOMock.insertSedeAndReturnID(nuovaSede)).thenReturn(0); // Simula un fallimento restituendo 0

        System.out.println("\nCreazione Sede - Fallimento (ID non valido)");
        System.out.println("Dati di input:");
        System.out.println("Indirizzo: " + nuovaSede.getIndirizzo() + ", Nome: " + nuovaSede.getNome() + ", Città: " + nuovaSede.getCitta());

        // Chiamata al metodo del servizio
        int sedeID = gestioneSedeService.creaSede(nuovaSede);

        // Verifica che il metodo ritorni 0 in caso di errore
        assertEquals(0, sedeID, "La creazione della sede dovrebbe fallire restituendo 0.");
        verify(sedeDAOMock, times(1)).insertSedeAndReturnID(nuovaSede);
        System.out.println("Errore nella creazione della sede. ID restituito: " + sedeID);
    }

    @Test
    @DisplayName("TC01.3: Creazione Sede - Errore Generico (Eccezione)")
    void testCreaSedeFailureException() {
        // Crea la nuova sede da inserire (id=0 è un esempio)
        Sede nuovaSede = new Sede("Via Roma 1", "Sede Centrale", "Roma", 0);

        // Simula un'eccezione nel DAO (ad esempio, connessione al database fallita)
        when(sedeDAOMock.insertSedeAndReturnID(nuovaSede)).thenThrow(new RuntimeException("Errore durante l'inserimento"));

        System.out.println("\nCreazione Sede - Errore Generico (Eccezione)");
        System.out.println("Dati di input:");
        System.out.println("Indirizzo: " + nuovaSede.getIndirizzo() + ", Nome: " + nuovaSede.getNome() + ", Città: " + nuovaSede.getCitta());

        // Chiamata al metodo del servizio
        int sedeID = gestioneSedeService.creaSede(nuovaSede);

        // Verifica che venga restituito 0 in caso di errore
        assertEquals(0, sedeID, "In caso di errore, la creazione della sede dovrebbe restituire 0.");
        verify(sedeDAOMock, times(1)).insertSedeAndReturnID(nuovaSede);
        System.out.println("Errore nella creazione della sede. ID restituito: " + sedeID);
    }
}
