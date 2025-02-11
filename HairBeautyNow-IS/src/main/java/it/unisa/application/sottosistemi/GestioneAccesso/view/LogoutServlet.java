package it.unisa.application.sottosistemi.GestioneAccesso.view;

import it.unisa.application.sottosistemi.GestioneAccesso.service.UtenteService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {


    private UtenteService utenteService;

    // Costruttore vuoto per il testing
    public LogoutServlet() {
        // Costruttore vuoto
    }

    @Override
    public void init() throws ServletException {
        // Inizializza l'oggetto UtenteService solo se non è stato iniettato
        if (utenteService == null) {
            utenteService = new UtenteService(); // Inizializza l'oggetto UtenteService se non è iniettato
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Verifica se l'utente è autenticato
        if (request.getSession().getAttribute("user") != null) {
            // Se l'utente è autenticato, invoca il metodo logout() dal servizio
            utenteService.logout(request);
        }

        // Invalida la sessione
        request.getSession().invalidate();

        // Reindirizza alla pagina di login
        response.sendRedirect(request.getContextPath() + "/home");
    }

    // Metodo per iniettare il servizio nei test
    public void setUtenteService(UtenteService utenteService) {
        this.utenteService = utenteService;
    }
}
