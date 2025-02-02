package it.unisa.application.model.entity;

public class Sede {

    private String indirizzo;
    private String nome;
    private String città;
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

    public String getCittà() {
        return città;
    }

    public void setCittà(String città) {
        this.città = città;
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
                ", città='" + città + '\'' +
                ", id=" + id +
                '}';
    }

    public Sede(String indirizzo, String nome, String città, int id) {
        this.indirizzo = indirizzo;
        this.nome = nome;
        this.città = città;
        this.id = id;
    }
}
