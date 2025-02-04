package it.unisa.application.model.entity;

import java.time.LocalDate;

public class FasciaOraria {
    private int id;
    private int professionistaId;
    private LocalDate giorno;  // Cambiato da Date a LocalDate
    private String fascia; // Es: "08:00-08:30"
    private boolean disponibile; // true = libero, false = occupato

    // Costruttore vuoto
    public FasciaOraria() {}

    // Costruttore con tutti i campi
    public FasciaOraria(int id, int professionistaId, LocalDate giorno, String fascia, boolean disponibile) {
        this.id = id;
        this.professionistaId = professionistaId;
        this.giorno = giorno;
        this.fascia = fascia;
        this.disponibile = disponibile;
    }

    // Getter e Setter per 'id'
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter e Setter per 'professionistaId'
    public int getProfessionistaId() {
        return professionistaId;
    }

    public void setProfessionistaId(int professionistaId) {
        this.professionistaId = professionistaId;
    }

    // Getter e Setter per 'giorno'
    public LocalDate getGiorno() {
        return giorno;
    }

    public void setGiorno(LocalDate giorno) {
        this.giorno = giorno;
    }

    // Getter e Setter per 'fascia'
    public String getFascia() {
        return fascia;
    }

    public void setFascia(String fascia) {
        this.fascia = fascia;
    }

    // Getter e Setter per 'disponibile'
    public boolean isDisponibile() {
        return disponibile;
    }

    public void setDisponibile(boolean disponibile) {
        this.disponibile = disponibile;
    }

    // Metodo per rappresentare l'oggetto come stringa
    @Override
    public String toString() {
        return "FasciaOraria [id=" + id + ", professionistaId=" + professionistaId + ", giorno=" + giorno + ", fascia=" + fascia + ", disponibile=" + disponibile + "]";
    }
}
