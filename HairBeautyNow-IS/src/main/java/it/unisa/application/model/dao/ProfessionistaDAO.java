package it.unisa.application.model.dao;

import it.unisa.application.model.entity.Professionista;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProfessionistaDAO {

    private static final Logger LOGGER = Logger.getLogger(ProfessionistaDAO.class.getName());

    // Parametri di connessione centralizzati
    private static final String URL = "jdbc:mysql://localhost:3306/HairBeautyNow";
    private static final String USER = "root";  // Modifica con le tue credenziali
    private static final String PASSWORD = "root";  // Modifica con le tue credenziali

    // Metodo per ottenere la connessione al database
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Metodo per ottenere il nome del professionista
    public String getProfessionistaById(int id) {
        String query = "SELECT nome, sedeId FROM professionista WHERE id = ?";
        String nome = null;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                nome = resultSet.getString("nome");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while fetching professionista by ID", e);
        }

        return nome;
    }

    // Metodo per ottenere l'indirizzo del professionista tramite il suo sedeId
    public String getIndirizzoBySedeId(int sedeId) {
        String query = "SELECT indirizzo FROM sede WHERE id = ?";
        String indirizzo = null;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, sedeId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                indirizzo = resultSet.getString("indirizzo");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while fetching indirizzo by sedeId", e);
        }

        return indirizzo;
    }

    // Metodo per ottenere una lista di professionisti da una specifica sede
    public List<Professionista> getProfessionistiBySede(int sedeId) {
        List<Professionista> professionisti = new ArrayList<>();
        String query = "SELECT * FROM professionista WHERE sedeId = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, sedeId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Professionista professionista = new Professionista();
                professionista.setId(resultSet.getInt("id"));
                professionista.setNome(resultSet.getString("nome"));
                professionista.setSedeId(resultSet.getInt("sedeId"));

                // Aggiungere altre informazioni se necessarie
                professionisti.add(professionista);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while fetching professionals by sedeId", e);
        }

        return professionisti;
    }

    public int getSedeIdByProfessionistaId(int professionistaId) {
        String query = "SELECT sedeId FROM professionista WHERE id = ?";
        int sedeId = -1;  // Un valore che rappresenta l'assenza di una sede

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, professionistaId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                sedeId = resultSet.getInt("sedeId");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while fetching sedeId by professionistaId", e);
        }

        return sedeId;
    }

    // Metodo per rimuovere un professionista
    public boolean rimuoviProfessionista(int professionistaId) {
        String deleteProfessionistaQuery = "DELETE FROM professionista WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement deleteProfessionistaStatement = connection.prepareStatement(deleteProfessionistaQuery)) {

            // Elimina il professionista
            deleteProfessionistaStatement.setInt(1, professionistaId);
            int rowsAffected = deleteProfessionistaStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while removing professionista", e);
        }

        return false; // Se qualcosa è andato storto
    }

    // Metodo per inserire un nuovo professionista
    public void insertProfessionista(Professionista professionista) {
        String query = "INSERT INTO professionista (nome, sedeId) VALUES (?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, professionista.getNome());
            statement.setInt(2, professionista.getSedeId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public double getPrezzoByServizio(String servizioName) {
        String query = "SELECT prezzo FROM Servizio WHERE nome = ?";
        double prezzo = 0.0;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, servizioName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                prezzo = resultSet.getDouble("prezzo");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while fetching price by servizio", e);
        }

        return prezzo;
    }
    public boolean hasPrenotazioni(int professionistaId) {
        String query = "SELECT COUNT(*) FROM Prenotazione WHERE professionistaId = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, professionistaId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Se ci sono prenotazioni
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while checking if professionista has prenotazioni", e);
        }
        return false; // Se non ci sono prenotazioni
    }
}
