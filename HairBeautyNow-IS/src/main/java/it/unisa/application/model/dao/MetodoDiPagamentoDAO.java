package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.MetodoDiPagamento;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MetodoDiPagamentoDAO {
    private DataSource ds;

    public MetodoDiPagamentoDAO() {
        this.ds = DataSourceSingleton.getInstance();
    }

    // Aggiungi un metodo di pagamento per un utente specifico
    public void addMetodoDiPagamento(MetodoDiPagamento metodo) throws SQLException {
        String query = "INSERT INTO MetodoDiPagamento (nCarta, dataScadenza, nomeIntestatario, cvv, indirizzo, username, metodoPagamento) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, metodo.getnCarta());
            stmt.setDate(2, new java.sql.Date(metodo.getDataScadenza().getTime()));
            stmt.setString(3, metodo.getNomeIntestatario());
            stmt.setInt(4, metodo.getCvv());
            stmt.setString(5, metodo.getIndirizzo());
            stmt.setString(6, metodo.getUsername());  // Collega il metodo di pagamento all'utente
            stmt.setString(7, metodo.getMetodoPagamento());  // Aggiungi il tipo di metodo di pagamento
            stmt.executeUpdate();
        }
    }





    // Recupera il metodo di pagamento dato l'username
    public MetodoDiPagamento getMetodoDiPagamentoByUsername(String username) throws SQLException {
        String query = "SELECT * FROM MetodoDiPagamento WHERE username = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new MetodoDiPagamento(
                        rs.getString("nCarta"),
                        rs.getDate("dataScadenza"),
                        rs.getString("nomeIntestatario"),
                        rs.getString("indirizzo"),
                        rs.getInt("cvv"),
                        rs.getString("username"),  // Recupera l'username associato
                        rs.getString("metodoPagamento")  // Recupera il tipo di metodo di pagamento
                );
            }
        }
        return null;  // Se il metodo di pagamento non esiste per l'utente
    }

}
