package unit.GestioneCatena;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import it.unisa.application.model.dao.SedeDAO;
import it.unisa.application.model.entity.Sede;
import it.unisa.application.sottosistemi.GestioneCatena.service.GestioneSedeService;
import org.junit.jupiter.api.*;
import unit.DAO.DatabaseSetupForTest;

@DisplayName("Test per il servizio GestioneSedeService")
public class GestioneSedeServiceTest {

    private GestioneSedeService gestioneSedeService;
    private SedeDAO sedeDAOMock;

    @BeforeAll
    static void globalSetup() {
        DatabaseSetupForTest.configureH2DataSource();
        System.out.println("Datasource H2 configurato per i test.");
    }

    @BeforeEach
    void setUp() {
        // Mock del DAO
        sedeDAOMock = mock(SedeDAO.class);

        // Inizializzazione del servizio con il DAO mockato tramite costruttore
        gestioneSedeService = new GestioneSedeService();

        // Per sicurezza, settiamo il DAO mockato al servizio, se necessario
        // gestioneSedeService.setSedeDAO(sedeDAOMock); (Assicurati che il servizio possa accettare un mock se necessario)
    }

    @Test
    @DisplayName("Creazione di una nuova sede - Successo")
    void testCreaSedeSuccess() {
        System.out.println("Test: Creazione di una nuova sede - Successo");

        // Prepara il dato mockato (passiamo tutti i 4 parametri)
        Sede nuovaSede = new Sede("Via Roma 123", "Sede Milano", "Milano", 0); // 'id' è inizializzato a 0 perché sarà restituito dal DB

        // Simula il comportamento del DAO per il metodo insert
        when(sedeDAOMock.insertSedeAndReturnID(nuovaSede)).thenReturn(1); // Simuliamo che l'inserimento restituisca l'ID 1

        // Chiamata al servizio
        int sedeID = gestioneSedeService.creaSede(nuovaSede);

        // Verifica che l'ID della sede restituito sia 1
        assertEquals(1, sedeID, "L'ID della sede dovrebbe essere 1.");
    }








}
