package it.unisa.application.sottosistemi.GestioneAccesso.service;

import it.unisa.application.model.dao.UtenteAcquirenteDAO;
import it.unisa.application.model.dao.UtenteGestoreCatenaDAO;
import it.unisa.application.model.dao.UtenteGestoreSedeDAO;
import it.unisa.application.model.entity.UtenteAcquirente;
import it.unisa.application.model.entity.UtenteGestoreCatena;
import it.unisa.application.model.entity.UtenteGestoreSede;
import jakarta.servlet.http.HttpServletRequest;

public class UtenteService {
    private UtenteAcquirenteDAO utenteAcquirenteDAO;
    private UtenteGestoreCatenaDAO utenteGestoreCatenaDAO;
    private UtenteGestoreSedeDAO utenteGestoreSedeDAO;

    public UtenteService() {
        this.utenteAcquirenteDAO = new UtenteAcquirenteDAO();
        this.utenteGestoreCatenaDAO = new UtenteGestoreCatenaDAO();
        this.utenteGestoreSedeDAO = new UtenteGestoreSedeDAO();
    }

    public Object login(String username, String password) { // Cambiato 'usernameOrEmail' in 'username'
        // Controllo in UtenteAcquirente (per username, non più email)
        UtenteAcquirente utenteAcquirente = utenteAcquirenteDAO.getByUsernameAndPassword(username, password); // Cambiato da getByEmailAndPassword
        if (utenteAcquirente != null) {
            return utenteAcquirente;
        }

        // Controllo in UtenteGestoreCatena (username)
        UtenteGestoreCatena utenteGestoreCatena = utenteGestoreCatenaDAO.getByUsername(username,password);
        if (utenteGestoreCatena != null ) {
            return utenteGestoreCatena;
        }

        // Controllo in UtenteGestoreSede (username)
        UtenteGestoreSede utenteGestoreSede = utenteGestoreSedeDAO.getByUsername(username,password);
        if (utenteGestoreSede != null && utenteGestoreSede.getPassword().equals(password)) {
            return utenteGestoreSede;
        }

        return null; // Nessun utente trovato
    }
    public void logout(HttpServletRequest request) {
        request.getSession().invalidate(); // Invalida la sessione
    }


}
