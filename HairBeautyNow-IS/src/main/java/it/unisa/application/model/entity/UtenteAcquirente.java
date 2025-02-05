package it.unisa.application.model.entity;

public class UtenteAcquirente {
    private String username; //PK
    private String email;
    private String password;
    private String nome;
    private String cognome;
    private String citta;

    public UtenteAcquirente(String username, String email, String password, String nome, String cognome, String citta) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
        this.citta = citta;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }
    public String getCitta() { return citta; }
    public void setCitta(String citta) { this.citta = citta; }

    @Override
    public String toString() {
        return "UtenteAcquirente{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", citta='" + citta + '\'' +
                '}';
    }
}
