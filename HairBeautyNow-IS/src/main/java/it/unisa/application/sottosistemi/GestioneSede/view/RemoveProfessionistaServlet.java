package it.unisa.application.sottosistemi.GestioneSede.view;

import it.unisa.application.sottosistemi.GestioneSede.service.GestioneProfessionistaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/removeProfessionista")
public class RemoveProfessionistaServlet extends HttpServlet {

    private GestioneProfessionistaService gestioneRimozioneProfessionistaService = new GestioneProfessionistaService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String professionistaIdStr = request.getParameter("professionistaId");

        if (professionistaIdStr != null && !professionistaIdStr.isEmpty()) {
            try {
                int professionistaId = Integer.parseInt(professionistaIdStr);
                String result = gestioneRimozioneProfessionistaService.rimuoviProfessionista(professionistaId);
                response.getWriter().write(result);
                response.setStatus(result.contains("successo") ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("ID professionista non valido");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("ID professionista mancante");
        }
    }
}
