package it.unisa.application.sottosistemi.GestioneSede.view;

import it.unisa.application.model.dao.FasciaOrariaDAO;
import it.unisa.application.model.dao.PrenotazioneDAO;
import it.unisa.application.model.dao.ProfessionistaDAO;
import it.unisa.application.model.dao.SedeDAO;
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

    // Creazione dell'oggetto PrenotazioneService con tutti i DAO
    private PrenotazioneService gestionePrenotazioneService = new PrenotazioneService(
            new PrenotazioneDAO(),
            new ProfessionistaDAO(),
            new FasciaOrariaDAO(),
            new SedeDAO()
    );

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteGestoreSede utente = (UtenteGestoreSede) session.getAttribute("user");

        if (utente == null) {
            response.sendRedirect("/app/loginPage");  // Aggiorna in base alla tua configurazione
            return;
        }

        int sedeId = utente.getSedeID();

        try {
            // Usa 'gestionePrenotazioneService' con il costruttore corretto
            List<Prenotazione> prenotazioniAttive = gestionePrenotazioneService.getPrenotazioniAttive(sedeId);
            request.setAttribute("prenotazioniAttive", prenotazioniAttive);
            request.getRequestDispatcher("/WEB-INF/jsp/prenotazioniAttive.jsp").forward(request, response);
        } catch (SQLException e) {
            // Gestisci l'errore con un messaggio appropriato
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Errore durante il recupero delle prenotazioni: " + e.getMessage());
        }
    }
}
