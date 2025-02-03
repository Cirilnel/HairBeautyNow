package it.unisa.application.sottosistemi.GestioneRegistrazione.service;


import it.unisa.application.model.dao.UtenteAcquirenteDAO;
import it.unisa.application.model.entity.UtenteAcquirente;

public class UtenteAcquirenteService {

    // Metodo per creare un nuovo utente
    public boolean createUser(UtenteAcquirente utenteAcquirente) {
        UtenteAcquirenteDAO dao = new UtenteAcquirenteDAO();
        // Chiamata al DAO per inserire l'utente nel database
        return dao.insert(utenteAcquirente);
    }
}
