package it.unisa.application.sottosistemi.GestioneSede.service;

import it.unisa.application.model.dao.ProfessionistaDAO;
import it.unisa.application.model.dao.SedeDAO;
import it.unisa.application.model.entity.Professionista;
import it.unisa.application.model.entity.Sede;

import java.util.List;

public class GestioneProfessionistaService {

    private ProfessionistaDAO professionistaDAO = new ProfessionistaDAO();
    private SedeDAO sedeDAO = new SedeDAO();


    public Sede getSedeById(Integer sedeId) {
        return sedeDAO.getSedeById(sedeId);
    }
    public void assumiProfessionista(Professionista professionista) {
        System.out.println(professionista.getSedeId());
        professionistaDAO.insertProfessionista(professionista);
    }
    public String rimuoviProfessionista(int professionistaId) {
        if (professionistaDAO.hasPrenotazioni(professionistaId)) {
            return "Prenotazioni esistenti per questo professionista.";
        }
        boolean success = professionistaDAO.rimuoviProfessionista(professionistaId);
        return success ? "Professionista rimosso con successo!" : "Errore durante la rimozione del professionista.";
    }
    public List<Professionista> getProfessionistiBySede(int sedeId) {
        return professionistaDAO.getProfessionistiBySede(sedeId);
    }
}
