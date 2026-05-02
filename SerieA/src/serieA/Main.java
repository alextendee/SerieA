package serieA;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import serieA.controller.LoginController;
import serieA.model.Gestione;
import serieA.model.Giocatore;
import serieA.model.Squadra;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    private Gestione gestione;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Serie A Manager");
        gestione = new Gestione();
        mostraLogin();
    }

    private void mostraErrore(String titolo, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

    private FXMLLoader caricaFxml(String nomefile) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        java.net.URL url = Main.class.getResource("view/" + nomefile);
        if (url == null) {
            throw new IOException("File FXML non trovato: view/" + nomefile
                    + "\nVerifica che i .fxml siano in src/serieA/view/");
        }
        loader.setLocation(url);
        return loader;
    }

    public void mostraLogin() {
        try {
            FXMLLoader loader = caricaFxml("Login.fxml");
            AnchorPane pane = loader.load();
            primaryStage.setScene(new Scene(pane, 450, 430));
            primaryStage.setResizable(true);
            primaryStage.setTitle("Serie A Manager - Login");
            LoginController ctrl = loader.getController();
            ctrl.setMain(this, gestione);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mostraErrore("Errore avvio", e.toString());
        }
    }

    public void mostraClassifica(String username, boolean isAdmin, String squadraAdmin) {
        try {
            FXMLLoader loader = caricaFxml("Classifica.fxml");
            AnchorPane pane = loader.load();
            primaryStage.setScene(new Scene(pane, 1000, 650));
            primaryStage.setResizable(true);
            primaryStage.setTitle("Serie A - Classifica");
            serieA.controller.ClassificaController ctrl = loader.getController();
            ctrl.setMain(this, gestione, username, isAdmin, squadraAdmin);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mostraErrore("Errore apertura Classifica", e.toString());
        }
    }

    public void mostraStatisticheSquadra(Squadra squadra, boolean isAdmin, String squadraAdmin) {
        try {
            FXMLLoader loader = caricaFxml("StatisticheSquadra.fxml");
            AnchorPane pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Statistiche - " + squadra.getNome());
            stage.setScene(new Scene(pane, 700, 500));
            stage.setResizable(true);
            serieA.controller.StatisticheSquadraController ctrl = loader.getController();
            ctrl.setMain(this, gestione, squadra, isAdmin, squadraAdmin, stage);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mostraErrore("Errore apertura Statistiche", e.toString());
        }
    }

    public void mostraRosa(Squadra squadra, boolean isAdmin, String squadraAdmin) {
        try {
            FXMLLoader loader = caricaFxml("Rosa.fxml");
            AnchorPane pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Rosa - " + squadra.getNome());
            stage.setScene(new Scene(pane, 900, 600));
            stage.setResizable(true);
            serieA.controller.RosaController ctrl = loader.getController();
            ctrl.setMain(this, gestione, squadra, isAdmin, squadraAdmin, stage);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mostraErrore("Errore apertura Rosa", e.toString());
        }
    }

    public void mostraGiocatore(Giocatore giocatore, Squadra squadraGiocatore,
                                boolean isAdmin, String squadraAdmin) {
        try {
            FXMLLoader loader = caricaFxml("DettaglioGiocatore.fxml");
            AnchorPane pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Giocatore - " + giocatore.getNome() + " " + giocatore.getCognome());
            stage.setScene(new Scene(pane, 700, 550));
            stage.setResizable(true);
            serieA.controller.DettaglioGiocatoreController ctrl = loader.getController();
            ctrl.setMain(this, gestione, giocatore, squadraGiocatore, isAdmin, squadraAdmin, stage);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mostraErrore("Errore apertura Giocatore", e.toString());
        }
    }

    public void mostraStoricoTrasferimenti(Squadra squadra) {
        try {
            FXMLLoader loader = caricaFxml("StoricoTrasferimenti.fxml");
            AnchorPane pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Storico Trasferimenti - " + squadra.getNome());
            stage.setScene(new Scene(pane, 850, 550));
            stage.setResizable(true);
            serieA.controller.StoricoTrasferimentiController ctrl = loader.getController();
            ctrl.setMain(gestione, squadra, stage);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mostraErrore("Errore apertura Storico", e.toString());
        }
    }

    public void mostraBilancio(Squadra squadra) {
        try {
            FXMLLoader loader = caricaFxml("Bilancio.fxml");
            AnchorPane pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Bilancio - " + squadra.getNome());
            stage.setScene(new Scene(pane, 600, 450));
            stage.setResizable(true);
            serieA.controller.BilancioController ctrl = loader.getController();
            ctrl.setMain(gestione, squadra, stage);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mostraErrore("Errore apertura Bilancio", e.toString());
        }
    }

    public void mostraRegistrazione() {
        try {
            FXMLLoader loader = caricaFxml("Registrazione.fxml");
            AnchorPane pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Registrazione Nuovo Utente");
            stage.setScene(new Scene(pane, 450, 420));
            stage.setResizable(true);
            serieA.controller.RegistrazioneController ctrl = loader.getController();
            ctrl.setMain(this, gestione, stage);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mostraErrore("Errore apertura Registrazione", e.toString());
        }
    }

    public Stage getPrimaryStage() { return primaryStage; }

    public static void main(String[] args) {
        launch(args);
    }
}
