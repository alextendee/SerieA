package serieA.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;

public class Squadra {
    private StringProperty nome;
    private IntegerProperty punti;
    private IntegerProperty partiteGiocate;
    private IntegerProperty partiteVinte;
    private IntegerProperty partitePareg;
    private IntegerProperty partitePerse;
    private IntegerProperty golFatti;
    private IntegerProperty golSubiti;
    private DoubleProperty bilancioCorrente;
    private DoubleProperty bilancioIniziale;
    private DoubleProperty entrate;
    private DoubleProperty uscite;
    private StringProperty logo;

    private ObservableList<Giocatore> rosa = FXCollections.observableArrayList();
    private ObservableList<Trasferimento> storicoTrasferimenti = FXCollections.observableArrayList();

    public Squadra(String nome, int punti, int pG, int pV, int pPar, int pPer,
                   int gF, int gS, double bilancioIniziale, String logo) {
        this.nome = new SimpleStringProperty(nome);
        this.punti = new SimpleIntegerProperty(punti);
        this.partiteGiocate = new SimpleIntegerProperty(pG);
        this.partiteVinte = new SimpleIntegerProperty(pV);
        this.partitePareg = new SimpleIntegerProperty(pPar);
        this.partitePerse = new SimpleIntegerProperty(pPer);
        this.golFatti = new SimpleIntegerProperty(gF);
        this.golSubiti = new SimpleIntegerProperty(gS);
        this.bilancioIniziale = new SimpleDoubleProperty(bilancioIniziale);
        this.bilancioCorrente = new SimpleDoubleProperty(bilancioIniziale);
        this.entrate = new SimpleDoubleProperty(0);
        this.uscite = new SimpleDoubleProperty(0);
        this.logo = new SimpleStringProperty(logo);
    }

    // Getters
    public String getNome() { return nome.get(); }
    public int getPunti() { return punti.get(); }
    public int getPartiteGiocate() { return partiteGiocate.get(); }
    public int getPartiteVinte() { return partiteVinte.get(); }
    public int getPartitePareg() { return partitePareg.get(); }
    public int getPartitePerse() { return partitePerse.get(); }
    public int getGolFatti() { return golFatti.get(); }
    public int getGolSubiti() { return golSubiti.get(); }
    public double getBilancioCorrente() { return bilancioCorrente.get(); }
    public double getBilancioIniziale() { return bilancioIniziale.get(); }
    public double getEntrate() { return entrate.get(); }
    public double getUscite() { return uscite.get(); }
    public boolean isInDebito() { return bilancioCorrente.get() < 0; }
    public String getLogo() { return logo.get(); }

    // Setters
    public void setBilancioCorrente(double v) { bilancioCorrente.set(v); }

    // Properties
    public StringProperty nomeProperty() { return nome; }
    public IntegerProperty puntiProperty() { return punti; }
    public IntegerProperty partiteGiocateProperty() { return partiteGiocate; }
    public IntegerProperty partiteVinteProperty() { return partiteVinte; }
    public IntegerProperty partiteParegProperty() { return partitePareg; }
    public IntegerProperty partitePerseProperty() { return partitePerse; }
    public IntegerProperty golFattiProperty() { return golFatti; }
    public IntegerProperty golSubitiProperty() { return golSubiti; }
    public DoubleProperty bilancioCorrenteProperty() { return bilancioCorrente; }

    // Rosa e trasferimenti
    public ObservableList<Giocatore> getRosa() { return rosa; }
    public ObservableList<Trasferimento> getStoricoTrasferimenti() { return storicoTrasferimenti; }

    public void aggiungiGiocatore(Giocatore g) { rosa.add(g); }
    public void rimuoviGiocatore(Giocatore g) { rosa.remove(g); }

    public void registraVendita(String nomeGiocatore, String squadraAcquirente, double importo) {
        LocalDate oggi = LocalDate.now();
        storicoTrasferimenti.add(new Trasferimento(nomeGiocatore, this.getNome(),
                squadraAcquirente, importo, oggi.toString()));
        entrate.set(entrate.get() + importo);
        bilancioCorrente.set(bilancioCorrente.get() + importo);
    }

    public void registraAcquisto(String nomeGiocatore, String squadraVenditrice, double importo) {
        LocalDate oggi = LocalDate.now();
        storicoTrasferimenti.add(new Trasferimento(nomeGiocatore, squadraVenditrice,
                this.getNome(), importo, oggi.toString()));
        uscite.set(uscite.get() + importo);
        bilancioCorrente.set(bilancioCorrente.get() - importo);
    }

    @Override
    public String toString() { return nome.get(); }
}
