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

    // Inserimento di un nuovo utente gestore sede
    public void insert(UtenteGestoreSede utenteGestoreSede) {
        String sql = "INSERT INTO UtentiGestoriSede (usernameUGS, password, prenotazioneID, professionsitaID) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, utenteGestoreSede.getUsernameUGS());
            preparedStatement.setString(2, utenteGestoreSede.getPassword());
            preparedStatement.setInt(3, utenteGestoreSede.getPrenotazioneID());
            preparedStatement.setInt(4, utenteGestoreSede.getProfessionsitaID());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Recupero di un utente gestore sede tramite username
    public UtenteGestoreSede getByUsername(String usernameUGS) {
        String sql = "SELECT * FROM UtentiGestoriSede WHERE usernameUGS = ?";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, usernameUGS);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String password = resultSet.getString("password");
                int prenotazioneID = resultSet.getInt("prenotazioneID");
                int professionsitaID = resultSet.getInt("professionsitaID");

                return new UtenteGestoreSede(usernameUGS, password, professionsitaID, prenotazioneID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Return null if no user found
    }

    // Recupero di tutti gli utenti gestori sede
    public List<UtenteGestoreSede> getAll() {
        List<UtenteGestoreSede> utenti = new ArrayList<>();
        String sql = "SELECT * FROM UtentiGestoriSede";
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String usernameUGS = resultSet.getString("usernameUGS");
                String password = resultSet.getString("password");
                int prenotazioneID = resultSet.getInt("prenotazioneID");
                int professionsitaID = resultSet.getInt("professionsitaID");

                utenti.add(new UtenteGestoreSede(usernameUGS, password, professionsitaID, prenotazioneID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utenti;
    }

    // Aggiornamento delle informazioni di un utente gestore sede
    public void update(UtenteGestoreSede utenteGestoreSede) {
        String sql = "UPDATE UtentiGestoriSede SET password = ?, prenotazioneID = ?, professionsitaID = ? WHERE usernameUGS = ?";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, utenteGestoreSede.getPassword());
            preparedStatement.setInt(2, utenteGestoreSede.getPrenotazioneID());
            preparedStatement.setInt(3, utenteGestoreSede.getProfessionsitaID());
            preparedStatement.setString(4, utenteGestoreSede.getUsernameUGS());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Eliminazione di un utente gestore sede
    public void delete(String usernameUGS) {
        String sql = "DELETE FROM UtentiGestoriSede WHERE usernameUGS = ?";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, usernameUGS);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
