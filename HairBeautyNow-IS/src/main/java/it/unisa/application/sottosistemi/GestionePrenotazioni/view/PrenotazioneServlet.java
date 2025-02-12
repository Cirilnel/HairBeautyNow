package it.unisa.application.sottosistemi.GestionePrenotazioni.view;

import it.unisa.application.model.dao.SedeDAO;
import it.unisa.application.model.dao.PrenotazioneDAO;
import it.unisa.application.model.dao.ProfessionistaDAO;
import it.unisa.application.model.dao.FasciaOrariaDAO;
import it.unisa.application.model.entity.Sede;
import it.unisa.application.model.entity.UtenteAcquirente;
import it.unisa.application.sottosistemi.GestionePrenotazioni.service.PrenotazioneService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebServlet("/prenota")
public class PrenotazioneServlet extends HttpServlet {

    // Creazione dell'oggetto PrenotazioneService con tutti i DAO
    private final PrenotazioneService prenotazioneService = new PrenotazioneService(
            new PrenotazioneDAO(),
            new ProfessionistaDAO(),
            new FasciaOrariaDAO(),
            new SedeDAO()
    );

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servizioSelezionato = request.getParameter("servizio");
        String cittaSelezionata = request.getParameter("citta");
        HttpSession session = request.getSession();
        session.setAttribute("servizioPrenotato", servizioSelezionato);

        // Verifica che l'utente sia presente nella sessione
        UtenteAcquirente user = (UtenteAcquirente) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("/app/loginPage"); // Redirect se l'utente non è loggato
            return;
        }

        // Ottieni la città dell'utente
        String cittaUtente = prenotazioneService.getCittaUtente(user);

        // Se non viene selezionata una città, prendi quella dell'utente
        if (cittaSelezionata == null || cittaSelezionata.isEmpty()) {
            cittaSelezionata = cittaUtente;
        }

        // Ottieni tutte le sedi per la città selezionata
        List<Sede> sedi = prenotazioneService.getSediByCitta(cittaSelezionata);

        // Ottieni tutte le città disponibili senza duplicati
        Set<String> cittaSet = prenotazioneService.getCittaDisponibili(prenotazioneService.getAllSedi());

        // Aggiungi "Nessuna selezione" come prima opzione nel menu delle città
        List<String> cittaDisponibili = new ArrayList<>(cittaSet);
        cittaDisponibili.add(0, "Nessuna selezione");

        // Se non ci sono sedi per la città selezionata
        String messaggio = null;
        if (cittaSelezionata != null && !cittaSelezionata.equals("Nessuna selezione") && sedi.isEmpty()) {
            messaggio = "Non ci sono saloni nella città di residenza.";
            cittaSelezionata = "Nessuna selezione";
            sedi = prenotazioneService.getSediByCitta(cittaSelezionata);
        }

        // Imposta gli attributi per la JSP
        request.setAttribute("cittaDisponibili", cittaDisponibili);
        request.setAttribute("saloni", sedi);
        request.setAttribute("messaggio", messaggio);
        request.setAttribute("cittaSelezionata", cittaSelezionata);

        // Forward alla pagina JSP
        request.getRequestDispatcher("/WEB-INF/jsp/saloni.jsp").forward(request, response);
    }
}
