package it.unisa.application.sottosistemi.GestioneAccesso.view;

import it.unisa.application.model.entity.UtenteAcquirente;
import it.unisa.application.model.entity.UtenteGestoreSede;
import it.unisa.application.model.entity.UtenteGestoreCatena;
import it.unisa.application.sottosistemi.GestioneAccesso.service.UtenteService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet2 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username"); // Cambiato da 'email' a 'username'
        String password = request.getParameter("password");

        UtenteService utenteService = new UtenteService();
        Object utente = utenteService.login(username, password); // Passa 'username' invece di 'email'

        if (utente != null) {
            request.getSession().setAttribute("user", utente);

            if (utente instanceof UtenteAcquirente) {
                response.sendRedirect(request.getContextPath() + "/home");
            } else if (utente instanceof UtenteGestoreSede) {
                response.sendRedirect(request.getContextPath() + "/homeSede");
            } else if (utente instanceof UtenteGestoreCatena) {
                response.sendRedirect(request.getContextPath() + "/homeCatena");
            } else {
                request.setAttribute("errorMessage", "Tipo di utente non riconosciuto!");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/loginPage.jsp");
                dispatcher.forward(request, response);
            }
        } else {
            request.setAttribute("errorMessage", "Username o password errati!"); // Cambiato il messaggio di errore
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/loginPage.jsp");
            dispatcher.forward(request, response);
        }
    }
}
