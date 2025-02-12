package integration.GestionePrenotazioni;

import it.unisa.application.model.dao.FasciaOrariaDAO;
import it.unisa.application.model.entity.FasciaOraria;
import it.unisa.application.sottosistemi.GestionePrenotazioni.service.FasciaOrariaService;
import org.junit.jupiter.api.*;
import org.mockito.*;
import unit.DAO.DatabaseSetupForTest;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FasciaOrariaServiceIntegrationTest {

    @Mock
    private FasciaOrariaDAO fasciaOrariaDAOMock;

    private FasciaOrariaService fasciaOrariaService;

    @BeforeAll
    static void setupDatabase() {
        // Configura il database, se necessario
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Inizializziamo FasciaOrariaService e iniettiamo il mock di FasciaOrariaDAO
        fasciaOrariaService = new FasciaOrariaService(fasciaOrariaDAOMock);
    }

    @Test
    @DisplayName("TC01.1: Recupera Fascia Oraria - Successo")
    void testGetFasciaOrariaSuccess() {
        int professionistaId = 1;
        LocalDate giorno = LocalDate.of(2025, 2, 11);
        String fascia = "10:00-10:30";

        // Simula il recupero di una fascia oraria dal database
        FasciaOraria fasciaOrariaMock = new FasciaOraria(1, professionistaId, giorno, fascia, true);
        when(fasciaOrariaDAOMock.getFasciaOraria(professionistaId, giorno, fascia)).thenReturn(fasciaOrariaMock);

        System.out.println("\nRecupero Fascia Oraria - Successo");
        System.out.println("Dati di input:");
        System.out.println("Professionista ID: " + professionistaId + " - Giorno: " + giorno + " - Fascia: " + fascia);

        // Chiamata al metodo del servizio
        FasciaOraria risultato = fasciaOrariaService.getFasciaOraria(professionistaId, giorno, fascia);

        // Verifica che la fascia oraria sia stata recuperata correttamente
        assertNotNull(risultato, "La fascia oraria non dovrebbe essere null.");
        assertEquals(fasciaOrariaMock.getId(), risultato.getId(), "Gli ID non coincidono.");
        assertEquals(fasciaOrariaMock.getProfessionistaId(), risultato.getProfessionistaId(), "L'ID del professionista non coincide.");
        assertEquals(fasciaOrariaMock.getGiorno(), risultato.getGiorno(), "Il giorno non coincide.");
        assertEquals(fasciaOrariaMock.getFascia(), risultato.getFascia(), "La fascia oraria non coincide.");
        assertTrue(risultato.isDisponibile(), "La fascia oraria dovrebbe essere disponibile.");

        // Verifica che il metodo getFasciaOraria sia stato chiamato una sola volta
        verify(fasciaOrariaDAOMock, times(1)).getFasciaOraria(professionistaId, giorno, fascia);
    }

    @Test
    @DisplayName("TC01.2: Aggiorna Fascia Oraria - Successo")
    void testUpdateFasciaOrariaSuccess() throws SQLException {
        // Dati di input
        int professionistaId = 1;
        LocalDate giorno = LocalDate.of(2025, 2, 11);
        String fascia = "10:00-10:30";
        boolean disponibilita = false;

        FasciaOraria fasciaOrariaMock = new FasciaOraria(1, professionistaId, giorno, fascia, disponibilita);

        // Se il metodo restituiva un valore (es. booleano, o altro), modifica il mock in questo modo:
        when(fasciaOrariaDAOMock.aggiornaFasciaOraria(fasciaOrariaMock)).thenReturn(true);  // O qualsiasi valore tu stia restituendo

        System.out.println("\nAggiorna Fascia Oraria - Successo");
        System.out.println("Dati di input:");
        System.out.println("Professionista ID: " + professionistaId + " - Giorno: " + giorno + " - Fascia: " + fascia);

        // Chiamata al metodo del servizio
        fasciaOrariaService.updateFasciaOraria(fasciaOrariaMock);

        // Verifica che il metodo aggiornaFasciaOraria sia stato chiamato
        verify(fasciaOrariaDAOMock, times(1)).aggiornaFasciaOraria(fasciaOrariaMock);
    }


    @Test
    @DisplayName("TC02: Recupera Fascia Oraria Non Trovata")
    void testGetFasciaOrariaNotFound() {
        int professionistaId = 1;
        LocalDate giorno = LocalDate.of(2025, 2, 11);
        String fascia = "10:00-10:30";

        // Simula che non ci sia nessuna fascia oraria nel database
        when(fasciaOrariaDAOMock.getFasciaOraria(professionistaId, giorno, fascia)).thenReturn(null);

        System.out.println("\nRecupero Fascia Oraria Non Trovata");
        System.out.println("Dati di input:");
        System.out.println("Professionista ID: " + professionistaId + " - Giorno: " + giorno + " - Fascia: " + fascia);

        // Chiamata al metodo del servizio
        FasciaOraria risultato = fasciaOrariaService.getFasciaOraria(professionistaId, giorno, fascia);

        // Verifica che la fascia oraria non sia stata trovata
        assertNull(risultato, "La fascia oraria dovrebbe essere null.");
        verify(fasciaOrariaDAOMock, times(1)).getFasciaOraria(professionistaId, giorno, fascia);
    }

    @Test
    @DisplayName("TC03: Aggiorna Fascia Oraria - Errore di SQL")
    void testUpdateFasciaOrariaSQLException() throws SQLException {
        // Dati di input
        int professionistaId = 1;
        LocalDate giorno = LocalDate.of(2025, 2, 11);
        String fascia = "10:00-10:30";
        boolean disponibilita = false;

        FasciaOraria fasciaOrariaMock = new FasciaOraria(1, professionistaId, giorno, fascia, disponibilita);

        // Simula un'eccezione di SQL
        doThrow(new SQLException("Errore di aggiornamento")).when(fasciaOrariaDAOMock).aggiornaFasciaOraria(fasciaOrariaMock);

        System.out.println("\nAggiorna Fascia Oraria - Errore di SQL");

        // Chiamata al metodo del servizio e verifica che venga lanciata l'eccezione
        assertThrows(SQLException.class, () -> fasciaOrariaService.updateFasciaOraria(fasciaOrariaMock));

        // Verifica che il metodo aggiornaFasciaOraria sia stato chiamato una sola volta
        verify(fasciaOrariaDAOMock, times(1)).aggiornaFasciaOraria(fasciaOrariaMock);
    }
}
