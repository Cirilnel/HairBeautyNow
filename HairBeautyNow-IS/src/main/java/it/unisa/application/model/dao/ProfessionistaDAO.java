package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.Professionista;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProfessionistaDAO {

    private static final Logger LOGGER = Logger.getLogger(ProfessionistaDAO.class.getName());
    private final DataSource ds;

    public ProfessionistaDAO() {
        this.ds = DataSourceSingleton.getInstance();
    }

    // Metodo per ottenere il nome del professionista
    public String getProfessionistaById(int id) {
        String query = "SELECT nome FROM professionista WHERE id = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("nome");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while fetching professionista by ID", e);
        }
        return null;
    }

    // Metodo per ottenere l'indirizzo del professionista tramite il suo sedeId
    public String getIndirizzoBySedeId(int sedeId) {
        String query = "SELECT indirizzo FROM sede WHERE id = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, sedeId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("indirizzo");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while fetching indirizzo by sedeId", e);
        }
        return null;
    }

    // Metodo per ottenere una lista di professionisti da una specifica sede
    public List<Professionista> getProfessionistiBySede(int sedeId) {
        List<Professionista> professionisti = new ArrayList<>();
        String query = "SELECT * FROM professionista WHERE sedeId = ?";

        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, sedeId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Professionista professionista = new Professionista();
                professionista.setId(resultSet.getInt("id"));
                professionista.setNome(resultSet.getString("nome"));
                professionista.setSedeId(resultSet.getInt("sedeId"));

                professionisti.add(professionista);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while fetching professionals by sedeId", e);
        }
        return professionisti;
    }

    public int getSedeIdByProfessionistaId(int professionistaId) {
        String query = "SELECT sedeId FROM professionista WHERE id = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, professionistaId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("sedeId");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while fetching sedeId by professionistaId", e);
        }
        return -1;
    }

    // Metodo per rimuovere le fasce orarie di un professionista
    public boolean rimuoviFasceOrarie(int professionistaId) {
        String deleteFasceOrarieQuery = "DELETE FROM fascia_oraria WHERE professionista_id = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement deleteFasceOrarieStatement = connection.prepareStatement(deleteFasceOrarieQuery)) {

            deleteFasceOrarieStatement.setInt(1, professionistaId);
            return deleteFasceOrarieStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while removing fasce orarie", e);
        }
        return false;
    }

    // Metodo per rimuovere un professionista
    public boolean rimuoviProfessionista(int professionistaId) {
        if (!hasPrenotazioni(professionistaId) && rimuoviFasceOrarie(professionistaId)) {
            String deleteProfessionistaQuery = "DELETE FROM professionista WHERE id = ?";
            try (Connection connection = ds.getConnection();
                 PreparedStatement deleteProfessionistaStatement = connection.prepareStatement(deleteProfessionistaQuery)) {

                deleteProfessionistaStatement.setInt(1, professionistaId);
                return deleteProfessionistaStatement.executeUpdate() > 0;

            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error while removing professionista", e);
            }
        }
        return false;
    }

    public void insertProfessionista(Professionista professionista) {
        String queryProfessionista = "INSERT INTO professionista (nome, sedeId) VALUES (?, ?)";
        String queryFasceOrarie = "INSERT INTO fascia_oraria (professionista_id, giorno, fascia, disponibile) VALUES (?, ?, ?, ?)";

        try (Connection connection = ds.getConnection();
             PreparedStatement statementProfessionista = connection.prepareStatement(queryProfessionista, Statement.RETURN_GENERATED_KEYS)) {

            statementProfessionista.setString(1, professionista.getNome());
            statementProfessionista.setInt(2, professionista.getSedeId());
            statementProfessionista.executeUpdate();

            ResultSet generatedKeys = statementProfessionista.getGeneratedKeys();
            if (generatedKeys.next()) {
                int professionistaId = generatedKeys.getInt(1);

                try (PreparedStatement statementFasceOrarie = connection.prepareStatement(queryFasceOrarie)) {
                    String[] fasce = {
                            "08:00-08:30", "08:30-09:00", "09:00-09:30", "09:30-10:00", "10:00-10:30",
                            "10:30-11:00", "11:00-11:30", "11:30-12:00", "14:00-14:30", "14:30-15:00",
                            "15:00-15:30", "15:30-16:00", "16:00-16:30", "16:30-17:00", "17:00-17:30",
                            "17:30-18:00"
                    };

                    // Ciclo per i giorni dal 18 febbraio al 18 marzo
                    for (int day = 18; day <= 31; day++) {  // Dal 18 febbraio al 28 febbraio
                        String data = String.format("2025-02-%02d", day);
                        for (String fascia : fasce) {
                            statementFasceOrarie.setInt(1, professionistaId);
                            statementFasceOrarie.setString(2, data);
                            statementFasceOrarie.setString(3, fascia);
                            statementFasceOrarie.setBoolean(4, true);
                            statementFasceOrarie.addBatch();
                        }
                    }

                    // Aggiungi marzo dal 1 al 18
                    for (int day = 1; day <= 18; day++) {  // Dal 1 marzo al 18 marzo
                        String data = String.format("2025-03-%02d", day);
                        for (String fascia : fasce) {
                            statementFasceOrarie.setInt(1, professionistaId);
                            statementFasceOrarie.setString(2, data);
                            statementFasceOrarie.setString(3, fascia);
                            statementFasceOrarie.setBoolean(4, true);
                            statementFasceOrarie.addBatch();
                        }
                    }

                    statementFasceOrarie.executeBatch();
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while inserting professionista", e);
        }
    }


    public double getPrezzoByServizio(String servizioName) {
        String query = "SELECT prezzo FROM Servizio WHERE nome = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, servizioName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble("prezzo");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while fetching price by servizio", e);
        }
        return 0.0;
    }

    public boolean hasPrenotazioni(int professionistaId) {
        String query = "SELECT COUNT(*) FROM Prenotazione WHERE professionistaId = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, professionistaId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while checking if professionista has prenotazioni", e);
        }
        return false;
    }
}