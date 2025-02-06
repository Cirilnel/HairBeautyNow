package it.unisa.application.sottosistemi.GestioneCatena.view;


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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Reindirizza alla pagina JSP per l'inserimento della sede
        request.getRequestDispatcher("/WEB-INF/jsp/aggiungiSede.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recupera i dati dal form
        String indirizzo = request.getParameter("indirizzo");
        String citta = request.getParameter("citta");

        // Crea un oggetto Sede con i dati (nome fisso "HairBeauty Now")
        Sede nuovaSede = new Sede(indirizzo, "HairBeauty Now", citta, 0); // ID = 0 perché non ancora salvato nel DB

        // Salva temporaneamente la sede nella sessione (senza inserirla nel DB)
        HttpSession session = request.getSession();
        session.setAttribute("nuovaSede", nuovaSede);

        // Reindirizza a una pagina successiva per la conferma finale
        response.sendRedirect(request.getContextPath() + "/assegnaGestore");
    }
}

