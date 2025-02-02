package it.unisa.application.sottosistemi;


import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import it.unisa.application.database_connection.DataSourceSingleton;

@WebServlet("/testConnection")
public class TestConnectionServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection conn = dataSource.getConnection()) {
            if (conn != null) {
                response.getWriter().write("Connection to the database was successful!");
            } else {
                response.getWriter().write("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            response.getWriter().write("An error occurred while connecting to the database: " + e.getMessage());
        }
    }
}
