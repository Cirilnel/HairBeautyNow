package it.unisa.application.sottosistemi.GestionePrenotazioni.view;

import it.unisa.application.model.dao.MetodoDiPagamentoDAO;
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

            // Aggiungi il metodo di pagamento se non esiste già
            String metodoPagamento = request.getParameter("metodoPagamento");
            String numeroCarta = request.getParameter("numeroCarta");
            String cvv = request.getParameter("cvv");
            String scadenza = request.getParameter("scadenza");
            String indirizzo = request.getParameter("indirizzo");

            if (metodoPagamento != null) {
                PagamentoStrategy pagamentoStrategy = null;
                MetodoDiPagamento metodo = null;

                try {
                    // Controlla se esiste già un metodo di pagamento per l'utente
                    metodo = metodoDiPagamentoDAO.getMetodoDiPagamentoByUsername(utente.getUsername());

                    // Usa la PagamentoFactory per ottenere il giusto tipo di strategia
                    pagamentoStrategy = PagamentoFactory.getPagamentoStrategy(metodoPagamento);

                    // Se PayPal è selezionato
                    if ("paypal".equals(metodoPagamento)) {
                        String email = request.getParameter("emailPaypal");

                        // Verifica e aggiungi o aggiorna il metodo PayPal
                        if (metodo != null) {
                            metodo.setEmail(email);
                            metodo.setIndirizzo(indirizzo);
                            metodoDiPagamentoDAO.updateMetodoDiPagamento(metodo);
                        } else {
                            metodo = new MetodoDiPagamento(
                                    null,  // nCarta è null per PayPal
                                    null,  // dataScadenza non necessaria per PayPal
                                    utente.getNome() + " " + utente.getCognome(),
                                    indirizzo,
                                    0,  // cvv non necessario per PayPal
                                    utente.getUsername(),
                                    metodoPagamento,
                                    email
                            );
                            metodoDiPagamentoDAO.addMetodoDiPagamento(metodo);
                        }
                    } else { // Visa o Mastercard
                        if (scadenza != null && !scadenza.isEmpty()) {
                            String[] scadenzaArray = scadenza.split("/");
                            LocalDate dataScadenza = LocalDate.of(2000 + Integer.parseInt(scadenzaArray[1]), Integer.parseInt(scadenzaArray[0]), 1);

                            if (metodo != null) {
                                metodo.setnCarta(numeroCarta);
                                metodo.setDataScadenza(dataScadenza);
                                metodo.setNomeIntestatario(utente.getNome() + " " + utente.getCognome());
                                metodo.setCvv(Integer.parseInt(cvv));
                                metodo.setIndirizzo(indirizzo);
                                metodo.setMetodoPagamento(metodoPagamento);
                                metodoDiPagamentoDAO.updateMetodoDiPagamento(metodo);
                            } else {
                                metodo = new MetodoDiPagamento(
                                        numeroCarta,
                                        dataScadenza,
                                        utente.getNome() + " " + utente.getCognome(),
                                        indirizzo,
                                        Integer.parseInt(cvv),
                                        utente.getUsername(),
                                        metodoPagamento,
                                        null
                                );
                                metodoDiPagamentoDAO.addMetodoDiPagamento(metodo);
                            }
                        } else {
                            request.setAttribute("errore", "Data di scadenza mancante o non valida.");
                            request.getRequestDispatcher("/WEB-INF/jsp/metodoPagamento.jsp").forward(request, response);
                            return;
                        }
                    }

                    // Esegui il pagamento tramite la strategia scelta
                    pagamentoStrategy.effettuaPagamento(metodo, prezzo);

                } catch (IllegalArgumentException e) {
                    request.setAttribute("errore", e.getMessage());
                    request.getRequestDispatcher("/WEB-INF/jsp/metodoPagamento.jsp").forward(request, response);
                    return;
                }
            }

            // Solo dopo il pagamento, crea la prenotazione
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

        } catch (SQLException | NullPointerException | IllegalArgumentException e) {
            request.setAttribute("errore", "Errore durante la prenotazione: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/metodoPagamento.jsp").forward(request, response);
        }
    }
}

