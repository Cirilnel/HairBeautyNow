package it.unisa.application.sottosistemi;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/promozioni")
public class PromozioniServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Reindirizza alla pagina index.jsp
        request.getRequestDispatcher("/WEB-INF/jsp/promozioni.jsp").forward(request, response);
    }
}
