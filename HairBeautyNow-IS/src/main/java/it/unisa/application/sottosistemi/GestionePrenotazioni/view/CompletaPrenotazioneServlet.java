package it.unisa.application.sottosistemi.GestionePrenotazioni.view;

import it.unisa.application.model.dao.MetodoDiPagamentoDAO;
import it.unisa.application.model.entity.MetodoDiPagamento;
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
            String numeroCarta = request.getParameter("numeroCarta");
            String cvv = request.getParameter("cvv");
            String scadenza = request.getParameter("scadenza");
            String indirizzo = request.getParameter("indirizzo");
            String metodoPagamento = request.getParameter("metodoPagamento");

            if (numeroCarta != null && !numeroCarta.isEmpty() && metodoPagamento != null) {
                MetodoDiPagamento metodo = new MetodoDiPagamento(
                        numeroCarta,  // nCarta
                        java.sql.Date.valueOf("20" + scadenza.split("/")[1] + "-" + scadenza.split("/")[0] + "-01"),  // dataScadenza (formatto MM/YY in un oggetto java.sql.Date)
                        utente.getNome() + " " + utente.getCognome(),  // nomeIntestatario
                        indirizzo,  // indirizzo
                        Integer.parseInt(cvv),  // cvv
                        utente.getUsername(),  // username
                        metodoPagamento  // metodoPagamento (Visa, MasterCard, PayPal)
                );

                // Aggiungi il metodo di pagamento al database
                metodoDiPagamentoDAO.addMetodoDiPagamento(metodo);
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

