package it.unisa.application.model.dao;

import it.unisa.application.model.entity.UtenteGestoreCatena;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtenteGestoreCatenaDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/HairBeautyNow";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Inserimento di un nuovo utente gestore catena
    public void insert(UtenteGestoreCatena utenteGestoreCatena) {
        String sql = "INSERT INTO UtenteGestoreCatena (username, password, n_SediGestite, sedeID, usernameUGS) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, utenteGestoreCatena.getUsername());
            preparedStatement.setString(2, utenteGestoreCatena.getPassword());
            preparedStatement.setInt(3, utenteGestoreCatena.getN_SediGestite());
            preparedStatement.setInt(4, utenteGestoreCatena.getSedeID());
            preparedStatement.setString(5, utenteGestoreCatena.getUsernameUGS());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Recupero di un utente gestore catena tramite username
    public UtenteGestoreCatena getByUsername(String username) {
        String sql = "SELECT * FROM UtenteGestoreCatena WHERE username = ?";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String password = resultSet.getString("password");
                int n_SediGestite = resultSet.getInt("n_SediGestite");
                int sedeID = resultSet.getInt("sedeID");
                String usernameUGS = resultSet.getString("usernameUGS");

                return new UtenteGestoreCatena(password, n_SediGestite, username, sedeID, usernameUGS);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Return null if no user found
    }

    // Recupero di tutti gli utenti gestori catena
    public List<UtenteGestoreCatena> getAll() {
        List<UtenteGestoreCatena> utenti = new ArrayList<>();
        String sql = "SELECT * FROM UtenteGestoreCatena";
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                int n_SediGestite = resultSet.getInt("n_SediGestite");
                int sedeID = resultSet.getInt("sedeID");
                String usernameUGS = resultSet.getString("usernameUGS");

                utenti.add(new UtenteGestoreCatena(password, n_SediGestite, username, sedeID, usernameUGS));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utenti;
    }

    // Aggiornamento delle informazioni di un utente gestore catena
    public void update(UtenteGestoreCatena utenteGestoreCatena) {
        String sql = "UPDATE UtenteGestoreCatena SET password = ?, n_SediGestite = ?, sedeID = ?, usernameUGS = ? WHERE username = ?";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, utenteGestoreCatena.getPassword());
            preparedStatement.setInt(2, utenteGestoreCatena.getN_SediGestite());
            preparedStatement.setInt(3, utenteGestoreCatena.getSedeID());
            preparedStatement.setString(4, utenteGestoreCatena.getUsernameUGS());
            preparedStatement.setString(5, utenteGestoreCatena.getUsername());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Eliminazione di un utente gestore catena
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
