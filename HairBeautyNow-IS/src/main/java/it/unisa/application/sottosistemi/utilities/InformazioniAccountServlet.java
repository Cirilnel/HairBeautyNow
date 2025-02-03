package it.unisa.application.sottosistemi.utilities;

import it.unisa.application.model.entity.UtenteAcquirente;
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
        // Puoi fare delle verifiche sull'utente qui, ad esempio se l'utente è loggato

        // Recupera l'utente dalla sessione (se presente)
        UtenteAcquirente user = (UtenteAcquirente) request.getSession().getAttribute("user");

        // Se l'utente non è loggato, puoi fare un redirect alla pagina di login
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/loginPage");
            return;
        }

        // Se l'utente è loggato, reindirizza alla pagina delle informazioni dell'account
        request.getRequestDispatcher("/WEB-INF/jsp/informazioniAccount.jsp").forward(request, response);
    }

    // Gestione della richiesta POST (opzionale)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
