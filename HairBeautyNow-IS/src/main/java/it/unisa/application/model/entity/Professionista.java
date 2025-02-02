package it.unisa.application.model.entity;

import java.util.Map;
import java.util.HashMap;

public class Professionista {
    private int id; //PK
    private String nome;
    private Map<String, Boolean> fasciaOraria;

    // Costruttore
    public Professionista(int id, String nome, Map<String, Boolean> fasciaOraria) {
        this.id = id;
        this.nome = nome;
        this.fasciaOraria = fasciaOraria != null ? fasciaOraria : new HashMap<>();
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

    public Map<String, Boolean> getFasciaOraria() {
        return fasciaOraria;
    }

    public void setFasciaOraria(Map<String, Boolean> fasciaOraria) {
        this.fasciaOraria = fasciaOraria;
    }

    // toString
    @Override
    public String toString() {
        return "Professionista{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", fasciaOraria=" + fasciaOraria +
                '}';
    }
}
