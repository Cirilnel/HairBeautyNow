package it.unisa.application.sottosistemi.GestioneSede.view;

import it.unisa.application.model.dao.ProfessionistaDAO;
import it.unisa.application.model.dao.SedeDAO;
import it.unisa.application.model.dao.UtenteGestoreSedeDAO;
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

    private UtenteGestoreSedeDAO utenteGestoreSedeDAO;
    private SedeDAO sedeDAO;
    private ProfessionistaDAO professionistaDAO;

    @Override
    public void init() throws ServletException {
        utenteGestoreSedeDAO = new UtenteGestoreSedeDAO();
        sedeDAO = new SedeDAO();
        professionistaDAO = new ProfessionistaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recupera l'oggetto UtenteGestoreSede dalla sessione
        HttpSession session = request.getSession();
        UtenteGestoreSede utente = (UtenteGestoreSede) session.getAttribute("user");

        if (utente != null) {
            // Ottieni l'ID della sede dal gestore
            int sedeId = utente.getSedeID();

            // Recupera la sede e i professionisti della sede
            Sede sede = sedeDAO.getSedeById(sedeId);
            List<Professionista> professionisti = professionistaDAO.getProfessionistiBySede(sedeId);

            // Imposta gli attributi nella request
            request.setAttribute("sede", sede);
            request.setAttribute("professionisti", professionisti);

            // Forward alla JSP per la visualizzazione
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/rimozioneProfessionisti.jsp");
            dispatcher.forward(request, response);
        } else {
            // Se l'utente non è trovato nella sessione, mostra un errore
            response.getWriter().println("Utente Gestore Sede non trovato nella sessione!");
        }
    }
}
