package it.unisa.application.model.entity;

import java.util.Date;

public class Prenotazione {
    private int id; //PK
    private String servizioName; // FK
    private int professionistaId; // FK
    private Date data;

    // Costruttore
    public Prenotazione(int id, String servizioId, int professionistaId, Date data) {
        this.id = id;
        this.servizioName = servizioId;
        this.professionistaId = professionistaId;
        this.data = data;
    }

    // Getter e Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServizioName() {
        return servizioName;
    }

    public void setServizioName(String servizioName) {
        this.servizioName = servizioName;
    }

    public int getProfessionistaId() {
        return professionistaId;
    }

    public void setProfessionistaId(int professionistaId) {
        this.professionistaId = professionistaId;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    // toString
    @Override
    public String toString() {
        return "Prenotazione{" +
                "id=" + id +
                ", servizioId=" + servizioName +
                ", professionistaId=" + professionistaId +
                ", data=" + data +
                '}';
    }
}
