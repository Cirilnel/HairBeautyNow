package it.unisa.application.sottosistemi.GestioneServizi.view;

import it.unisa.application.model.entity.UtenteAcquirente;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/HairStylingServlet")
public class HairStylingServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recupera l'oggetto UtenteAcquirente dalla sessione
        UtenteAcquirente user = (UtenteAcquirente) request.getSession().getAttribute("user");

        // Se l'utente non è loggato, reindirizza alla pagina di login
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/loginPage");
        } else {
            // Se l'utente è loggato, invia alla pagina Hair Styling
            request.getRequestDispatcher("/WEB-INF/jsp/hair.jsp").forward(request, response);
        }
    }
}

