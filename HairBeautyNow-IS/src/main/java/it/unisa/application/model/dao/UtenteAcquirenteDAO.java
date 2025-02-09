package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.UtenteAcquirente;
import it.unisa.application.sottosistemi.utilities.PasswordUtils;

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

        // Hash the password before saving it
        String hashedPassword = PasswordUtils.hashPassword(utente.getPassword());

        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Imposta i valori per i parametri
            preparedStatement.setString(1, utente.getUsername());
            preparedStatement.setString(2, utente.getEmail());
            preparedStatement.setString(3, hashedPassword); // Save the hashed password
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
        String sql = "SELECT * FROM UtenteAcquirente WHERE email = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String nome = resultSet.getString("nome");
                String cognome = resultSet.getString("cognome");
                String citta = resultSet.getString("citta");
                String storedHashedPassword = resultSet.getString("password");

                // Hash the provided password and compare it with the stored hash
                if (storedHashedPassword.equals(PasswordUtils.hashPassword(password))) {
                    return new UtenteAcquirente(username, email, password, nome, cognome, citta);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Nessun utente trovato con queste credenziali
    }

    // Metodo per recuperare un utente tramite username e password
    public UtenteAcquirente getByUsernameAndPassword(String username, String password) {
        String sql = "SELECT * FROM UtenteAcquirente WHERE username = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);  // Usa il 'username' come parametro
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String email = resultSet.getString("email");  // Recupera anche l'email
                String nome = resultSet.getString("nome");
                String cognome = resultSet.getString("cognome");
                String citta = resultSet.getString("citta");
                String storedHashedPassword = resultSet.getString("password");

                // Hash the provided password and compare it with the stored hash
                if (storedHashedPassword.equals(PasswordUtils.hashPassword(password))) {
                    return new UtenteAcquirente(username, email, password, nome, cognome, citta);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Nessun utente trovato con queste credenziali
    }
}
