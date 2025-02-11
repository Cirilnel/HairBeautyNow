package it.unisa.application.sottosistemi.GestioneSede.view;

import it.unisa.application.model.entity.Prenotazione;
import it.unisa.application.model.entity.UtenteGestoreSede;
import it.unisa.application.sottosistemi.GestionePrenotazioni.service.PrenotazioneService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/prenotazioniAttive")
public class PrenotazioniAttiveServlet extends HttpServlet {

    private PrenotazioneService gestionePrenotazioniService = new PrenotazioneService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteGestoreSede utente = (UtenteGestoreSede) session.getAttribute("user");

        if (utente == null) {
            response.sendRedirect("/app/loginPage");  // Updated to match your test expectation
            return;
        }

        int sedeId = utente.getSedeID();

        try {
            List<Prenotazione> prenotazioniAttive = gestionePrenotazioniService.getPrenotazioniAttive(sedeId);
            request.setAttribute("prenotazioniAttive", prenotazioniAttive);
            request.getRequestDispatcher("/WEB-INF/jsp/prenotazioniAttive.jsp").forward(request, response);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Errore durante il recupero delle prenotazioni");
        }
    }

}
