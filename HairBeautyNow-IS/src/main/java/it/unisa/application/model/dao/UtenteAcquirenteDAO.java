package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.UtenteAcquirente;
import javax.sql.DataSource;
import java.sql.*;

public class UtenteAcquirenteDAO {
    private DataSource ds;

    public UtenteAcquirenteDAO() {
        this.ds = DataSourceSingleton.getInstance();
    }

    // Inserimento di un nuovo utente acquirente
    public boolean insert(UtenteAcquirente utente) {
        String sql = "INSERT INTO UtenteAcquirente (username, email, password, nome, cognome, citta) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Imposta i valori per i parametri
            preparedStatement.setString(1, utente.getUsername());
            preparedStatement.setString(2, utente.getEmail());
            preparedStatement.setString(3, utente.getPassword());
            preparedStatement.setString(4, utente.getNome());
            preparedStatement.setString(5, utente.getCognome());
            preparedStatement.setString(6, utente.getCitta());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Metodo per recuperare un utente tramite email e password
    public UtenteAcquirente getByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM UtenteAcquirente WHERE email = ? AND password = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String nome = resultSet.getString("nome");
                String cognome = resultSet.getString("cognome");
                String citta = resultSet.getString("citta");

                return new UtenteAcquirente(username, email, password, nome, cognome, citta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Nessun utente trovato con queste credenziali
    }
}
