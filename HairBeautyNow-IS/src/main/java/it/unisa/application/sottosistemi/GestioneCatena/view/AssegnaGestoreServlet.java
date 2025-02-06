package it.unisa.application.sottosistemi.GestioneCatena.view;

import it.unisa.application.model.dao.SedeDAO;
import it.unisa.application.model.dao.UtenteGestoreSedeDAO;
import it.unisa.application.model.entity.Sede;
import it.unisa.application.model.entity.UtenteGestoreSede;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/assegnaGestore")
public class AssegnaGestoreServlet extends HttpServlet {

    private UtenteGestoreSedeDAO utenteGestoreSedeDAO = new UtenteGestoreSedeDAO();
    private SedeDAO sedeDAO = new SedeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recupera tutti i gestori che non hanno una sede assegnata
        List<UtenteGestoreSede> gestoriSenzaSede = utenteGestoreSedeDAO.getGestoriSenzaSede();

        // Passa la lista alla pagina JSP
        request.setAttribute("gestoriSenzaSede", gestoriSenzaSede);
        request.getRequestDispatcher("/WEB-INF/jsp/assegnaGestore.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Recupera l'oggetto Sede dalla sessione
        Sede nuovaSede = (Sede) session.getAttribute("nuovaSede");

        if (nuovaSede != null) {
            // Inserisce la sede nel database e ottiene il suo ID generato automaticamente
            int sedeID = sedeDAO.insertSedeAndReturnID(nuovaSede);

            if (sedeID > 0) {
                // Recupera il gestore selezionato dal form
                String usernameUGS = request.getParameter("usernameUGS");

                // Assegna la sede al gestore nel database
                utenteGestoreSedeDAO.assegnaSede(usernameUGS, sedeID);

                // Rimuove la sede dalla sessione per evitare duplicazioni future
                session.removeAttribute("nuovaSede");

                // Reindirizza alla stessa pagina per vedere l'aggiornamento
                response.sendRedirect(request.getContextPath() + "/homeCatena?successo=ok");
                return;
            } else {
                response.sendRedirect(request.getContextPath() + "/assegnaGestore?errore=Creazione sede fallita");
                return;
            }
        }

        response.sendRedirect(request.getContextPath() + "/assegnaGestore?errore=Nessuna sede in sessione");
    }
}
