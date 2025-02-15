package it.unisa.application.sottosistemi.GestionePrenotazioni.view;

import it.unisa.application.model.dao.*;
import it.unisa.application.model.entity.MetodoDiPagamento;
import it.unisa.application.sottosistemi.GestionePrenotazioni.service.FasciaOrariaService;
import it.unisa.application.sottosistemi.GestionePrenotazioni.service.PrenotazioneService;
import it.unisa.application.model.entity.Prenotazione;
import it.unisa.application.model.entity.FasciaOraria;
import it.unisa.application.model.entity.UtenteAcquirente;
import it.unisa.application.sottosistemi.GestioneServizi.service.ServizioService;
import it.unisa.application.sottosistemi.utilities.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/completaPrenotazione")
public class CompletaPrenotazioneServlet extends HttpServlet {
    private final PrenotazioneService prenotazioneService = new PrenotazioneService();
    private final FasciaOrariaService fasciaOrariaService = new FasciaOrariaService();
    private final ServizioService servizioService = new ServizioService();
    private final MetodoDiPagamentoDAO metodoDiPagamentoDAO = new MetodoDiPagamentoDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        try {
            // Verifica dati in sessione
            String giornoString = (String) session.getAttribute("giorno");
            if (giornoString == null) throw new IllegalArgumentException("Giorno non selezionato.");
            LocalDate giorno = LocalDate.parse(giornoString, DateTimeFormatter.ISO_LOCAL_DATE);

            String orario = (String) session.getAttribute("orario");
            if (orario == null) throw new IllegalArgumentException("Orario non selezionato.");
            String[] orari = orario.split("-");
            LocalTime time = LocalTime.parse(orari[0], DateTimeFormatter.ofPattern("HH:mm"));

            int professionistaId = Integer.parseInt((String) session.getAttribute("professionistaId"));
            String servizioName = (String) session.getAttribute("servizioPrenotato");

            // Recupero del prezzo del servizio
            double prezzo = servizioService.getPrezzoByNome(servizioName);

            // Recupera l'utente dalla sessione
            UtenteAcquirente utente = (UtenteAcquirente) session.getAttribute("user");
            if (utente == null) throw new IllegalArgumentException("Utente non loggato.");

            // Metodo di pagamento
            String metodoPagamento = request.getParameter("metodoPagamento");
            if (metodoPagamento == null) throw new IllegalArgumentException("Metodo di pagamento non selezionato.");

            String numeroCarta = request.getParameter("numeroCarta");
            String cvv = request.getParameter("cvv");
            String scadenza = request.getParameter("scadenza");
            String indirizzo = request.getParameter("indirizzo");

            MetodoDiPagamento metodo = metodoDiPagamentoDAO.getMetodoDiPagamentoByUsername(utente.getUsername());

            if (metodo != null) {
                // Metodo di pagamento esistente, aggiorniamo i dati se necessario
                if ("paypal".equals(metodoPagamento)) {
                    String email = request.getParameter("emailPaypal");
                    metodo.setEmail(email);
                    metodo.setIndirizzo(indirizzo);
                } else {
                    if (scadenza == null || scadenza.isEmpty()) throw new IllegalArgumentException("Data di scadenza mancante.");
                    String[] scadenzaArray = scadenza.split("/");
                    LocalDate dataScadenza = LocalDate.of(2000 + Integer.parseInt(scadenzaArray[1]), Integer.parseInt(scadenzaArray[0]), 1);
                    metodo.setnCarta(numeroCarta);
                    metodo.setDataScadenza(dataScadenza);
                    metodo.setNomeIntestatario(utente.getNome() + " " + utente.getCognome());
                    metodo.setCvv(Integer.parseInt(cvv));
                    metodo.setIndirizzo(indirizzo);
                    metodo.setMetodoPagamento(metodoPagamento);
                }
                metodoDiPagamentoDAO.updateMetodoDiPagamento(metodo);
            } else {
                // Creiamo un nuovo metodo di pagamento
                if ("paypal".equals(metodoPagamento)) {
                    String email = request.getParameter("emailPaypal");
                    metodo = new MetodoDiPagamento(null, null, utente.getNome() + " " + utente.getCognome(), indirizzo, 0, utente.getUsername(), metodoPagamento, email);
                } else {
                    if (scadenza == null || scadenza.isEmpty()) throw new IllegalArgumentException("Data di scadenza mancante.");
                    String[] scadenzaArray = scadenza.split("/");
                    LocalDate dataScadenza = LocalDate.of(2000 + Integer.parseInt(scadenzaArray[1]), Integer.parseInt(scadenzaArray[0]), 1);
                    metodo = new MetodoDiPagamento(numeroCarta, dataScadenza, utente.getNome() + " " + utente.getCognome(), indirizzo, Integer.parseInt(cvv), utente.getUsername(), metodoPagamento, null);
                }
                metodoDiPagamentoDAO.addMetodoDiPagamento(metodo);
            }

            // Esegui il pagamento
            try {
                PagamentoStrategy pagamentoStrategy = PagamentoFactory.getPagamentoStrategy(metodoPagamento);
                pagamentoStrategy.effettuaPagamento(metodo, prezzo);
            } catch (IllegalArgumentException e) {
                throw new ServletException("Errore nel pagamento: " + e.getMessage(), e);
            }

            // Creazione prenotazione solo dopo il pagamento
            LocalDateTime localDateTime = LocalDateTime.of(giorno, time);
            Prenotazione prenotazione = new Prenotazione(servizioName, professionistaId, localDateTime, utente.getUsername(), prezzo);
            prenotazioneService.addPrenotazione(prenotazione);

            // Aggiorna la fascia oraria
            FasciaOraria fasciaOraria = fasciaOrariaService.getFasciaOraria(professionistaId, giorno, orario);
            if (fasciaOraria != null) {
                fasciaOraria.setDisponibile(false);
                fasciaOrariaService.updateFasciaOraria(fasciaOraria);
            }

            response.sendRedirect("index.jsp");

        } catch (IllegalArgumentException e) {
            request.setAttribute("errore", "Errore nei dati inseriti: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/metodoPagamento.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("errore", "Errore di sistema: problema con il database.");
            request.getRequestDispatcher("/WEB-INF/jsp/metodoPagamento.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errore", "Errore imprevisto: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/metodoPagamento.jsp").forward(request, response);
        }
    }
}
