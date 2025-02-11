package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.Prenotazione;
import it.unisa.application.model.entity.Professionista;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PrenotazioneDAO {
    private DataSource ds;

    public PrenotazioneDAO() {
        this.ds = DataSourceSingleton.getInstance();
    }

    // Aggiungi una prenotazione
    public void addPrenotazione(Prenotazione prenotazione) throws SQLException {
        String checkServizioQuery = "SELECT COUNT(*) FROM servizio WHERE nome = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkServizioQuery)) {

            checkStmt.setString(1, prenotazione.getServizioName());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                throw new SQLException("Il servizio " + prenotazione.getServizioName() + " non esiste.");
            }

            String query = "INSERT INTO Prenotazione (servizioName, professionistaId, data, username, prezzo) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, prenotazione.getServizioName());
                stmt.setInt(2, prenotazione.getProfessionistaId());
                stmt.setTimestamp(3, Timestamp.valueOf(prenotazione.getData()));
                stmt.setString(4, prenotazione.getUsername());
                stmt.setDouble(5, prenotazione.getPrezzo());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            prenotazione.setId(generatedKeys.getInt(1));
                        }
                    }
                }
            }
        }
    }

    // Recupera prenotazioni per un determinato username
    public List<Prenotazione> getPrenotazioniByUsername(String username) throws SQLException {
        List<Prenotazione> lista = new ArrayList<>();
        String query = "SELECT * FROM Prenotazione WHERE username = ?";

        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Prenotazione(
                        rs.getInt("id"),
                        rs.getString("servizioName"),
                        rs.getInt("professionistaId"),
                        rs.getTimestamp("data").toLocalDateTime(),
                        rs.getString("username"),
                        rs.getDouble("prezzo")
                ));
            }
        }
        return lista;
    }

    // Recupera prenotazioni per una lista di professionisti
    public List<Prenotazione> getPrenotazioniByProfessionisti(List<Professionista> professionisti) throws SQLException {
        List<Prenotazione> lista = new ArrayList<>();
        List<Integer> professionistaIds = professionisti.stream()
                .map(Professionista::getId)
                .collect(Collectors.toList());

        if (professionistaIds.isEmpty()) {
            return lista;
        }

        String query = "SELECT * FROM Prenotazione WHERE professionistaId IN (" +
                professionistaIds.stream().map(String::valueOf).collect(Collectors.joining(", ")) + ")";

        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Prenotazione(
                        rs.getInt("id"),
                        rs.getString("servizioName"),
                        rs.getInt("professionistaId"),
                        rs.getTimestamp("data").toLocalDateTime(),
                        rs.getString("username"),
                        rs.getDouble("prezzo")
                ));
            }
        }
        return lista;
    }

    // Rimuovi una prenotazione
    public boolean rimuoviPrenotazione(int id) throws SQLException {
        String query = "DELETE FROM Prenotazione WHERE id = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // Recupera una prenotazione per un determinato ID
    public Prenotazione getPrenotazioneById(int id) throws SQLException {
        String query = "SELECT * FROM Prenotazione WHERE id = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Prenotazione(
                        rs.getInt("id"),
                        rs.getString("servizioName"),
                        rs.getInt("professionistaId"),
                        rs.getTimestamp("data").toLocalDateTime(),
                        rs.getString("username"),
                        rs.getDouble("prezzo")
                );
            }
        }
        return null;
    }
}