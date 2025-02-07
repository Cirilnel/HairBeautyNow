package it.unisa.application.sottosistemi.GestioneCatena.view;

import it.unisa.application.sottosistemi.GestioneCatena.service.GestioneGestoreService;
import it.unisa.application.model.entity.UtenteGestoreSede;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/creaGestore")
public class CreaGestoreServlet extends HttpServlet {

    private GestioneGestoreService gestioneGestoreService = new GestioneGestoreService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("removeGestoreCreato") != null) {
            request.getSession().removeAttribute("gestoreCreato");
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        request.getRequestDispatcher("/WEB-INF/jsp/creaGestore.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("usernameUGS");
        String password = request.getParameter("password");

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/creaGestore?errore=Tutti i campi sono obbligatori");
            return;
        }

        UtenteGestoreSede nuovoGestore = new UtenteGestoreSede(username, password, 0);
        boolean success = gestioneGestoreService.creaGestore(nuovoGestore);

        if (success) {
            request.getSession().setAttribute("gestoreCreato", "ok");
            response.sendRedirect(request.getContextPath() + "/homeCatena");
        } else {
            response.sendRedirect(request.getContextPath() + "/creaGestore?errore=Username gi&agrave; in uso");
        }
    }
}
