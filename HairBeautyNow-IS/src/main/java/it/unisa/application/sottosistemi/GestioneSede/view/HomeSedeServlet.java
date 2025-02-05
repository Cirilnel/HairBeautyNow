package it.unisa.application.sottosistemi.GestioneSede.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/homeSede")
public class HomeSedeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Puoi aggiungere qui logica per recuperare dati da una base dati, logica di business, ecc.

        // Dispatch della richiesta alla pagina homesede.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/homeSede.jsp");
        dispatcher.forward(request, response);
    }
}
