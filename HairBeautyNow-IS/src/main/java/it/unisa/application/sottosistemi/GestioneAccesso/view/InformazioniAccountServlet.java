package it.unisa.application.sottosistemi.GestioneAccesso.view;

import it.unisa.application.model.entity.UtenteAcquirente;
import it.unisa.application.model.entity.UtenteGestoreSede;
import it.unisa.application.model.entity.UtenteGestoreCatena;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/accountInfo")
public class InformazioniAccountServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Gestione della richiesta GET
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recupera l'utente dalla sessione (se presente)
        Object user = request.getSession().getAttribute("user");

        // Se l'utente non è loggato, puoi fare un redirect alla pagina di login
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/loginPage");
            return;
        }

        // Passa l'utente come attributo alla JSP
        request.setAttribute("user", user);

        // Se l'utente è loggato, reindirizza alla pagina delle informazioni dell'account
        request.getRequestDispatcher("/WEB-INF/jsp/informazioniAccount.jsp").forward(request, response);
    }

    // Gestione della richiesta POST (opzionale)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
