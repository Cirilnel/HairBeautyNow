package it.unisa.application.sottosistemi.GestionePrenotazioni.view;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/saloneSelezionato")
public class SaloneSelezionatoServlet extends HttpServlet {

    private final SaloneService saloneService = new SaloneService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String saloneId = request.getParameter("saloneId");

        // Validazione per saloneId
        if (saloneId == null || !saloneId.matches("\\d+")) {
            request.setAttribute("errorMessage", "ID salone non valido.");
            forwardToPage(request, response);
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("saloneSelezionato", saloneId);

        List<Professionista> professionisti = saloneService.getProfessionistiBySalone(Integer.parseInt(saloneId));

        // Se non ci sono professionisti, evita di eseguire controlli inutili
        if (professionisti == null || professionisti.isEmpty()) {
            request.setAttribute("errorMessage", "Non ci sono professionisti disponibili in questa sede.");
            request.setAttribute("professionisti", null); // Evita errori nella JSP
            request.setAttribute("fasceOrarieByProfessionista", null);
            forwardToPage(request, response);
            return;
        }

        try {
            // Inizializza sempre la mappa per evitare NullPointerException
            Map<Integer, Map<LocalDate, List<String>>> fasceOrarieByProfessionista = new HashMap<>();

            for (Professionista professionista : professionisti) {
                Map<LocalDate, List<String>> fasceOrarie = saloneService.getFasceOrarieByProfessionista(professionista.getId());
                if (fasceOrarie != null) {
                    fasceOrarieByProfessionista.put(professionista.getId(), fasceOrarie);
                }
            }

            // Passa i dati alla JSP
            request.setAttribute("fasceOrarieByProfessionista", fasceOrarieByProfessionista);
            request.setAttribute("professionisti", professionisti);
            request.setAttribute("saloneId", saloneId);

        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Si è verificato un errore durante il recupero degli orari disponibili.");
        }

        // Inoltra sempre alla JSP
        forwardToPage(request, response);
    }

    private void forwardToPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/professionista.jsp").forward(request, response);
    }
}