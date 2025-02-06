package it.unisa.application.sottosistemi.GestioneSede.view;

import it.unisa.application.model.dao.FasciaOrariaDAO;
import it.unisa.application.model.dao.PrenotazioneDAO;
import it.unisa.application.model.entity.FasciaOraria;
import it.unisa.application.model.entity.Prenotazione;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/rimuoviPrenotazione")
public class RimuoviPrenotazioneServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String prenotazioneIdParam = request.getParameter("prenotazioneId");

        if (prenotazioneIdParam != null) {
            try {
                int prenotazioneId = Integer.parseInt(prenotazioneIdParam);

                PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();

                // Rimuovi la prenotazione dal database
                Prenotazione prenotazione = prenotazioneDAO.getPrenotazioneById(prenotazioneId);
                boolean successo = prenotazioneDAO.rimuoviPrenotazione(prenotazioneId);

                if (successo) {
                    System.out.println("Prenotazione rimossa con successo.");

                    // Ottieni la fascia oraria associata alla prenotazione
                    LocalDate giorno = prenotazione.getData().toLocalDate();
                    String fascia = prenotazione.getData().toLocalTime().toString(); // Potresti dover adattare questa parte se necessario
                    System.out.println("Giorno della prenotazione: " + giorno);
                    System.out.println("Fascia oraria della prenotazione: " + fascia);

                    // Recupera la fascia oraria corrispondente al professionista e al giorno
                    FasciaOrariaDAO fasciaOrariaDAO = new FasciaOrariaDAO();
                    FasciaOraria fasciaOraria = fasciaOrariaDAO.getFasciaByProfessionistaAndGiorno(prenotazione.getProfessionistaId(), giorno, fascia);

                    if (fasciaOraria != null) {
                        // Imposta la fascia oraria come disponibile (true)
                        fasciaOraria.setDisponibile(true);
                        boolean aggiornamentoFascia = fasciaOrariaDAO.aggiornaFasciaOraria(fasciaOraria);

                        if (aggiornamentoFascia) {
                            System.out.println("Fascia oraria aggiornata con successo.");
                            response.setStatus(HttpServletResponse.SC_OK);
                        } else {
                            System.out.println("Errore durante l'aggiornamento della fascia oraria.");
                            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        }
                    } else {
                        System.out.println("Fascia oraria non trovata per la prenotazione.");
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                } else {
                    System.out.println("Errore durante la rimozione della prenotazione.");
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } catch (NumberFormatException e) {
                System.out.println("Errore nel formato dell'ID della prenotazione: " + prenotazioneIdParam);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } catch (SQLException e) {
                System.out.println("Errore SQL durante la rimozione della prenotazione o aggiornamento fascia oraria: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            System.out.println("ID della prenotazione non fornito.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}

