package it.unisa.application.sottosistemi.GestioneSede.view;

import it.unisa.application.model.dao.ProfessionistaDAO;
import it.unisa.application.model.dao.SedeDAO;
import it.unisa.application.model.entity.Professionista;
import it.unisa.application.model.entity.Sede;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/assumiprofessionista")
public class AssumiProfessionistaServlet extends HttpServlet {

    private ProfessionistaDAO professionistaDAO = new ProfessionistaDAO();

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

        // Recuperiamo l'oggetto Sede dalla sessione
        HttpSession session = request.getSession();
        Sede sede = (Sede) session.getAttribute("sede");

        // Se l'oggetto sede non è trovato nella sessione, gestisci l'errore
        if (sede == null) {
            response.sendRedirect("erroreSede");  // Aggiungi una pagina di errore
            return;
        }

        // Recuperiamo l'ID della sede dall'oggetto Sede
        Integer sedeId = sede.getId();

        // Verifica che il nome del professionista non sia vuoto
        if (nomeProfessionista == null || nomeProfessionista.trim().isEmpty()) {
            response.sendRedirect("erroreNome");  // Puoi reindirizzare a una pagina di errore
            return;
        }

        // Creiamo il professionista e associamo la sede
        Professionista professionista = new Professionista();
        professionista.setNome(nomeProfessionista);
        professionista.setSedeId(sedeId);

        // Salviamo il professionista nel database
        professionistaDAO.insertProfessionista(professionista);

        // Reindirizziamo alla pagina di successo o a una pagina che conferma l'assunzione
        response.sendRedirect(request.getContextPath() + "/successo");
    }

}

