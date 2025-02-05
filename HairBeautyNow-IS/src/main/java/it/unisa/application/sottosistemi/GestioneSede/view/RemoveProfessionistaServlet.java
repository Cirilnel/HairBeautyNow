package it.unisa.application.sottosistemi.GestioneSede.view;

import it.unisa.application.model.dao.ProfessionistaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/removeProfessionista")
public class RemoveProfessionistaServlet extends HttpServlet {
    private ProfessionistaDAO professionistaDAO;

    @Override
    public void init() throws ServletException {
        professionistaDAO = new ProfessionistaDAO();  // Inizializzazione dell'oggetto DAO
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String professionistaIdStr = request.getParameter("professionistaId");

        if (professionistaIdStr != null && !professionistaIdStr.isEmpty()) {
            try {
                int professionistaId = Integer.parseInt(professionistaIdStr);

                // Controlla se ci sono prenotazioni per il professionista
                boolean hasPrenotazioni = professionistaDAO.hasPrenotazioni(professionistaId);

                if (hasPrenotazioni) {
                    // Se ci sono prenotazioni, non si può procedere con l'eliminazione
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("Prenotazioni esistenti per questo professionista.");
                } else {
                    // Se non ci sono prenotazioni, procedi con l'eliminazione del professionista
                    boolean success = professionistaDAO.rimuoviProfessionista(professionistaId);
                    if (success) {
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write("success");
                    } else {
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        response.getWriter().write("error");
                    }
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("invalid ID");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("missing ID");
        }
    }
}
