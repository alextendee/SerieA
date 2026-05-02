package serieA.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import serieA.model.Gestione;
import serieA.model.Squadra;
import serieA.model.Trasferimento;

public class StoricoTrasferimentiController {
    @FXML private Label lblSquadra;
    @FXML private TableView<Trasferimento> tabellaStoico;
    @FXML private TableColumn<Trasferimento, String> colGiocatore;
    @FXML private TableColumn<Trasferimento, String> colProvenienza;
    @FXML private TableColumn<Trasferimento, String> colDestinazione;
    @FXML private TableColumn<Trasferimento, Number> colImporto;
    @FXML private TableColumn<Trasferimento, String> colData;

    private Gestione gestione;
    private Squadra squadra;
    private Stage stage;

    public void setMain(Gestione gestione, Squadra squadra, Stage stage) {
        this.gestione = gestione;
        this.squadra = squadra;
        this.stage = stage;
        lblSquadra.setText("Storico Trasferimenti - " + squadra.getNome());
        tabellaStoico.setItems(squadra.getStoricoTrasferimenti());
    }

    @FXML
    private void initialize() {
        tabellaStoico.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colGiocatore.setCellValueFactory(c -> c.getValue().giocatoreProperty());
        colProvenienza.setCellValueFactory(c -> c.getValue().squadraProvenienzaProperty());
        colDestinazione.setCellValueFactory(c -> c.getValue().squadraDestinazioneProperty());
        colImporto.setCellValueFactory(c -> c.getValue().importoProperty());
        colData.setCellValueFactory(c -> c.getValue().dataProperty());

        colImporto.setCellFactory(col -> new TableCell<Trasferimento, Number>() {
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
