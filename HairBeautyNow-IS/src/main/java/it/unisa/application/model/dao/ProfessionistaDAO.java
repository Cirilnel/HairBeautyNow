package it.unisa.application.model.dao;

import it.unisa.application.model.entity.FasciaOraria;
import it.unisa.application.model.entity.Professionista;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProfessionistaDAO {

    private static final Logger LOGGER = Logger.getLogger(ProfessionistaDAO.class.getName());

    // Parametri di connessione centralizzati
    private static final String URL = "jdbc:mysql://localhost:3306/HairBeautyNow";
    private static final String USER = "root";  // Modifica con le tue credenziali
    private static final String PASSWORD = "root";  // Modifica con le tue credenziali

    // Metodo per ottenere la connessione al database
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Metodo per ottenere il nome del professionista
    public String getProfessionistaById(int id) {
        String query = "SELECT nome, sedeId FROM professionista WHERE id = ?";
        String nome = null;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                nome = resultSet.getString("nome");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while fetching professionista by ID", e);
        }

        return nome;
    }

    // Metodo per ottenere l'indirizzo del professionista tramite il suo sedeId
    public String getIndirizzoBySedeId(int sedeId) {
        String query = "SELECT indirizzo FROM sede WHERE id = ?";
        String indirizzo = null;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, sedeId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                indirizzo = resultSet.getString("indirizzo");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while fetching indirizzo by sedeId", e);
        }

        return indirizzo;
    }

    // Metodo per ottenere il prezzo del servizio
    public double getPrezzoByServizio(String servizioName) {
        String query = "SELECT prezzo FROM Servizio WHERE nome = ?";
        double prezzo = 0.0;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, servizioName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                prezzo = resultSet.getDouble("prezzo");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while fetching price by servizio", e);
        }

        return prezzo;
    }

    // Metodo per ottenere una fascia oraria per un professionista, giorno e orario
    public FasciaOraria getFasciaOraria(int professionistaId, LocalDate giorno, String fascia) {
        String query = "SELECT * FROM fascia_oraria WHERE professionista_id = ? AND giorno = ? AND fascia = ?";
        FasciaOraria fasciaOraria = null;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

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
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while fetching fascia oraria", e);
        }

        return fasciaOraria;
    }

    // Metodo per ottenere le fasce orarie per un professionista
    public List<FasciaOraria> getFasceOrarieByProfessionista(int professionistaId) {
        List<FasciaOraria> fasceOrarie = new ArrayList<>();
        String sql = "SELECT * FROM fascia_oraria WHERE professionista_id = ?"; // Modificata la tabella da 'FasceOrarie' a 'fascia_oraria'

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, professionistaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                FasciaOraria fascia = new FasciaOraria();
                fascia.setGiorno(rs.getDate("giorno").toLocalDate()); // Controlla il nome corretto della colonna
                fascia.setFascia(rs.getString("fascia")); // Assicurati che la colonna esista nel database
                fascia.setDisponibile(rs.getBoolean("disponibile"));
                fasceOrarie.add(fascia);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while fetching fasce orarie", e);
        }

        return fasceOrarie;
    }

    // Metodo per aggiornare una fascia oraria, ad esempio quando una prenotazione viene effettuata
    public void updateFasciaOraria(FasciaOraria fasciaOraria) {
        String query = "UPDATE fascia_oraria SET disponibile = ? WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setBoolean(1, fasciaOraria.isDisponibile());
            statement.setInt(2, fasciaOraria.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while updating fascia oraria", e);
        }
    }

    // Nuovo metodo per ottenere una lista di professionisti da una specifica sede
    public List<Professionista> getProfessionistiBySede(int sedeId) {
        List<Professionista> professionisti = new ArrayList<>();
        String query = "SELECT * FROM professionista WHERE sedeId = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, sedeId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Professionista professionista = new Professionista();
                professionista.setId(resultSet.getInt("id"));
                professionista.setNome(resultSet.getString("nome"));
                professionista.setSedeId(resultSet.getInt("sedeId"));

                // Aggiungere altre informazioni se necessarie
                professionisti.add(professionista);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while fetching professionals by sedeId", e);
        }

        return professionisti;
    }

    public int getSedeIdByProfessionistaId(int professionistaId) {
        String query = "SELECT sedeId FROM professionista WHERE id = ?";
        int sedeId = -1;  // Un valore che rappresenta l'assenza di una sede

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, professionistaId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                sedeId = resultSet.getInt("sedeId");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while fetching sedeId by professionistaId", e);
        }

        return sedeId;
    }

    // Metodo per rimuovere un professionista
    public boolean rimuoviProfessionista(int professionistaId) {
        // Prima rimuovi le fasce orarie associate
        String deleteFasceQuery = "DELETE FROM fascia_oraria WHERE professionista_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement deleteFasceStatement = connection.prepareStatement(deleteFasceQuery)) {

            // Elimina le fasce orarie
            deleteFasceStatement.setInt(1, professionistaId);
            int rowsAffected = deleteFasceStatement.executeUpdate();

            // Se sono state rimosse delle fasce orarie, ora puoi rimuovere il professionista
            if (rowsAffected > 0) {
                String deleteProfessionistaQuery = "DELETE FROM professionista WHERE id = ?";
                try (PreparedStatement deleteProfessionistaStatement = connection.prepareStatement(deleteProfessionistaQuery)) {
                    deleteProfessionistaStatement.setInt(1, professionistaId);
                    int professionistaRowsAffected = deleteProfessionistaStatement.executeUpdate();

                    // Se la rimozione del professionista ha successo
                    return professionistaRowsAffected > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante la rimozione del professionista e delle fasce orarie", e);
        }

        return false; // Se qualcosa è andato storto
    }


    // Metodo per verificare se un professionista ha prenotazioni
    public boolean hasPrenotazioni(int professionistaId) {
        String query = "SELECT COUNT(*) FROM Prenotazione WHERE professionistaId = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, professionistaId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Se ci sono prenotazioni
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while checking if professionista has prenotazioni", e);
        }
        return false; // Se non ci sono prenotazioni
    }
}
