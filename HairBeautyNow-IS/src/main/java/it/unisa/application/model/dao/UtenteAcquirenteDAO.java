package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.UtenteAcquirente;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtenteAcquirenteDAO {
    private DataSource ds;

    public UtenteAcquirenteDAO() {
        this.ds = DataSourceSingleton.getInstance();
    }

    // Inserimento di un nuovo utente acquirente
    public boolean insert(UtenteAcquirente utente) {
        String sql = "INSERT INTO UtenteAcquirente (username, email, password, nome, cognome, citta, prenotazioneID, nCarta) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        // Usa la connessione tramite DataSource
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Imposta i valori per i parametri
            preparedStatement.setString(1, utente.getUsername());
            preparedStatement.setString(2, utente.getEmail());
            preparedStatement.setString(3, utente.getPassword());
            preparedStatement.setString(4, utente.getNome());
            preparedStatement.setString(5, utente.getCognome());
            preparedStatement.setString(6, utente.getCitta());

            // Gestisci prenotazioneID che può essere null
            if (utente.getPrenotazioneID() == null) {
                preparedStatement.setNull(7, Types.INTEGER);  // Imposta null se prenotazioneID è null
            } else {
                preparedStatement.setInt(7, utente.getPrenotazioneID());  // Imposta il valore se prenotazioneID non è null
            }

            // Gestisci nCarta che può essere null
            if (utente.getNCarta() == null) {
                preparedStatement.setNull(8, Types.VARCHAR);  // Imposta null se nCarta è null
            } else {
                preparedStatement.setString(8, utente.getNCarta());  // Imposta il valore se nCarta non è null
            }

            // Esegui l'inserimento e controlla se è andato a buon fine
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Altri metodi come `getByUsername`, `getAll`, etc., sono simili, basta cambiare `DriverManager.getConnection` in `ds.getConnection`
}
