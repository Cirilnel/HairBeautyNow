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
        // Query per inserire il professionista
        String queryProfessionista = "INSERT INTO professionista (nome, sedeId) VALUES (?, ?)";

        // Query per inserire la fascia oraria
        String queryFasceOrarie = "INSERT INTO fascia_oraria (professionista_id, giorno, fascia, disponibile) VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statementProfessionista = connection.prepareStatement(queryProfessionista, Statement.RETURN_GENERATED_KEYS)) {

            // Inserisce il professionista
            statementProfessionista.setString(1, professionista.getNome());
            statementProfessionista.setInt(2, professionista.getSedeId());
            statementProfessionista.executeUpdate();

            // Ottieni l'ID generato del nuovo professionista
            ResultSet generatedKeys = statementProfessionista.getGeneratedKeys();
            if (generatedKeys.next()) {
                int professionistaId = generatedKeys.getInt(1); // Ottieni l'ID del professionista appena inserito

                // Preparare la query per inserire le fasce orarie
                try (PreparedStatement statementFasceOrarie = connection.prepareStatement(queryFasceOrarie)) {

                    // Fasce orarie
                    String[] fasce = {
                            "08:00-08:30", "08:30-09:00", "09:00-09:30", "09:30-10:00", "10:00-10:30",
                            "10:30-11:00", "11:00-11:30", "11:30-12:00", "14:00-14:30", "14:30-15:00",
                            "15:00-15:30", "15:30-16:00", "16:00-16:30", "16:30-17:00", "17:00-17:30",
                            "17:30-18:00"
                    };

                    // Inserimento delle fasce orarie per ogni giorno dal 4 al 28 febbraio 2025
                    for (int day = 4; day <= 28; day++) {
                        // Costruisci la data per ogni giorno dal 4 al 28 febbraio
                        String data = String.format("2025-02-%02d", day);

                        // Inserimento delle fasce orarie per ogni data
                        for (String fascia : fasce) {
                            // Imposta i valori della query per ogni fascia oraria
                            statementFasceOrarie.setInt(1, professionistaId); // professionista_id
                            statementFasceOrarie.setString(2, data);         // giorno
                            statementFasceOrarie.setString(3, fascia);       // fascia
                            statementFasceOrarie.setBoolean(4, true);        // disponibile (true = libero)
                            statementFasceOrarie.addBatch();                 // Aggiungi la query al batch
                        }
                    }

                    // Esegui il batch per inserire tutte le fasce orarie
                    statementFasceOrarie.executeBatch();
                }
            }

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
