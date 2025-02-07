package it.unisa.application.sottosistemi.GestioneCatena.view;

import it.unisa.application.sottosistemi.GestioneCatena.service.GestioneSedeService;
import it.unisa.application.model.entity.Sede;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/aggiungiSede")
public class AggiungiSedeServlet extends HttpServlet {

    private GestioneSedeService gestioneSedeService = new GestioneSedeService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/aggiungiSede.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String indirizzo = request.getParameter("indirizzo");
        String citta = request.getParameter("citta");

        Sede nuovaSede = new Sede(indirizzo, "HairBeauty Now", citta, 0);

        HttpSession session = request.getSession();
        session.setAttribute("nuovaSede", nuovaSede);

        int sedeID = gestioneSedeService.creaSede(nuovaSede);

        if (sedeID > 0) {
            response.sendRedirect(request.getContextPath() + "/assegnaGestore");
        } else {
            response.sendRedirect(request.getContextPath() + "/aggiungiSede?errore=Creazione sede fallita");
        }
    }
}
