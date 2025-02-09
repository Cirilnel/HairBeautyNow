package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.Servizio;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
        String sql = "INSERT INTO Servizi (nome, descrizione, tipo, durata, prezzo) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, servizio.getNome());
            preparedStatement.setString(2, servizio.getDescrizione());
            preparedStatement.setString(3, servizio.getTipo());
            preparedStatement.setDouble(5, servizio.getPrezzo());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get a servizio by nome
    public Servizio getByNome(String nome) {
        String sql = "SELECT * FROM Servizi WHERE nome = ?";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, nome);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String descrizione = resultSet.getString("descrizione");
                String tipo = resultSet.getString("tipo");
                double prezzo = resultSet.getDouble("prezzo");

                return new Servizio(nome, prezzo, tipo, descrizione);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Return null if no service is found
    }

    // Get all servizi
    public List<Servizio> getAll() {
        List<Servizio> servizi = new ArrayList<>();
        String sql = "SELECT * FROM Servizi";
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

    // Update an existing servizio
    public void update(Servizio servizio) {
        String sql = "UPDATE Servizi SET descrizione = ?, tipo = ?, durata = ?, prezzo = ? WHERE nome = ?";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, servizio.getDescrizione());
            preparedStatement.setString(2, servizio.getTipo());
            preparedStatement.setDouble(4, servizio.getPrezzo());
            preparedStatement.setString(5, servizio.getNome());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a servizio by nome
    public void delete(String nome) {
        String sql = "DELETE FROM Servizi WHERE nome = ?";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, nome);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get prezzo by servizio nome
    public double getPrezzoByNome(String nome) {
        String sql = "SELECT prezzo FROM Servizi WHERE nome = ?";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, nome);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("prezzo");  // Return the prezzo
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;  // If the servizio is not found, return 0.0 as default
    }
}
