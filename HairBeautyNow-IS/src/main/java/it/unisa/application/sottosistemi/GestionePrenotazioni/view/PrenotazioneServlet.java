package it.unisa.application.sottosistemi.GestionePrenotazioni.view;

import it.unisa.application.model.dao.SedeDAO;
import it.unisa.application.model.entity.Sede;
import it.unisa.application.model.entity.UtenteAcquirente;
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
import java.util.stream.Collectors;
@WebServlet("/prenota")
public class PrenotazioneServlet extends HttpServlet {
    private final SedeDAO sedeDAO = new SedeDAO(); // Inizializzazione del DAO

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servizioSelezionato = request.getParameter("servizio");
        String cittaSelezionata = request.getParameter("citta");  // Aggiunto per ottenere la città selezionata
        HttpSession session = request.getSession();
        session.setAttribute("servizioPrenotato", servizioSelezionato);

        // Recupero dell'oggetto "user" dalla sessione per ottenere la città di residenza
        UtenteAcquirente user = (UtenteAcquirente) session.getAttribute("user");  // Assumendo che l'oggetto utente sia salvato come "user"
        String cittaUtente = user != null ? user.getCitta() : null;  // Recupera la città dell'utente (se esiste)

        // Se la città non è stata selezionata nel form, si usa la città dell'utente (residenza)
        if (cittaSelezionata == null || cittaSelezionata.isEmpty()) {
            cittaSelezionata = cittaUtente;
        }

        // Recupera tutte le sedi dal database
        List<Sede> sedi = sedeDAO.getAllSedi();

        // Estrai le città disponibili senza duplicati
        Set<String> cittaSet = new HashSet<>();
        for (Sede sede : sedi) {
            cittaSet.add(sede.getCittà());
        }

        // Usa una lista mutabile per aggiungere "Nessuna selezione"
        List<String> cittaDisponibili = new ArrayList<>(cittaSet);

        // Aggiungi "Nessuna selezione" come prima opzione nel menu delle città
        cittaDisponibili.add(0, "Nessuna selezione");

        // Filtra le sedi in base alla città selezionata
        if (cittaSelezionata != null && !cittaSelezionata.equals("Nessuna selezione") && !cittaSelezionata.isEmpty()) {
            final String cittaFinale = cittaSelezionata; // Variabile finale per l'uso nella lambda
            sedi = sedi.stream()
                    .filter(sede -> sede.getCittà() != null && sede.getCittà().equalsIgnoreCase(cittaFinale)) // Uso della variabile finale
                    .collect(Collectors.toList());
        }

        // Se non ci sono sedi per la città selezionata e la città non è "Nessuna selezione", impostiamo un messaggio.
        String messaggio = null;
        if (cittaSelezionata != null && !cittaSelezionata.equals("Nessuna selezione") && sedi.isEmpty()) {
            messaggio = "Non ci sono saloni nella città di residenza.";
            cittaSelezionata = "Nessuna selezione"; // Impostiamo "Nessuna selezione" nel caso in cui non ci siano saloni.
            // Mostriamo comunque tutti i saloni disponibili
            sedi = sedeDAO.getAllSedi();  // Ricarichiamo tutti i saloni
        }

        // Imposta gli attributi per la JSP
        request.setAttribute("cittaDisponibili", cittaDisponibili);
        request.setAttribute("saloni", sedi);
        request.setAttribute("messaggio", messaggio);
        request.setAttribute("cittaSelezionata", cittaSelezionata);

        request.getRequestDispatcher("/WEB-INF/jsp/saloni.jsp").forward(request, response);
    }
}






