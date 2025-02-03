package it.unisa.application.model.dao;


import it.unisa.application.model.entity.Prenotazione;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrenotazioneDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/HairBeautyNow";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public void addPrenotazione(Prenotazione prenotazione) throws SQLException {
        String query = "INSERT INTO Prenotazione (id, servizioName, professionistaId, data) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, prenotazione.getId());
            stmt.setString(2, prenotazione.getServizioName());
            stmt.setInt(3, prenotazione.getProfessionistaId());
            stmt.setDate(4, new java.sql.Date(prenotazione.getData().getTime()));
            stmt.executeUpdate();
        }
    }

    public Prenotazione getPrenotazione(int id) throws SQLException {
        String query = "SELECT * FROM Prenotazione WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Prenotazione(
                        rs.getInt("id"),
                        rs.getString("servizioName"),
                        rs.getInt("professionistaId"),
                        rs.getDate("data")
                );
            }
        }
        return null;
    }

    public List<Prenotazione> getAllPrenotazioni() throws SQLException {
        List<Prenotazione> lista = new ArrayList<>();
        String query = "SELECT * FROM Prenotazione";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                lista.add(new Prenotazione(
                        rs.getInt("id"),
                        rs.getString("servizioName"),
                        rs.getInt("professionistaId"),
                        rs.getDate("data")
                ));
            }
        }
        return lista;
    }

    public void updatePrenotazione(Prenotazione prenotazione) throws SQLException {
        String query = "UPDATE Prenotazione SET servizioName = ?, professionistaId = ?, data = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, prenotazione.getServizioName());
            stmt.setInt(2, prenotazione.getProfessionistaId());
            stmt.setDate(3, new java.sql.Date(prenotazione.getData().getTime()));
            stmt.setInt(4, prenotazione.getId());
            stmt.executeUpdate();
        }
    }

    public void deletePrenotazione(int id) throws SQLException {
        String query = "DELETE FROM Prenotazione WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}