package it.unisa.application.sottosistemi.GestioneServizi.view;

import it.unisa.application.model.dao.ServizioDAO;
import it.unisa.application.model.entity.Servizio;
import it.unisa.application.model.entity.UtenteAcquirente;
import it.unisa.application.sottosistemi.GestioneServizi.service.MakeUpService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/hair%20stylingServlet")
public class HairStylingServlet extends HttpServlet {
    private MakeUpService makeUpService;  // Dichiarazione dell'istanza di MakeUpService

    @Override
    public void init() throws ServletException {
        super.init();
        // Inizializzazione del ServizioDAO
        ServizioDAO servizioDAO = new ServizioDAO(); // Assicurati di avere un costruttore senza parametri o un'istanza valida
        makeUpService = new MakeUpService(servizioDAO);  // Passa l'istanza di ServizioDAO al costruttore di MakeUpService
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recupera l'oggetto UtenteAcquirente dalla sessione
        UtenteAcquirente user = (UtenteAcquirente) request.getSession().getAttribute("user");

        // Se l'utente non è loggato, reindirizza alla pagina di login
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/loginPage");
        } else {
            // Recupera i servizi dal MakeUpService
            Map<String, List<Servizio>> serviziPerTipo = makeUpService.getServiziPerTipo();

            // Se "hair styling" esiste come tipo di servizio, prendi i servizi relativi
            List<Servizio> serviziHairStyling = serviziPerTipo.get("hair styling");

            // Imposta i servizi di hair styling come attributo nella request
            request.setAttribute("serviziPerTipo", Map.of("hair styling", serviziHairStyling));

            // Inoltra la richiesta alla pagina hairstyling.jsp
            request.getRequestDispatcher("/WEB-INF/jsp/hairstyling.jsp").forward(request, response);
        }
    }
}