package it.unisa.application.sottosistemi.GestionePrenotazioni.view;

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

    private final PrenotazioneService prenotazioneService = new PrenotazioneService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servizioSelezionato = request.getParameter("servizio");
        String cittaSelezionata = request.getParameter("citta");
        HttpSession session = request.getSession();
        session.setAttribute("servizioPrenotato", servizioSelezionato);

        UtenteAcquirente user = (UtenteAcquirente) session.getAttribute("user");
        String cittaUtente = prenotazioneService.getCittaUtente(user);

        if (cittaSelezionata == null || cittaSelezionata.isEmpty()) {
            cittaSelezionata = cittaUtente;
        }

        // Ottieni tutte le sedi e filtra in base alla città
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

        request.setAttribute("cittaDisponibili", cittaDisponibili);
        request.setAttribute("saloni", sedi);
        request.setAttribute("messaggio", messaggio);
        request.setAttribute("cittaSelezionata", cittaSelezionata);

        request.getRequestDispatcher("/WEB-INF/jsp/saloni.jsp").forward(request, response);
    }
}
