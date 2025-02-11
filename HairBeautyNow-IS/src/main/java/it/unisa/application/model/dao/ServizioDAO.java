package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.Servizio;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServizioDAO {
    private DataSource ds;

    public ServizioDAO() {
        this.ds = DataSourceSingleton.getInstance();
    }

    // Get a connection from the DataSource
    private Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    // Insert a new servizio
    public void insert(Servizio servizio) {
        String sql = "INSERT INTO Servizio (nome, prezzo, tipo, descrizione) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, servizio.getNome());
            preparedStatement.setDouble(2, servizio.getPrezzo());
            preparedStatement.setString(3, servizio.getTipo());
            preparedStatement.setString(4, servizio.getDescrizione());  // Aggiungi la descrizione

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all servizi
    public List<Servizio> getAll() {
        List<Servizio> servizi = new ArrayList<>();
        String sql = "SELECT * FROM Servizio";
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String nome = resultSet.getString("nome");
                String descrizione = resultSet.getString("descrizione");
                String tipo = resultSet.getString("tipo");
                double prezzo = resultSet.getDouble("prezzo");

                servizi.add(new Servizio(nome, prezzo, tipo, descrizione));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return servizi;
    }

    // Get a specific servizio by nome
    public Servizio getByNome(String nome) {
        String sql = "SELECT * FROM Servizio WHERE nome = ?";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, nome);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String nomeResult = resultSet.getString("nome");
                String tipo = resultSet.getString("tipo");
                double prezzo = resultSet.getDouble("prezzo");
                String descrizione = resultSet.getString("descrizione");

                return new Servizio(nomeResult, prezzo, tipo, descrizione);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if the service was not found
    }
    public List<Servizio> getAllWithDescription() {
        List<Servizio> servizi = new ArrayList<>();
        String sql = "SELECT nome, prezzo, tipo, descrizione FROM Servizio"; // Query aggiornata per ottenere anche la descrizione
        try (Connection connection = getConnection(); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String nome = rs.getString("nome");
                double prezzo = rs.getDouble("prezzo");
                String tipo = rs.getString("tipo");
                String descrizione = rs.getString("descrizione"); // Recupera la descrizione dalla query
                Servizio servizio = new Servizio(nome, prezzo, tipo, descrizione);
                servizi.add(servizio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return servizi;
    }
    public Map<String, List<Servizio>> getServiziPerTipo() {
        List<Servizio> allServizi = getAllWithDescription();
        Map<String, List<Servizio>> serviziPerTipo = new HashMap<>();
        for (Servizio servizio : allServizi) {
            serviziPerTipo.computeIfAbsent(servizio.getTipo(), k -> new ArrayList<>()).add(servizio);
        }
        return serviziPerTipo;
    }
}
