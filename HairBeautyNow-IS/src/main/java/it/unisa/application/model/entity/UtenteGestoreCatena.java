package it.unisa.application.model.entity;

public class UtenteGestoreCatena {
    private String username; //PK
    private String password;


    public UtenteGestoreCatena(String password, String username) {
        this.password = password;
        this.username = username;
    }

    public UtenteGestoreCatena() {}
    @Override
    public String toString() {
        return "UtenteGestoreCatena{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
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


}
