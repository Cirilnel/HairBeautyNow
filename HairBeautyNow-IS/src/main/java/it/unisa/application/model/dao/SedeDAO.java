package it.unisa.application.model.dao;

import it.unisa.application.model.entity.Sede;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SedeDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/HairBeautyNow";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    // Connessione al database
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Metodo per inserire una nuova sede
    public void insertSede(Sede sede) {
        String query = "INSERT INTO sede (indirizzo, nome, città, id) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, sede.getIndirizzo());
            statement.setString(2, sede.getNome());
            statement.setString(3, sede.getCittà());
            statement.setInt(4, sede.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Metodo per ottenere una sede per ID
    public Sede getSedeById(int id) {
        String query = "SELECT * FROM sede WHERE id = ?";
        Sede sede = null;

        try (Connection connection = getConnection();
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
            e.printStackTrace();
        }
        return sede;
    }

    // Metodo per ottenere tutte le sedi
    public List<Sede> getAllSedi() {
        List<Sede> sedi = new ArrayList<>();
        String query = "SELECT * FROM sede";

        try (Connection connection = getConnection();
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
            e.printStackTrace();
        }
        return sedi;
    }

    // Metodo per aggiornare una sede
    public void updateSede(Sede sede) {
        String query = "UPDATE sede SET indirizzo = ?, nome = ?, città = ? WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, sede.getIndirizzo());
            statement.setString(2, sede.getNome());
            statement.setString(3, sede.getCittà());
            statement.setInt(4, sede.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Metodo per eliminare una sede
    public void deleteSede(int id) {
        String query = "DELETE FROM sede WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
