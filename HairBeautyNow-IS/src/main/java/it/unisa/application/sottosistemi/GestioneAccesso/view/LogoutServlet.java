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

    private UtenteService utenteService;  // Aggiungi la dipendenza da UtenteService

    @Override
    public void init() throws ServletException {
        utenteService = new UtenteService(); // Inizializza l'oggetto UtenteService
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Invoca il metodo logout() dal servizio
        utenteService.logout(request);

        // Reindirizza alla pagina di login
        response.sendRedirect(request.getContextPath() + "/home");
    }
}
