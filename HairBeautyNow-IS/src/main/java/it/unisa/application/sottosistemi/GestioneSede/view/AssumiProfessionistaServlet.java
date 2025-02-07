package it.unisa.application.sottosistemi.GestioneSede.view;

import it.unisa.application.model.entity.Professionista;
import it.unisa.application.model.entity.Sede;
import it.unisa.application.sottosistemi.GestioneSede.service.GestioneProfessionistaService;
import it.unisa.application.model.entity.UtenteGestoreSede; // Import UtenteGestoreSede
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/assumiProfessionista")
public class AssumiProfessionistaServlet extends HttpServlet {

    private final GestioneProfessionistaService gestioneProfessionistaService = new GestioneProfessionistaService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Visualizza il modulo per inserire il nome del professionista
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/assumiProfessionista.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recuperiamo il nome del professionista dal form
        String nomeProfessionista = request.getParameter("nome");

        // Recuperiamo l'utente loggato dalla sessione
        HttpSession session = request.getSession();
        UtenteGestoreSede utente = (UtenteGestoreSede) session.getAttribute("user");

        // Verifica se l'utente è un GestoreSede
        if (utente == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"message\":\"Errore: Utente non trovato\"}");
            return;
        }

        // Otteniamo l'ID della sede dal gestore
        Integer sedeId = utente.getSedeID();
        Sede sede = gestioneProfessionistaService.getSedeById(sedeId);
        // Se la sede non esiste o è nulla, gestisci l'errore
        if (sede == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"message\":\"Errore: Sede non trovata\"}");
            return;
        }

        // Verifica che il nome del professionista non sia vuoto
        if (nomeProfessionista == null || nomeProfessionista.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"message\":\"Errore: Nome professionista non valido\"}");
            return;
        }
        // Delegare la logica al service
        Professionista professionista = new Professionista();
        professionista.setNome(nomeProfessionista);
        professionista.setSedeId(sedeId);
        // Gestire la risposta in base al risultato
        gestioneProfessionistaService.assumiProfessionista(professionista);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("{\"message\":\"Professionista assunto con successo!\"}");

    }
}
