package it.unisa.application.sottosistemi.GestioneSede.view;

import it.unisa.application.sottosistemi.GestioneSede.service.GestioneProfessionistaService;
import it.unisa.application.model.entity.Professionista;
import it.unisa.application.model.entity.Sede;
import it.unisa.application.model.entity.UtenteGestoreSede;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/rimuoviProfessionista")
public class RimuoviProfessionistaServlet extends HttpServlet {

    private GestioneProfessionistaService gestioneRimozioneProfessionistaService;

    @Override
    public void init() throws ServletException {
        gestioneRimozioneProfessionistaService = new GestioneProfessionistaService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recupera l'oggetto UtenteGestoreSede dalla sessione
        HttpSession session = request.getSession();
        UtenteGestoreSede utente = (UtenteGestoreSede) session.getAttribute("user");

        if (utente != null) {
            // Ottieni l'ID della sede dal gestore
            int sedeId = utente.getSedeID();

            // Recupera la sede e i professionisti della sede tramite il service
            Sede sede = gestioneRimozioneProfessionistaService.getSedeById(sedeId);
            List<Professionista> professionisti = gestioneRimozioneProfessionistaService.getProfessionistiBySede(sedeId);

            // Imposta gli attributi nella request
            request.setAttribute("sede", sede);
            request.setAttribute("professionisti", professionisti);

            // Forward alla JSP per la visualizzazione
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/rimozioneProfessionisti.jsp");
            dispatcher.forward(request, response);
        } else {
            // Se l'utente non Ã¨ trovato nella sessione, mostra un errore
            response.getWriter().println("Utente Gestore Sede non trovato nella sessione!");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recupera l'ID del professionista da rimuovere
        String professionistaIdStr = request.getParameter("professionistaId");

        if (professionistaIdStr != null && !professionistaIdStr.isEmpty()) {
            try {
                int professionistaId = Integer.parseInt(professionistaIdStr);
                String result = gestioneRimozioneProfessionistaService.rimuoviProfessionista(professionistaId);

                // Verifica il risultato del servizio di rimozione
                if (result.equals("successo")) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("Professionista rimosso con successo.");
                } else if (result.equals("Errore durante la rimozione del professionista o ci sono prenotazioni attive.")) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);  // Risposta con errore 400
                    response.getWriter().write("Ci sono prenotazioni attive su questo professionista, non è possibile eliminarlo al momento.");
                } else {
                    // Invia il risultato dell'errore generico se nessuna delle condizioni è soddisfatta
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write(result);
                }
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