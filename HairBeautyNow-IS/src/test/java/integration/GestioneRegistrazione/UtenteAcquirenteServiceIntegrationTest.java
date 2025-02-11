package integration.GestioneRegistrazione;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import it.unisa.application.model.dao.UtenteAcquirenteDAO;
import it.unisa.application.model.entity.UtenteAcquirente;
import it.unisa.application.sottosistemi.GestioneRegistrazione.service.UtenteAcquirenteService;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.sql.SQLException;

public class UtenteAcquirenteServiceIntegrationTest {

    @Mock
    private UtenteAcquirenteDAO utenteAcquirenteDAOMock;

    private UtenteAcquirenteService utenteAcquirenteService;

    @BeforeAll
    static void globalSetup() {
        System.out.println("Configurazione iniziale.");
    }

    @BeforeEach
    void setUp() {
        // Creiamo il mock dell'UtenteAcquirenteDAO
        MockitoAnnotations.openMocks(this); // Inizializza i mock

        // Inizializzazione del servizio con il mock del DAO
        utenteAcquirenteService = new UtenteAcquirenteService(utenteAcquirenteDAOMock);
    }

    @AfterEach
    void tearDown() {
        // Chiusura e pulizia, se necessaria
    }

    @Test
    @DisplayName("TC01.1: Creazione utente acquirente - Successo")
    void testCreateUserSuccess() {
        // Dati dell'utente che stiamo per creare
        UtenteAcquirente utenteAcquirente = new UtenteAcquirente("user123", "testuser@example.com", "password123",
                "Test User", "Test Surname", "Roma");

        // Stampa dei dati dell'utente che stiamo creando
        System.out.println("Dati dell'utente da creare: " + utenteAcquirente);

        // Configura il comportamento del mock per il metodo createUser
        when(utenteAcquirenteDAOMock.insert(utenteAcquirente)).thenReturn(true); // Simula la creazione con successo

        // Chiamata al servizio per creare l'utente
        boolean isCreated = utenteAcquirenteService.createUser(utenteAcquirente);

        // Verifica che la creazione sia avvenuta con successo
        assertTrue(isCreated, "L'utente dovrebbe essere creato con successo.");

        // Verifica che il metodo del DAO sia stato chiamato correttamente
        verify(utenteAcquirenteDAOMock, times(1)).insert(utenteAcquirente);

        System.out.println("Test completato: Creazione utente acquirente riuscita.");
    }


    @Test
    @DisplayName("TC01.2: Creazione utente acquirente - Fallito, dati mancanti")
    void testCreateUserFail() {
        // Proviamo a creare un utente senza fornire l'email, che è un campo obbligatorio
        UtenteAcquirente utenteAcquirente = new UtenteAcquirente("user123", null, "password123",
                "Test User", "Test Surname", "Roma");

        // Stampa dei dati dell'utente (mancanti email)
        System.out.println("Dati dell'utente da creare (mancante email): " + utenteAcquirente);

        // Configura il comportamento del mock per il metodo createUser
        when(utenteAcquirenteDAOMock.insert(utenteAcquirente)).thenReturn(false); // Simula il fallimento della creazione

        // Chiamata al servizio per creare l'utente
        boolean isCreated = utenteAcquirenteService.createUser(utenteAcquirente);

        // Verifica che la creazione fallisca
        assertFalse(isCreated, "La creazione dell'utente dovrebbe fallire quando l'email manca.");

        // Verifica che il metodo del DAO sia stato chiamato
        verify(utenteAcquirenteDAOMock, times(1)).insert(utenteAcquirente);

        System.out.println("Test completato: Creazione utente acquirente fallita per email mancante.");
    }
}
