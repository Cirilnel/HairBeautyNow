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
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        UtenteAcquirenteService utenteService = new UtenteAcquirenteService();
        UtenteAcquirente utente = utenteService.login(email, password);

        if (utente != null) {
            request.getSession().setAttribute("user", utente); // Salva l'utente nella sessione
            response.sendRedirect(request.getContextPath() + "/home"); // Reindirizza alla home
        } else {
            request.setAttribute("errorMessage", "Email o password errati!");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/loginPage.jsp");
            dispatcher.forward(request, response);
        }
    }
}

