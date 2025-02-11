package unit.GestioneCatena;

import it.unisa.application.model.entity.Sede;
import it.unisa.application.sottosistemi.GestioneCatena.service.GestioneSedeService;
import org.junit.jupiter.api.*;
import unit.DAO.DatabaseSetupForTest;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test per GestioneSedeService (DAO reale, senza modifiche al codice del Service)")
public class GestioneSedeServiceTest {

    private GestioneSedeService gestioneSedeService;

    @BeforeAll
    static void globalSetup() {
        // Configura il DB in-memory H2
        DatabaseSetupForTest.configureH2DataSource();
        System.out.println("Datasource H2 configurato per i test.");
    }

    @BeforeEach
    void setUp() {
        // Inizializza il service (usa internamente un SedeDAO reale)
        gestioneSedeService = new GestioneSedeService();

        // Pulisce la tabella "sede" prima di ogni test

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

        // Chiamata al servizio per l'inserimento
        int idGenerato = gestioneSedeService.creaSede(nuovaSede);

        System.out.println("ID restituito dopo l'inserimento: " + idGenerato);

        // Dovrebbe essere > 0 (perché la tabella ha colonna auto-increment)
        assertTrue(idGenerato > 0, "L'inserimento dovrebbe restituire un ID positivo.");

    }


}