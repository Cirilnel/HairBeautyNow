package it.unisa.application.sottosistemi.GestioneCatena.view;

import it.unisa.application.model.dao.SedeDAO;
import it.unisa.application.model.entity.Sede;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/homeCatena")
public class HomeCatenaServlet extends HttpServlet {

    private final SedeDAO sedeDAO = new SedeDAO(); // Inizializzazione del DAO

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/homeCatena.jsp");
        dispatcher.forward(request, response);
    }
}
