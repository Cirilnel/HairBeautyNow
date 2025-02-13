package it.unisa.application.sottosistemi.GestioneCatena.view;

import it.unisa.application.sottosistemi.GestioneCatena.service.GestioneGestoreService;
import it.unisa.application.model.entity.UtenteGestoreSede;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/creaGestore")
public class CreaGestoreServlet extends HttpServlet {

    private GestioneGestoreService gestioneGestoreService;

    public CreaGestoreServlet(GestioneGestoreService gestioneGestoreService) {
        this.gestioneGestoreService = gestioneGestoreService;
    }

    public CreaGestoreServlet() {
        this.gestioneGestoreService = new GestioneGestoreService(); // Costruttore di default
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Rimuove l'attributo "gestoreCreato" dalla sessione se la richiesta lo richiede
        if (request.getParameter("removeGestoreCreato") != null) {
            request.getSession().removeAttribute("gestoreCreato");
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // Forward alla pagina JSP per la creazione del gestore
        request.getRequestDispatcher("/WEB-INF/jsp/creaGestore.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("usernameUGS");
        String password = request.getParameter("password");

        // Se i campi sono vuoti, restituisci un errore
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/creaGestore?errore=Tutti i campi sono obbligatori");
            return;
        }

        // Crea il nuovo UtenteGestoreSede con ID sede corretto (ad esempio 1)
        UtenteGestoreSede nuovoGestore = new UtenteGestoreSede(username, password, null); // Usando l'ID valido per la sede

        // Chiama il servizio per creare il gestore
        boolean success = gestioneGestoreService.creaGestore(nuovoGestore);
        System.out.println("Success: " + success);  // Aggiungi questa linea per il debug

        if (success) {
            // Se la creazione ha avuto successo, aggiungi l'attributo alla sessione
            request.getSession().setAttribute("gestoreCreato", "ok");
            response.sendRedirect(request.getContextPath() + "/homeCatena");
        } else {
            // Se fallisce (ad esempio, se il nome utente è già in uso), mostra il messaggio di errore
            response.sendRedirect(request.getContextPath() + "/creaGestore?errore=Username gi&agrave; in uso");
        }
    }
}
