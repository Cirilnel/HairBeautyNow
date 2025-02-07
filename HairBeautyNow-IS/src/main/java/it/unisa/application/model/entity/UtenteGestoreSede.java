package it.unisa.application.model.entity;

public class UtenteGestoreSede {
    private String usernameUGS; //PK
    private String password;
    private Integer sedeID; // FK referring to the Sede table

    // Constructor with updated parameters
    public UtenteGestoreSede(String usernameUGS, String password, int sedeID) {
        this.usernameUGS = usernameUGS;
        this.password = password;
        this.sedeID = sedeID;
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

    public Integer getSedeID() {
        return sedeID;
    }

    public void setSedeID(Integer sedeID) {
        this.sedeID = sedeID;
    }

    @Override
    public String toString() {
        return "UtenteGestoreSede{" +
                "usernameUGS='" + usernameUGS + '\'' +
                ", password='" + password + '\'' +
                ", sedeID=" + sedeID +
                '}';
    }
}
