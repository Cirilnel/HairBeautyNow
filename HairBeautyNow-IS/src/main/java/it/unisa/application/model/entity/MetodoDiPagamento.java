package it.unisa.application.model.entity;

import java.util.Date;

public class MetodoDiPagamento {
    private String nCarta; // PK
    private Date dataScadenza;
    private String nomeIntestatario;
    private int cvv;
    private String indirizzo;
    private String username; // FK (associato all'utente che possiede il metodo di pagamento)
    private String metodoPagamento; // Nuovo campo per tipo di metodo di pagamento (PayPal, VISA, etc.)

    // Costruttore aggiornato
    public MetodoDiPagamento(String nCarta, Date dataScadenza, String nomeIntestatario, String indirizzo, int cvv, String username, String metodoPagamento) {
        this.nCarta = nCarta;
        this.dataScadenza = dataScadenza;
        this.nomeIntestatario = nomeIntestatario;
        this.indirizzo = indirizzo;
        this.cvv = cvv;
        this.username = username; // Assegna l'utente associato
        this.metodoPagamento = metodoPagamento; // Assegna il tipo di metodo di pagamento
    }


    // Getter e Setter per username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter e Setter per metodoPagamento
    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    // Getter e Setter per gli altri attributi
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

    // Metodo toString
    @Override
    public String toString() {
        return "MetodoDiPagamento{" +
                "nCarta='" + nCarta + '\'' +
                ", dataScadenza=" + dataScadenza +
                ", nomeIntestatario='" + nomeIntestatario + '\'' +
                ", cvv=" + cvv +
                ", indirizzo='" + indirizzo + '\'' +
                ", username='" + username + '\'' +
                ", metodoPagamento='" + metodoPagamento + '\'' +
                '}';
    }
}
