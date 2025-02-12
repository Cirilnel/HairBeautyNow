package unit.GestioneCatena;

import it.unisa.application.model.entity.Sede;
import it.unisa.application.sottosistemi.GestioneCatena.service.GestioneSedeService;
import it.unisa.application.model.dao.SedeDAO;
import org.junit.jupiter.api.*;
import unit.DAO.DatabaseSetupForTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test per GestioneSedeService (DAO mockato, senza modifiche al codice del Service)")
public class GestioneSedeServiceTest {

    private GestioneSedeService gestioneSedeService;
    private SedeDAO sedeDAOMock;

    @BeforeAll
    static void globalSetup() {
        // Configura il DB in-memory H2 (opzionale, dipende dalla tua configurazione di test)
        DatabaseSetupForTest.configureH2DataSource();
        System.out.println("Datasource H2 configurato per i test.");
    }

    @BeforeEach
    void setUp() {
        // Crea il mock del DAO
        sedeDAOMock = mock(SedeDAO.class);

        // Inizializza il servizio con il DAO mockato
        gestioneSedeService = new GestioneSedeService(sedeDAOMock);
    }

    @Test
    @DisplayName("testCreaSedeSuccess - Inserimento corretto di una sede")
    void testCreaSedeSuccess() {
        System.out.println("Inserimento corretto di una sede");

        // Creiamo una sede valida (tutti i campi NOT NULL)
        Sede nuovaSede = new Sede("Via Roma 123", "Sede Milano", "Milano", 0);

        System.out.println("Dati della nuova sede:");
        System.out.println("Indirizzo: " + nuovaSede.getIndirizzo());
        System.out.println("Nome: " + nuovaSede.getNome());
        System.out.println("Città: " + nuovaSede.getCitta());
        System.out.println("ID iniziale (prima del salvataggio): " + nuovaSede.getId());

        // Simula il comportamento del DAO quando viene inserita la sede (restituendo un ID generato)
        when(sedeDAOMock.insertSedeAndReturnID(nuovaSede)).thenReturn(1); // L'ID restituito è 1 (successo)

        // Chiamata al servizio per l'inserimento
        int idGenerato = gestioneSedeService.creaSede(nuovaSede);

        System.out.println("ID restituito dopo l'inserimento: " + idGenerato);

        // Verifica che l'ID restituito sia positivo
        assertTrue(idGenerato > 0, "L'inserimento dovrebbe restituire un ID positivo.");
    }

    @Test
    @DisplayName("testCreaSedeFailure - Inserimento fallito di una sede")
    void testCreaSedeFailure() {
        System.out.println("Inserimento fallito di una sede");

        // Creiamo una sede valida (tutti i campi NOT NULL)
        Sede nuovaSede = new Sede("Via Roma 123", "Sede Milano", "Milano", 0);

        // Simula il comportamento del DAO quando fallisce l'inserimento (restituendo 0, errore)
        when(sedeDAOMock.insertSedeAndReturnID(nuovaSede)).thenReturn(0); // L'ID restituito è 0 (fallimento)

        // Chiamata al servizio per l'inserimento
        int idGenerato = gestioneSedeService.creaSede(nuovaSede);

        System.out.println("ID restituito dopo il tentativo di inserimento: " + idGenerato);

        // Verifica che l'ID restituito sia 0, a simboleggiare un errore nell'inserimento
        assertEquals(0, idGenerato, "L'inserimento dovrebbe restituire 0 in caso di errore.");
    }
}
