package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.Sede;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SedeDAO {

    private static final Logger LOGGER = Logger.getLogger(SedeDAO.class.getName());
    private final DataSource ds;

    public SedeDAO() {
        this.ds = DataSourceSingleton.getInstance();
    }

    // Metodo per ottenere una sede per ID
    public Sede getSedeById(int id) {
        String query = "SELECT * FROM sede WHERE id = ?";
        Sede sede = null;

        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String indirizzo = resultSet.getString("indirizzo");
                String nome = resultSet.getString("nome");
                String città = resultSet.getString("città");

                sede = new Sede(indirizzo, nome, città, id);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while fetching sede by ID", e);
        }
        return sede;
    }

    // Metodo per ottenere tutte le sedi
    public List<Sede> getAllSedi() {
        List<Sede> sedi = new ArrayList<>();
        String query = "SELECT * FROM sede";

        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String indirizzo = resultSet.getString("indirizzo");
                String nome = resultSet.getString("nome");
                String città = resultSet.getString("città");

                sedi.add(new Sede(indirizzo, nome, città, id));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while fetching all sedi", e);
        }
        return sedi;
    }

    // Metodo per ottenere una sede per ID (utile per l'utente loggato)
    public Sede findSedeById(int sedeId) {
        return getSedeById(sedeId);
    }

    public int insertSedeAndReturnID(Sede sede) {
        String query = "INSERT INTO sede (indirizzo, nome, città) VALUES (?, ?, ?)";
        int generatedID = -1;

        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, sede.getIndirizzo());
            statement.setString(2, sede.getNome());
            statement.setString(3, sede.getCitta());

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    generatedID = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while inserting sede", e);
        }
        return generatedID;
    }

    // Metodo per ottenere le sedi di una specifica città
    public List<Sede> getSediByCitta(String citta) {
        List<Sede> sedi = new ArrayList<>();
        String query = "SELECT * FROM sede WHERE città = ?";

        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, citta);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String indirizzo = resultSet.getString("indirizzo");
                String nome = resultSet.getString("nome");

                sedi.add(new Sede(indirizzo, nome, citta, id));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while fetching sedi by città", e);
        }
        return sedi;
    }
}
