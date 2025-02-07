package it.unisa.application.sottosistemi.GestioneCatena.service;

import it.unisa.application.model.dao.UtenteGestoreSedeDAO;
import it.unisa.application.model.entity.UtenteGestoreSede;

import java.util.List;

public class GestioneGestoreService {

    private UtenteGestoreSedeDAO utenteGestoreSedeDAO = new UtenteGestoreSedeDAO();

    // Metodo per creare un nuovo gestore
    public boolean creaGestore(UtenteGestoreSede nuovoGestore) {
        return utenteGestoreSedeDAO.insert(nuovoGestore);
    }

    // Metodo per assegnare una sede a un gestore
    public void assegnaSede(String usernameUGS, int sedeID) {
        utenteGestoreSedeDAO.assegnaSede(usernameUGS, sedeID);
    }

    // Metodo per licenziare un gestore (rimuovere la sede)
    public void licenziaGestore(String usernameUGS) {
        utenteGestoreSedeDAO.licenziaGestore(usernameUGS);
    }

    // Metodo per recuperare i gestori senza una sede
    public List<UtenteGestoreSede> getGestoriSenzaSede() {
        return utenteGestoreSedeDAO.getGestoriSenzaSede();
    }

    // Metodo per recuperare i gestori con una sede assegnata
    public List<UtenteGestoreSede> getGestoriConSede() {
        return utenteGestoreSedeDAO.getGestoriConSede();
    }
}
