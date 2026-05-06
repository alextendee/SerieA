package serieA.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import serieA.model.Gestione;
import serieA.model.Squadra;
import javafx.scene.image.ImageView;

import java.io.File;


public class StatisticheSquadraController {
    @FXML private Label lblNome;
    @FXML private Label lblPunti;
    @FXML private Label lblRecord;
    @FXML private Label lblGol;
    @FXML private Label lblBilancio;
    @FXML private Button btnBilancio;
    @FXML private ImageView imgLogo;

    private Gestione gestione;
    private Squadra squadra;
    private boolean isAdmin;
    private String squadraAdmin;
    private boolean isSuperAdmin;
    private Stage stage;
    public void initData(Gestione gestione, Squadra squadra,
                        boolean isAdmin, String squadraAdmin, boolean isSuperAdmin, Stage stage) {
        this.gestione = gestione;
        this.squadra = squadra;
        this.isAdmin = isAdmin;
        this.squadraAdmin = squadraAdmin;
        this.isSuperAdmin = isSuperAdmin;
        this.stage = stage;

        lblNome.setText(squadra.getNome());
        lblPunti.setText("Punti: " + squadra.getPunti());
        lblRecord.setText(String.format("V %d  P %d  S %d  (su %d partite)",
                squadra.getPartiteVinte(), squadra.getPartitePareg(),
                squadra.getPartitePerse(), squadra.getPartiteGiocate()));
        lblGol.setText(String.format("Gol fatti: %d  |  Gol subiti: %d  |  Differenza: %+d",
                squadra.getGolFatti(), squadra.getGolSubiti(),
                squadra.getGolFatti() - squadra.getGolSubiti()));
        String percorsoLogo = squadra.getLogo();
        if (percorsoLogo != null && !percorsoLogo.isEmpty()) {
            File fileLogo = new File(percorsoLogo);
            if (fileLogo.exists()) {
                imgLogo.setImage(new Image(fileLogo.toURI().toString()));
            }
        }


        // Bilancio visibile all'admin della squadra o al super admin
        boolean puoVedereBilancio = isSuperAdmin || (isAdmin && squadra.getNome().equals(squadraAdmin));
        btnBilancio.setVisible(puoVedereBilancio);
        btnBilancio.setManaged(puoVedereBilancio);
    }

    @FXML
    private void handleRosa() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/serieA/view/Rosa.fxml"));
            javafx.scene.layout.AnchorPane pane = loader.load();
            Stage s = new Stage();
            s.setTitle("Rosa - " + squadra.getNome());
            s.setScene(new javafx.scene.Scene(pane));
            s.sizeToScene();
            serieA.controller.RosaController ctrl = loader.getController();
            ctrl.initData(gestione, squadra, isAdmin, squadraAdmin, isSuperAdmin, s);
            stage.close();
            s.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleStoico() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/serieA/view/StoricoTrasferimenti.fxml"));
            javafx.scene.layout.AnchorPane pane = loader.load();
            Stage s = new Stage();
            s.setTitle("Storico Trasferimenti - " + squadra.getNome());
            s.setScene(new javafx.scene.Scene(pane));
            s.sizeToScene();
            serieA.controller.StoricoTrasferimentiController ctrl = loader.getController();
            ctrl.setMain(gestione, squadra, s);
            s.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBilancio() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/serieA/view/Bilancio.fxml"));
            javafx.scene.layout.AnchorPane pane = loader.load();
            Stage s = new Stage();
            s.setTitle("Bilancio - " + squadra.getNome());
            s.setScene(new javafx.scene.Scene(pane));
            s.sizeToScene();
            serieA.controller.BilancioController ctrl = loader.getController();
            ctrl.setMain(gestione, squadra, s);
            s.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleChiudi() {
        stage.close();
    }
}
