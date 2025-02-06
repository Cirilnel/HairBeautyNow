package it.unisa.application.model.dao;

import it.unisa.application.model.entity.UtenteGestoreSede;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtenteGestoreSedeDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/HairBeautyNow";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Insert a new user (UtenteGestoreSede)
    public void insert(UtenteGestoreSede utenteGestoreSede) {
        String sql = "INSERT INTO UtenteGestoreSede (usernameUGS, password, sedeID) VALUES (?, ?, ?)";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, utenteGestoreSede.getUsernameUGS());
            preparedStatement.setString(2, utenteGestoreSede.getPassword());
            preparedStatement.setInt(3, utenteGestoreSede.getSedeID());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Retrieve a user by username
    public UtenteGestoreSede getByUsername(String usernameUGS) {
        String sql = "SELECT * FROM UtenteGestoreSede WHERE usernameUGS = ?";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, usernameUGS);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String password = resultSet.getString("password");
                int sedeID = resultSet.getInt("sedeID");

                return new UtenteGestoreSede(usernameUGS, password, sedeID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Return null if no user found
    }

    // Retrieve all users
    public List<UtenteGestoreSede> getAll() {
        List<UtenteGestoreSede> utenti = new ArrayList<>();
        String sql = "SELECT * FROM UtenteGestoreSede";
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String usernameUGS = resultSet.getString("usernameUGS");
                String password = resultSet.getString("password");
                int sedeID = resultSet.getInt("sedeID");

                utenti.add(new UtenteGestoreSede(usernameUGS, password, sedeID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utenti;
    }

    // Update a user's information
    public void update(UtenteGestoreSede utenteGestoreSede) {
        String sql = "UPDATE UtenteGestoreSede SET password = ?, sedeID = ? WHERE usernameUGS = ?";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, utenteGestoreSede.getPassword());
            preparedStatement.setInt(2, utenteGestoreSede.getSedeID());
            preparedStatement.setString(3, utenteGestoreSede.getUsernameUGS());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a user by username
    public void delete(String usernameUGS) {
        String sql = "DELETE FROM UtenteGestoreSede WHERE usernameUGS = ?";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, usernameUGS);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Recupera tutti i gestori che non hanno una sede assegnata (sedeID NULL o 0)
    public List<UtenteGestoreSede> getGestoriSenzaSede() {
        List<UtenteGestoreSede> gestori = new ArrayList<>();
        String sql = "SELECT * FROM UtenteGestoreSede WHERE sedeID IS NULL OR sedeID = 0";
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String usernameUGS = resultSet.getString("usernameUGS");
                String password = resultSet.getString("password");
                gestori.add(new UtenteGestoreSede(usernameUGS, password, 0)); // sedeID 0 perché non assegnato
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gestori;
    }

    // Assegna una sede a un gestore
    public void assegnaSede(String usernameUGS, int sedeID) {
        String sql = "UPDATE UtenteGestoreSede SET sedeID = ? WHERE usernameUGS = ?";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, sedeID);
            preparedStatement.setString(2, usernameUGS);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
