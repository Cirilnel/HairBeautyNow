package it.unisa.application.model.entity;

public class UtenteGestoreCatena {
    private String username; //PK
    private String password;
    private int n_SediGestite;
    private int sedeID; //FK
    private String usernameUGS; //FK

    public UtenteGestoreCatena(String password, int n_SediGestite, String username, int sedeID, String usernameUGS) {
        this.password = password;
        this.n_SediGestite = n_SediGestite;
        this.username = username;
        this.sedeID = sedeID;
        this.usernameUGS = usernameUGS;
    }


    @Override
    public String toString() {
        return "UtenteGestoreCatena{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", n_SediGestite=" + n_SediGestite +
                ", sedeID=" + sedeID +
                ", usernameUGS='" + usernameUGS + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getN_SediGestite() {
        return n_SediGestite;
    }

    public void setN_SediGestite(int n_SediGestite) {
        this.n_SediGestite = n_SediGestite;
    }

    public int getSedeID() {
        return sedeID;
    }

    public void setSedeID(int sedeID) {
        this.sedeID = sedeID;
    }

    public String getUsernameUGS() {
        return usernameUGS;
    }

    public void setUsernameUGS(String usernameUGS) {
        this.usernameUGS = usernameUGS;
    }
}
