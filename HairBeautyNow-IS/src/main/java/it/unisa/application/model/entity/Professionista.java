package it.unisa.application.model.entity;

import java.util.List;

public class Professionista {
    private int id; // PK
    private String nome;
    private int sedeId; // FK -> Sede
    private List<FasciaOraria> fasceOrarie; // Lista di fasce orarie

    // Costruttore
    public Professionista(int id, String nome, int sedeId, List<FasciaOraria> fasceOrarie) {
        this.id = id;
        this.nome = nome;
        this.sedeId = sedeId;
        this.fasceOrarie = fasceOrarie;
    }

    // Getter e Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getSedeId() {
        return sedeId;
    }

    public void setSedeId(int sedeId) {
        this.sedeId = sedeId;
    }

    public List<FasciaOraria> getFasceOrarie() {
        return fasceOrarie;
    }

    public void setFasceOrarie(List<FasciaOraria> fasceOrarie) {
        this.fasceOrarie = fasceOrarie;
    }

    @Override
    public String toString() {
        return "Professionista{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", sedeId=" + sedeId +
                ", fasceOrarie=" + fasceOrarie +
                '}';
    }
}
