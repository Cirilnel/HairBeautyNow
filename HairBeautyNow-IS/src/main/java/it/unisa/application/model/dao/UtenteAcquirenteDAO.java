package it.unisa.application.model.dao;

import it.unisa.application.model.entity.UtenteAcquirente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtenteAcquirenteDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/HairBeautyNow";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Inserimento di un nuovo utente acquirente
    public boolean insert(UtenteAcquirente utente) {
        String sql = "INSERT INTO UtentiAcquirente (username, email, password, nome, cognome, citta, prenotazioneID, nCarta) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, utente.getUsername());
            preparedStatement.setString(2, utente.getEmail());
            preparedStatement.setString(3, utente.getPassword());
            preparedStatement.setString(4, utente.getNome());
            preparedStatement.setString(5, utente.getCognome());
            preparedStatement.setString(6, utente.getCitta());
            preparedStatement.setInt(7, utente.getPrenotazioneID());  // Puoi lasciare prenotazioneID a 0 se non lo conosci
            preparedStatement.setString(8, utente.getNCarta());      // Se nCarta è null, inserisci null

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Recupero di un utente acquirente tramite username
    public UtenteAcquirente getByUsername(String username) {
        String sql = "SELECT * FROM UtentiAcquirenti WHERE username = ?";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String nome = resultSet.getString("nome");
                String cognome = resultSet.getString("cognome");
                String citta = resultSet.getString("citta");
                int prenotazioneID = resultSet.getInt("prenotazioneID");
                String nCarta = resultSet.getString("nCarta");

                return new UtenteAcquirente(username, email, password, nome, cognome, citta, prenotazioneID, nCarta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Return null if no user found
    }

    // Recupero di tutti gli utenti acquirenti
    public List<UtenteAcquirente> getAll() {
        List<UtenteAcquirente> utenti = new ArrayList<>();
        String sql = "SELECT * FROM UtentiAcquirenti";
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String nome = resultSet.getString("nome");
                String cognome = resultSet.getString("cognome");
                String citta = resultSet.getString("citta");
                int prenotazioneID = resultSet.getInt("prenotazioneID");
                String nCarta = resultSet.getString("nCarta");

                utenti.add(new UtenteAcquirente(username, email, password, nome, cognome, citta, prenotazioneID, nCarta));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utenti;
    }

    // Aggiornamento delle informazioni di un utente acquirente
    public void update(UtenteAcquirente utenteAcquirente) {
        String sql = "UPDATE UtentiAcquirenti SET email = ?, password = ?, nome = ?, cognome = ?, citta = ?, prenotazioneID = ?, nCarta = ? WHERE username = ?";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, utenteAcquirente.getEmail());
            preparedStatement.setString(2, utenteAcquirente.getPassword());
            preparedStatement.setString(3, utenteAcquirente.getNome());
            preparedStatement.setString(4, utenteAcquirente.getCognome());
            preparedStatement.setString(5, utenteAcquirente.getCitta());
            preparedStatement.setInt(6, utenteAcquirente.getPrenotazioneID());
            preparedStatement.setString(7, utenteAcquirente.getNCarta());
            preparedStatement.setString(8, utenteAcquirente.getUsername());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Eliminazione di un utente acquirente
    public void delete(String username) {
        String sql = "DELETE FROM UtentiAcquirenti WHERE username = ?";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
