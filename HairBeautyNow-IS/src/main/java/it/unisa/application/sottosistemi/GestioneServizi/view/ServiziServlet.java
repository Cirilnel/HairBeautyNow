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
        // Chiamata alla servlet "servizi" utilizzando il RequestDispatcher
        RequestDispatcher dispatcher = request.getRequestDispatcher("/servizi"); // Assumendo che "/servizi" sia l'URL della servlet servizi
        dispatcher.forward(request, response);
    }
}
