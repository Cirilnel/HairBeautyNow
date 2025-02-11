package it.unisa.application.sottosistemi.GestioneRegistrazione.view;

import it.unisa.application.model.dao.UtenteAcquirenteDAO;
import it.unisa.application.sottosistemi.GestioneRegistrazione.service.UtenteAcquirenteService;
import it.unisa.application.model.entity.UtenteAcquirente;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet2 extends HttpServlet {

    private UtenteAcquirenteService utenteAcquirenteService;

    @Override
    public void init() throws ServletException {
        // Inizializza il servizio con un'istanza del DAO
        this.utenteAcquirenteService = new UtenteAcquirenteService(new UtenteAcquirenteDAO());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String username = request.getParameter("username");
        String citta = request.getParameter("citta");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        UtenteAcquirente utente = new UtenteAcquirente(username, email, password, nome, cognome, citta);

        boolean result = utenteAcquirenteService.createUser(utente);

        if (result) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/loginPage.jsp");
            dispatcher.forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Errore nella registrazione. Riprova!");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/registerPage.jsp");
            dispatcher.forward(request, response);
        }
    }
}

