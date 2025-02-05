package it.unisa.application.model.dao;

import it.unisa.application.model.entity.Servizio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServizioDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/HairBeautyNow";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

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

    public void delete(String nome) {
        String sql = "DELETE FROM Servizi WHERE nome = ?";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, nome);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public double getPrezzoByNome(String nome) {
        String sql = "SELECT prezzo FROM Servizio WHERE nome = ?";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, nome);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("prezzo");  // Restituisce il prezzo
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;  // Se il servizio non è trovato, restituisce 0.0 come valore di default
    }
}
