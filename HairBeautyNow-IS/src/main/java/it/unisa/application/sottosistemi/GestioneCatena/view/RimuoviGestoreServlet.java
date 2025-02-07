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

@WebServlet("/rimuoviGestore")
public class RimuoviGestoreServlet extends HttpServlet {

    private UtenteGestoreSedeDAO utenteGestoreSedeDAO = new UtenteGestoreSedeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Verifica se l'attributo per la rimozione del gestore è stato impostato per essere rimosso
        HttpSession session = request.getSession();
        if (request.getParameter("removeGestoreRimosso") != null) {
            session.removeAttribute("gestoreRimosso");
        }

        // Recupera tutti i gestori che hanno una sede assegnata (sedeID != null o 0)
        List<UtenteGestoreSede> gestoriConSede = utenteGestoreSedeDAO.getGestoriConSede();

        // Passa la lista alla pagina JSP
        request.setAttribute("gestoriConSede", gestoriConSede);
        request.getRequestDispatcher("/WEB-INF/jsp/rimuoviGestore.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usernameUGS = request.getParameter("usernameUGS");

        // Verifica che sia stato selezionato un gestore valido
        if (usernameUGS != null && !usernameUGS.isEmpty()) {
            // Ottieni il gestore da licenziare
            UtenteGestoreSede gestoreLicenziato = utenteGestoreSedeDAO.getByUsername(usernameUGS);

            // Salva sedeID del gestore da licenziare nella sessione
            HttpSession session = request.getSession();
            session.setAttribute("sedeIDLicenziato", gestoreLicenziato.getSedeID());
            session.setAttribute("usernameLicenziato", gestoreLicenziato.getUsernameUGS());

            // Aggiungi un log per verificare che i dati siano stati correttamente letti dalla sessione
            System.out.println("Gestore da licenziare: " + gestoreLicenziato.getUsernameUGS() + ", SedeID: " + gestoreLicenziato.getSedeID());

            // Reindirizza alla pagina per assegnare un nuovo gestore alla sede
            response.sendRedirect(request.getContextPath() + "/assegnaGestore");
        } else {
            response.sendRedirect(request.getContextPath() + "/rimuoviGestore?errore=Gestore non valido");
        }
    }
}




