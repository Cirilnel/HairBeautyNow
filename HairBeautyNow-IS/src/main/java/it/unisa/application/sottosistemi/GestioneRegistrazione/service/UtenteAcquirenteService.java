package it.unisa.application.sottosistemi.GestioneRegistrazione.service;

import it.unisa.application.model.dao.UtenteAcquirenteDAO;
import it.unisa.application.model.entity.UtenteAcquirente;

public class UtenteAcquirenteService {

    private UtenteAcquirenteDAO dao;

    // Costruttore che accetta un DAO come parametro
    public UtenteAcquirenteService(UtenteAcquirenteDAO dao) {
        this.dao = dao;
    }

    // Metodo per creare un nuovo utente
    public boolean createUser(UtenteAcquirente utenteAcquirente) {
        // Chiamata al DAO per inserire l'utente nel database
        return dao.insert(utenteAcquirente);
    }
}
