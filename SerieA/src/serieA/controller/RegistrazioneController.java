package serieA.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import serieA.Main;
import serieA.model.Gestione;
import serieA.model.Utente;

public class RegistrazioneController {
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfermaPassword;
    @FXML private Label lblErrore;
    @FXML private Label lblSuccesso;

    private Main main;
    private Gestione gestione;
    private Stage stage;

    public void setMain(Main main, Gestione gestione, Stage stage) {
        this.main = main;
        this.gestione = gestione;
        this.stage = stage;
    }

    @FXML
    private void initialize() {
        lblErrore.setText("");
        lblSuccesso.setText("");
    }

    @FXML
    private void handleRegistra() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText();
        String conferma = txtConfermaPassword.getText();
        lblErrore.setText("");
        lblSuccesso.setText("");

        if (username.isEmpty() || password.isEmpty()) {
            lblErrore.setText("Compila tutti i campi.");
            return;
        }
        if (!password.equals(conferma)) {
            lblErrore.setText("Le password non coincidono.");
            return;
        }
        if (password.length() < 4) {
            lblErrore.setText("Password troppo corta (min. 4 caratteri).");
            return;
        }
        if (gestione.usernameEsiste(username)) {
            lblErrore.setText("Username già in uso. Scegline un altro.");
            return;
        }

        gestione.registraUtente(new Utente(username, password, false, null));
        lblSuccesso.setText("Registrazione completata! Puoi ora fare il login.");
        txtUsername.clear();
        txtPassword.clear();
        txtConfermaPassword.clear();
    }

    @FXML
    private void handleTornaLogin() {
        stage.close();
        main.mostraLogin();
    }
}
