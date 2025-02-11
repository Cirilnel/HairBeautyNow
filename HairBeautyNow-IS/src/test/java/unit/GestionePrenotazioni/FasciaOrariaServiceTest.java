package unit.GestionePrenotazioni;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import it.unisa.application.model.dao.FasciaOrariaDAO;
import it.unisa.application.model.entity.FasciaOraria;
import it.unisa.application.sottosistemi.GestionePrenotazioni.service.FasciaOrariaService;
import org.junit.jupiter.api.*;
import unit.DAO.DatabaseSetupForTest;

import java.sql.SQLException;
import java.time.LocalDate;

@DisplayName("Test per il servizio FasciaOrariaService")
public class FasciaOrariaServiceTest {

    private FasciaOrariaService fasciaOrariaService;
    private FasciaOrariaDAO fasciaOrariaDAOMock;

    @BeforeAll
    static void globalSetup() {
        DatabaseSetupForTest.configureH2DataSource();
        System.out.println("Datasource H2 configurato per i test.");
    }


    @BeforeEach
    void setUp() {
        // Mock del DAO
        fasciaOrariaDAOMock = mock(FasciaOrariaDAO.class);

        // Inizializzazione del servizio con il DAO mockato tramite costruttore
      //  fasciaOrariaService = new FasciaOrariaService(fasciaOrariaDAOMock);  // Ensure that the service is using the mock DAO
    }

    @Test
    @DisplayName("Recupero fascia oraria - Successo")
    void testGetFasciaOrariaSuccess() {
        System.out.println("Test: Recupero fascia oraria - Successo");

        // Dati di input per il test
        int professionistaId = 1;
        LocalDate giorno = LocalDate.of(2025, 2, 11);
        String orario = "09:00";

        // Creazione di un oggetto FasciaOraria mockato
        FasciaOraria fasciaOrariaMock = new FasciaOraria(1, professionistaId, giorno, orario, true);

        // Simula il comportamento del DAO per il metodo getFasciaOraria
        when(fasciaOrariaDAOMock.getFasciaOraria(professionistaId, giorno, orario)).thenReturn(fasciaOrariaMock);

        // Chiamata al servizio
        FasciaOraria result = fasciaOrariaService.getFasciaOraria(professionistaId, giorno, orario);

        // Verifica il risultato
        assertNotNull(result, "La fascia oraria non dovrebbe essere nulla.");
        assertEquals(professionistaId, result.getProfessionistaId(), "Il professionista ID dovrebbe essere uguale.");
        assertEquals(giorno, result.getGiorno(), "Il giorno dovrebbe essere uguale.");
        assertEquals(orario, result.getFascia(), "L'orario dovrebbe essere uguale.");
        System.out.println("Risultato del test 'Recupero fascia oraria': " + result);
    }


    @Test
    @DisplayName("Aggiornamento fascia oraria - Successo")
    void testUpdateFasciaOrariaSuccess() throws SQLException {
        System.out.println("Test: Aggiornamento fascia oraria - Successo");

        FasciaOraria fasciaOrariaMock = new FasciaOraria(1, 1, LocalDate.of(2025, 2, 11), "09:00", true);

        // Mock the return value of aggiornaFasciaOraria, assuming it returns a boolean
        when(fasciaOrariaDAOMock.aggiornaFasciaOraria(fasciaOrariaMock)).thenReturn(true);  // assuming it returns boolean

        // Call the service method
        fasciaOrariaService.updateFasciaOraria(fasciaOrariaMock);

        // Verify that the method aggiornaFasciaOraria was called exactly once
        verify(fasciaOrariaDAOMock, times(1)).aggiornaFasciaOraria(fasciaOrariaMock);
        System.out.println("Risultato del test 'Aggiornamento fascia oraria': Aggiornamento riuscito.");
    }




    @Test
    @DisplayName("Recupero fascia oraria non trovata - Fallimento")
    void testGetFasciaOrariaNotFound() {
        System.out.println("Test: Recupero fascia oraria non trovata - Fallimento");

        // Dati di input per il test
        int professionistaId = 1;
        LocalDate giorno = LocalDate.of(2025, 2, 11);
        String orario = "09:00";

        // Simula il comportamento del DAO quando non trova la fascia oraria
        when(fasciaOrariaDAOMock.getFasciaOraria(professionistaId, giorno, orario)).thenReturn(null);

        // Chiamata al servizio
        FasciaOraria result = fasciaOrariaService.getFasciaOraria(professionistaId, giorno, orario);

        // Verifica che il risultato sia nullo
        assertNull(result, "La fascia oraria dovrebbe essere nulla.");
        System.out.println("Risultato del test 'Recupero fascia oraria non trovata': Nessun risultato trovato.");
    }
}
