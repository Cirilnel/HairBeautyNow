package it.unisa.application.sottosistemi.GestioneSede.view;

import it.unisa.application.sottosistemi.GestionePrenotazioni.service.PrenotazioneService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/rimuoviPrenotazione")
public class RimuoviPrenotazioneServlet extends HttpServlet {

    private PrenotazioneService gestionePrenotazioneService = new PrenotazioneService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String prenotazioneIdStr = request.getParameter("prenotazioneId");

        if (prenotazioneIdStr != null && !prenotazioneIdStr.isEmpty()) {
            try {
                int prenotazioneId = Integer.parseInt(prenotazioneIdStr);
                String result = gestionePrenotazioneService.rimuoviPrenotazione(prenotazioneId);
                response.getWriter().write(result);
                response.setStatus(result.contains("successo") ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
            } catch (SQLException | NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Errore durante la rimozione della prenotazione");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("ID prenotazione mancante");
        }
    }
}
