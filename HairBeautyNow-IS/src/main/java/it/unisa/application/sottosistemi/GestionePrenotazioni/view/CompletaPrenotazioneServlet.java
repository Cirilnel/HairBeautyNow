package it.unisa.application.sottosistemi.GestionePrenotazioni.view;

import it.unisa.application.model.dao.PrenotazioneDAO;
import it.unisa.application.model.dao.ProfessionistaDAO;
import it.unisa.application.model.dao.ServizioDAO;  // Aggiungi il DAO per Servizio
import it.unisa.application.model.entity.Prenotazione;
import it.unisa.application.model.entity.FasciaOraria;
import it.unisa.application.model.entity.UtenteAcquirente;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.sql.SQLException;

@WebServlet("/completaPrenotazione")
public class CompletaPrenotazioneServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        String giornoString = (String) session.getAttribute("giorno");
        if (giornoString == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Giorno non trovato nella sessione.");
            return;
        }

        LocalDate giorno;
        try {
            giorno = LocalDate.parse(giornoString, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato data non valido.");
            return;
        }

        String orario = (String) session.getAttribute("orario");
        if (orario == null || orario.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Orario non trovato nella sessione.");
            return;
        }

        String[] orari = orario.split("-");
        if (orari.length != 2) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato orario non valido.");
            return;
        }

        LocalTime time;
        try {
            time = LocalTime.parse(orari[0], DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato orario non valido.");
            return;
        }

        String professionistaIdStr = (String) session.getAttribute("professionistaId");
        if (professionistaIdStr == null || professionistaIdStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID del professionista mancante.");
            return;
        }

        int professionistaId = Integer.parseInt(professionistaIdStr);

        String servizioName = (String) session.getAttribute("servizioPrenotato");
        if (servizioName == null || servizioName.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nome del servizio mancante.");
            return;
        }

        ServizioDAO servizioDAO = new ServizioDAO();
        double prezzo = servizioDAO.getPrezzoByNome(servizioName);

        UtenteAcquirente utente = (UtenteAcquirente) session.getAttribute("user");
        if (utente == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Utente non trovato nella sessione.");
            return;
        }

        LocalDateTime localDateTime = LocalDateTime.of(giorno, time);
        Prenotazione prenotazione = new Prenotazione(servizioName, professionistaId, localDateTime, utente.getUsername(), prezzo);

        PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();
        ProfessionistaDAO professionistaDAO = new ProfessionistaDAO(); // Aggiunto DAO per la fascia oraria

        try {
            prenotazioneDAO.addPrenotazione(prenotazione);

            // Recupera la fascia oraria selezionata
            FasciaOraria fasciaOraria = professionistaDAO.getFasciaOraria(professionistaId, giorno, orario);
            if (fasciaOraria != null) {
                fasciaOraria.setDisponibile(false); // Imposta disponibile a false
                professionistaDAO.updateFasciaOraria(fasciaOraria); // Aggiorna nel database
            }

        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nel salvataggio della prenotazione.");
            return;
        }

        response.sendRedirect("index.jsp");
    }
}

