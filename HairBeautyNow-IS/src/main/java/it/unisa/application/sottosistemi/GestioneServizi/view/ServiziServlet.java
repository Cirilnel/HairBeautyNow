package it.unisa.application.sottosistemi.GestioneServizi.view;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/serviziServlet") // URL di accesso alla servlet
public class ServiziServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Reindirizza alla pagina servizi.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("/servizi.jsp");
        dispatcher.forward(request, response);
    }
}
