package it.unisa.application.model.entity;

public class Sede {

    private String indirizzo;
    private String nome;
    private String citta;
    private int id; //PK



    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Sede{" +
                "indirizzo='" + indirizzo + '\'' +
                ", nome='" + nome + '\'' +
                ", città='" + citta + '\'' +
                ", id=" + id +
                '}';
    }

    public Sede(String indirizzo, String nome, String città, int id) {
        this.indirizzo = indirizzo;
        this.nome = nome;
        this.citta = città;
        this.id = id;
    }
}
