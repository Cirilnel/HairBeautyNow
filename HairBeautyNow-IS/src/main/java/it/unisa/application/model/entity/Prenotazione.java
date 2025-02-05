package it.unisa.application.model.entity;

import java.time.LocalDateTime;

public class Prenotazione {
    private int id; // PK, auto-increment
    private String servizioName; // FK
    private int professionistaId; // FK
    private LocalDateTime data; // Usato LocalDateTime al posto di Date
    private String username; // FK associato all'utente che ha fatto la prenotazione
    private double prezzo; // Prezzo della prenotazione

    // Costruttore aggiornato con LocalDateTime
    public Prenotazione(String servizioName, int professionistaId, LocalDateTime data, String username, double prezzo) {
        this.servizioName = servizioName;
        this.professionistaId = professionistaId;
        this.data = data;
        this.username = username;
        this.prezzo = prezzo;
    }

    // Getter e Setter per l'ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;  // L'ID viene impostato dal database dopo l'inserimento
    }

    // Getter e Setter per il nome del servizio
    public String getServizioName() {
        return servizioName;
    }

    public void setServizioName(String servizioName) {
        this.servizioName = servizioName;
    }

    // Getter e Setter per l'ID del professionista
    public int getProfessionistaId() {
        return professionistaId;
    }

    public void setProfessionistaId(int professionistaId) {
        this.professionistaId = professionistaId;
    }

    // Getter e Setter per la data della prenotazione
    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    // Getter e Setter per l'username (associato all'utente che ha fatto la prenotazione)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter e Setter per il prezzo
    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    // toString
    @Override
    public String toString() {
        return "Prenotazione{" +
                "id=" + id +
                ", servizioName='" + servizioName + '\'' +
                ", professionistaId=" + professionistaId +
                ", data=" + data +
                ", username='" + username + '\'' +
                ", prezzo=" + prezzo +
                '}';
    }
}
