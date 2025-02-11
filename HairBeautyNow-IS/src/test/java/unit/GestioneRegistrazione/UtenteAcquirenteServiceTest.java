package unit.GestioneRegistrazione;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import it.unisa.application.model.dao.UtenteAcquirenteDAO;
import it.unisa.application.model.entity.UtenteAcquirente;
import it.unisa.application.sottosistemi.GestioneRegistrazione.service.UtenteAcquirenteService;
import org.junit.jupiter.api.*;
import org.mockito.*;

@DisplayName("Test per il servizio UtenteAcquirenteService")
public class UtenteAcquirenteServiceTest {

    private UtenteAcquirenteService utenteAcquirenteService;

    @Mock
    private UtenteAcquirenteDAO utenteAcquirenteDAOMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inizializza i mock
        utenteAcquirenteService = new UtenteAcquirenteService(utenteAcquirenteDAOMock);
    }

    @Test
    @DisplayName("Creazione utente - Successo")
    void testCreateUserSuccess() {
        UtenteAcquirente utente = new UtenteAcquirente("utente1", "utente1@example.com", "password123", "Nome", "Cognome", "Città");
        when(utenteAcquirenteDAOMock.insert(utente)).thenReturn(true);

        System.out.println("Test: Creazione utente - Successo");
        System.out.println("Dati dell'utente: " + utente);

        boolean result = utenteAcquirenteService.createUser(utente);

        assertTrue(result, "L'utente dovrebbe essere creato con successo.");
        verify(utenteAcquirenteDAOMock).insert(utente);
        System.out.println("Esito: L'utente è stato creato con successo.\n");
    }

    @Test
    @DisplayName("Creazione utente - Fallimento")
    void testCreateUserFailure() {
        UtenteAcquirente utente = new UtenteAcquirente("utente2", "utente2@example.com", "password456", "Nome2", "Cognome2", "Città2");
        when(utenteAcquirenteDAOMock.insert(utente)).thenReturn(false);

        System.out.println("Test: Creazione utente - Fallimento");
        System.out.println("Dati dell'utente: " + utente);

        boolean result = utenteAcquirenteService.createUser(utente);

        assertFalse(result, "L'utente non dovrebbe essere creato in caso di errore nel DAO.");
        verify(utenteAcquirenteDAOMock).insert(utente);
        System.out.println("Esito: L'utente non è stato creato.\n");
    }
}