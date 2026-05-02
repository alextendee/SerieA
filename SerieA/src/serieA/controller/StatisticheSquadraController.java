package serieA.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import serieA.Main;
import serieA.model.Gestione;
import serieA.model.Squadra;

public class StatisticheSquadraController {
    @FXML private Label lblNome;
    @FXML private Label lblPunti;
    @FXML private Label lblRecord;
    @FXML private Label lblGol;
    @FXML private Label lblBilancio;
    @FXML private Button btnBilancio;

    private Main main;
    private Gestione gestione;
    private Squadra squadra;
    private boolean isAdmin;
    private String squadraAdmin;
    private Stage stage;

    public void setMain(Main main, Gestione gestione, Squadra squadra,
                        boolean isAdmin, String squadraAdmin, Stage stage) {
        this.main = main;
        this.gestione = gestione;
        this.squadra = squadra;
        this.isAdmin = isAdmin;
        this.squadraAdmin = squadraAdmin;
        this.stage = stage;

        lblNome.setText(squadra.getNome());
        lblPunti.setText("Punti: " + squadra.getPunti());
        lblRecord.setText(String.format("V %d  P %d  S %d  (su %d partite)",
                squadra.getPartiteVinte(), squadra.getPartitePareg(),
                squadra.getPartitePerse(), squadra.getPartiteGiocate()));
        lblGol.setText(String.format("Gol fatti: %d  |  Gol subiti: %d  |  Differenza: %+d",
                squadra.getGolFatti(), squadra.getGolSubiti(),
                squadra.getGolFatti() - squadra.getGolSubiti()));

        // Bilancio visibile solo all'admin della squadra
        boolean puoVedereBilancio = isAdmin && squadra.getNome().equals(squadraAdmin);
        btnBilancio.setVisible(puoVedereBilancio);
        btnBilancio.setManaged(puoVedereBilancio);
    }

    @FXML
    private void handleRosa() {
        stage.close();
        main.mostraRosa(squadra, isAdmin, squadraAdmin);
    }

    @FXML
    private void handleStoico() {
        main.mostraStoricoTrasferimenti(squadra);
    }

    @FXML
    private void handleBilancio() {
        main.mostraBilancio(squadra);
    }

    @FXML
    private void handleChiudi() {
        stage.close();
    }
}
