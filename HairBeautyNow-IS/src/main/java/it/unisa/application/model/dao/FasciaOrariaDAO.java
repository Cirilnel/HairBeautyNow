package it.unisa.application.model.dao;

import it.unisa.application.model.entity.FasciaOraria;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FasciaOrariaDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/HairBeautyNow";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    // Metodo per ottenere la connessione al database
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Metodo per ottenere una fascia oraria specifica per un professionista, giorno e fascia
    public FasciaOraria getFasciaByProfessionistaAndGiorno(int professionistaId, LocalDate giorno, String fascia) throws SQLException {
        // Eseguiamo il confronto sui primi 5 caratteri della fascia oraria (per includere anche l'ora "8:00")
        String fasciaConfronto = fascia.substring(0, 5);  // Ottieni solo i primi 5 caratteri (es. "8:00")

        String query = "SELECT * FROM fascia_oraria WHERE professionista_id = ? AND giorno = ? AND SUBSTRING(fascia, 1, 5) = ?";
        System.out.println("Eseguendo query: " + query);

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, professionistaId);
            stmt.setDate(2, Date.valueOf(giorno));
            stmt.setString(3, fasciaConfronto);  // Usa solo i primi 5 caratteri di 'fascia'
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                FasciaOraria fasciaOraria = new FasciaOraria(
                        rs.getInt("id"),
                        rs.getInt("professionista_id"),
                        rs.getDate("giorno").toLocalDate(),
                        rs.getString("fascia"),
                        rs.getBoolean("disponibile")
                );
                return fasciaOraria;
            } else {
                System.out.println("Fascia oraria non trovata per professionista: " + professionistaId + ", giorno: " + giorno + ", fascia: " + fascia);
            }
        }
        return null;
    }


    // Metodo per ottenere le fasce orarie per un professionista
    public List<FasciaOraria> getFasceOrarieByProfessionista(int professionistaId) throws SQLException {
        List<FasciaOraria> fasceOrarie = new ArrayList<>();
        String sql = "SELECT * FROM fascia_oraria WHERE professionista_id = ?";
        System.out.println("Eseguendo query per ottenere fasce orarie per il professionista con ID: " + professionistaId);

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, professionistaId);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Nessuna fascia oraria trovata per il professionista con ID: " + professionistaId);
            }

            do {
                FasciaOraria fascia = new FasciaOraria();
                fascia.setGiorno(rs.getDate("giorno").toLocalDate());
                fascia.setFascia(rs.getString("fascia"));
                fascia.setDisponibile(rs.getBoolean("disponibile"));

                fasceOrarie.add(fascia);
            } while (rs.next());

        } catch (SQLException e) {
            System.out.println("Errore durante la ricerca delle fasce orarie per il professionista con ID: " + professionistaId);
            throw e;
        }

        return fasceOrarie;
    }


    // Metodo per aggiornare una fascia oraria
    public boolean aggiornaFasciaOraria(FasciaOraria fasciaOraria) throws SQLException {
        String updateQuery = "UPDATE fascia_oraria SET disponibile = ? WHERE id = ?";
        System.out.println("Eseguendo l'update per la fascia oraria con ID: " + fasciaOraria.getId());

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setBoolean(1, fasciaOraria.isDisponibile());
            stmt.setInt(2, fasciaOraria.getId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Fascia oraria aggiornata correttamente.");
                return true;
            } else {
                System.out.println("Nessuna fascia oraria aggiornata.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Errore durante l'aggiornamento della fascia oraria con ID: " + fasciaOraria.getId());
            throw e;
        }
    }

    // Metodo per aggiornare una fascia oraria, ad esempio quando una prenotazione viene effettuata
    public void updateFasciaOraria(FasciaOraria fasciaOraria) throws SQLException {
        String query = "UPDATE fascia_oraria SET disponibile = ? WHERE id = ?";
        System.out.println("Eseguendo l'update sulla fascia oraria, nuovo stato disponibile: " + fasciaOraria.isDisponibile());

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBoolean(1, fasciaOraria.isDisponibile());
            stmt.setInt(2, fasciaOraria.getId());
            stmt.executeUpdate();
            System.out.println("Fascia oraria aggiornata con successo.");
        } catch (SQLException e) {
            System.out.println("Errore durante l'aggiornamento della fascia oraria con ID: " + fasciaOraria.getId());
            throw e;
        }
    }

    // Metodo per ottenere una fascia oraria (duplicato, forse non necessario)
    public FasciaOraria getFasciaOraria(int professionistaId, LocalDate giorno, String fascia) {
        String query = "SELECT * FROM fascia_oraria WHERE professionista_id = ? AND giorno = ? AND fascia = ?";
        FasciaOraria fasciaOraria = null;
        System.out.println("Eseguendo query per ottenere fascia oraria (duplicato): " + query);

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, professionistaId);
            statement.setDate(2, Date.valueOf(giorno));  // Converti LocalDate in java.sql.Date
            statement.setString(3, fascia);  // Usato 'fascia' invece di 'orario'
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                fasciaOraria = new FasciaOraria();
                fasciaOraria.setId(resultSet.getInt("id"));
                fasciaOraria.setProfessionistaId(resultSet.getInt("professionista_id"));
                fasciaOraria.setGiorno(resultSet.getDate("giorno").toLocalDate());  // Converti java.sql.Date in LocalDate
                fasciaOraria.setFascia(resultSet.getString("fascia"));  // Assicurati che la colonna esista nel database
                fasciaOraria.setDisponibile(resultSet.getBoolean("disponibile"));
                System.out.println("Fascia oraria trovata (duplicato): " + fasciaOraria);
            }
        } catch (SQLException e) {
            System.out.println("Errore durante la ricerca della fascia oraria (duplicato): " + e.getMessage());
        }

        return fasciaOraria;
    }
}
