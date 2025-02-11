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
        request.setAttribute("gestoriSenzaSede", gestoriSenzaSede);

        // Se siamo arrivati dalla creazione di una nuova sede, recupera la sede dalla sessione
        HttpSession session = request.getSession();
        Sede nuovaSede = (Sede) session.getAttribute("nuovaSede");

        // Se una sede è stata appena creata, aggiungi il relativo messaggio
        if (nuovaSede != null) {
            request.setAttribute("nuovaSede", nuovaSede);
        }

        request.getRequestDispatcher("/WEB-INF/jsp/assegnaGestore.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Recupera sedeID e username del gestore licenziato, se presenti nella sessione
        Integer sedeIDLicenziato = (Integer) session.getAttribute("sedeIDLicenziato");
        String usernameLicenziato = (String) session.getAttribute("usernameLicenziato");

        // Recupera l'oggetto Sede dalla sessione (se presente)
        Sede nuovaSede = (Sede) session.getAttribute("nuovaSede");

        // Caso 1: Nuova sede creata
        if (nuovaSede != null) {
            int sedeID = sedeDAO.insertSedeAndReturnID(nuovaSede);

            if (sedeID > 0) {
                // Recupera il gestore selezionato dal form
                String usernameUGS = request.getParameter("usernameUGS");

                // Assegna la sede al nuovo gestore
                utenteGestoreSedeDAO.assegnaSede(usernameUGS, sedeID);

                // Rimuove la sede dalla sessione
                session.removeAttribute("nuovaSede");

                // Caso 2: Se c'è un gestore licenziato, rimuovi la sua sede
                if (sedeIDLicenziato != null && sedeIDLicenziato > 0 && usernameLicenziato != null) {
                    // Licenzia il vecchio gestore (rimuovendo la sua sede)
                    utenteGestoreSedeDAO.licenziaGestore(usernameLicenziato);  // Imposta sedeID a null per il licenziato
                    System.out.println("Licenziato il gestore: " + usernameLicenziato);
                }

                // Successo: Reindirizza alla home della catena con un messaggio di successo
                response.sendRedirect(request.getContextPath() + "/homeCatena?successo=ok");
                return;
            } else {
                // Errore: Creazione sede fallita
                response.sendRedirect(request.getContextPath() + "/assegnaGestore?errore=Creazione sede fallita");
                return;
            }
        }

        // Caso 2: Nessuna nuova sede (licenzia il vecchio gestore e assegna la sede al nuovo)
        if (sedeIDLicenziato != null && sedeIDLicenziato > 0 && usernameLicenziato != null) {
            // Recupera il gestore selezionato dal form
            String usernameUGS = request.getParameter("usernameUGS");

            // Licenzia il vecchio gestore
            utenteGestoreSedeDAO.licenziaGestore(usernameLicenziato);  // Imposta sedeID a null per il licenziato

            // Assegna la sede al nuovo gestore
            utenteGestoreSedeDAO.assegnaSede(usernameUGS, sedeIDLicenziato);

            // Aggiungi l'attributo alla sessione per notificare che il gestore è stato rimosso
            session.setAttribute("gestoreRimosso", "ok");

            // Successo: Reindirizza alla home della catena con un messaggio di successo
            response.sendRedirect(request.getContextPath() + "/homeCatena");
        } else {
            // Errore: Non ci sono informazioni sui gestori da licenziare
            response.sendRedirect(request.getContextPath() + "/assegnaGestore?errore=Nessun gestore licenziato");
        }
    }
}


