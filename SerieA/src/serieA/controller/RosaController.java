package serieA.controller;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import serieA.Main;
import serieA.model.Gestione;
import serieA.model.Giocatore;
import serieA.model.Squadra;

public class RosaController {
    @FXML private Label lblSquadra;
    @FXML private TextField txtRicerca;
    @FXML private TableView<Giocatore> tabellaRosa;
    @FXML private TableColumn<Giocatore, String> colNome;
    @FXML private TableColumn<Giocatore, String> colCognome;
    @FXML private TableColumn<Giocatore, String> colNazionalita;
    @FXML private TableColumn<Giocatore, Number> colEta;
    @FXML private TableColumn<Giocatore, Number> colAltezza;
    @FXML private TableColumn<Giocatore, Number> colGol;
    @FXML private TableColumn<Giocatore, Number> colAssist;
    @FXML private TableColumn<Giocatore, Number> colValore;

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

        lblSquadra.setText("Rosa - " + squadra.getNome());

        FilteredList<Giocatore> filtrata = new FilteredList<>(squadra.getRosa(), g -> true);
        txtRicerca.textProperty().addListener((obs, old, val) -> {
            filtrata.setPredicate(g -> {
                if (val == null || val.isEmpty()) return true;
                String lower = val.toLowerCase();
                return g.getNome().toLowerCase().contains(lower)
                        || g.getCognome().toLowerCase().contains(lower)
                        || g.getNazionalita().toLowerCase().contains(lower);
            });
        });
        tabellaRosa.setItems(filtrata);

        tabellaRosa.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, newG) -> {
                    if (newG != null) {
                        tabellaRosa.getSelectionModel().clearSelection();
                        main.mostraGiocatore(newG, squadra, isAdmin, squadraAdmin);
                    }
                });
    }

    @FXML
    private void initialize() {
        tabellaRosa.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colNome.setCellValueFactory(c -> c.getValue().nomeProperty());
        colCognome.setCellValueFactory(c -> c.getValue().cognomeProperty());
        colNazionalita.setCellValueFactory(c -> c.getValue().nazionalitaProperty());
        colEta.setCellValueFactory(c -> c.getValue().etaProperty());
        colAltezza.setCellValueFactory(c -> c.getValue().altezzaProperty());
        colGol.setCellValueFactory(c -> c.getValue().golProperty());
        colAssist.setCellValueFactory(c -> c.getValue().assistProperty());
        colValore.setCellValueFactory(c -> c.getValue().valoreEuroProperty());

        // Formatta valore come M€
        colValore.setCellFactory(col -> new TableCell<Giocatore, Number>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("%.1f M€", item.doubleValue()));
            }
        });
    }

    @FXML
    private void handleChiudi() { stage.close(); }
}
