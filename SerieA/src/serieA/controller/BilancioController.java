package serieA.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import serieA.model.Gestione;
import serieA.model.Squadra;

public class BilancioController {
    @FXML private Label lblSquadra;
    @FXML private Label lblBilancioIniziale;
    @FXML private Label lblBilancioCorrente;
    @FXML private Label lblEntrate;
    @FXML private Label lblUscite;
    @FXML private Label lblSaldo;
    @FXML private Label lblStatoDebito;

    private Gestione gestione;
    private Squadra squadra;
    private Stage stage;

    public void setMain(Gestione gestione, Squadra squadra, Stage stage) {
        this.gestione = gestione;
        this.squadra = squadra;
        this.stage = stage;
        aggiornaDati();
    }

    private void aggiornaDati() {
        lblSquadra.setText("Bilancio - " + squadra.getNome());
        lblBilancioIniziale.setText(String.format("%.1f M€", squadra.getBilancioIniziale()));
        lblBilancioCorrente.setText(String.format("%.1f M€", squadra.getBilancioCorrente()));
        lblEntrate.setText(String.format("+ %.1f M€", squadra.getEntrate()));
        lblUscite.setText(String.format("- %.1f M€", squadra.getUscite()));
        double saldo = squadra.getEntrate() - squadra.getUscite();
        lblSaldo.setText(String.format("%+.1f M€", saldo));
        if (squadra.isInDebito()) {
            lblStatoDebito.setText("SQUADRA IN DEBITO");
            lblStatoDebito.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        } else {
            lblStatoDebito.setText("Bilancio positivo");
            lblStatoDebito.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        }
    }

    @FXML
    private void handleChiudi() { stage.close(); }
}
