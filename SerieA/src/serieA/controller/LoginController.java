package serieA.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import serieA.model.Gestione;
import serieA.model.Utente;

import java.util.Optional;

public class LoginController {
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblErrore;
    @FXML private Hyperlink linkRegistrati;

    private Gestione gestione;

    // Initialize controller with model
    public void initData(Gestione gestione) {
        this.gestione = gestione;
    }

    // Helper to load fxml for this controller
    private FXMLLoader caricaFxml(String nomefile) throws java.io.IOException {
        FXMLLoader loader = new FXMLLoader();
        java.net.URL url = getClass().getResource("/serieA/view/" + nomefile);
        if (url == null) throw new java.io.IOException("File FXML non trovato: view/" + nomefile);
        loader.setLocation(url);
        return loader;
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
            // Load Classifica view directly
            try {
                FXMLLoader loader = caricaFxml("Classifica.fxml");
                javafx.scene.layout.AnchorPane pane = loader.load();
                javafx.stage.Stage stage = (javafx.stage.Stage) txtUsername.getScene().getWindow();
                stage.setScene(new javafx.scene.Scene(pane, 1000, 650));
                stage.setTitle("Serie A - Classifica");
                serieA.controller.ClassificaController ctrl = loader.getController();
                ctrl.initData(gestione, u.getUsername(), u.isAdmin(), u.getSquadraAmministrata(), u.isSuperAdmin(), stage);
            } catch (Exception e) {
                e.printStackTrace();
                lblErrore.setText("Errore apertura Classifica: " + e.getMessage());
            }
        } else {
            lblErrore.setText("Credenziali errate. Riprova.");
        }
    }

    @FXML
    private void handleRegistrati() {
        try {
            FXMLLoader loader = caricaFxml("Registrazione.fxml");
            javafx.scene.layout.AnchorPane pane = loader.load();
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Registrazione Nuovo Utente");
            stage.setScene(new javafx.scene.Scene(pane, 450, 420));
            serieA.controller.RegistrazioneController ctrl = loader.getController();
            ctrl.initData(gestione, stage);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            lblErrore.setText("Errore apertura Registrazione: " + e.getMessage());
        }
    }
}
