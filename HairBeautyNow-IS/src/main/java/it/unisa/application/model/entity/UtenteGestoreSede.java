package it.unisa.application.model.entity;

public class UtenteGestoreSede {
    private String usernameUGS; //PK
    private String password;
    private int prenotazioneID; //FK
    private int professionsitaID; //FK


    public UtenteGestoreSede(String username, String password, int professionsitaID, int prenotazioneID) {
        this.usernameUGS = username;
        this.password = password;
        this.professionsitaID = professionsitaID;
        this.prenotazioneID = prenotazioneID;
    }


    public String getUsernameUGS() {
        return usernameUGS;
    }

    public void setUsernameUGS(String usernameUGS) {
        this.usernameUGS = usernameUGS;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPrenotazioneID() {
        return prenotazioneID;
    }

    public void setPrenotazioneID(int prenotazioneID) {
        this.prenotazioneID = prenotazioneID;
    }

    public int getProfessionsitaID() {
        return professionsitaID;
    }

    public void setProfessionsitaID(int professionsitaID) {
        this.professionsitaID = professionsitaID;
    }

    @Override
    public String toString() {
        return "UtenteGestoreSede{" +
                "username='" + usernameUGS + '\'' +
                ", password='" + password + '\'' +
                ", prenotazioneID=" + prenotazioneID +
                ", professionsitaID=" + professionsitaID +
                '}';
    }
}
