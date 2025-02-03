package it.unisa.application.sottosistemi.GestioneAccesso.view;


import it.unisa.application.model.entity.UtenteAcquirente;

import it.unisa.application.sottosistemi.GestioneAccesso.service.UtenteAcquirenteService;
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
        // Raccogli i dati dal form di login
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Crea un'istanza del service
        UtenteAcquirenteService utenteService = new UtenteAcquirenteService();

        // Verifica le credenziali dell'utente
        UtenteAcquirente utente = utenteService.login(email, password);

        if (utente != null) {
            // Se le credenziali sono corrette, l'utente è autenticato e viene reindirizzato alla home
            request.getSession().setAttribute("user", utente);  // Salva l'utente nella sessione
            response.sendRedirect(request.getContextPath() + "/index.jsp");  // Reindirizza alla home
        } else {
            // Se le credenziali sono errate, invia un messaggio di errore
            request.setAttribute("errorMessage", "Email o password errati!");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/loginPage.jsp");
            dispatcher.forward(request, response);
        }
    }
}
