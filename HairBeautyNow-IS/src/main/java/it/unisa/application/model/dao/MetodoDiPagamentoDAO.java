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

    // Verifica se un metodo di pagamento esiste già per l'utente
    public boolean metodoDiPagamentoEsistente(String username, String metodoPagamento) throws SQLException {
        String query = "SELECT COUNT(*) FROM MetodoDiPagamento WHERE username = ? AND metodoPagamento = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, metodoPagamento);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;  // Se il conteggio è maggiore di 0, significa che esiste già
            }
        }
        return false;
    }

    // Aggiungi un metodo di pagamento per un utente specifico
    public void addMetodoDiPagamento(MetodoDiPagamento metodo) throws SQLException {
        // Controlla se il metodo di pagamento esiste già
        if (metodoDiPagamentoEsistente(metodo.getUsername(), metodo.getMetodoPagamento())) {
            // Se esiste, aggiorna i dati esistenti (ad esempio, indirizzo, email, etc.)
            updateMetodoDiPagamento(metodo);
        } else {
            // Se non esiste, aggiungi un nuovo metodo di pagamento
            String query = "INSERT INTO MetodoDiPagamento (nCarta, dataScadenza, nomeIntestatario, cvv, indirizzo, username, metodoPagamento, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = ds.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                // Aggiungi il numero della carta se presente
                stmt.setString(1, metodo.getnCarta());

                // Se il metodo di pagamento è PayPal, non impostiamo la data di scadenza
                if ("paypal".equals(metodo.getMetodoPagamento())) {
                    stmt.setNull(2, Types.DATE);  // Imposta la dataScadenza come NULL per PayPal
                } else {
                    // Imposta la data di scadenza per carte di credito (Visa, MasterCard, ecc.)
                    stmt.setDate(2, java.sql.Date.valueOf(metodo.getDataScadenza()));
                }

                // Aggiungi gli altri parametri del metodo di pagamento
                stmt.setString(3, metodo.getNomeIntestatario());
                stmt.setInt(4, metodo.getCvv());
                stmt.setString(5, metodo.getIndirizzo());
                stmt.setString(6, metodo.getUsername());  // Collega il metodo di pagamento all'utente
                stmt.setString(7, metodo.getMetodoPagamento());  // Aggiungi il tipo di metodo di pagamento

                // Se il metodo di pagamento è PayPal, aggiungi l'email, altrimenti imposta null
                if (metodo.getEmail() != null) {
                    stmt.setString(8, metodo.getEmail());
                } else {
                    stmt.setNull(8, Types.VARCHAR);  // Se email è null (ad esempio per carta), setta il valore a null
                }

                // Esegui l'inserimento nel database
                stmt.executeUpdate();
            }
        }
    }

    // Aggiorna un metodo di pagamento esistente
    public void updateMetodoDiPagamento(MetodoDiPagamento metodo) throws SQLException {
        String query = "UPDATE MetodoDiPagamento SET nCarta = ?, dataScadenza = ?, nomeIntestatario = ?, cvv = ?, indirizzo = ?, email = ? WHERE username = ? AND metodoPagamento = ?";

        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Aggiungi il numero della carta se presente
            stmt.setString(1, metodo.getnCarta());

            // Se il metodo di pagamento è PayPal, non impostiamo la data di scadenza
            if ("paypal".equals(metodo.getMetodoPagamento())) {
                stmt.setNull(2, Types.DATE);  // Imposta la dataScadenza come NULL per PayPal
            } else {
                // Imposta la data di scadenza per carte di credito (Visa, MasterCard, ecc.)
                stmt.setDate(2, java.sql.Date.valueOf(metodo.getDataScadenza()));
            }

            // Aggiungi gli altri parametri del metodo di pagamento
            stmt.setString(3, metodo.getNomeIntestatario());
            stmt.setInt(4, metodo.getCvv());
            stmt.setString(5, metodo.getIndirizzo());

            // Se il metodo di pagamento è PayPal, aggiungi l'email, altrimenti imposta null
            if (metodo.getEmail() != null) {
                stmt.setString(6, metodo.getEmail());
            } else {
                stmt.setNull(6, Types.VARCHAR);  // Se email è null (ad esempio per carta), setta il valore a null
            }

            stmt.setString(7, metodo.getUsername());  // Collega il metodo di pagamento all'utente
            stmt.setString(8, metodo.getMetodoPagamento());  // Aggiungi il tipo di metodo di pagamento

            // Esegui l'aggiornamento nel database
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
                        rs.getDate("dataScadenza").toLocalDate(),  // Converte java.sql.Date in LocalDate
                        rs.getString("nomeIntestatario"),
                        rs.getString("indirizzo"),
                        rs.getInt("cvv"),
                        rs.getString("username"),  // Recupera l'username associato
                        rs.getString("metodoPagamento"),  // Recupera il tipo di metodo di pagamento
                        rs.getString("email")  // Recupera l'email, che può essere null
                );

            }
        }
        return null;  // Se il metodo di pagamento non esiste per l'utente
    }
}
