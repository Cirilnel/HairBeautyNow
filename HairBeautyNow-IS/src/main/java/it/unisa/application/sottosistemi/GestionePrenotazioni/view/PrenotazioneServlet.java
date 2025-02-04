package it.unisa.application.sottosistemi.GestionePrenotazioni.view;

import it.unisa.application.model.dao.SedeDAO;
import it.unisa.application.model.entity.Sede;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
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

        // Recupera tutte le sedi dal database
        List<Sede> sedi = sedeDAO.getAllSedi();

        // Filtra le sedi in base alla città selezionata
        if (cittaSelezionata != null && !cittaSelezionata.isEmpty()) {
            sedi = sedi.stream()
                    .filter(sede -> sede.getCittà().equals(cittaSelezionata))
                    .collect(Collectors.toList());
        }

        // Estrai le città disponibili senza duplicati
        Set<String> cittaSet = new HashSet<>();
        for (Sede sede : sedi) {
            cittaSet.add(sede.getCittà());
        }
        List<String> cittaDisponibili = List.copyOf(cittaSet);

        // Imposta gli attributi per la JSP
        request.setAttribute("cittaDisponibili", cittaDisponibili);
        request.setAttribute("saloni", sedi);

        request.getRequestDispatcher("/WEB-INF/jsp/saloni.jsp").forward(request, response);
    }
}

