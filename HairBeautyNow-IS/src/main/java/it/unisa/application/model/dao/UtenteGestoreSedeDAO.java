package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.UtenteGestoreSede;
import it.unisa.application.sottosistemi.utilities.PasswordUtils; // Importa PasswordUtils
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtenteGestoreSedeDAO {
    private DataSource ds;

    public UtenteGestoreSedeDAO() {
        this.ds = DataSourceSingleton.getInstance();
    }

    // Get a connection from the DataSource
    private Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    // Controlla se lo username esiste già
    public boolean existsByUsername(String usernameUGS) {
        String sql = "SELECT COUNT(*) FROM UtenteGestoreSede WHERE usernameUGS = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, usernameUGS);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Se il conteggio è > 0, lo username esiste già
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Inserisce un nuovo utente gestore sede (con password hashata)
    public boolean insert(UtenteGestoreSede utenteGestoreSede) {
        if (existsByUsername(utenteGestoreSede.getUsernameUGS())) {
            System.err.println("Errore: username già esistente nel database.");
            return false;
        }

        String hashedPassword = PasswordUtils.hashPassword(utenteGestoreSede.getPassword()); // Hash della password

        String sql = "INSERT INTO UtenteGestoreSede (usernameUGS, password, sedeID) VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, utenteGestoreSede.getUsernameUGS());
            preparedStatement.setString(2, hashedPassword); // Salva la password hashata

            // Se sedeID è NULL, setta il parametro come NULL nel PreparedStatement
            if (utenteGestoreSede.getSedeID() == null || utenteGestoreSede.getSedeID() == 0) {
                preparedStatement.setNull(3, Types.INTEGER);
            } else {
                preparedStatement.setInt(3, utenteGestoreSede.getSedeID());
            }

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Ottiene un utente dal suo username
    // Metodo per recuperare un utente tramite username e password
    public UtenteGestoreSede getByUsername(String usernameUGS, String password) {
        String sql = "SELECT * FROM UtenteGestoreSede WHERE usernameUGS = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, usernameUGS);  // Usa il 'username' come parametro
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String storedHashedPassword = resultSet.getString("password");
                int sedeID = resultSet.getInt("sedeID");

                // Hash la password fornita e confronta con l'hash salvato
                if (storedHashedPassword.equals(PasswordUtils.hashPassword(password))) {
                    return new UtenteGestoreSede(usernameUGS, password, sedeID);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Nessun utente trovato con queste credenziali
    }


    // Ottiene tutti gli utenti
    public List<UtenteGestoreSede> getAll() {
        List<UtenteGestoreSede> utenti = new ArrayList<>();
        String sql = "SELECT * FROM UtenteGestoreSede";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

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

    // Recupera tutti i gestori che non hanno una sede assegnata
    public List<UtenteGestoreSede> getGestoriSenzaSede() {
        List<UtenteGestoreSede> gestori = new ArrayList<>();
        String sql = "SELECT * FROM UtenteGestoreSede WHERE sedeID IS NULL OR sedeID = 0";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String usernameUGS = resultSet.getString("usernameUGS");
                String password = resultSet.getString("password");
                gestori.add(new UtenteGestoreSede(usernameUGS, password, 0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gestori;
    }

    // Recupera tutti i gestori che hanno una sede assegnata (sedeID != null o 0)
    public List<UtenteGestoreSede> getGestoriConSede() {
        List<UtenteGestoreSede> gestori = new ArrayList<>();
        String sql = "SELECT * FROM UtenteGestoreSede WHERE sedeID IS NOT NULL AND sedeID != 0";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String usernameUGS = resultSet.getString("usernameUGS");
                String password = resultSet.getString("password");
                int sedeID = resultSet.getInt("sedeID");
                gestori.add(new UtenteGestoreSede(usernameUGS, password, sedeID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gestori;
    }

    // Assegna una sede a un gestore
    public void assegnaSede(String usernameUGS, int sedeID) {
        String sql = "UPDATE UtenteGestoreSede SET sedeID = ? WHERE usernameUGS = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, sedeID);
            preparedStatement.setString(2, usernameUGS);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Licenzia un gestore (rimuove la sede assegnata senza eliminarlo dal DB)
    public void licenziaGestore(String usernameUGS) {
        String sql = "UPDATE UtenteGestoreSede SET sedeID = NULL WHERE usernameUGS = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, usernameUGS);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
