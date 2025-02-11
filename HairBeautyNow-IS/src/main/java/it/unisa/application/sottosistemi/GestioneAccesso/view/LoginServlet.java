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

@WebServlet("/loginPage")
public class LoginServlet extends HttpServlet {

    private UtenteService utenteService;

    // Costruttore per iniettare il servizio (usato nei test)
    public LoginServlet(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    // Costruttore di default, usato nel codice di produzione
    public LoginServlet() {
        this.utenteService = new UtenteService(); // Servizio reale in produzione
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("LoginServlet");
        request.getRequestDispatcher("/WEB-INF/jsp/loginPage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username"); // Cambiato da 'email' a 'username'
        String password = request.getParameter("password");

        // Usa l'oggetto utenteService (che può essere un mock nei test)
        Object utente = utenteService.login(username, password);

        if (utente != null) {
            // Se l'utente è autenticato correttamente, mettilo nella sessione
            request.getSession().setAttribute("user", utente);

            // Controllo del tipo di utente per il reindirizzamento
            if (utente instanceof UtenteAcquirente) {
                response.sendRedirect(request.getContextPath() + "/home");
            } else if (utente instanceof UtenteGestoreSede) {
                response.sendRedirect(request.getContextPath() + "/homeSede");
            } else if (utente instanceof UtenteGestoreCatena) {
                response.sendRedirect(request.getContextPath() + "/homeCatena");
            } else {
                // Se il tipo di utente non è riconosciuto, mostra un errore
                request.setAttribute("errorMessage", "Tipo di utente non riconosciuto!");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/loginPage.jsp");
                dispatcher.forward(request, response);
            }
        } else {
            // Se il login fallisce (utente non trovato), mostra un messaggio di errore
            request.setAttribute("errorMessage", "Username o password errati!");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/loginPage.jsp");
            dispatcher.forward(request, response);
        }
    }
}
