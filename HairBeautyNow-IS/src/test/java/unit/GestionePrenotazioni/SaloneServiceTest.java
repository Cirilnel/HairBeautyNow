package unit.GestionePrenotazioni;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import it.unisa.application.model.dao.FasciaOrariaDAO;
import it.unisa.application.model.dao.ProfessionistaDAO;
import it.unisa.application.model.entity.FasciaOraria;
import it.unisa.application.model.entity.Professionista;
import it.unisa.application.sottosistemi.GestionePrenotazioni.service.SaloneService;
import org.junit.jupiter.api.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@DisplayName("Test per il servizio SaloneService")
public class SaloneServiceTest {

    private SaloneService saloneService;
    private ProfessionistaDAO professionistaDAOMock;
    private FasciaOrariaDAO fasciaOrariaDAOMock;

    @BeforeAll
    static void globalSetup() {
        // Setup del database o configurazione globale, se necessaria
        System.out.println("Datasource H2 configurato per i test.");
    }

    @BeforeEach
    void setUp() {
        // Mock dei DAO
        professionistaDAOMock = mock(ProfessionistaDAO.class);
        fasciaOrariaDAOMock = mock(FasciaOrariaDAO.class);

        // Inizializzazione del servizio con i DAO mockati
        saloneService = new SaloneService(professionistaDAOMock, fasciaOrariaDAOMock);  // Inizializza correttamente l'oggetto saloneService
    }


    @Test
    @DisplayName("Recupero professionisti per salone - Successo")
    void testGetProfessionistiBySaloneSuccess() {
        System.out.println("Test: Recupero professionisti per salone - Successo");

        // Dati di input per il test
        int saloneId = 1;

        // Creazione di una lista di fasce orarie mockate
        FasciaOraria fascia1 = new FasciaOraria(1, saloneId, LocalDate.of(2025, 2, 11), "09:00", true);
        FasciaOraria fascia2 = new FasciaOraria(2, saloneId, LocalDate.of(2025, 2, 11), "10:00", true);
        List<FasciaOraria> fasceOrarie = Arrays.asList(fascia1, fascia2);

        // Creazione di una lista di professionisti mockati con fasce orarie
        Professionista professionista1 = new Professionista(1, "Mario Rossi", saloneId, fasceOrarie);
        Professionista professionista2 = new Professionista(2, "Giovanni Bianchi", saloneId, fasceOrarie);
        List<Professionista> professionistiMock = Arrays.asList(professionista1, professionista2);

        // Simula il comportamento del DAO per il metodo getProfessionistiBySede
        when(professionistaDAOMock.getProfessionistiBySede(saloneId)).thenReturn(professionistiMock);

        // Chiamata al servizio
        List<Professionista> result = saloneService.getProfessionistiBySalone(saloneId);

        // Verifica il risultato
        assertNotNull(result, "La lista dei professionisti non dovrebbe essere nulla.");
        assertEquals(2, result.size(), "La lista dei professionisti dovrebbe contenere 2 elementi.");
        assertTrue(result.contains(professionista1), "La lista dei professionisti dovrebbe contenere Mario Rossi.");
        assertTrue(result.contains(professionista2), "La lista dei professionisti dovrebbe contenere Giovanni Bianchi.");
        System.out.println("Risultato del test 'Recupero professionisti per salone': " + result);
    }

    @Test
    @DisplayName("Recupero fasce orarie per professionista - Successo")
    void testGetFasceOrarieByProfessionistaSuccess() throws SQLException {
        System.out.println("Test: Recupero fasce orarie per professionista - Successo");

        int professionistaId = 1;

        // Creazione di fasce orarie mockate
        FasciaOraria fascia1 = new FasciaOraria(1, professionistaId, LocalDate.of(2025, 2, 11), "09:00", true);
        FasciaOraria fascia2 = new FasciaOraria(2, professionistaId, LocalDate.of(2025, 2, 11), "10:00", true);
        List<FasciaOraria> fasceOrarieMock = Arrays.asList(fascia1, fascia2);

        // Mappa che si aspetta di ottenere dal servizio
        Map<LocalDate, List<String>> expectedMap = new HashMap<>();
        expectedMap.put(LocalDate.of(2025, 2, 11), Arrays.asList("09:00", "10:00"));

        // Simula il comportamento del DAO per il metodo getFasceOrarieByProfessionista
        when(fasciaOrariaDAOMock.getFasceOrarieByProfessionista(professionistaId)).thenReturn(fasceOrarieMock);

        // Chiamata al servizio
        Map<LocalDate, List<String>> result = saloneService.getFasceOrarieByProfessionista(professionistaId);

        // Verifica il risultato
        assertNotNull(result, "La mappa delle fasce orarie non dovrebbe essere nulla.");
        assertEquals(expectedMap, result, "La mappa delle fasce orarie dovrebbe corrispondere a quella attesa.");
        System.out.println("Risultato del test 'Recupero fasce orarie per professionista': " + result);
    }

    @Test
    @DisplayName("Recupero fasce orarie per professionista - Fallimento (assenza di fasce orarie)")
    void testGetFasceOrarieByProfessionistaNotFound() throws SQLException {
        System.out.println("Test: Recupero fasce orarie per professionista - Fallimento");

        int professionistaId = 1;

        // Simula il comportamento del DAO quando non trova fasce orarie
        when(fasciaOrariaDAOMock.getFasceOrarieByProfessionista(professionistaId)).thenReturn(Collections.emptyList());

        // Chiamata al servizio
        Map<LocalDate, List<String>> result = saloneService.getFasceOrarieByProfessionista(professionistaId);

        // Verifica che la mappa sia vuota
        assertTrue(result.isEmpty(), "La mappa delle fasce orarie dovrebbe essere vuota.");
        System.out.println("Risultato del test 'Recupero fasce orarie per professionista - Fallimento': " + result);
    }
}
