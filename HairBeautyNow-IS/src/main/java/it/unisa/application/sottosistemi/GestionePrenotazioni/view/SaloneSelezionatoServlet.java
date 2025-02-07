package it.unisa.application.sottosistemi.GestionePrenotazioni.view;

import it.unisa.application.model.entity.FasciaOraria;
import it.unisa.application.model.entity.Professionista;
import it.unisa.application.sottosistemi.GestionePrenotazioni.service.SaloneService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/saloneSelezionato")
public class SaloneSelezionatoServlet extends HttpServlet {

    private final SaloneService saloneService = new SaloneService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String saloneId = request.getParameter("saloneId");

        // Aggiunta validazione per saloneId
        if (saloneId == null || !saloneId.matches("\\d+")) {
            request.setAttribute("errorMessage", "Invalid salon ID.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("saloneSelezionato", saloneId);

        List<Professionista> professionisti = saloneService.getProfessionistiBySalone(Integer.parseInt(saloneId));

        if (professionisti == null || professionisti.isEmpty()) {
            request.setAttribute("errorMessage", "No professionals found for the selected salon.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        try {
            // Iniziamo a preparare una mappa che contiene le fasce orarie per ogni professionista
            Map<Integer, Map<LocalDate, List<String>>> fasceOrarieByProfessionista = new HashMap<>();

            // Recuperiamo le fasce orarie per ogni professionista
            for (Professionista professionista : professionisti) {
                // Passiamo l'ID del professionista singolo
                Map<LocalDate, List<String>> fasceOrarie = saloneService.getFasceOrarieByProfessionista(professionista.getId());

                // Aggiungi le fasce orarie del professionista alla mappa per professionista
                fasceOrarieByProfessionista.put(professionista.getId(), fasceOrarie);
            }

            // Se non ci sono fasce orarie disponibili
            if (fasceOrarieByProfessionista.isEmpty()) {
                request.setAttribute("errorMessage", "No available time slots found for the professionals.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            // Passiamo le fasce orarie e i professionisti alla JSP
            request.setAttribute("fasceOrarieByProfessionista", fasceOrarieByProfessionista);
            request.setAttribute("professionisti", professionisti);
            request.setAttribute("saloneId", saloneId);

            // Forward alla JSP per la selezione
            request.getRequestDispatcher("/WEB-INF/jsp/professionista.jsp").forward(request, response);
        } catch (SQLException e) {
            // Gestisci l'errore di database
            request.setAttribute("errorMessage", "An error occurred while fetching available time slots.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}


