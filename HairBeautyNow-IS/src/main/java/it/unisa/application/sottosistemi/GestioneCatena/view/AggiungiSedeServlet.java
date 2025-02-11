package it.unisa.application.sottosistemi.GestioneCatena.view;

import it.unisa.application.sottosistemi.GestioneCatena.service.GestioneSedeService;
import it.unisa.application.sottosistemi.GestioneCatena.service.GestioneGestoreService;  // AGGIUNTO
import it.unisa.application.model.entity.Sede;
import it.unisa.application.model.entity.UtenteGestoreSede;  // AGGIUNTO
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/aggiungiSede")
public class AggiungiSedeServlet extends HttpServlet {

    private GestioneSedeService gestioneSedeService = new GestioneSedeService();
    private GestioneGestoreService gestioneGestoreService = new GestioneGestoreService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Ottieni la lista dei gestori senza sede
        List<UtenteGestoreSede> gestoriSenzaSede = gestioneGestoreService.getGestoriSenzaSede();

        // Se non ci sono gestori disponibili, invia un errore
        if (gestoriSenzaSede.isEmpty()) {
            request.setAttribute("errore", "Impossibile creare la sede non ci sono gestori disponibili");
        }

        // Mostra la pagina per aggiungere la sede
        request.getRequestDispatcher("/WEB-INF/jsp/aggiungiSede.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String indirizzo = request.getParameter("indirizzo");
        String citta = request.getParameter("citta");

        // Crea una nuova sede (non la salviamo ancora nel database)
        Sede nuovaSede = new Sede(indirizzo, "HairBeauty Now", citta, 0);

        // Salva la sede nella sessione, in modo che la possiamo usare più tardi
        HttpSession session = request.getSession();
        session.setAttribute("nuovaSede", nuovaSede);

        // Se non ci sono gestori disponibili, mostra un errore nella JSP
        List<UtenteGestoreSede> gestoriSenzaSede = gestioneGestoreService.getGestoriSenzaSede();
        if (gestoriSenzaSede.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/aggiungiSede?errore=Impossibile creare la sede non ci sono gestori disponibili.");
            return;
        }

        // Se ci sono gestori disponibili, redirigi alla pagina di assegnazione del gestore
        response.sendRedirect(request.getContextPath() + "/assegnaGestore");
    }
}

