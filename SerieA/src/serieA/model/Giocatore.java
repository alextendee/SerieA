package serieA.model;

import javafx.beans.property.*;

public class Giocatore {
    private StringProperty nome;
    private StringProperty cognome;
    private StringProperty nazionalita;
    private IntegerProperty eta;
    private DoubleProperty altezza; // in cm
    private IntegerProperty gol;
    private IntegerProperty assist;
    private DoubleProperty valoreEuro; // in milioni €

    public Giocatore(String nome, String cognome, String nazionalita,
                     int eta, double altezza, int gol, int assist, double valoreEuro) {
        this.nome = new SimpleStringProperty(nome);
        this.cognome = new SimpleStringProperty(cognome);
        this.nazionalita = new SimpleStringProperty(nazionalita);
        this.eta = new SimpleIntegerProperty(eta);
        this.altezza = new SimpleDoubleProperty(altezza);
        this.gol = new SimpleIntegerProperty(gol);
        this.assist = new SimpleIntegerProperty(assist);
        this.valoreEuro = new SimpleDoubleProperty(valoreEuro);
    }

    // Getters
    public String getNome() { return nome.get(); }
    public String getCognome() { return cognome.get(); }
    public String getNazionalita() { return nazionalita.get(); }
    public int getEta() { return eta.get(); }
    public double getAltezza() { return altezza.get(); }
    public int getGol() { return gol.get(); }
    public int getAssist() { return assist.get(); }
    public double getValoreEuro() { return valoreEuro.get(); }

    // Setters
    public void setNome(String v) { nome.set(v); }
    public void setCognome(String v) { cognome.set(v); }
    public void setNazionalita(String v) { nazionalita.set(v); }
    public void setEta(int v) { eta.set(v); }
    public void setAltezza(double v) { altezza.set(v); }
    public void setGol(int v) { gol.set(v); }
    public void setAssist(int v) { assist.set(v); }
    public void setValoreEuro(double v) { valoreEuro.set(v); }

    // Properties
    public StringProperty nomeProperty() { return nome; }
    public StringProperty cognomeProperty() { return cognome; }
    public StringProperty nazionalitaProperty() { return nazionalita; }
    public IntegerProperty etaProperty() { return eta; }
    public DoubleProperty altezzaProperty() { return altezza; }
    public IntegerProperty golProperty() { return gol; }
    public IntegerProperty assistProperty() { return assist; }
    public DoubleProperty valoreEuroProperty() { return valoreEuro; }

    public String getNomeCompleto() { return nome.get() + " " + cognome.get(); }

    @Override
    public String toString() {
        return getNomeCompleto();
    }
}
