package it.unisa.application.sottosistemi.GestionePrenotazioni.view;

import it.unisa.application.model.dao.FasciaOrariaDAO;
import it.unisa.application.model.dao.ProfessionistaDAO;
import it.unisa.application.model.entity.FasciaOraria;
import it.unisa.application.model.entity.Professionista;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/saloneSelezionato")
public class SaloneSelezionatoServlet extends HttpServlet {
    private final ProfessionistaDAO professionistaDAO = new ProfessionistaDAO();
    private final FasciaOrariaDAO fasciaOrariaDAO = new FasciaOrariaDAO();

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

        // Ottieni i professionisti dal DAO
        List<Professionista> professionisti = professionistaDAO.getProfessionistiBySede(Integer.parseInt(saloneId));

        if (professionisti == null || professionisti.isEmpty()) {
            request.setAttribute("errorMessage", "No professionals found for the selected salon.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        // Carica le fasce orarie per ciascun professionista
        for (Professionista professionista : professionisti) {
            try {
                if (professionista.getId() != 0) {  // Verifica che l'ID del professionista sia valido
                    List<FasciaOraria> fasceOrarie = fasciaOrariaDAO.getFasceOrarieByProfessionista(professionista.getId());
                    if (fasceOrarie != null && !fasceOrarie.isEmpty()) {
                        professionista.setFasceOrarie(fasceOrarie);  // Imposta le fasce orarie del professionista
                    } else {
                        // Se non ci sono fasce orarie, logghiamo il problema
                        System.out.println("Nessuna fascia oraria trovata per il professionista ID: " + professionista.getId());
                    }
                } else {
                    System.out.println("ID professionista non valido: " + professionista.getId());
                }
            } catch (Exception e) {
                // Log degli errori
                e.printStackTrace();
                request.setAttribute("errorMessage", "Errore nel caricamento delle fasce orarie.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
        }

        // Creiamo una mappa che associa il giorno alle fasce orarie disponibili
        Map<LocalDate, List<String>> fasceOrarieByDay = new HashMap<>();
        for (Professionista professionista : professionisti) {
            if (professionista.getFasceOrarie() != null) {
                for (FasciaOraria fascia : professionista.getFasceOrarie()) {
                    if (fascia.isDisponibile()) {
                        fasceOrarieByDay.putIfAbsent(fascia.getGiorno(), new ArrayList<>());
                        fasceOrarieByDay.get(fascia.getGiorno()).add(fascia.getFascia());
                    }
                }
            }
        }

        // Passiamo la mappa delle fasce orarie alla JSP
        request.setAttribute("fasceOrarieByDay", fasceOrarieByDay);
        request.setAttribute("professionisti", professionisti);
        request.setAttribute("saloneId", saloneId);

        // Forward alla JSP
        request.getRequestDispatcher("/WEB-INF/jsp/professionista.jsp").forward(request, response);
    }
}


