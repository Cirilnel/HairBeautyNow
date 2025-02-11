package it.unisa.application.sottosistemi.GestioneSede.service;

import it.unisa.application.model.dao.ProfessionistaDAO;
import it.unisa.application.model.dao.SedeDAO;
import it.unisa.application.model.entity.Professionista;
import it.unisa.application.model.entity.Sede;

import java.util.List;

public class GestioneProfessionistaService {

    private ProfessionistaDAO professionistaDAO = new ProfessionistaDAO();
    private SedeDAO sedeDAO = new SedeDAO();


    public GestioneProfessionistaService(ProfessionistaDAO professionistaDAO, SedeDAO sedeDAO) {
        this.professionistaDAO = professionistaDAO;
        this.sedeDAO = sedeDAO;
    }
    public GestioneProfessionistaService() {}
    public Sede getSedeById(Integer sedeId) {
        return sedeDAO.getSedeById(sedeId);
    }
    public void assumiProfessionista(Professionista professionista) {
        System.out.println(professionista.getSedeId());
        professionistaDAO.insertProfessionista(professionista);
    }
    public String rimuoviProfessionista(int professionistaId) {
        // Usa il DAO mockato iniettato nel servizio
        boolean successo = professionistaDAO.rimuoviProfessionista(professionistaId);

        if (successo) {
            return "successo";
        } else {
            return "Errore durante la rimozione del professionista o ci sono prenotazioni attive.";
        }
    }


    public List<Professionista> getProfessionistiBySede(int sedeId) {
        return professionistaDAO.getProfessionistiBySede(sedeId);
    }
}
