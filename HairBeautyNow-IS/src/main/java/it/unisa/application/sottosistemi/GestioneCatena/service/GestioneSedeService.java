package it.unisa.application.sottosistemi.GestioneCatena.service;


import it.unisa.application.model.dao.SedeDAO;
import it.unisa.application.model.entity.Sede;

public class GestioneSedeService {

    private SedeDAO sedeDAO = new SedeDAO();

    public int creaSede(Sede nuovaSede) {
        try {
            // Chiamata al DAO per inserire la sede
            int sedeID = sedeDAO.insertSedeAndReturnID(nuovaSede);

            // Se l'inserimento non ha prodotto un ID valido (<= 0), consideriamo il fallimento
            if (sedeID <= 0) {
                return 0; // Restituire 0 per simulare l'errore
            }

            return sedeID;
        } catch (Exception e) {
            // In caso di errore (esempio: connessione al database fallita), restituiamo 0
            System.err.println("Errore durante la creazione della sede: " + e.getMessage());
            return 0;
        }
    }





}

