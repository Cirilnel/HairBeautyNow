package it.unisa.application.model.entity;

import java.time.LocalDate;
import java.util.Date;
public class MetodoDiPagamento {
    private String nCarta; // PK
    private LocalDate dataScadenza;
    private String nomeIntestatario;
    private int cvv;
    private String indirizzo;
    private String username; // FK (associato all'utente che possiede il metodo di pagamento)
    private String metodoPagamento; // Nuovo campo per tipo di metodo di pagamento (PayPal, VISA, etc.)
    private String email; // Campo email per PayPal (può essere null)

    // Costruttore aggiornato
    public MetodoDiPagamento(String nCarta, LocalDate dataScadenza, String nomeIntestatario, String indirizzo, int cvv, String username, String metodoPagamento, String email) {
        this.nCarta = nCarta;
        this.dataScadenza = dataScadenza;
        this.nomeIntestatario = nomeIntestatario;
        this.indirizzo = indirizzo;
        this.cvv = cvv;
        this.username = username; // Assegna l'utente associato
        this.metodoPagamento = metodoPagamento; // Assegna il tipo di metodo di pagamento
        this.email = email; // Assegna l'email se presente
    }

    // Getter e Setter per email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setnCarta(String nCarta) {
        this.nCarta = nCarta;
    }

    public void setDataScadenza(LocalDate dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public void setNomeIntestatario(String nomeIntestatario) {
        this.nomeIntestatario = nomeIntestatario;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public String getnCarta() {
        return nCarta;
    }

    public LocalDate getDataScadenza() {
        return dataScadenza;
    }

    public String getNomeIntestatario() {
        return nomeIntestatario;
    }

    public int getCvv() {
        return cvv;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public String getUsername() {
        return username;
    }
// Getter e Setter per gli altri attributi
    // [Inserisci qui i metodi getter e setter per gli altri campi...]

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
                ", email='" + email + '\'' +  // Aggiungi l'email alla stampa
                '}';
    }
}