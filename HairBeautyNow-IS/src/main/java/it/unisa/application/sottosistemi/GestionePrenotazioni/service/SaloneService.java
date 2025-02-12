package it.unisa.application.sottosistemi.GestionePrenotazioni.service;

import it.unisa.application.model.dao.FasciaOrariaDAO;
import it.unisa.application.model.dao.ProfessionistaDAO;
import it.unisa.application.model.entity.FasciaOraria;
import it.unisa.application.model.entity.Professionista;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaloneService {

    private final ProfessionistaDAO professionistaDAO;
    private final FasciaOrariaDAO fasciaOrariaDAO;

    // Costruttore vuoto per l'uso normale nel sito
    public SaloneService() {
        this.professionistaDAO = new ProfessionistaDAO();
        this.fasciaOrariaDAO = new FasciaOrariaDAO();
    }

    // Costruttore con parametri per facilitare i test
    public SaloneService(ProfessionistaDAO professionistaDAO, FasciaOrariaDAO fasciaOrariaDAO) {
        this.professionistaDAO = professionistaDAO;
        this.fasciaOrariaDAO = fasciaOrariaDAO;
    }

    // Ottieni professionisti per un dato salone
    public List<Professionista> getProfessionistiBySalone(int saloneId) {
        return professionistaDAO.getProfessionistiBySede(saloneId);
    }

    // Ottieni fasce orarie per professionista
    public Map<LocalDate, List<String>> getFasceOrarieByProfessionista(int professionistaId) throws SQLException {
        Map<LocalDate, List<String>> fasceOrarieByDay = new HashMap<>();

        // Otteniamo le fasce orarie per il professionista
        List<FasciaOraria> fasceOrarie = fasciaOrariaDAO.getFasceOrarieByProfessionista(professionistaId);

        // Aggiungiamo le fasce orarie disponibili alla mappa
        for (FasciaOraria fascia : fasceOrarie) {
            if (fascia.isDisponibile()) {
                fasceOrarieByDay.putIfAbsent(fascia.getGiorno(), new ArrayList<>());
                fasceOrarieByDay.get(fascia.getGiorno()).add(fascia.getFascia());
            }
        }

        return fasceOrarieByDay;
    }
}
