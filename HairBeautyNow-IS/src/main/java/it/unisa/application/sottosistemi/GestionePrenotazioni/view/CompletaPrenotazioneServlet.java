package it.unisa.application.sottosistemi.GestionePrenotazioni.view;

import it.unisa.application.model.dao.MetodoDiPagamentoDAO;
import it.unisa.application.model.entity.MetodoDiPagamento;
import it.unisa.application.sottosistemi.GestionePrenotazioni.service.FasciaOrariaService;
import it.unisa.application.sottosistemi.GestionePrenotazioni.service.PrenotazioneService;
import it.unisa.application.model.entity.Prenotazione;
import it.unisa.application.model.entity.FasciaOraria;
import it.unisa.application.model.entity.UtenteAcquirente;
import it.unisa.application.sottosistemi.GestioneServizi.service.ServizioService;
import it.unisa.application.sottosistemi.utilities.MasterCardStrategy;
import it.unisa.application.sottosistemi.utilities.PagamentoStrategy;
import it.unisa.application.sottosistemi.utilities.PayPalStrategy;
import it.unisa.application.sottosistemi.utilities.VisaStrategy;
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
    private final MetodoDiPagamentoDAO metodoDiPagamentoDAO = new MetodoDiPagamentoDAO();

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

            // Aggiungi il metodo di pagamento se non esiste già
            String metodoPagamento = request.getParameter("metodoPagamento");
            String numeroCarta = request.getParameter("numeroCarta");
            String cvv = request.getParameter("cvv");
            String scadenza = request.getParameter("scadenza");
            String indirizzo = request.getParameter("indirizzo");

            if (metodoPagamento != null) {
                // Debug: Verifica i dati ricevuti
                System.out.println("Metodo di pagamento selezionato: " + metodoPagamento);
                System.out.println("Numero carta: " + numeroCarta);
                System.out.println("CVV: " + cvv);
                System.out.println("Data di scadenza: " + scadenza);
                System.out.println("Indirizzo di fatturazione: " + indirizzo);

                // Se il metodo di pagamento è PayPal
                if ("paypal".equals(metodoPagamento)) {
                    String email = request.getParameter("emailPaypal");
                    System.out.println("Email PayPal: " + email);  // Debug: Mostra l'email PayPal ricevuta

                    // Crea il metodo di pagamento PayPal (senza carta, scadenza o CVV)
                    MetodoDiPagamento metodo = new MetodoDiPagamento(
                            null,  // nCarta è null per PayPal
                            null,  // dataScadenza non necessaria per PayPal
                            utente.getNome() + " " + utente.getCognome(),
                            indirizzo,
                            0,  // cvv non necessario per PayPal
                            utente.getUsername(),
                            metodoPagamento,
                            email
                    );

                    // Aggiungi il metodo di pagamento al database
                    metodoDiPagamentoDAO.addMetodoDiPagamento(metodo);
                    System.out.println("Metodo di pagamento PayPal aggiunto al database");

                    // Scegli la strategia PayPal
                    PagamentoStrategy pagamentoStrategy = new PayPalStrategy();
                    pagamentoStrategy.effettuaPagamento(metodo, prezzo);
                    System.out.println("Pagamento PayPal effettuato con successo.");
                } else {
                    // Elaborazione della data di scadenza per Visa, MasterCard, ecc.
                    if (scadenza != null && !scadenza.isEmpty()) {
                        String[] scadenzaArray = scadenza.split("/");
                        LocalDate dataScadenza = LocalDate.of(2000 + Integer.parseInt(scadenzaArray[1]), Integer.parseInt(scadenzaArray[0]), 1);
                        System.out.println("Data di scadenza elaborata: " + dataScadenza);

                        // Crea il metodo di pagamento per carta di credito
                        MetodoDiPagamento metodo = new MetodoDiPagamento(
                                numeroCarta,  // nCarta
                                java.sql.Date.valueOf(dataScadenza),  // dataScadenza
                                utente.getNome() + " " + utente.getCognome(),  // nomeIntestatario
                                indirizzo,  // indirizzo
                                Integer.parseInt(cvv),  // cvv
                                utente.getUsername(),  // username
                                metodoPagamento,  // metodoPagamento
                                null  // PayPal ha email, carte no
                        );

                        // Aggiungi il metodo di pagamento al database
                        metodoDiPagamentoDAO.addMetodoDiPagamento(metodo);
                        System.out.println("Metodo di pagamento aggiunto al database");

                        // Seleziona la strategia di pagamento
                        PagamentoStrategy pagamentoStrategy = null;
                        if ("visa".equals(metodoPagamento)) {
                            pagamentoStrategy = new VisaStrategy();
                            System.out.println("Strategia VISA selezionata.");
                        } else if ("mastercard".equals(metodoPagamento)) {
                            pagamentoStrategy = new MasterCardStrategy();
                            System.out.println("Strategia Mastercard selezionata.");
                        }

                        // Verifica se la strategia di pagamento è stata selezionata correttamente
                        if (pagamentoStrategy != null) {
                            pagamentoStrategy.effettuaPagamento(metodo, prezzo);
                            System.out.println("Pagamento effettuato con successo.");
                        }
                    } else {
                        // Gestione dell'errore se la data di scadenza non è valida
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Data di scadenza mancante o non valida.");
                        return;
                    }
                }
            }

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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        try {
            // Recupera l'utente dalla sessione
            UtenteAcquirente utente = (UtenteAcquirente) session.getAttribute("user");

            // Verifica se l'utente ha già un metodo di pagamento
            MetodoDiPagamento metodoDiPagamento = metodoDiPagamentoDAO.getMetodoDiPagamentoByUsername(utente.getUsername());

            if (metodoDiPagamento != null) {
                // Se esiste, passalo alla pagina JSP per precompilare il form
                request.setAttribute("metodoDiPagamento", metodoDiPagamento);
            }

            // Invia la richiesta alla pagina metodoPagamento.jsp
            request.getRequestDispatcher("/metodoPagamento.jsp").forward(request, response);

        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Errore durante il recupero del metodo di pagamento: " + e.getMessage());
        }
    }
}
