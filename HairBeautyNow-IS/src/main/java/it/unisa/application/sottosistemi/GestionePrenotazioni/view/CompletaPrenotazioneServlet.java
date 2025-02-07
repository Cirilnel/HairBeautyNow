package it.unisa.application.sottosistemi.GestionePrenotazioni.view;

import it.unisa.application.sottosistemi.GestionePrenotazioni.service.FasciaOrariaService;
import it.unisa.application.sottosistemi.GestionePrenotazioni.service.PrenotazioneService;
import it.unisa.application.model.entity.Prenotazione;
import it.unisa.application.model.entity.FasciaOraria;
import it.unisa.application.model.entity.UtenteAcquirente;
import it.unisa.application.sottosistemi.GestioneServizi.service.ServizioService;
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
    private final PrenotazioneService prenotazioneService = new PrenotazioneService();
    private final FasciaOrariaService fasciaOrariaService = new FasciaOrariaService();
    private final ServizioService servizioService = new ServizioService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        try {
            // Ottieni i dati dalla sessione
            String giornoString = (String) session.getAttribute("giorno");
            LocalDate giorno = LocalDate.parse(giornoString, DateTimeFormatter.ISO_LOCAL_DATE);
            String orario = (String) session.getAttribute("orario");
            String[] orari = orario.split("-");
            LocalTime time = LocalTime.parse(orari[0], DateTimeFormatter.ofPattern("HH:mm"));
            int professionistaId = Integer.parseInt((String) session.getAttribute("professionistaId"));
            String servizioName = (String) session.getAttribute("servizioPrenotato");

            // Recupero del prezzo del servizio
            double prezzo = servizioService.getPrezzoByNome(servizioName);

            // Recupera l'utente dalla sessione
            UtenteAcquirente utente = (UtenteAcquirente) session.getAttribute("user");

            // Crea l'oggetto Prenotazione
            LocalDateTime localDateTime = LocalDateTime.of(giorno, time);
            Prenotazione prenotazione = new Prenotazione(servizioName, professionistaId, localDateTime, utente.getUsername(), prezzo);

            // Aggiungi la prenotazione tramite il service
            prenotazioneService.addPrenotazione(prenotazione);

            // Ottieni la fascia oraria dal service
            FasciaOraria fasciaOraria = fasciaOrariaService.getFasciaOraria(professionistaId, giorno, orario);

            // Se la fascia oraria esiste, aggiorna la sua disponibilità
            if (fasciaOraria != null) {
                fasciaOraria.setDisponibile(false);
                fasciaOrariaService.updateFasciaOraria(fasciaOraria);
            }

            // Redirect alla home
            response.sendRedirect("index.jsp");
        } catch (SQLException | NullPointerException | IllegalArgumentException e) {
            // Gestione degli errori
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Errore durante la prenotazione: " + e.getMessage());
        }
    }
}
