package it.unisa.application.model.dao;
import it.unisa.application.model.entity.FasciaOraria;
import it.unisa.application.model.entity.Professionista;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfessionistaDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/HairBeautyNow";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    // Connessione al database
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Metodo per inserire un nuovo professionista
    public void insertProfessionista(Professionista professionista) {
        String query = "INSERT INTO professionista (id, nome, sedeId) VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, professionista.getId());
            statement.setString(2, professionista.getNome());
            statement.setInt(3, professionista.getSedeId());

            statement.executeUpdate();

            // Inserisci le fasce orarie associate
            for (FasciaOraria fascia : professionista.getFasceOrarie()) {
                insertFasciaOraria(professionista.getId(), fascia, connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Metodo per inserire una fascia oraria
    private void insertFasciaOraria(int professionistaId, FasciaOraria fascia, Connection connection) throws SQLException {
        String query = "INSERT INTO fascia_oraria (professionista_id, giorno, fascia, disponibile) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, professionistaId);
            statement.setDate(2, Date.valueOf(fascia.getGiorno())); // Usa Date.valueOf per la conversione
            statement.setString(3, fascia.getFascia());
            statement.setBoolean(4, fascia.isDisponibile());

            statement.executeUpdate();
        }
    }


    // Metodo per ottenere un professionista per ID
    public Professionista getProfessionistaById(int id) {
        String query = "SELECT p.id, p.nome, p.sedeId, f.id as fasciaId, f.giorno, f.fascia, f.disponibile " +
                "FROM professionista p " +
                "JOIN fascia_oraria f ON p.id = f.professionista_id " +
                "WHERE p.id = ?";

        Professionista professionista = null;
        Map<Integer, Professionista> professionistiMap = new HashMap<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int professionistaId = resultSet.getInt("id");
                String nome = resultSet.getString("nome");
                int sedeId = resultSet.getInt("sedeId");
                int fasciaId = resultSet.getInt("fasciaId");
                LocalDate giorno = resultSet.getDate("giorno").toLocalDate(); // Convertito a LocalDate
                String fascia = resultSet.getString("fascia");
                boolean disponibile = resultSet.getBoolean("disponibile");

                // Crea un oggetto FasciaOraria per ogni fascia, passando tutti i parametri
                FasciaOraria fasciaOraria = new FasciaOraria(fasciaId, professionistaId, giorno, fascia, disponibile);

                // Se il professionista non è ancora presente nella mappa, lo aggiungiamo
                if (!professionistiMap.containsKey(professionistaId)) {
                    professionista = new Professionista(professionistaId, nome, sedeId, new ArrayList<>());
                    professionistiMap.put(professionistaId, professionista);
                }

                // Aggiungiamo la fascia oraria alla lista delle fasce orarie del professionista
                professionistiMap.get(professionistaId).getFasceOrarie().add(fasciaOraria);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return professionista;
    }



    // Metodo per ottenere tutti i professionisti di una sede
    public List<Professionista> getProfessionistiBySede(int sedeId) {
        String query = "SELECT p.id, p.nome, f.id as fasciaId, f.giorno, f.fascia, f.disponibile " +
                "FROM professionista p " +
                "JOIN fascia_oraria f ON p.id = f.professionista_id " +
                "WHERE p.sedeId = ?";

        List<Professionista> professionisti = new ArrayList<>();
        Map<Integer, Professionista> professionistiMap = new HashMap<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, sedeId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int professionistaId = resultSet.getInt("id");
                String nome = resultSet.getString("nome");
                LocalDate giorno = resultSet.getDate("giorno").toLocalDate();
                String fascia = resultSet.getString("fascia");
                boolean disponibile = resultSet.getBoolean("disponibile");
                int fasciaId = resultSet.getInt("fasciaId");

                // Crea un oggetto FasciaOraria per ogni fascia, passando tutti i parametri
                FasciaOraria fasciaOraria = new FasciaOraria(fasciaId, professionistaId, giorno, fascia, disponibile);

                // Se il professionista non è ancora presente nella mappa, lo aggiungiamo
                if (!professionistiMap.containsKey(professionistaId)) {
                    Professionista professionista = new Professionista(professionistaId, nome, sedeId, new ArrayList<>());
                    professionistiMap.put(professionistaId, professionista);
                }

                // Aggiungiamo la fascia oraria alla lista delle fasce orarie del professionista
                professionistiMap.get(professionistaId).getFasceOrarie().add(fasciaOraria);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        professionisti.addAll(professionistiMap.values());
        return professionisti;
    }

    public void updateFasciaOraria(FasciaOraria fascia) throws SQLException {
        String query = "UPDATE fascia_oraria SET disponibile = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBoolean(1, fascia.isDisponibile());
            stmt.setInt(2, fascia.getId());
            stmt.executeUpdate();
        }
    }
    public FasciaOraria getFasciaOraria(int professionistaId, String giorno, String fasciaOraria) throws SQLException {
        String query = "SELECT id, professionista_id, giorno, fascia, disponibile FROM fascia_oraria " +
                "WHERE professionista_id = ? AND giorno = ? AND fascia = ?";

        FasciaOraria fascia = null;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, professionistaId);
            statement.setDate(2, Date.valueOf(giorno)); // Converti la stringa giorno in un oggetto Date
            statement.setString(3, fasciaOraria);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Recupera i dati dal resultSet
                int id = resultSet.getInt("id");
                LocalDate giornoFascia = resultSet.getDate("giorno").toLocalDate(); // Assicurati della conversione
                String fasciat = resultSet.getString("fascia");
                boolean disponibile = resultSet.getBoolean("disponibile");

                // Crea un oggetto FasciaOraria
                fascia = new FasciaOraria(id, professionistaId, giornoFascia, fasciat, disponibile);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Errore durante il recupero della fascia oraria", e);
        }

        return fascia;  // Restituisce null se non trovata
    }


}
