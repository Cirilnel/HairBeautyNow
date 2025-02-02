package it.unisa.application.sottosistemi.GestioneServizi.view;

import it.unisa.application.model.entity.Servizio;
import it.unisa.application.sottosistemi.GestioneServizi.service.ServizioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/servizi")
public class ServizioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ServizioService servizioService;

    @Override
    public void init() throws ServletException {
        super.init();
        servizioService = new ServizioService(); // inizializza il servizio
        System.out.println("ServizioServlet inizializzato correttamente!");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("ServizioServlet: doGet chiamato");

        // Recupera tutti i servizi dal servizio
        List<Servizio> listaServizi = servizioService.getAllServizi();
        System.out.println("Servizi recuperati: " + listaServizi.size());

        // Raggruppa i servizi per tipo
        Map<String, List<Servizio>> serviziPerTipo = servizioService.getServiziPerTipo();
        System.out.println("Servizi raggruppati per tipo: " + serviziPerTipo.size());

        // Imposta l'attributo nella request per la visualizzazione
        request.setAttribute("serviziPerTipo", serviziPerTipo);

        // Inoltra la richiesta alla JSP per la visualizzazione
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/servizi.jsp");
        dispatcher.forward(request, response);
    }
}