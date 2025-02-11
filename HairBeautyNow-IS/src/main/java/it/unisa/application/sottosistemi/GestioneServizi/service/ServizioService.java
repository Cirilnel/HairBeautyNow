package it.unisa.application.sottosistemi.GestioneServizi.service;

import it.unisa.application.model.dao.ServizioDAO;
import it.unisa.application.model.entity.Servizio;
import java.util.*;

public class ServizioService {
    private ServizioDAO servizioDAO;

    public ServizioService() {
        this.servizioDAO = new ServizioDAO(); // Instanzia il DAO
    }

    public ServizioService(ServizioDAO servizioDAOMock) {
        this.servizioDAO = servizioDAOMock;
    }

    // Metodo per ottenere tutti i servizi dal DB
    public List<Servizio> getAllServizi() {
        return servizioDAO.getAll(); // Chiama il DAO per ottenere i servizi
    }

    // Metodo per ottenere il prezzo di un servizio dato il nome
    public double getPrezzoByNome(String nomeServizio) {
        Servizio servizio = servizioDAO.getByNome(nomeServizio); // Chiama il DAO per ottenere il servizio
        if (servizio != null) {
            return servizio.getPrezzo();
        }
        return 0.0; // Se il servizio non viene trovato
    }

    // Metodo per ottenere i servizi raggruppati per tipo
    public Map<String, List<Servizio>> getServiziPerTipo() {
        return servizioDAO.getServiziPerTipo();
    }
}
