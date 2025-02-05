package it.unisa.application.model.dao;

import it.unisa.application.model.entity.MetodoDiPagamento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MetodoDiPagamentoDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/HairBeautyNow";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    // Aggiungi un metodo di pagamento per un utente specifico
    public void addMetodoDiPagamento(MetodoDiPagamento metodo) throws SQLException {
        String query = "INSERT INTO MetodoDiPagamento (nCarta, dataScadenza, nomeIntestatario, cvv, indirizzo, username) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, metodo.getnCarta());
            stmt.setDate(2, new java.sql.Date(metodo.getDataScadenza().getTime()));
            stmt.setString(3, metodo.getNomeIntestatario());
            stmt.setInt(4, metodo.getCvv());
            stmt.setString(5, metodo.getIndirizzo());
            stmt.setString(6, metodo.getUsername());  // Collega il metodo di pagamento all'utente
            stmt.executeUpdate();
        }
    }

    // Recupera il metodo di pagamento dato il numero della carta
    public MetodoDiPagamento getMetodoDiPagamento(String nCarta) throws SQLException {
        String query = "SELECT * FROM MetodoDiPagamento WHERE nCarta = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nCarta);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new MetodoDiPagamento(
                        rs.getString("nCarta"),
                        rs.getDate("dataScadenza"),
                        rs.getString("nomeIntestatario"),
                        rs.getString("indirizzo"),
                        rs.getInt("cvv"),
                        rs.getString("username")  // Recupera l'username associato
                );
            }
        }
        return null;
    }

    // Recupera tutti i metodi di pagamento
    public List<MetodoDiPagamento> getAllMetodiDiPagamento() throws SQLException {
        List<MetodoDiPagamento> lista = new ArrayList<>();
        String query = "SELECT * FROM MetodoDiPagamento";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                lista.add(new MetodoDiPagamento(
                        rs.getString("nCarta"),
                        rs.getDate("dataScadenza"),
                        rs.getString("nomeIntestatario"),
                        rs.getString("indirizzo"),
                        rs.getInt("cvv"),
                        rs.getString("username")  // Aggiungi l'username associato
                ));
            }
        }
        return lista;
    }

    // Aggiorna un metodo di pagamento esistente
    public void updateMetodoDiPagamento(MetodoDiPagamento metodo) throws SQLException {
        String query = "UPDATE MetodoDiPagamento SET dataScadenza = ?, nomeIntestatario = ?, cvv = ?, indirizzo = ?, username = ? WHERE nCarta = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, new java.sql.Date(metodo.getDataScadenza().getTime()));
            stmt.setString(2, metodo.getNomeIntestatario());
            stmt.setInt(3, metodo.getCvv());
            stmt.setString(4, metodo.getIndirizzo());
            stmt.setString(5, metodo.getUsername());  // Aggiorna l'username associato
            stmt.setString(6, metodo.getnCarta());
            stmt.executeUpdate();
        }
    }

    // Elimina un metodo di pagamento
    public void deleteMetodoDiPagamento(String nCarta) throws SQLException {
        String query = "DELETE FROM MetodoDiPagamento WHERE nCarta = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nCarta);
            stmt.executeUpdate();
        }
    }
}
