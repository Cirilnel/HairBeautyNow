package it.unisa.application.sottosistemi.GestionePrenotazioni.view;

import it.unisa.application.model.dao.PrenotazioneDAO;
import it.unisa.application.model.dao.ProfessionistaDAO;
import it.unisa.application.model.entity.Prenotazione;
import it.unisa.application.model.entity.FasciaOraria;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/completaPrenotazione")
public class CompletaPrenotazioneServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recupera i dati dalla sessione
        HttpSession session = request.getSession();
        String giorno = (String) session.getAttribute("giorno");
        String orario = (String) session.getAttribute("orario");
        String professionistaIdStr = (String) session.getAttribute("professionistaId");
        String servizioName = (String) session.getAttribute("servizioPrenotato"); // Recupera il servizio dalla sessione

        // Verifica che professionistaId non sia null o vuoto e convertilo in int
        if (professionistaIdStr == null || professionistaIdStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID del professionista mancante.");
            return;
        }

        int professionistaId = 0;
        try {
            professionistaId = Integer.parseInt(professionistaIdStr); // Converte la stringa in int
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID del professionista non valido.");
            return;
        }

        // Verifica che il servizioName non sia null o vuoto
        if (servizioName == null || servizioName.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nome del servizio mancante.");
            return;
        }

        // Recupera i dati aggiuntivi del pagamento
        String metodoPagamento = request.getParameter("metodoPagamento");
        String numeroCarta = request.getParameter("numeroCarta");
        String cvv = request.getParameter("cvv");
        String scadenza = request.getParameter("scadenza");
        String indirizzo = request.getParameter("indirizzo");

        // Verifica che tutti i dati siano validi (esempio: metodo di pagamento)
        if (metodoPagamento == null || metodoPagamento.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Metodo di pagamento obbligatorio.");
            return;
        }

        // Creazione della prenotazione
        // Verifica che la data sia corretta (giorno e orario)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dataPrenotazione = null;
        try {
            dataPrenotazione = sdf.parse(giorno + " " + orario); // Combina giorno e orario in un formato Date
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato data/ora non valido.");
            return;
        }

        // Crea la prenotazione
        Prenotazione prenotazione = new Prenotazione(0, servizioName, professionistaId, dataPrenotazione);  // ID autoincrementato

        // Salva la prenotazione nel database
        PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();
        try {
            prenotazioneDAO.addPrenotazione(prenotazione);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante la creazione della prenotazione.");
            return;
        }

        // Aggiorna la disponibilità della fascia oraria
        ProfessionistaDAO fasciaOrariaDAO = new ProfessionistaDAO();
        try {
            FasciaOraria fasciaOraria = fasciaOrariaDAO.getFasciaOraria(professionistaId, giorno, orario);
            if (fasciaOraria != null) {
                fasciaOraria.setDisponibile(false);  // Rendi la fascia oraria non disponibile
                fasciaOrariaDAO.updateFasciaOraria(fasciaOraria);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante l'aggiornamento della fascia oraria.");
            return;
        }

        // Fai il forward alla home page (index.jsp)
        response.sendRedirect("index.jsp");
    }
}




