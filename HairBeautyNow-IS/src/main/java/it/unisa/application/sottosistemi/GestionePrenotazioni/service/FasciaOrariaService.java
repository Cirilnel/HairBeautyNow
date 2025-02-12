package it.unisa.application.sottosistemi.GestionePrenotazioni.service;

import it.unisa.application.model.dao.FasciaOrariaDAO;
import it.unisa.application.model.entity.FasciaOraria;

import java.sql.SQLException;
import java.time.LocalDate;

public class FasciaOrariaService {

    private final FasciaOrariaDAO fasciaOrariaDAO;

    // Costruttore con iniezione del mock del DAO (per test)
    public FasciaOrariaService(FasciaOrariaDAO fasciaOrariaDAO) {
        this.fasciaOrariaDAO = fasciaOrariaDAO;
    }

    // Costruttore vuoto (per l'uso in produzione quando non si vuole iniettare il mock)
    public FasciaOrariaService() {
        this.fasciaOrariaDAO = new FasciaOrariaDAO();
    }

    // Metodo per recuperare una fascia oraria per un determinato professionista e giorno
    public FasciaOraria getFasciaOraria(int professionistaId, LocalDate giorno, String orario) {
        return fasciaOrariaDAO.getFasciaOraria(professionistaId, giorno, orario);
    }

    // Metodo per aggiornare la disponibilità della fascia oraria
    public void updateFasciaOraria(FasciaOraria fasciaOraria) throws SQLException {
        fasciaOrariaDAO.aggiornaFasciaOraria(fasciaOraria);
    }
}
