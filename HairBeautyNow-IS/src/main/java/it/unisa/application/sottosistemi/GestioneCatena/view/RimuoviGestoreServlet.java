package it.unisa.application.sottosistemi.GestioneCatena.view;

import it.unisa.application.model.entity.UtenteGestoreSede;
import it.unisa.application.sottosistemi.GestioneCatena.service.GestioneGestoreService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/rimuoviGestore")
public class RimuoviGestoreServlet extends HttpServlet {

    private GestioneGestoreService gestioneGestoreService = new GestioneGestoreService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (request.getParameter("removeGestoreRimosso") != null) {
            session.removeAttribute("gestoreRimosso");
        }

        List<UtenteGestoreSede> gestoriConSede = gestioneGestoreService.getGestoriConSede();
        request.setAttribute("gestoriConSede", gestoriConSede);
        request.getRequestDispatcher("/WEB-INF/jsp/rimuoviGestore.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usernameUGS = request.getParameter("usernameUGS");

        if (usernameUGS != null && !usernameUGS.isEmpty()) {
            gestioneGestoreService.licenziaGestore(usernameUGS);
            HttpSession session = request.getSession();
            session.setAttribute("gestoreRimosso", "ok");
            response.sendRedirect(request.getContextPath() + "/homeCatena");
        } else {
            response.sendRedirect(request.getContextPath() + "/rimuoviGestore?errore=Gestore non valido");
        }
    }
}
