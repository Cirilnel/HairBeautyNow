package it.unisa.application.sottosistemi.GestioneAccesso.service;


import it.unisa.application.model.dao.UtenteAcquirenteDAO;
import it.unisa.application.model.entity.UtenteAcquirente;

public class UtenteAcquirenteService {

    private UtenteAcquirenteDAO utenteAcquirenteDAO;

    public UtenteAcquirenteService() {
        this.utenteAcquirenteDAO = new UtenteAcquirenteDAO();
    }

    // Metodo per il login
    public UtenteAcquirente login(String email, String password) {
        return utenteAcquirenteDAO.getByEmailAndPassword(email, password);
    }
}
