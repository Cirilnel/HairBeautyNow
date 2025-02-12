package integration.GestionePrenotazioni;

import it.unisa.application.model.dao.FasciaOrariaDAO;
import it.unisa.application.model.dao.ProfessionistaDAO;
import it.unisa.application.model.entity.FasciaOraria;
import it.unisa.application.model.entity.Professionista;
import it.unisa.application.sottosistemi.GestionePrenotazioni.service.SaloneService;
import org.junit.jupiter.api.*;
import org.mockito.*;
import unit.DAO.DatabaseSetupForTest;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SaloneServiceIntegrationTest {

    @Mock
    private ProfessionistaDAO professionistaDAOMock;

    @Mock
    private FasciaOrariaDAO fasciaOrariaDAOMock;

    @InjectMocks
    private SaloneService saloneService;

    @BeforeAll
    static void setupDatabase() {
        // Configura il database, se necessario
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        saloneService = new SaloneService(professionistaDAOMock, fasciaOrariaDAOMock);
    }


    @Test
    @DisplayName("TC01: Recupera tutti i professionisti per un dato salone")
    void testGetProfessionistiBySalone() {
        // Mock dei professionisti associati a una sede
        List<Professionista> professionistiMock = Arrays.asList(
                new Professionista(1, "Mario Rossi", 1, Arrays.asList(
                        new FasciaOraria(1, 1, LocalDate.of(2025, 2, 11), "09:00-10:00", true),
                        new FasciaOraria(2, 1, LocalDate.of(2025, 2, 11), "10:00-11:00", true)
                )),
                new Professionista(2, "Giulia Bianchi", 1, Arrays.asList(
                        new FasciaOraria(3, 2, LocalDate.of(2025, 2, 11), "09:00-10:00", false),
                        new FasciaOraria(4, 2, LocalDate.of(2025, 2, 12), "10:00-11:00", true)
                ))
        );

        // Configura il mock per professionistaDAO
        when(professionistaDAOMock.getProfessionistiBySede(1)).thenReturn(professionistiMock);

        // Chiamata al servizio
        List<Professionista> professionisti = saloneService.getProfessionistiBySalone(1);

        // Debug: Stampa dei professionisti per il salone con ID 1
        System.out.println("Professionisti per il salone con ID 1:");
        professionisti.forEach(professionista -> {
            System.out.println("Nome: " + professionista.getNome() + ", ID: " + professionista.getId());
            professionista.getFasceOrarie().forEach(fascia -> {
                System.out.println("   Fascia oraria: " + fascia.getFascia() + ", Giorno: " + fascia.getGiorno() + ", Disponibile: " + fascia.isDisponibile());
            });
        });

        // Verifica che i professionisti siano correttamente restituiti
        assertNotNull(professionisti);
        assertEquals(2, professionisti.size());
        assertEquals("Mario Rossi", professionisti.get(0).getNome());
        assertEquals("Giulia Bianchi", professionisti.get(1).getNome());

        // Verifica che il mock sia stato chiamato correttamente
        verify(professionistaDAOMock, times(1)).getProfessionistiBySede(1);
    }

    @Test
    @DisplayName("TC02: Recupera fasce orarie per professionista")
    void testGetFasceOrarieByProfessionista() throws SQLException {
        // Mock delle fasce orarie per un professionista con id 1
        List<FasciaOraria> fasceOrarieMock = Arrays.asList(
                new FasciaOraria(1, 1, LocalDate.of(2025, 2, 11), "09:00-10:00", true),
                new FasciaOraria(2, 1, LocalDate.of(2025, 2, 11), "10:00-11:00", true),
                new FasciaOraria(3, 1, LocalDate.of(2025, 2, 12), "09:00-10:00", false)
        );

        // Configura il mock per fasciaOrariaDAO
        when(fasciaOrariaDAOMock.getFasceOrarieByProfessionista(1)).thenReturn(fasceOrarieMock);

        // Chiamata al servizio
        Map<LocalDate, List<String>> fasceOrarieByDay = saloneService.getFasceOrarieByProfessionista(1);

        // Debug: Stampa delle fasce orarie per il professionista con ID 1
        System.out.println("Fasce orarie per il professionista con ID 1:");
        fasceOrarieByDay.forEach((giorno, fasce) -> {
            System.out.println("Giorno: " + giorno);
            fasce.forEach(fascia -> System.out.println("Fascia oraria: " + fascia));
        });

        // Verifica che le fasce orarie siano correttamente restituite
        assertNotNull(fasceOrarieByDay);
        assertEquals(1, fasceOrarieByDay.size());  // Solo un giorno con fasce orarie disponibili
        assertTrue(fasceOrarieByDay.containsKey(LocalDate.of(2025, 2, 11)));
        assertEquals(2, fasceOrarieByDay.get(LocalDate.of(2025, 2, 11)).size());
        assertTrue(fasceOrarieByDay.get(LocalDate.of(2025, 2, 11)).contains("09:00-10:00"));
        assertTrue(fasceOrarieByDay.get(LocalDate.of(2025, 2, 11)).contains("10:00-11:00"));

        // Verifica che il mock sia stato chiamato correttamente
        verify(fasciaOrariaDAOMock, times(1)).getFasceOrarieByProfessionista(1);
    }
}
