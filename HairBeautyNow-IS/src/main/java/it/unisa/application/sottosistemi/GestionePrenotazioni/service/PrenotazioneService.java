package it.unisa.application.sottosistemi.GestionePrenotazioni.service;

import it.unisa.application.model.dao.FasciaOrariaDAO;
import it.unisa.application.model.dao.PrenotazioneDAO;
import it.unisa.application.model.dao.ProfessionistaDAO;
import it.unisa.application.model.dao.SedeDAO;
import it.unisa.application.model.entity.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PrenotazioneService {
    private final PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO(); // Aggiungi il DAO per Prenotazione
    private ProfessionistaDAO professionistaDAO = new ProfessionistaDAO();
    private final FasciaOrariaDAO fasciaOrariaDAO = new FasciaOrariaDAO();

    private final SedeDAO sedeDAO = new SedeDAO(); // Inizializzazione del DAO

    public List<Sede> getSediByCitta(String cittaSelezionata) {
       return sedeDAO.getSediByCitta(cittaSelezionata);
    }
    public List<Prenotazione> getPrenotazioniAttive(int sedeId) throws SQLException {
        List<Professionista> professionisti = professionistaDAO.getProfessionistiBySede(sedeId);
        return prenotazioneDAO.getPrenotazioniByProfessionisti(professionisti);
    }
    public Set<String> getCittaDisponibili(List<Sede> sedi) {
        return sedi.stream().map(Sede::getCitta).collect(Collectors.toSet());
    }
    public List<Sede> getAllSedi() {
        return sedeDAO.getAllSedi();
    }
    public String getCittaUtente(UtenteAcquirente user) {
        return user != null ? user.getCitta() : null;
    }
    public void addPrenotazione(Prenotazione prenotazione) throws SQLException {
        prenotazioneDAO.addPrenotazione(prenotazione); // Supponiamo che nel DAO ci sia il metodo insertPrenotazione
    }
    public String rimuoviPrenotazione(int prenotazioneId) throws SQLException {
        Prenotazione prenotazione = prenotazioneDAO.getPrenotazioneById(prenotazioneId);
        boolean success = prenotazioneDAO.rimuoviPrenotazione(prenotazioneId);

        if (success) {
            LocalDate giorno = prenotazione.getData().toLocalDate();
            String fascia = prenotazione.getData().toLocalTime().toString();
            FasciaOraria fasciaOraria = fasciaOrariaDAO.getFasciaByProfessionistaAndGiorno(prenotazione.getProfessionistaId(), giorno, fascia);

            if (fasciaOraria != null) {
                fasciaOrariaDAO.aggiornaFasciaOraria(fasciaOraria);
            }
            return "Prenotazione rimossa con successo!";
        }
        return "Errore durante la rimozione della prenotazione.";
    }
}
