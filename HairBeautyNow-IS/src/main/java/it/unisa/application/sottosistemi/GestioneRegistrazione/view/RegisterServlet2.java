package it.unisa.application.sottosistemi.GestioneRegistrazione.view;

import it.unisa.application.model.dao.UtenteAcquirenteDAO;
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

        // Debug: stampa i parametri ricevuti dalla request
        System.out.println("Registrazione ricevuta:");
        System.out.println("Nome: " + nome);
        System.out.println("Cognome: " + cognome);
        System.out.println("Username: " + username);
        System.out.println("Città: " + citta);
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);  // Evita di stampare password in un ambiente di produzione!

        // Crea un nuovo oggetto UtenteAcquirente, con prenotazioneID=0 e nCarta=null di default
        UtenteAcquirente utente = new UtenteAcquirente(username, email, password, nome, cognome, citta, null, null);

        // Debug: stampa l'oggetto utente che sta per essere inserito
        System.out.println("Utente creato: " + utente);

        // Usa il DAO per inserire l'utente
        UtenteAcquirenteDAO utenteDAO = new UtenteAcquirenteDAO();
        boolean result = utenteDAO.insert(utente);

        // Debug: stampa il risultato dell'operazione di creazione dell'utente
        System.out.println("Risultato della creazione dell'utente: " + result);

        // Se l'utente è stato creato con successo, invia l'utente alla pagina di login
        if (result) {
            System.out.println("Registrazione riuscita! Redirigo alla pagina di login.");
            // Redirige alla pagina di login dopo la registrazione
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/loginPage.jsp");
            dispatcher.forward(request, response);
        } else {
            // Se si è verificato un errore, invia l'utente alla pagina di registrazione con un errore
            System.out.println("Errore nella registrazione. Riprovo...");
            request.setAttribute("errorMessage", "Errore nella registrazione. Riprova!");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/registerPage.jsp");
            dispatcher.forward(request, response);
        }
    }
}
