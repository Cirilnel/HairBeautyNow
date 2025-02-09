package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.FasciaOraria;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FasciaOrariaDAO {
    private DataSource ds;

    public FasciaOrariaDAO() {
        this.ds = DataSourceSingleton.getInstance();
    }

    public FasciaOraria getFasciaByProfessionistaAndGiorno(int professionistaId, LocalDate giorno, String fascia) throws SQLException {
        String fasciaConfronto = fascia.substring(0, 5);
        String query = "SELECT * FROM fascia_oraria WHERE professionista_id = ? AND giorno = ? AND SUBSTRING(fascia, 1, 5) = ?";

        try (Connection conn = ds.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, professionistaId);
            stmt.setDate(2, Date.valueOf(giorno));
            stmt.setString(3, fasciaConfronto);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new FasciaOraria(
                        rs.getInt("id"),
                        rs.getInt("professionista_id"),
                        rs.getDate("giorno").toLocalDate(),
                        rs.getString("fascia"),
                        rs.getBoolean("disponibile")
                );
            }
        }
        return null;
    }

    public List<FasciaOraria> getFasceOrarieByProfessionista(int professionistaId) throws SQLException {
        List<FasciaOraria> fasceOrarie = new ArrayList<>();
        String sql = "SELECT * FROM fascia_oraria WHERE professionista_id = ?";

        try (Connection conn = ds.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, professionistaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                fasceOrarie.add(new FasciaOraria(
                        rs.getInt("id"),
                        rs.getInt("professionista_id"),
                        rs.getDate("giorno").toLocalDate(),
                        rs.getString("fascia"),
                        rs.getBoolean("disponibile")
                ));
            }
        }
        return fasceOrarie;
    }

    public boolean aggiornaFasciaOraria(FasciaOraria fasciaOraria) throws SQLException {
        String updateQuery = "UPDATE fascia_oraria SET disponibile = ? WHERE id = ?";

        try (Connection conn = ds.getConnection(); PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setBoolean(1, fasciaOraria.isDisponibile());
            stmt.setInt(2, fasciaOraria.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    public FasciaOraria getFasciaOraria(int professionistaId, LocalDate giorno, String fascia) {
        String query = "SELECT * FROM fascia_oraria WHERE professionista_id = ? AND giorno = ? AND fascia = ?";

        try (Connection connection = ds.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, professionistaId);
            statement.setDate(2, Date.valueOf(giorno));
            statement.setString(3, fascia);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new FasciaOraria(
                        resultSet.getInt("id"),
                        resultSet.getInt("professionista_id"),
                        resultSet.getDate("giorno").toLocalDate(),
                        resultSet.getString("fascia"),
                        resultSet.getBoolean("disponibile")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
