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
    private GestioneSedeService gestioneSedeService = new GestioneSedeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Ottieni la lista dei gestori senza sede
        List<UtenteGestoreSede> gestoriSenzaSede = gestioneGestoreService.getGestoriSenzaSede();

        // Se ci sono gestori disponibili, invia la lista alla vista
        if (gestoriSenzaSede.isEmpty()) {
            // Nessun gestore disponibile, invia un errore
            request.setAttribute("errore", "Nessun gestore disponibile per l'assegnazione.");
            request.getRequestDispatcher("/WEB-INF/jsp/assegnaGestore.jsp").forward(request, response);
        } else {
            // Altrimenti, mostra la pagina per assegnare il gestore
            request.setAttribute("gestoriSenzaSede", gestoriSenzaSede);
            request.getRequestDispatcher("/WEB-INF/jsp/assegnaGestore.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Recupera la sede dalla sessione
        Sede nuovaSede = (Sede) session.getAttribute("nuovaSede");

        // Verifica se è stato trovato un gestore da assegnare
        String usernameUGS = request.getParameter("usernameUGS");

        // Se non esiste un gestore disponibile, invia un errore
        if (usernameUGS == null || usernameUGS.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/assegnaGestore?errore=Selezionare un gestore.");
            return;
        }

        // Se la sede esiste e il gestore è valido
        if (nuovaSede != null) {
            // Crea la sede nel database
            int sedeID = gestioneSedeService.creaSede(nuovaSede);

            if (sedeID > 0) {
                // Assegna il gestore alla sede
                gestioneGestoreService.assegnaSede(usernameUGS, sedeID);
                session.removeAttribute("nuovaSede");

                // Se c'è un gestore licenziato, licenzia il gestore
                Integer sedeIDLicenziato = (Integer) session.getAttribute("sedeIDLicenziato");
                String usernameLicenziato = (String) session.getAttribute("usernameLicenziato");

                if (sedeIDLicenziato != null && sedeIDLicenziato > 0 && usernameLicenziato != null) {
                    gestioneGestoreService.licenziaGestore(usernameLicenziato);
                }

                response.sendRedirect(request.getContextPath() + "/homeCatena?successo=ok");
            } else {
                // Se la creazione della sede fallisce
                response.sendRedirect(request.getContextPath() + "/assegnaGestore?errore=Creazione sede fallita");
            }
        } else {
            // Se la sede non è stata trovata nella sessione
            response.sendRedirect(request.getContextPath() + "/assegnaGestore?errore=Sede non trovata");
        }
    }
}



