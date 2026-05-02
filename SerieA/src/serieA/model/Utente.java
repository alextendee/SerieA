package serieA.model;

public class Utente {
    private String username;
    private String password;
    private boolean admin;
    private String squadraAmministrata; // null se utente normale

    public Utente(String username, String password, boolean admin, String squadraAmministrata) {
        this.username = username;
        this.password = password;
        this.admin = admin;
        this.squadraAmministrata = squadraAmministrata;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public boolean isAdmin() { return admin; }
    public String getSquadraAmministrata() { return squadraAmministrata; }
}
