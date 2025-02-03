package it.unisa.application.sottosistemi.GestioneServizi.view;

import it.unisa.application.model.entity.Servizio;
import it.unisa.application.model.entity.UtenteAcquirente;
import it.unisa.application.sottosistemi.GestioneServizi.service.MakeUpService;  // Import the MakeUpService
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/make%20upServlet")
public class MakeUpServlet extends HttpServlet {
    private MakeUpService makeUpService;  // Declare the MakeUpService instance

    @Override
    public void init() throws ServletException {
        super.init();
        makeUpService = new MakeUpService();  // Initialize the MakeUpService
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recupera l'oggetto UtenteAcquirente dalla sessione
        UtenteAcquirente user = (UtenteAcquirente) request.getSession().getAttribute("user");

        // Se l'utente non è loggato, reindirizza alla pagina di loginPage
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/loginPage");
        } else {
            // Recupera i servizi di make up dal MakeUpService
            Map<String, List<Servizio>> serviziPerTipo = makeUpService.getServiziPerTipo();

            // Se "make up" esiste come tipo di servizio, prendi i servizi relativi
            List<Servizio> serviziMakeUp = serviziPerTipo.get("make up");

            // Imposta i servizi di make up come attributo nella request
            request.setAttribute("serviziPerTipo", Map.of("make up", serviziMakeUp));

            // Inoltra la richiesta alla pagina makeup.jsp
            request.getRequestDispatcher("/WEB-INF/jsp/makeup.jsp").forward(request, response);
        }
    }
}
