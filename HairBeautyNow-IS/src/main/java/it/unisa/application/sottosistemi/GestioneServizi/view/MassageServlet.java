package it.unisa.application.sottosistemi.GestioneServizi.view;

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

@WebServlet("/massaggiServlet")
public class MassageServlet extends HttpServlet {
    private MakeUpService makeUpService;  // Dichiarazione dell'istanza di MakeUpService

    @Override
    public void init() throws ServletException {
        super.init();
        makeUpService = new MakeUpService();  // Inizializzazione del MakeUpService
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

            // Se "massage" esiste come tipo di servizio, prendi i servizi relativi
            List<Servizio> serviziMassage = serviziPerTipo.get("massaggi");

            // Imposta i servizi di massaggio come attributo nella request
            request.setAttribute("serviziPerTipo", Map.of("massaggi", serviziMassage));

            // Inoltra la richiesta alla pagina massage.jsp
            request.getRequestDispatcher("/WEB-INF/jsp/massage.jsp").forward(request, response);
        }
    }
}
