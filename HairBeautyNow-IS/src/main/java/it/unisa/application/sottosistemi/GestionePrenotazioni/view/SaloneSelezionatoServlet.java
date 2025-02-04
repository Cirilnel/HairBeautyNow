package it.unisa.application.sottosistemi.GestionePrenotazioni.view;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/saloneSelezionato")
public class SaloneSelezionatoServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recupera l'ID del salone selezionato dalla richiesta
        String saloneId = request.getParameter("saloneId");

        if (saloneId != null && !saloneId.isEmpty()) {
            // Recupera la sessione utente
            HttpSession session = request.getSession();

            // Salva l'ID del salone nella sessione
            session.setAttribute("saloneSelezionato", saloneId);

            // Redirigi alla pagina di selezione del professionista (supponiamo che sia /selezionaProfessionista)
            response.sendRedirect("selezionaProfessionista");
        } else {
            // Se non è stato selezionato un salone, mostra un errore
            response.sendRedirect("error.jsp"); // oppure potresti gestire direttamente un messaggio di errore
        }
    }
}
