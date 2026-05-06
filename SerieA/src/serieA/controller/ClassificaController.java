package serieA.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import serieA.model.Gestione;
import serieA.model.Squadra;

import java.util.Comparator;

public class ClassificaController {
    @FXML private TableView<Squadra> tabellaClassifica;
    @FXML private TableColumn<Squadra, String> colNome;
    @FXML private TableColumn<Squadra, Number> colPunti;
    @FXML private TableColumn<Squadra, Number> colPG;
    @FXML private TableColumn<Squadra, Number> colPV;
    @FXML private TableColumn<Squadra, Number> colPP;
    @FXML private TableColumn<Squadra, Number> colPL;
    @FXML private TableColumn<Squadra, Number> colGF;
    @FXML private TableColumn<Squadra, Number> colGS;
    @FXML private Label lblUtente;
    @FXML private Label lblRuolo;

    private Gestione gestione;
    private boolean isAdmin;
    private String squadraAdmin;

    // Initialize controller with model and the stage that hosts this view
    public void initData(Gestione gestione, String username,
                         boolean isAdmin, String squadraAdmin, Stage stage) {
        this.gestione = gestione;
        this.isAdmin = isAdmin;
        this.squadraAdmin = squadraAdmin;

        lblUtente.setText("Benvenuto, " + username);
        lblRuolo.setText(isAdmin ? "Admin - " + squadraAdmin : "Utente");

        // Sort list alphabetically
        FXCollections.sort(
                gestione.getSquadre(),
                new Comparator<Squadra>() {
                    @Override
                    public int compare(Squadra a, Squadra b) {
                        return a.getNome().compareToIgnoreCase(b.getNome());
                    }
                }
        );
        tabellaClassifica.setItems(gestione.getSquadre());
    }

    @FXML
    private void initialize() {
        tabellaClassifica.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colNome.setCellValueFactory(c -> c.getValue().nomeProperty());
        colPunti.setCellValueFactory(c -> c.getValue().puntiProperty());
        colPG.setCellValueFactory(c -> c.getValue().partiteGiocateProperty());
        colPV.setCellValueFactory(c -> c.getValue().partiteVinteProperty());
        colPP.setCellValueFactory(c -> c.getValue().partiteParegProperty());
        colPL.setCellValueFactory(c -> c.getValue().partitePerseProperty());
        colGF.setCellValueFactory(c -> c.getValue().golFattiProperty());
        colGS.setCellValueFactory(c -> c.getValue().golSubitiProperty());

        // Center numeric columns
        for (TableColumn<Squadra, Number> col : java.util.Arrays.asList(
                colPunti, colPG, colPV, colPP, colPL, colGF, colGS)) {
            col.setCellFactory(tc -> new TableCell<Squadra, Number>() {
                @Override
                protected void updateItem(Number item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(String.valueOf(item.intValue()));
                        setStyle("-fx-alignment: CENTER;");
                    }
                }
            });
        }

        tabellaClassifica.setOnMouseClicked(event -> {
            Squadra selezionata = tabellaClassifica.getSelectionModel().getSelectedItem();
            if (selezionata != null) {
                tabellaClassifica.getSelectionModel().clearSelection();
                // Load Statistiche view
                try {
                    FXMLLoader loader = new FXMLLoader();
                    java.net.URL url = getClass().getResource("/serieA/view/StatisticheSquadra.fxml");
                    loader.setLocation(url);
                    AnchorPane pane = loader.load();
                    Stage s = new Stage();
                    s.setTitle("Statistiche - " + selezionata.getNome());
                    s.setScene(new javafx.scene.Scene(pane, 700, 500));
                    StatisticheSquadraController ctrl = loader.getController();
                    ctrl.initData(gestione, selezionata, isAdmin, squadraAdmin, s);
                    s.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            java.net.URL url = getClass().getResource("/serieA/view/Login.fxml");
            loader.setLocation(url);
            AnchorPane pane = loader.load();
            Stage stage = (Stage) lblUtente.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(pane, 450, 430));
            LoginController ctrl = loader.getController();
            ctrl.initData(gestione);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
