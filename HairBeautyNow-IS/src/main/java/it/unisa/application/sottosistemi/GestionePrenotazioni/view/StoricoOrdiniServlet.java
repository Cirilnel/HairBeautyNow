package it.unisa.application.sottosistemi.GestionePrenotazioni.view;

import it.unisa.application.model.entity.Prenotazione;
import it.unisa.application.model.entity.UtenteAcquirente;
import it.unisa.application.sottosistemi.GestionePrenotazioni.service.StoricoOrdiniService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/storicoOrdini")
public class StoricoOrdiniServlet extends HttpServlet {

    private final StoricoOrdiniService storicoOrdiniService = new StoricoOrdiniService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteAcquirente user = (UtenteAcquirente) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/loginPage");
            return;
        }

        String username = user.getUsername();
        if (username == null || username.isEmpty()) {
            request.setAttribute("errorMessage", "Errore: l'utente non ha uno username valido.");
            request.getRequestDispatcher("/WEB-INF/jsp/storicoOrdini.jsp").forward(request, response);
            return;
        }

        try {
            List<Prenotazione> prenotazioni = storicoOrdiniService.getPrenotazioniByUsername(username);
            if (prenotazioni.isEmpty()) {
                request.setAttribute("errorMessage", "Nessuna prenotazione trovata.");
            } else {
                for (Prenotazione p : prenotazioni) {
                    double prezzo = storicoOrdiniService.getPrezzoByServizio(p.getServizioName());
                    p.setPrezzo(prezzo);

                    String indirizzo = storicoOrdiniService.getIndirizzoBySedeId(p.getProfessionistaId());
                    request.setAttribute("indirizzo", indirizzo);
                }
                request.setAttribute("prenotazioni", prenotazioni);
            }
            request.getRequestDispatcher("/WEB-INF/jsp/storicoOrdini.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nel recupero dello storico prenotazioni");
        }
    }
}
