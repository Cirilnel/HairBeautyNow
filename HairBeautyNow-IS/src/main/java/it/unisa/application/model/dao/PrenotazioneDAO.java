package it.unisa.application.model.dao;

import it.unisa.application.model.entity.Prenotazione;
import it.unisa.application.model.entity.Professionista;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PrenotazioneDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/HairBeautyNow";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    // Aggiungi una prenotazione
    public void addPrenotazione(Prenotazione prenotazione) throws SQLException {
        // Verifica se il servizio esiste
        String checkServizioQuery = "SELECT COUNT(*) FROM servizio WHERE nome = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement checkStmt = conn.prepareStatement(checkServizioQuery)) {

            checkStmt.setString(1, prenotazione.getServizioName());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                if (count == 0) {
                    throw new SQLException("Il servizio " + prenotazione.getServizioName() + " non esiste.");
                }
            }

            // Inserisci la prenotazione, includendo l'username e il prezzo
            String query = "INSERT INTO Prenotazione (servizioName, professionistaId, data, username, prezzo) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, prenotazione.getServizioName());
                stmt.setInt(2, prenotazione.getProfessionistaId());

                // Conversione di LocalDateTime a Timestamp per inserire nel DB
                stmt.setTimestamp(3, Timestamp.valueOf(prenotazione.getData())); // Usa Timestamp.valueOf per convertire LocalDateTime in Timestamp
                stmt.setString(4, prenotazione.getUsername());
                stmt.setDouble(5, prenotazione.getPrezzo());

                int affectedRows = stmt.executeUpdate();

                // Se l'inserimento ha avuto successo, ottieni l'ID generato
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int generatedId = generatedKeys.getInt(1);  // Ottieni l'ID generato
                            prenotazione.setId(generatedId);  // Imposta l'ID sulla prenotazione
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

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Prenotazione(
                        rs.getInt("id"),  // Aggiungi l'ID recuperato dal DB
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
    public List<Prenotazione> getPrenotazioniByProfessionisti(List<Professionista> professionisti) throws SQLException {
        List<Prenotazione> lista = new ArrayList<>();

        // Estrai gli ID dei professionisti dalla lista di Professionista
        List<Integer> professionistaIds = professionisti.stream()
                .map(Professionista::getId)  // Assuming Professionista has getId() method
                .collect(Collectors.toList());

        // Se la lista è vuota, ritorna una lista vuota
        if (professionistaIds.isEmpty()) {
            return lista;
        }

        // Crea una query con IN per cercare più professionisti
        String query = "SELECT * FROM Prenotazione WHERE professionistaId IN (" +
                professionistaIds.stream().map(String::valueOf).collect(Collectors.joining(", ")) + ")";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Prenotazione(
                        rs.getInt("id"),  // Aggiungi l'ID recuperato dal DB
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

    // Rimuovi una prenotazione dato l'ID
    // Rimuovi una prenotazione e restituisci un valore booleano di successo
    public boolean rimuoviPrenotazione(int id) throws SQLException {
        String query = "DELETE FROM Prenotazione WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0; // Se sono state rimosse righe, restituisci true
        }
    }
    // Recupera una prenotazione per un determinato ID
    public Prenotazione getPrenotazioneById(int id) throws SQLException {
        Prenotazione prenotazione = null;
        String query = "SELECT * FROM Prenotazione WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                prenotazione = new Prenotazione(
                        rs.getInt("id"),  // Aggiungi l'ID recuperato dal DB
                        rs.getString("servizioName"),
                        rs.getInt("professionistaId"),
                        rs.getTimestamp("data").toLocalDateTime(),
                        rs.getString("username"),
                        rs.getDouble("prezzo")
                );
            }
        }
        return prenotazione;
    }

}




