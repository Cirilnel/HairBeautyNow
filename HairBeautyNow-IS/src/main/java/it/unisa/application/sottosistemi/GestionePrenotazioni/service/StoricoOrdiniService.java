package it.unisa.application.sottosistemi.GestionePrenotazioni.service;

import it.unisa.application.model.dao.PrenotazioneDAO;
import it.unisa.application.model.dao.ProfessionistaDAO;
import it.unisa.application.model.entity.Prenotazione;

import java.sql.SQLException;
import java.util.List;

public class StoricoOrdiniService {

    private final PrenotazioneDAO prenotazioneDAO;
    private final ProfessionistaDAO professionistaDAO;

    // Costruttore vuoto (per il sito o per altre necessità)
    public StoricoOrdiniService() {
        this.prenotazioneDAO = new PrenotazioneDAO();
        this.professionistaDAO = new ProfessionistaDAO();
    }

    // Costruttore con DAO passati (per il testing)
    public StoricoOrdiniService(PrenotazioneDAO prenotazioneDAO, ProfessionistaDAO professionistaDAO) {
        this.prenotazioneDAO = prenotazioneDAO;
        this.professionistaDAO = professionistaDAO;
    }

    public List<Prenotazione> getPrenotazioniByUsername(String username) throws SQLException {
        return prenotazioneDAO.getPrenotazioniByUsername(username);
    }

    public double getPrezzoByServizio(String servizioName) {
        return professionistaDAO.getPrezzoByServizio(servizioName);
    }

    public String getIndirizzoBySedeId(int sedeId) {
        return professionistaDAO.getIndirizzoBySedeId(sedeId);
    }
}
