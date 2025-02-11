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

    private final ProfessionistaDAO professionistaDAO = new ProfessionistaDAO();
    private final FasciaOrariaDAO fasciaOrariaDAO = new FasciaOrariaDAO();

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
                // Per ogni giorno, aggiungiamo la fascia oraria
                fasceOrarieByDay.putIfAbsent(fascia.getGiorno(), new ArrayList<>());
                fasceOrarieByDay.get(fascia.getGiorno()).add(fascia.getFascia());
            }
        }

        return fasceOrarieByDay;
    }

}