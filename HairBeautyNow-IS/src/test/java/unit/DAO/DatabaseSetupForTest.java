package unit.DAO;

import it.unisa.application.database_connection.DataSourceSingleton;
import org.h2.jdbcx.JdbcDataSource;

import java.lang.reflect.Field;
import java.sql.Connection;

public class DatabaseSetupForTest {

    public static void configureH2DataSource() {
        try {
            // Configura il DataSource H2 in-memory
            JdbcDataSource ds = new JdbcDataSource();
            ds.setURL("jdbc:h2:mem:HairBeautyNow;DB_CLOSE_DELAY=-1"); // Nome DB in-memory
            ds.setUser("sa");
            ds.setPassword("");

            // Riflette sul DataSourceSingleton per sostituire l'istanza con quella di H2
            Field instanceField = DataSourceSingleton.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, ds);

            // Crea le tabelle
            try (Connection conn = ds.getConnection()) {
                String schemaCreationScript = """
                    CREATE TABLE IF NOT EXISTS UtenteAcquirente (
                        username VARCHAR(50) PRIMARY KEY,
                        email VARCHAR(100) NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        nome VARCHAR(100) NOT NULL,
                        cognome VARCHAR(100) NOT NULL,
                        citta VARCHAR(100) NOT NULL
                    );

                    CREATE TABLE IF NOT EXISTS MetodoDiPagamento (
                        nCarta VARCHAR(16),
                        dataScadenza DATE,
                        nomeIntestatario VARCHAR(100) NOT NULL,
                        cvv INT NOT NULL,
                        indirizzo VARCHAR(255) NOT NULL,
                        username VARCHAR(50) NOT NULL,
                        metodoPagamento VARCHAR(50),
                        email VARCHAR(100),
                        PRIMARY KEY (username, metodoPagamento),
                        FOREIGN KEY (username) REFERENCES UtenteAcquirente(username)
                    );

                    CREATE TABLE IF NOT EXISTS Sede (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        indirizzo VARCHAR(255) NOT NULL,
                        nome VARCHAR(100) NOT NULL,
                        città VARCHAR(100) NOT NULL
                    );

                    CREATE TABLE IF NOT EXISTS Professionista (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        nome VARCHAR(100) NOT NULL,
                        sedeId INT NOT NULL,
                        FOREIGN KEY (sedeId) REFERENCES Sede(id) ON DELETE CASCADE
                    );

                    CREATE TABLE IF NOT EXISTS fascia_oraria (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        professionista_id INT NOT NULL,
                        giorno DATE NOT NULL,
                        fascia VARCHAR(20) NOT NULL,
                        disponibile BOOLEAN NOT NULL,
                        FOREIGN KEY (professionista_id) REFERENCES Professionista(id)
                    );

                    CREATE TABLE IF NOT EXISTS Servizio (
                        nome VARCHAR(100) PRIMARY KEY,
                        prezzo DECIMAL(10, 2) NOT NULL,
                        tipo VARCHAR(50) NOT NULL,
                        descrizione TEXT
                    );

                    CREATE TABLE IF NOT EXISTS Prenotazione (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        servizioName VARCHAR(100) NOT NULL,
                        professionistaId INT NOT NULL,
                        prezzo DECIMAL(10, 2) NOT NULL,
                        data DATETIME NOT NULL,
                        username VARCHAR(50),
                        FOREIGN KEY (servizioName) REFERENCES Servizio(nome),
                        FOREIGN KEY (professionistaId) REFERENCES Professionista(id),
                        FOREIGN KEY (username) REFERENCES UtenteAcquirente(username) ON DELETE CASCADE
                    );

                    CREATE TABLE IF NOT EXISTS UtenteGestoreSede (
                        usernameUGS VARCHAR(50) PRIMARY KEY,
                        password VARCHAR(255) NOT NULL,
                        sedeID INT,
                        FOREIGN KEY (sedeID) REFERENCES Sede(id) ON DELETE CASCADE
                    );

                    CREATE TABLE IF NOT EXISTS UtenteGestoreCatena (
                        username VARCHAR(50) PRIMARY KEY,
                        password VARCHAR(255) NOT NULL
                    );
                """;

                conn.createStatement().execute(schemaCreationScript);
            }
        } catch (Exception e) {
            throw new RuntimeException("Errore nella configurazione del DataSource di test", e);
        }
    }
}
