package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.UtenteGestoreCatena;
import it.unisa.application.sottosistemi.utilities.PasswordUtils; // Importa PasswordUtils
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtenteGestoreCatenaDAO {
    private DataSource ds;

    public UtenteGestoreCatenaDAO() {
        this.ds = DataSourceSingleton.getInstance();
    }

    // Get a connection from the DataSource
    private Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    // Insert a new utente gestore catena (with hashed password)
    public void insert(UtenteGestoreCatena utenteGestoreCatena) {
        String hashedPassword = PasswordUtils.hashPassword(utenteGestoreCatena.getPassword()); // Hash della password
        String sql = "INSERT INTO UtenteGestoreCatena (username, password) VALUES (?, ?)";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, utenteGestoreCatena.getUsername());
            preparedStatement.setString(2, hashedPassword); // Salva la password hashata
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Retrieve an utente gestore catena by username
    public UtenteGestoreCatena getByUsername(String username, String password) {
        String sql = "SELECT * FROM UtenteGestoreCatena WHERE username = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);  // Usa il 'username' come parametro
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String storedHashedPassword = resultSet.getString("password");

                // Confronta la password fornita con quella salvata (hashata)
                if (storedHashedPassword.equals(PasswordUtils.hashPassword(password))) {
                    // Se la password è corretta, restituisci l'oggetto UtenteGestoreCatena
                    return new UtenteGestoreCatena(resultSet.getString("password"), username);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Nessun utente trovato o password non corretta
    }

    // Update an utente gestore catena's information (with hashed password)
    public void update(UtenteGestoreCatena utenteGestoreCatena) {
        String hashedPassword = PasswordUtils.hashPassword(utenteGestoreCatena.getPassword()); // Hash della password
        String sql = "UPDATE UtenteGestoreCatena SET password = ? WHERE username = ?";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, hashedPassword); // Usa la password hashata
            preparedStatement.setString(2, utenteGestoreCatena.getUsername());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete an utente gestore catena by username
    public void delete(String username) {
        String sql = "DELETE FROM UtenteGestoreCatena WHERE username = ?";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
