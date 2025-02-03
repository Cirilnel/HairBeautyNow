package it.unisa.application.model.dao;

import it.unisa.application.model.entity.MetodoDiPagamento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MetodoDiPagamentoDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/HairBeautyNow";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public void addMetodoDiPagamento(MetodoDiPagamento metodo) throws SQLException {
        String query = "INSERT INTO MetodoDiPagamento (nCarta, dataScadenza, nomeIntestatario, cvv, indirizzo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, metodo.getnCarta());
            stmt.setDate(2, new java.sql.Date(metodo.getDataScadenza().getTime()));
            stmt.setString(3, metodo.getNomeIntestatario());
            stmt.setInt(4, metodo.getCvv());
            stmt.setString(5, metodo.getIndirizzo());
            stmt.executeUpdate();
        }
    }

    public MetodoDiPagamento getMetodoDiPagamento(String nCarta) throws SQLException {
        String query = "SELECT * FROM MetodoDiPagamento WHERE nCarta = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nCarta);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new MetodoDiPagamento(
                        rs.getDate("dataScadenza"),
                        rs.getString("nCarta"),
                        rs.getString("nomeIntestatario"),
                        rs.getString("indirizzo"),
                        rs.getInt("cvv")
                );
            }
        }
        return null;
    }

    public List<MetodoDiPagamento> getAllMetodiDiPagamento() throws SQLException {
        List<MetodoDiPagamento> lista = new ArrayList<>();
        String query = "SELECT * FROM MetodoDiPagamento";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                lista.add(new MetodoDiPagamento(
                        rs.getDate("dataScadenza"),
                        rs.getString("nCarta"),
                        rs.getString("nomeIntestatario"),
                        rs.getString("indirizzo"),
                        rs.getInt("cvv")
                ));
            }
        }
        return lista;
    }

    public void updateMetodoDiPagamento(MetodoDiPagamento metodo) throws SQLException {
        String query = "UPDATE MetodoDiPagamento SET dataScadenza = ?, nomeIntestatario = ?, cvv = ?, indirizzo = ? WHERE nCarta = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, new java.sql.Date(metodo.getDataScadenza().getTime()));
            stmt.setString(2, metodo.getNomeIntestatario());
            stmt.setInt(3, metodo.getCvv());
            stmt.setString(4, metodo.getIndirizzo());
            stmt.setString(5, metodo.getnCarta());
            stmt.executeUpdate();
        }
    }

    public void deleteMetodoDiPagamento(String nCarta) throws SQLException {
        String query = "DELETE FROM MetodoDiPagamento WHERE nCarta = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nCarta);
            stmt.executeUpdate();
        }
    }
}