package unit.GestionePrenotazioni;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import it.unisa.application.model.dao.FasciaOrariaDAO;
import it.unisa.application.model.dao.PrenotazioneDAO;
import it.unisa.application.model.dao.ProfessionistaDAO;
import it.unisa.application.model.dao.SedeDAO;
import it.unisa.application.model.entity.*;
import it.unisa.application.sottosistemi.GestionePrenotazioni.service.PrenotazioneService;
import org.junit.jupiter.api.*;
import unit.DAO.DatabaseSetupForTest;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@DisplayName("Test per il servizio PrenotazioneService")
public class PrenotazioneServiceTest {

    private PrenotazioneService prenotazioneService;
    private PrenotazioneDAO prenotazioneDAOMock;
    private ProfessionistaDAO professionistaDAOMock;
    private FasciaOrariaDAO fasciaOrariaDAOMock;
    private SedeDAO sedeDAOMock;

    @BeforeAll
    static void globalSetup() {
        DatabaseSetupForTest.configureH2DataSource();
        System.out.println("Datasource H2 configurato per i test.");
    }

    @BeforeEach
    void setUp() {
        // Mock dei DAO
        prenotazioneDAOMock = mock(PrenotazioneDAO.class);
        professionistaDAOMock = mock(ProfessionistaDAO.class);
        fasciaOrariaDAOMock = mock(FasciaOrariaDAO.class);
        sedeDAOMock = mock(SedeDAO.class);

        // Inizializzazione del servizio con i mock dei DAO
        prenotazioneService = new PrenotazioneService(prenotazioneDAOMock, professionistaDAOMock, fasciaOrariaDAOMock, sedeDAOMock);
    }


    @Test
    @DisplayName("Recupero sedi per una città - Successo")
    void testGetSediByCittaSuccess() {
        System.out.println("Test: Recupero sedi per una città - Successo");

        String cittaSelezionata = "Napoli";

        // Creazione di una lista di sedi mockate con il costruttore completo
        Sede sede1 = new Sede("Via Roma 1", "Sede Napoli 1", "Napoli", 1);
        Sede sede2 = new Sede("Via Napoli 2", "Sede Napoli 2", "Napoli", 2);
        List<Sede> sediMock = Arrays.asList(sede1, sede2);

        // Simula il comportamento del DAO per il metodo getSediByCitta
        when(sedeDAOMock.getSediByCitta(cittaSelezionata)).thenReturn(sediMock);

        // Chiamata al servizio
        List<Sede> result = prenotazioneService.getSediByCitta(cittaSelezionata);

        // Verifica il risultato
        assertNotNull(result, "La lista delle sedi non dovrebbe essere nulla.");
        assertEquals(2, result.size(), "La lista delle sedi dovrebbe contenere 2 elementi.");
        assertTrue(result.contains(sede1), "La lista delle sedi dovrebbe contenere 'Sede Napoli 1'.");
        assertTrue(result.contains(sede2), "La lista delle sedi dovrebbe contenere 'Sede Napoli 2'.");
        System.out.println("Risultato del test 'Recupero sedi per una città': " + result);
    }

    @Test
    @DisplayName("Recupero prenotazioni attive per una sede - Successo")
    void testGetPrenotazioniAttiveSuccess() throws SQLException {
        System.out.println("Test: Recupero prenotazioni attive per una sede - Successo");

        int sedeId = 1;

        // Mock: simulazione dei professionisti nella sede
        Professionista professionista1 = new Professionista(1, "Mario Rossi", sedeId, null);
        Professionista professionista2 = new Professionista(2, "Giovanni Bianchi", sedeId, null);
        List<Professionista> professionistiMock = Arrays.asList(professionista1, professionista2);

        // Mock: creazione delle prenotazioni
        Prenotazione prenotazione1 = new Prenotazione(1, "Servizio 1", 1, LocalDateTime.now(), "utente1", 50.0);
        Prenotazione prenotazione2 = new Prenotazione(2, "Servizio 2", 2, LocalDateTime.now(), "utente2", 70.0);
        List<Prenotazione> prenotazioniMock = Arrays.asList(prenotazione1, prenotazione2);

        // Simula la chiamata del DAO per ottenere i professionisti
        when(professionistaDAOMock.getProfessionistiBySede(sedeId)).thenReturn(professionistiMock);

        // Simula la chiamata del DAO per ottenere le prenotazioni
        when(prenotazioneDAOMock.getPrenotazioniByProfessionisti(professionistiMock)).thenReturn(prenotazioniMock);

        // Chiamata al servizio
        List<Prenotazione> result = prenotazioneService.getPrenotazioniAttive(sedeId);

        // Verifica che la lista contenga le due prenotazioni attive
        assertNotNull(result, "La lista delle prenotazioni non dovrebbe essere nulla.");
        assertEquals(2, result.size(), "La lista delle prenotazioni dovrebbe contenere 2 elementi.");
        assertTrue(result.contains(prenotazione1), "La lista delle prenotazioni dovrebbe contenere 'Prenotazione 1'.");
        assertTrue(result.contains(prenotazione2), "La lista delle prenotazioni dovrebbe contenere 'Prenotazione 2'.");
        System.out.println("Risultato del test 'Recupero prenotazioni attive per una sede': " + result);
    }

    @Test
    @DisplayName("Aggiunta prenotazione - Successo")
    void testAddPrenotazioneSuccess() throws SQLException {
        System.out.println("Test: Aggiunta prenotazione - Successo");

        // Creazione della prenotazione da aggiungere
        Prenotazione prenotazione = new Prenotazione("Servizio 1", 1, LocalDateTime.now(), "utente1", 50.0);

        // Mock del comportamento del DAO per l'inserimento della prenotazione
        // Non è necessario restituire nulla, quindi rimuoviamo 'thenReturn'
        doNothing().when(prenotazioneDAOMock).addPrenotazione(any(Prenotazione.class));  // Utilizzo di doNothing() per metodi void

        // Chiamata al servizio
        prenotazioneService.addPrenotazione(prenotazione);

        // Verifica che il metodo del DAO sia stato chiamato
        verify(prenotazioneDAOMock, times(1)).addPrenotazione(prenotazione);
        System.out.println("Risultato del test 'Aggiunta prenotazione': " + prenotazione);
    }



    @Test
    @DisplayName("Rimozione prenotazione - Successo")
    void testRimuoviPrenotazioneSuccess() throws SQLException {
        System.out.println("Test: Rimozione prenotazione - Successo");

        int prenotazioneId = 1;
        Prenotazione prenotazioneMock = new Prenotazione(prenotazioneId, "Servizio 1", 1, LocalDateTime.now(), "utente1", 50.0);

        // Mock del comportamento del DAO per ottenere la prenotazione
        when(prenotazioneDAOMock.getPrenotazioneById(prenotazioneId)).thenReturn(prenotazioneMock);
        when(prenotazioneDAOMock.rimuoviPrenotazione(prenotazioneId)).thenReturn(true);

        // Simula il comportamento del DAO per aggiornare la fascia oraria
        FasciaOraria fasciaOrariaMock = new FasciaOraria(1, 1, LocalDate.of(2025, 2, 11), "09:00", true);
        when(fasciaOrariaDAOMock.getFasciaByProfessionistaAndGiorno(1, LocalDate.of(2025, 2, 11), "09:00")).thenReturn(fasciaOrariaMock);

        // Chiamata al servizio
        String result = prenotazioneService.rimuoviPrenotazione(prenotazioneId);

        // Verifica che il messaggio di successo sia corretto
        assertEquals("Prenotazione rimossa con successo!", result, "Il messaggio di successo non è corretto.");
        System.out.println("Risultato del test 'Rimozione prenotazione': " + result);
    }
}
