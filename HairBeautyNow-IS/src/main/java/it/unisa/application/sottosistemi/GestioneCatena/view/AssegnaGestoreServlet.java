package it.unisa.application.sottosistemi.GestioneCatena.view;

import it.unisa.application.model.dao.UtenteGestoreSedeDAO;
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recupera tutti i gestori che non hanno una sede assegnata (sedeID = 0)
        List<UtenteGestoreSede> gestoriSenzaSede = utenteGestoreSedeDAO.getGestoriSenzaSede();

        // Passa la lista alla pagina JSP
        request.setAttribute("gestoriSenzaSede", gestoriSenzaSede);
        request.getRequestDispatcher("/WEB-INF/jsp/assegnaGestore.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recupera i parametri dal form
        String usernameUGS = request.getParameter("usernameUGS");
        int sedeID = Integer.parseInt(request.getParameter("sedeID"));

        // Assegna la sede al gestore nel database
        utenteGestoreSedeDAO.assegnaSede(usernameUGS, sedeID);

        // Reindirizza alla stessa pagina per vedere l'aggiornamento
        response.sendRedirect(request.getContextPath() + "/assegnaGestore");
    }
}
