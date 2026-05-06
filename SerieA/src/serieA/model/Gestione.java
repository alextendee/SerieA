package serieA.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

public class Gestione {

    private ObservableList<Squadra> squadre = FXCollections.observableArrayList();
    private ObservableList<Utente>  utenti  = FXCollections.observableArrayList();

    private static final String DATA_DIR = "data/";

    public Gestione() {
        caricaUtenti();
        caricaSquadre();
        caricaGiocatori();
        caricaTrasferimenti();
    }

    private BufferedReader apriCsv(String nomeFile) throws IOException {
        File f = new File(DATA_DIR + nomeFile);
        return new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
    }

    private File csvFile(String nomeFile) {
        return new File(DATA_DIR + nomeFile);
    }


    // CARICAMENTO UTENTI
    // formato: username,password,admin,squadra,superadmin
    private void caricaUtenti() {
        try (BufferedReader br = apriCsv("utenti.csv")) {
            String riga;
            boolean primaRiga = true;
            while ((riga = br.readLine()) != null) {
                riga = riga.trim();
                if (riga.isEmpty()) continue;
                if (primaRiga) { primaRiga = false; continue; } // salta header
                String[] col = riga.split(",", -1);
                if (col.length < 4) continue;
                String username    = col[0].trim();
                String password    = col[1].trim();
                boolean isAdmin    = Boolean.parseBoolean(col[2].trim());
                String squadra     = col[3].trim();
                boolean isSuperAdmin = col.length >= 5 && Boolean.parseBoolean(col[4].trim());
                utenti.add(new Utente(username, password, isAdmin,
                        squadra.isEmpty() ? null : squadra, isSuperAdmin));
            }
        } catch (IOException e) {
            System.err.println("Errore caricamento utenti.csv: " + e.getMessage());
        }
    }

    // CARICAMENTO SQUADRE
    // formato: nome,punti,partiteGiocate,partiteVinte,partitePareg,
    // partitePerse,golFatti,golSubiti,bilancioIniziale
    private void caricaSquadre() {
        try (BufferedReader br = apriCsv("squadre.csv")) {
            String riga;
            boolean primaRiga = true;
            while ((riga = br.readLine()) != null) {
                riga = riga.trim();
                if (riga.isEmpty()) continue;
                if (primaRiga) { primaRiga = false; continue; }
                String[] col = riga.split(",", -1);
                if (col.length < 9) continue;
                String nome   = col[0].trim();
                int punti     = Integer.parseInt(col[1].trim());
                int pg        = Integer.parseInt(col[2].trim());
                int pv        = Integer.parseInt(col[3].trim());
                int pp        = Integer.parseInt(col[4].trim());
                int pl        = Integer.parseInt(col[5].trim());
                int gf        = Integer.parseInt(col[6].trim());
                int gs        = Integer.parseInt(col[7].trim());
                double bil    = Double.parseDouble(col[8].trim());
                String logo = col[9].trim();
                squadre.add(new Squadra(nome, punti, pg, pv, pp, pl, gf, gs, bil, logo));
            }
        } catch (IOException e) {
            System.err.println("Errore caricamento squadre.csv: " + e.getMessage());
        }
    }

    // CARICAMENTO GIOCATORI
    // formato: squadra,nome,cognome,nazionalita,eta,altezza,gol,assist,valore
    private void caricaGiocatori() {
        try (BufferedReader br = apriCsv("giocatori.csv")) {
            String riga;
            boolean primaRiga = true;
            while ((riga = br.readLine()) != null) {
                riga = riga.trim();
                if (riga.isEmpty()) continue;
                if (primaRiga) { primaRiga = false; continue; }
                String[] col = riga.split(",", -1);
                if (col.length < 9) continue;
                String nomeSquadra = col[0].trim();
                String nome        = col[1].trim();
                String cognome     = col[2].trim();
                String naz         = col[3].trim();
                int eta            = Integer.parseInt(col[4].trim());
                double altezza     = Double.parseDouble(col[5].trim());
                int gol            = Integer.parseInt(col[6].trim());
                int assist         = Integer.parseInt(col[7].trim());
                double valore      = Double.parseDouble(col[8].trim());

                Squadra sq = getSquadraByNome(nomeSquadra);
                if (sq != null) {
                    sq.aggiungiGiocatore(
                            new Giocatore(nome, cognome, naz, eta, altezza, gol, assist, valore));
                } else {
                    System.err.println("Squadra non trovata per giocatore: " + nome + " " + cognome
                            + " (squadra: " + nomeSquadra + ")");
                }
            }
        } catch (IOException e) {
            System.err.println("Errore caricamento giocatori.csv: " + e.getMessage());
        }
    }

    // CARICAMENTO TRASFERIMENTI
    // formato: giocatore,squadraProvenienza,squadraDestinazione,importo,data
    private void caricaTrasferimenti() {
        try (BufferedReader br = apriCsv("trasferimenti.csv")) {
            String riga;
            boolean primaRiga = true;
            while ((riga = br.readLine()) != null) {
                riga = riga.trim();
                if (riga.isEmpty()) continue;
                if (primaRiga) { primaRiga = false; continue; }
                String[] col = riga.split(",", -1);
                if (col.length < 5) continue;
                String giocatore  = col[0].trim();
                String provenienza= col[1].trim();
                String dest       = col[2].trim();
                double importo    = Double.parseDouble(col[3].trim());
                String data       = col[4].trim();
                Trasferimento t = new Trasferimento(giocatore, provenienza, dest, importo, data);
                Squadra sq = getSquadraByNome(provenienza);
                if (sq != null) sq.getStoricoTrasferimenti().add(t);
                Squadra sqDest = getSquadraByNome(dest);
                if (sqDest != null) sqDest.getStoricoTrasferimenti().add(t);
            }
        } catch (IOException e) {
            System.err.println("Errore caricamento trasferimenti.csv: " + e.getMessage());
        }
    }


    public ObservableList<Squadra> getSquadre() { return squadre; }
    public ObservableList<Utente>  getUtenti()  { return utenti;  }

    /** Login: cerca nelle credenziali caricate dal CSV */
    public Optional<Utente> login(String username, String password) {
        return utenti.stream()
                .filter(u -> u.getUsername().equals(username)
                        && u.getPassword().equals(password))
                .findFirst();
    }

    public boolean usernameEsiste(String username) {
        return utenti.stream().anyMatch(u -> u.getUsername().equals(username));
    }

    /** Aggiunge un nuovo utente in memoria e lo salva su utenti.csv */
    public void registraUtente(Utente u) {
        utenti.add(u);
        salvaUtenti();
    }

    private void salvaUtenti() {
        File f = csvFile("utenti.csv");
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"))) {
            pw.println("username,password,admin,squadra,superadmin");
            for (Utente u : utenti) {
                pw.println(u.getUsername() + ","
                        + u.getPassword() + ","
                        + u.isAdmin() + ","
                        + (u.getSquadraAmministrata() != null ? u.getSquadraAmministrata() : "") + ","
                        + u.isSuperAdmin());
            }
        } catch (IOException e) {
            System.err.println("Errore salvataggio utenti.csv: " + e.getMessage());
        }
    }

    public Squadra getSquadraByNome(String nome) {
        for (Squadra s : squadre) {
            if (s.getNome().equals(nome)) {
                return s;
            }
        }
        return null;
    }

    public Squadra getSquadraRandom(String escludi) {
        List<Squadra> altre = new ArrayList<>(squadre);
        altre.removeIf(s -> s.getNome().equals(escludi));
        if (altre.isEmpty()) return null;
        return altre.get((int) (Math.random() * altre.size()));
    }

    public boolean offertaAccettata(double offerta, double valore, boolean venditoreInDebito) {
        if (offerta < valore) return false;
        double rapporto = offerta / valore;
        double prob  = 0.20 + (rapporto - 1.0) * 0.75;
        prob = Math.min(prob, 0.95);
        if (venditoreInDebito) prob = Math.min(prob + 0.20, 1.0);
        return Math.random() < prob;
    }

    public void eseguiTrasferimento(Giocatore g, Squadra venditrice,
                                    Squadra acquirente, double importo) {
        venditrice.rimuoviGiocatore(g);
        acquirente.aggiungiGiocatore(g);
        venditrice.registraVendita(g.getNomeCompleto(), acquirente.getNome(), importo);
        acquirente.registraAcquisto(g.getNomeCompleto(), venditrice.getNome(), importo);
        salvaGiocatori();
        salvaTrasferimenti();
        salvaBilanci();
    }

    /** Riscrive giocatori.csv con le rose aggiornate (dopo trasferimenti) */
    private void salvaGiocatori() {
        File f = csvFile("giocatori.csv");
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"))) {
            pw.println("squadra,nome,cognome,nazionalita,eta,altezza,gol,assist,valore");
            for (Squadra s : squadre) {
                for (Giocatore g : s.getRosa()) {
                    pw.println(s.getNome() + ","
                            + g.getNome() + ","
                            + g.getCognome() + ","
                            + g.getNazionalita() + ","
                            + g.getEta() + ","
                            + (int) g.getAltezza() + ","
                            + g.getGol() + ","
                            + g.getAssist() + ","
                            + g.getValoreEuro());
                }
            }
        } catch (IOException e) {
            System.err.println("Errore salvataggio giocatori.csv: " + e.getMessage());
        }
    }

    /** Aggiunge in append l'ultimo trasferimento a trasferimenti.csv */
    private void salvaTrasferimenti() {
        List<String> righe = new ArrayList<>();
        for (Squadra s : squadre) {
            for (Trasferimento t : s.getStoricoTrasferimenti()) {
                String line = t.getGiocatore() + ","
                        + t.getSquadraProvenienza() + ","
                        + t.getSquadraDestinazione() + ","
                        + t.getImporto() + ","
                        + t.getData();
                // Mantieni ordine e evita duplicati (comportamento simile a LinkedHashSet)
                if (!righe.contains(line)) righe.add(line);
            }
        }
        File f = csvFile("trasferimenti.csv");
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"))) {
            pw.println("giocatore,squadraProvenienza,squadraDestinazione,importo,data");
            for (String riga : righe) pw.println(riga);
        } catch (IOException e) {
            System.err.println("Errore salvataggio trasferimenti.csv: " + e.getMessage());
        }
    }

    /** Aggiorna squadre.csv con i bilanci correnti */
    private void salvaBilanci() {
        File f = csvFile("squadre.csv");
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"))) {
            pw.println("nome,punti,partiteGiocate,partiteVinte,partitePareg,partitePerse,golFatti,golSubiti,bilancioIniziale");
            for (Squadra s : squadre) {
                pw.println(s.getNome() + ","
                        + s.getPunti() + ","
                        + s.getPartiteGiocate() + ","
                        + s.getPartiteVinte() + ","
                        + s.getPartitePareg() + ","
                        + s.getPartitePerse() + ","
                        + s.getGolFatti() + ","
                        + s.getGolSubiti() + ","
                        + s.getBilancioCorrente());
            }
        } catch (IOException e) {
            System.err.println("Errore salvataggio squadre.csv: " + e.getMessage());
        }
    }
}
