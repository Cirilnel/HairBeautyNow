package it.unisa.application.model.entity;

import java.util.Date;

public class MetodoDiPagamento {
    private String nCarta; //PK
    private Date dataScadenza;
    private String nomeIntestatario;
    private int cvv;
    private String indirizzo;


    @Override
    public String toString() {
        return "MetodoDiPagamento{" +
                "nCarta='" + nCarta + '\'' +
                ", dataScadenza=" + dataScadenza +
                ", nomeIntestatario='" + nomeIntestatario + '\'' +
                ", cvv=" + cvv +
                ", indirizzo='" + indirizzo + '\'' +
                '}';
    }

    public MetodoDiPagamento(Date dataScadenza, String nCarta, String nomeIntestatario, String indirizzo, int cvv) {
        this.dataScadenza = dataScadenza;
        this.nCarta = nCarta;
        this.nomeIntestatario = nomeIntestatario;
        this.indirizzo = indirizzo;
        this.cvv = cvv;
    }

    public String getnCarta() {
        return nCarta;
    }

    public void setnCarta(String nCarta) {
        this.nCarta = nCarta;
    }

    public Date getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(Date dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public String getNomeIntestatario() {
        return nomeIntestatario;
    }

    public void setNomeIntestatario(String nomeIntestatario) {
        this.nomeIntestatario = nomeIntestatario;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }
}
