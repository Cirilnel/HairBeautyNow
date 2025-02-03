package it.unisa.application.sottosistemi.GestioneRegistrazione.view;


import it.unisa.application.model.entity.UtenteAcquirente;
import it.unisa.application.sottosistemi.GestioneRegistrazione.service.UtenteAcquirenteService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet2 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Raccogli i dati dal form di registrazione
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String username = request.getParameter("username");
        String citta = request.getParameter("citta");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Crea un nuovo oggetto UtenteAcquirente
        UtenteAcquirente utente = new UtenteAcquirente(username, email, password, nome, cognome, citta, 0, null);

        // Chiamata al service per l'inserimento dell'utente nel database
        UtenteAcquirenteService utenteService = new UtenteAcquirenteService();
        boolean result = utenteService.createUser(utente);

        // Se l'utente è stato creato con successo, invia l'utente alla pagina di login
        if (result) {
            response.sendRedirect("loginPage");  // Puoi indirizzare l'utente alla pagina di login
        } else {
            // Se si è verificato un errore, invia l'utente alla pagina di registrazione con un errore
            request.setAttribute("errorMessage", "Errore nella registrazione. Riprova!");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/register.jsp");
            dispatcher.forward(request, response);
        }
    }
}
