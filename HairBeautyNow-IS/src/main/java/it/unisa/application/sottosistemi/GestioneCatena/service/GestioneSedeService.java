package it.unisa.application.sottosistemi.GestioneCatena.service;


import it.unisa.application.model.dao.SedeDAO;
import it.unisa.application.model.entity.Sede;

public class GestioneSedeService {

    private SedeDAO sedeDAO = new SedeDAO();

    // Metodo per creare una nuova sede
    public int creaSede(Sede nuovaSede) {
        return sedeDAO.insertSedeAndReturnID(nuovaSede);
    }

}

