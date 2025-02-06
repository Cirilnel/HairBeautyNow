package it.unisa.application.sottosistemi.GestioneSede.view;

import it.unisa.application.model.dao.PrenotazioneDAO;
import it.unisa.application.model.dao.ProfessionistaDAO;
import it.unisa.application.model.dao.SedeDAO;
import it.unisa.application.model.entity.Prenotazione;
import it.unisa.application.model.entity.Professionista;
import it.unisa.application.model.entity.UtenteGestoreSede;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/prenotazioniAttive")
public class PrenotazioniAttiveServlet extends HttpServlet {

    private ProfessionistaDAO professionistaDAO = new ProfessionistaDAO();
    private PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recuperiamo l'utente loggato dalla sessione
        HttpSession session = request.getSession();
        UtenteGestoreSede utente = (UtenteGestoreSede) session.getAttribute("user");

        // Verifica se l'utente è loggato come Gestore Sede
        if (utente == null) {
            response.sendRedirect("erroreUtente");  // Reindirizza a pagina di errore
            return;
        }

        // Otteniamo l'ID della sede dal gestore
        Integer sedeId = utente.getSedeID();

        // Recuperiamo tutti i professionisti associati alla sede
        List<Professionista> professionisti = professionistaDAO.getProfessionistiBySede(sedeId);

        // Recuperiamo tutte le prenotazioni attive per i professionisti della sede
        List<Prenotazione> prenotazioniAttive = null;
        try {
            prenotazioniAttive = prenotazioneDAO.getPrenotazioniByProfessionisti(professionisti);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Aggiungiamo le prenotazioni attive alla richiesta
        request.setAttribute("prenotazioniAttive", prenotazioniAttive);

        // Salva gli ID delle prenotazioni in sessione per usarli nelle operazioni successive
        session.setAttribute("prenotazioneIds", prenotazioniAttive.stream().map(Prenotazione::getId).collect(Collectors.toList()));

        // Forward alla pagina JSP per visualizzare le prenotazioni
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/prenotazioniAttive.jsp");
        dispatcher.forward(request, response);
    }
}




