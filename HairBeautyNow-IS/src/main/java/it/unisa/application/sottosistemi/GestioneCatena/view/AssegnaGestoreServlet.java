package it.unisa.application.sottosistemi.GestioneCatena.view;

import it.unisa.application.model.entity.UtenteGestoreSede;
import it.unisa.application.sottosistemi.GestioneCatena.service.GestioneGestoreService;
import it.unisa.application.sottosistemi.GestioneCatena.service.GestioneSedeService;  // AGGIUNTO
import it.unisa.application.model.entity.Sede;
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

    private GestioneGestoreService gestioneGestoreService = new GestioneGestoreService();
    private GestioneSedeService gestioneSedeService = new GestioneSedeService();  // AGGIUNTO

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<UtenteGestoreSede> gestoriSenzaSede = gestioneGestoreService.getGestoriSenzaSede();
        request.setAttribute("gestoriSenzaSede", gestoriSenzaSede);
        request.getRequestDispatcher("/WEB-INF/jsp/assegnaGestore.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Sede nuovaSede = (Sede) session.getAttribute("nuovaSede");
        Integer sedeIDLicenziato = (Integer) session.getAttribute("sedeIDLicenziato");
        String usernameLicenziato = (String) session.getAttribute("usernameLicenziato");

        if (nuovaSede != null) {
            int sedeID = gestioneSedeService.creaSede(nuovaSede);  // ADESSO FUNZIONA
            String usernameUGS = request.getParameter("usernameUGS");

            gestioneGestoreService.assegnaSede(usernameUGS, sedeID);

            session.removeAttribute("nuovaSede");

            if (sedeIDLicenziato != null && sedeIDLicenziato > 0 && usernameLicenziato != null) {
                gestioneGestoreService.licenziaGestore(usernameLicenziato);
            }

            response.sendRedirect(request.getContextPath() + "/homeCatena?successo=ok");
        } else {
            response.sendRedirect(request.getContextPath() + "/assegnaGestore?errore=Creazione sede fallita");
        }
    }
}
