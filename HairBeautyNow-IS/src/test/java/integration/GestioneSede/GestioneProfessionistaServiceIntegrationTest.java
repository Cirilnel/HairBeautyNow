package integration.GestioneSede;

import it.unisa.application.model.dao.ProfessionistaDAO;
import it.unisa.application.model.dao.SedeDAO;
import it.unisa.application.model.entity.FasciaOraria;
import it.unisa.application.model.entity.Professionista;
import it.unisa.application.model.entity.Sede;
import it.unisa.application.sottosistemi.GestioneSede.service.GestioneProfessionistaService;
import org.junit.jupiter.api.*;
import org.mockito.*;
import unit.DAO.DatabaseSetupForTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class GestioneProfessionistaServiceIntegrationTest {

    @Mock
    private ProfessionistaDAO professionistaDAOMock;

    @Mock
    private SedeDAO sedeDAOMock;

    private GestioneProfessionistaService gestioneProfessionistaService;
    @BeforeAll
    static void setupDatabase() {
        // Configura il database, se necessario
        DatabaseSetupForTest.configureH2DataSource();
    }
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gestioneProfessionistaService = new GestioneProfessionistaService(professionistaDAOMock, sedeDAOMock);
    }


    @Test
    @DisplayName("TC01.1: Assunzione Professionista - Successo")
    void testAssumiProfessionistaSuccess() {
        Integer sedeId = 1;
        String nome = "Mario Rossi";
        List<FasciaOraria> fasceOrarie = List.of(
                new FasciaOraria(1, 0, LocalDate.of(2025, 2, 11), "09:00-12:00", true),
                new FasciaOraria(2, 0, LocalDate.of(2025, 2, 11), "14:00-18:00", true)
        );
        Professionista professionista = new Professionista(0, nome, sedeId, fasceOrarie);  // L'id sarà assegnato automaticamente dal DB

        // Configura il mock per l'inserimento del professionista
        doNothing().when(professionistaDAOMock).insertProfessionista(professionista);

        System.out.println("\nAssunzione Professionista - Successo");
        System.out.println("Dati di input:");
        System.out.println("Nome: " + nome);
        System.out.println("Sede ID: " + sedeId);
        System.out.println("Fasce orarie: " + fasceOrarie);

        // Chiamata al metodo del servizio
        gestioneProfessionistaService.assumiProfessionista(professionista);

        // Verifica che il professionista sia stato inserito
        verify(professionistaDAOMock, times(1)).insertProfessionista(professionista);
        System.out.println("Professionista assunto con successo.");
    }

    @Test
    @DisplayName("TC01.2: Rimozione Professionista - Successo")
    void testRimuoviProfessionistaSuccess() {
        Integer professionistaId = 1;
        boolean successo = true;

        // Configura il mock per la rimozione del professionista
        when(professionistaDAOMock.rimuoviProfessionista(professionistaId)).thenReturn(successo);

        System.out.println("\nRimozione Professionista - Successo");
        System.out.println("Professionista ID: " + professionistaId);

        // Chiamata al metodo del servizio
        String risultato = gestioneProfessionistaService.rimuoviProfessionista(professionistaId);

        // Verifica che la rimozione sia stata eseguita con successo
        assertEquals("successo", risultato, "La rimozione dovrebbe essere stata completata con successo.");
        System.out.println("Risultato ottenuto: " + risultato);
        verify(professionistaDAOMock, times(1)).rimuoviProfessionista(professionistaId);
    }

    @Test
    @DisplayName("TC01.3: Rimozione Professionista - Errore")
    void testRimuoviProfessionistaFailure() {
        Integer professionistaId = 1;
        boolean successo = false;

        // Configura il mock per la rimozione del professionista
        when(professionistaDAOMock.rimuoviProfessionista(professionistaId)).thenReturn(successo);

        System.out.println("\nRimozione Professionista - Errore");
        System.out.println("Professionista ID: " + professionistaId);

        // Chiamata al metodo del servizio
        String risultato = gestioneProfessionistaService.rimuoviProfessionista(professionistaId);

        // Verifica che venga restituito un errore
        assertEquals("Errore durante la rimozione del professionista o ci sono prenotazioni attive.", risultato, "Il risultato dovrebbe essere un errore.");
        System.out.println("Risultato ottenuto: " + risultato);
        verify(professionistaDAOMock, times(1)).rimuoviProfessionista(professionistaId);
    }

    @Test
    @DisplayName("TC02.1: Get Professionisti By Sede - Successo")
    void testGetProfessionistiBySedeSuccess() {
        Integer sedeId = 1;
        Professionista professionista1 = new Professionista(1, "Mario Rossi", sedeId, List.of(
                new FasciaOraria(1, 1, LocalDate.of(2025, 2, 11), "09:00-12:00", true)
        ));
        Professionista professionista2 = new Professionista(2, "Luca Bianchi", sedeId, List.of(
                new FasciaOraria(2, 2, LocalDate.of(2025, 2, 11), "14:00-18:00", true)
        ));
        List<Professionista> professionisti = List.of(professionista1, professionista2);

        // Configura il mock per ottenere i professionisti per sede
        when(professionistaDAOMock.getProfessionistiBySede(sedeId)).thenReturn(professionisti);

        System.out.println("\nGet Professionisti By Sede - Successo");
        System.out.println("Sede ID: " + sedeId);

        // Chiamata al metodo del servizio
        List<Professionista> risultato = gestioneProfessionistaService.getProfessionistiBySede(sedeId);

        // Verifica che i professionisti siano stati recuperati correttamente
        assertNotNull(risultato, "La lista di professionisti non dovrebbe essere null.");
        assertEquals(2, risultato.size(), "La lista dovrebbe contenere 2 professionisti.");
        System.out.println("Professionisti recuperati:");
        risultato.forEach(p -> System.out.println(p.getNome() + " - " + p.getFasceOrarie()));

        verify(professionistaDAOMock, times(1)).getProfessionistiBySede(sedeId);
    }

    @Test
    @DisplayName("TC02.2: Get Sede By ID - Successo")
    void testGetSedeByIdSuccess() {
        Integer sedeId = 1;
        Sede sede = new Sede("Via Roma 1", "Sede Centrale", "Roma", sedeId);

        // Configura il mock per ottenere la sede
        when(sedeDAOMock.getSedeById(sedeId)).thenReturn(sede);

        System.out.println("\nGet Sede By ID - Successo");
        System.out.println("Sede ID: " + sedeId);

        // Chiamata al metodo del servizio
        Sede risultato = gestioneProfessionistaService.getSedeById(sedeId);

        // Verifica che la sede sia stata recuperata correttamente
        assertNotNull(risultato, "La sede non dovrebbe essere null.");
        assertEquals(sedeId, risultato.getId(), "L'ID della sede dovrebbe corrispondere.");
        System.out.println("Sede recuperata: " + risultato.getNome() + " - " + risultato.getIndirizzo() + " - " + risultato.getCitta());

        verify(sedeDAOMock, times(1)).getSedeById(sedeId);
    }
}
