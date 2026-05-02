package serieA.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import serieA.Main;
import serieA.model.Gestione;
import serieA.model.Utente;

import java.util.Optional;

public class LoginController {
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblErrore;
    @FXML private Hyperlink linkRegistrati;

    private Main main;
    private Gestione gestione;

    public void setMain(Main main, Gestione gestione) {
        this.main = main;
        this.gestione = gestione;
    }

    @FXML
    private void initialize() {
        lblErrore.setText("");
    }

    @FXML
    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            lblErrore.setText("Inserisci username e password.");
            return;
        }

        Optional<Utente> result = gestione.login(username, password);
        if (result.isPresent()) {
            Utente u = result.get();
            main.mostraClassifica(u.getUsername(), u.isAdmin(), u.getSquadraAmministrata());
        } else {
            lblErrore.setText("Credenziali errate. Riprova.");
        }
    }

    @FXML
    private void handleRegistrati() {
        main.mostraRegistrazione();
    }
}
