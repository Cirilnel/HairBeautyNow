package it.unisa.application.model.entity;

public class Servizio {
    private String nome; //PK
    private String descrizione;
    private String tipo;
    private int durata;
    private double prezzo;

    public Servizio(String nome, double prezzo, int durata, String tipo, String descrizione) {
        this.nome = nome;
        this.prezzo = prezzo;
        this.durata = durata;
        this.tipo = tipo;
        this.descrizione = descrizione;
    }
    public Servizio(String nome, double prezzo, String tipo) {
        this.nome = nome;
        this.prezzo = prezzo;
        this.tipo = tipo;
    }
    @Override
    public String toString() {
        return "Servizio{" +
                "nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", tipo='" + tipo + '\'' +
                ", durata=" + durata +
                ", prezzo=" + prezzo +
                '}';
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getDurata() {
        return durata;
    }

    public void setDurata(int durata) {
        this.durata = durata;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }
}
