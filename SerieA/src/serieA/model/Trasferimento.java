package serieA.model;

import javafx.beans.property.*;

public class Trasferimento {
    private StringProperty giocatore;
    private StringProperty squadraProvenienza;
    private StringProperty squadraDestinazione;
    private DoubleProperty importo;
    private StringProperty data;

    public Trasferimento(String giocatore, String provenienza, String destinazione,
                         double importo, String data) {
        this.giocatore = new SimpleStringProperty(giocatore);
        this.squadraProvenienza = new SimpleStringProperty(provenienza);
        this.squadraDestinazione = new SimpleStringProperty(destinazione);
        this.importo = new SimpleDoubleProperty(importo);
        this.data = new SimpleStringProperty(data);
    }

    public String getGiocatore() { return giocatore.get(); }
    public String getSquadraProvenienza() { return squadraProvenienza.get(); }
    public String getSquadraDestinazione() { return squadraDestinazione.get(); }
    public double getImporto() { return importo.get(); }
    public String getData() { return data.get(); }

    public StringProperty giocatoreProperty() { return giocatore; }
    public StringProperty squadraProvenienzaProperty() { return squadraProvenienza; }
    public StringProperty squadraDestinazioneProperty() { return squadraDestinazione; }
    public DoubleProperty importoProperty() { return importo; }
    public StringProperty dataProperty() { return data; }
}
