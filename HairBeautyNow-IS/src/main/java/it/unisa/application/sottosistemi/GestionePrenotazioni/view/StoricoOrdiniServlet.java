package it.unisa.application.sottosistemi.GestionePrenotazioni.view;

import it.unisa.application.model.dao.PrenotazioneDAO;
import it.unisa.application.model.dao.ProfessionistaDAO;
import it.unisa.application.model.entity.Prenotazione;
import it.unisa.application.model.entity.UtenteAcquirente;
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

        PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();
        ProfessionistaDAO professionistaDAO = new ProfessionistaDAO();

        try {
            List<Prenotazione> prenotazioni = prenotazioneDAO.getPrenotazioniByUsername(username);
            if (prenotazioni.isEmpty()) {
                request.setAttribute("errorMessage", "Nessuna prenotazione trovata.");
            } else {
                // Recuperiamo il prezzo e l'indirizzo per ogni prenotazione
                for (Prenotazione p : prenotazioni) {
                    // Recuperiamo il prezzo del servizio
                    double prezzo = professionistaDAO.getPrezzoByServizio(p.getServizioName());
                    p.setPrezzo(prezzo);

                    // Recuperiamo l'indirizzo del professionista
                    String indirizzo = professionistaDAO.getIndirizzoBySedeId(p.getProfessionistaId());

                    // Aggiungiamo l'indirizzo e il prezzo come attributi
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


