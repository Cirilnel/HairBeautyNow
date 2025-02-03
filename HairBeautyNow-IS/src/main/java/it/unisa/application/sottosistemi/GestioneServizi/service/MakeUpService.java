package it.unisa.application.sottosistemi.GestioneServizi.service;


import it.unisa.application.model.entity.Servizio;

import java.sql.*;
import java.util.*;

public class MakeUpService {
    private static final String URL = "jdbc:mysql://localhost:3306/HairBeautyNow"; // URL del DB
    private static final String USER = "root"; // Nome utente DB
    private static final String PASSWORD = "root"; // Password del DB

    // Metodo per ottenere tutti i servizi dal DB
    public List<Servizio> getAllServiziWithDescription() {
        List<Servizio> servizi = new ArrayList<>();
        String sql = "SELECT nome, prezzo, tipo, descrizione FROM Servizio"; // Query aggiornata per ottenere anche la descrizione

        try {
            // Registra manualmente il driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver JDBC caricato correttamente!");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver JDBC non trovato!");
            e.printStackTrace();
            return servizi; // Restituisce una lista vuota in caso di errore
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (conn != null) {
                System.out.println("Connessione al database riuscita!"); // Log per confermare la connessione
            } else {
                System.out.println("Errore nella connessione al database!");
                return servizi; // Restituisce una lista vuota in caso di errore di connessione
            }

            // Itera sui risultati della query e crea gli oggetti Servizio con descrizione
            while (rs.next()) {
                String nome = rs.getString("nome");
                double prezzo = rs.getDouble("prezzo");
                String tipo = rs.getString("tipo");
                String descrizione = rs.getString("descrizione"); // Recupera la descrizione dalla query

                // Crea il servizio con tutti i dati, inclusa la descrizione
                Servizio servizio = new Servizio(nome, prezzo, tipo, descrizione);
                servizi.add(servizio);
            }
        } catch (SQLException e) {
            System.out.println("Errore SQL: " + e.getMessage()); // Log per eventuali errori SQL
            e.printStackTrace(); // Stampa l'errore per un'analisi più dettagliata
        }
        return servizi;
    }

    // Metodo per ottenere i servizi raggruppati per tipo
    public Map<String, List<Servizio>> getServiziPerTipo() {
        List<Servizio> allServizi = getAllServiziWithDescription();
        Map<String, List<Servizio>> serviziPerTipo = new HashMap<>();

        // Raggruppa i servizi per tipo
        for (Servizio servizio : allServizi) {
            serviziPerTipo.computeIfAbsent(servizio.getTipo(), k -> new ArrayList<>()).add(servizio);
        }

        return serviziPerTipo;
    }
}