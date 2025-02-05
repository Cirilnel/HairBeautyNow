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
        // Recupera i dati dalla sessione
        HttpSession session = request.getSession();

        // Recupera giorno come stringa dalla sessione e convertilo in LocalDate
        String giornoString = (String) session.getAttribute("giorno");
        if (giornoString == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Giorno non trovato nella sessione.");
            return;
        }

        // Converte la stringa in LocalDate
        LocalDate giorno = null;
        try {
            giorno = LocalDate.parse(giornoString, DateTimeFormatter.ISO_LOCAL_DATE); // Utilizza il formato ISO (yyyy-MM-dd)
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato data non valido.");
            return;
        }

        // Recupera orario dalla sessione (intervallo orario come '08:00-08:30')
        String orario = (String) session.getAttribute("orario");
        if (orario == null || orario.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Orario non trovato nella sessione.");
            return;
        }

        // Separiamo l'intervallo orario in due orari separati (inizio e fine)
        String[] orari = orario.split("-");
        if (orari.length != 2) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato orario non valido.");
            return;
        }

        // Analizziamo solo l'orario d'inizio
        LocalTime time = null;
        try {
            time = LocalTime.parse(orari[0], DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato orario non valido.");
            return;
        }

        // Recupera professionistaId dalla sessione
        String professionistaIdStr = (String) session.getAttribute("professionistaId");
        if (professionistaIdStr == null || professionistaIdStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID del professionista mancante.");
            return;
        }

        // Converte professionistaId in int
        int professionistaId = Integer.parseInt(professionistaIdStr);

        // Recupera nome del servizio prenotato
        String servizioName = (String) session.getAttribute("servizioPrenotato");
        if (servizioName == null || servizioName.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nome del servizio mancante.");
            return;
        }

        // Recupera il prezzo del servizio dal database
        double prezzo = 0.0;
        ServizioDAO servizioDAO = new ServizioDAO();  // Creare un DAO per i servizi
        prezzo = servizioDAO.getPrezzoByNome(servizioName);  // Recupera il prezzo per il servizio

        // Recupera l'entità utente dalla sessione
        UtenteAcquirente utente = (UtenteAcquirente) session.getAttribute("user");
        if (utente == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Utente non trovato nella sessione.");
            return;
        }

        // Costruzione del LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.of(giorno, time);

        // Creazione della prenotazione
        Prenotazione prenotazione = new Prenotazione(servizioName, professionistaId, localDateTime, utente.getUsername(), prezzo);

        // Salvataggio e altre operazioni
        PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();
        try {
            prenotazioneDAO.addPrenotazione(prenotazione);  // Salva la prenotazione nel database
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nel salvataggio della prenotazione.");
            return;
        }

        // Redirigi alla pagina di conferma
        response.sendRedirect("index.jsp");
    }
}
