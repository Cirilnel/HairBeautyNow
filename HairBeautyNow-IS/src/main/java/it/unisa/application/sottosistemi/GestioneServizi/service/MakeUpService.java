package it.unisa.application.sottosistemi.GestioneServizi.service;

import it.unisa.application.model.dao.ServizioDAO;
import it.unisa.application.model.entity.Servizio;
import java.util.*;

public class MakeUpService {

    private ServizioDAO servizioDAO;
    public MakeUpService() {}
    public MakeUpService(ServizioDAO servizioDAO) {this.servizioDAO = servizioDAO;}

    // Metodo per ottenere tutti i servizi dal DB
    public List<Servizio> getAllServiziWithDescription() {
        return servizioDAO.getAllWithDescription(); // Delegato al DAO
    }

    // Metodo per ottenere i servizi raggruppati per tipo
    public Map<String, List<Servizio>> getServiziPerTipo() {
        return servizioDAO.getServiziPerTipo(); // Delegato al DAO
    }

    // Metodo per ottenere il prezzo di un servizio per nome
    public double getPrezzoByNome(String nome) {
        Servizio servizio = servizioDAO.getByNome(nome);
        return (servizio != null) ? servizio.getPrezzo() : 0.0; // Se il servizio non esiste, restituisce 0.0
    }
}
