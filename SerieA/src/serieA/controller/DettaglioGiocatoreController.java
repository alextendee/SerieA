package serieA.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import serieA.Main;
import serieA.model.Gestione;
import serieA.model.Giocatore;
import serieA.model.Squadra;

import java.util.Optional;

public class DettaglioGiocatoreController {
    @FXML private Label lblNomeCompleto;
    @FXML private Label lblNazionalita;
    @FXML private Label lblEta;
    @FXML private Label lblAltezza;
    @FXML private Label lblGol;
    @FXML private Label lblAssist;
    @FXML private Label lblValore;
    @FXML private Label lblSquadra;
    @FXML private Button btnCompra;
    @FXML private Button btnVendi;

    private Main main;
    private Gestione gestione;
    private Giocatore giocatore;
    private Squadra squadraGiocatore;
    private boolean isAdmin;
    private String squadraAdmin;
    private Stage stage;

    public void setMain(Main main, Gestione gestione, Giocatore giocatore,
                        Squadra squadraGiocatore, boolean isAdmin, String squadraAdmin, Stage stage) {
        this.main = main;
        this.gestione = gestione;
        this.giocatore = giocatore;
        this.squadraGiocatore = squadraGiocatore;
        this.isAdmin = isAdmin;
        this.squadraAdmin = squadraAdmin;
        this.stage = stage;

        aggiornaDati();

        boolean mio = isAdmin && squadraGiocatore.getNome().equals(squadraAdmin);
        boolean altrui = isAdmin && !squadraGiocatore.getNome().equals(squadraAdmin);

        btnVendi.setVisible(mio);
        btnVendi.setManaged(mio);
        btnCompra.setVisible(altrui);
        btnCompra.setManaged(altrui);
    }

    private void aggiornaDati() {
        lblNomeCompleto.setText(giocatore.getNome() + " " + giocatore.getCognome());
        lblNazionalita.setText("Nazionalità: " + giocatore.getNazionalita());
        lblEta.setText("Età: " + giocatore.getEta() + " anni");
        lblAltezza.setText("Altezza: " + (int) giocatore.getAltezza() + " cm");
        lblGol.setText("Gol: " + giocatore.getGol());
        lblAssist.setText("Assist: " + giocatore.getAssist());
        lblValore.setText(String.format("Valore di mercato: %.1f M€", giocatore.getValoreEuro()));
        lblSquadra.setText("Squadra attuale: " + squadraGiocatore.getNome());
    }

    @FXML
    private void handleCompra() {
        Squadra squadraAcquirente = gestione.getSquadraByNome(squadraAdmin);
        double valore = giocatore.getValoreEuro();

        // Avviso se acquirente in debito
        if (squadraAcquirente.isInDebito()) {
            Alert avviso = new Alert(Alert.AlertType.WARNING,
                    "Attenzione: la tua squadra è in debito!\n"
                    + String.format("Bilancio attuale: %.1f M€\nVuoi procedere?", squadraAcquirente.getBilancioCorrente()),
                    ButtonType.YES, ButtonType.NO);
            avviso.setTitle("Squadra in debito");
            Optional<ButtonType> res = avviso.showAndWait();
            if (!res.isPresent() || res.get() != ButtonType.YES) return;
        }

        // Dialogo inserimento prezzo
        TextInputDialog dialog = new TextInputDialog(String.format("%.1f", valore));
        dialog.setTitle("Offerta di acquisto");
        dialog.setHeaderText("Acquisto di " + giocatore.getNomeCompleto());
        dialog.setContentText(String.format("Valore di mercato: %.1f M€\nInserisci la tua offerta (M€):", valore));
        Optional<String> risultato = dialog.showAndWait();
        if (!risultato.isPresent()) return;

        double offerta;
        try {
            offerta = Double.parseDouble(risultato.get().replace(",", "."));
        } catch (NumberFormatException e) {
            mostraErrore("Inserisci un numero valido.");
            return;
        }

        if (offerta < valore) {
            mostraInfoDialog("Offerta rifiutata",
                    "L'offerta è inferiore al valore di mercato.\nOfferta minima accettabile: " + String.format("%.1f M€", valore));
            return;
        }

        boolean venditoreInDebito = squadraGiocatore.isInDebito();
        boolean accettata = gestione.offertaAccettata(offerta, valore, venditoreInDebito);

        if (accettata) {
            gestione.eseguiTrasferimento(giocatore, squadraGiocatore, squadraAcquirente, offerta);
            mostraInfoDialog("Trasferimento completato!",
                    "" + giocatore.getNomeCompleto() + " è ora nella tua squadra!\n"
                    + String.format("Importo pagato: %.1f M€", offerta));
            aggiornaDati();
            btnCompra.setVisible(false);
            btnCompra.setManaged(false);
        } else {
            mostraInfoDialog("Offerta rifiutata",
                    "La squadra avversaria ha rifiutato la tua offerta di " + String.format("%.1f M€.", offerta));
        }
    }

    @FXML
    private void handleVendi() {
        Squadra squadraVenditrice = gestione.getSquadraByNome(squadraAdmin);
        Squadra squadraOfferente = gestione.getSquadraRandom(squadraAdmin);
        if (squadraOfferente == null) { mostraErrore("Nessuna squadra disponibile."); return; }

        double valore = giocatore.getValoreEuro();
        // Prima offerta: tra -20% e +50%
        double primaOfferta = valore * (0.80 + Math.random() * 0.70);
        primaOfferta = Math.round(primaOfferta * 10.0) / 10.0;

        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION,
                String.format("La squadra %s offre %.1f M€ per %s\n(Valore mercato: %.1f M€)\n\nAccetti?",
                        squadraOfferente.getNome(), primaOfferta, giocatore.getNomeCompleto(), valore),
                ButtonType.YES, ButtonType.NO);
        alert1.setTitle("Offerta ricevuta");
        Optional<ButtonType> risposta1 = alert1.showAndWait();

        if (risposta1.isPresent() && risposta1.get() == ButtonType.YES) {
            completaVendita(squadraVenditrice, squadraOfferente, primaOfferta);
            return;
        }

        // 30% fine trattative, 70% seconda offerta
        if (Math.random() < 0.30) {
            mostraInfoDialog("Trattativa chiusa", "La squadra ha deciso di interrompere le trattative.");
            return;
        }

        // Seconda offerta: deve essere >= prima
        double minSeconda = primaOfferta * 1.01;
        double maxSeconda = valore * 1.60;
        double secondaOfferta = minSeconda + Math.random() * (maxSeconda - minSeconda);
        secondaOfferta = Math.round(secondaOfferta * 10.0) / 10.0;
        if (secondaOfferta < minSeconda) secondaOfferta = minSeconda;

        Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION,
                String.format("Contro-offerta da %s: %.1f M€\n(Migliore della precedente %.1f M€)\n\nAccetti?",
                        squadraOfferente.getNome(), secondaOfferta, primaOfferta),
                ButtonType.YES, ButtonType.NO);
        alert2.setTitle("Seconda offerta");
        Optional<ButtonType> risposta2 = alert2.showAndWait();

        if (risposta2.isPresent() && risposta2.get() == ButtonType.YES) {
            completaVendita(squadraVenditrice, squadraOfferente, secondaOfferta);
        } else {
            mostraInfoDialog("Trattativa chiusa", "Trattativa terminata. Il giocatore rimane nella tua squadra.");
        }
    }

    private void completaVendita(Squadra venditrice, Squadra acquirente, double importo) {
        gestione.eseguiTrasferimento(giocatore, venditrice, acquirente, importo);
        mostraInfoDialog("Vendita completata!",
                String.format("%s venduto a %s per %.1f M€",
                        giocatore.getNomeCompleto(), acquirente.getNome(), importo));
        stage.close();
    }

    private void mostraInfoDialog(String titolo, String messaggio) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, messaggio, ButtonType.OK);
        a.setTitle(titolo);
        a.setHeaderText(null);
        a.showAndWait();
    }

    private void mostraErrore(String messaggio) {
        Alert a = new Alert(Alert.AlertType.ERROR, messaggio, ButtonType.OK);
        a.setTitle("Errore");
        a.showAndWait();
    }

    @FXML
    private void handleChiudi() { stage.close(); }
}
