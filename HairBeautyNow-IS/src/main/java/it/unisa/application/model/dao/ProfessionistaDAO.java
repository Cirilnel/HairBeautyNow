package it.unisa.application.model.dao;

import it.unisa.application.model.entity.Professionista;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ProfessionistaDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/HairBeautyNow";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public void addProfessionista(Professionista professionista) throws SQLException {
        String query = "INSERT INTO Professionista (id, nome) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, professionista.getId());
            stmt.setString(2, professionista.getNome());
            stmt.executeUpdate();
        }
        saveFasciaOraria(professionista.getId(), professionista.getFasciaOraria());
    }

    public Professionista getProfessionista(int id) throws SQLException {
        String query = "SELECT * FROM Professionista WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Professionista(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        loadFasciaOraria(id)
                );
            }
        }
        return null;
    }

    public void updateProfessionista(Professionista professionista) throws SQLException {
        String query = "UPDATE Professionista SET nome = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, professionista.getNome());
            stmt.setInt(2, professionista.getId());
            stmt.executeUpdate();
        }
        saveFasciaOraria(professionista.getId(), professionista.getFasciaOraria());
    }

    public void deleteProfessionista(int id) throws SQLException {
        String query = "DELETE FROM Professionista WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        deleteFasciaOraria(id);
    }

    private void saveFasciaOraria(int professionistaId, Map<String, Boolean> fasciaOraria) throws SQLException {
        String deleteQuery = "DELETE FROM FasciaOraria WHERE professionistaId = ?";
        String insertQuery = "INSERT INTO FasciaOraria (professionistaId, orario, disponibile) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
            deleteStmt.setInt(1, professionistaId);
            deleteStmt.executeUpdate();

            for (Map.Entry<String, Boolean> entry : fasciaOraria.entrySet()) {
                insertStmt.setInt(1, professionistaId);
                insertStmt.setString(2, entry.getKey());
                insertStmt.setBoolean(3, entry.getValue());
                insertStmt.executeUpdate();
            }
        }
    }

    private Map<String, Boolean> loadFasciaOraria(int professionistaId) throws SQLException {
        Map<String, Boolean> fasciaOraria = new HashMap<>();
        String query = "SELECT orario, disponibile FROM FasciaOraria WHERE professionistaId = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, professionistaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                fasciaOraria.put(rs.getString("orario"), rs.getBoolean("disponibile"));
            }
        }
        return fasciaOraria;
    }

    private void deleteFasciaOraria(int professionistaId) throws SQLException {
        String query = "DELETE FROM FasciaOraria WHERE professionistaId = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, professionistaId);
            stmt.executeUpdate();
        }
    }
}
