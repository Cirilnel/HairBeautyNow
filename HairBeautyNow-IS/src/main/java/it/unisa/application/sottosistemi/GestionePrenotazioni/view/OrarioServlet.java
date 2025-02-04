package it.unisa.application.sottosistemi.GestionePrenotazioni.view;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/prenotazione")
public class OrarioServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Ottieni i dati dalla richiesta (giorno, orario, professionista)
        String giorno = request.getParameter("data");
        String orario = request.getParameter("orario");
        String professionistaId = request.getParameter("professionistaId");

        // Salva i dati nella sessione
        HttpSession session = request.getSession();
        session.setAttribute("giorno", giorno);
        session.setAttribute("orario", orario);
        session.setAttribute("professionistaId", professionistaId);

        // Esegui il forward alla pagina JSP per il pagamento
        request.getRequestDispatcher("/WEB-INF/jsp/metodoPagamento.jsp").forward(request, response);
    }
}
