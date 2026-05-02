package serieA.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.URL;
import java.util.Optional;

public class Gestione {

    private ObservableList<Squadra> squadre = FXCollections.observableArrayList();
    private ObservableList<Utente>  utenti  = FXCollections.observableArrayList();

    // ── Cartella dei CSV (relativa alla posizione del JAR / classi) ─────────
    // Cerca prima nella cartella "data/" accanto all'eseguibile,
    // poi come risorsa nel classpath.
    private static final String DATA_DIR = "data/";
    // Percorso effettivo della cartella data/ trovata all'avvio
    private String dataDir = null;

    public Gestione() {
        caricaUtenti();
        caricaSquadre();
        caricaGiocatori();
        caricaTrasferimenti();
    }

    // ── Utility: trova la cartella data/ e la memorizza in dataDir ───────────
    private File trovaDataDir() {
        // 1) Relativo alla working directory
        File f1 = new File(DATA_DIR);
        if (f1.exists() && f1.isDirectory()) return f1;
        // 2) Accanto al JAR/classi
        File jarDir = new File(Gestione.class.getProtectionDomain()
                .getCodeSource().getLocation().getPath()).getParentFile();
        File f2 = new File(jarDir, DATA_DIR);
        if (f2.exists() && f2.isDirectory()) return f2;
        // 3) Due livelli su (IntelliJ: out/production/NomeProgetto -> radice progetto)
        File f3 = new File(jarDir.getParentFile().getParentFile(), DATA_DIR);
        if (f3.exists() && f3.isDirectory()) return f3;
        return f1; // fallback: restituisce il percorso relativo anche se non esiste
    }

    private BufferedReader apriCsv(String nomeFile) throws IOException {
        if (dataDir == null) {
            File dir = trovaDataDir();
            dataDir = dir.getAbsolutePath();
            System.out.println("[CSV] Cartella data trovata: " + dataDir);
        }
        File f = new File(dataDir, nomeFile);
        if (f.exists()) {
            return new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
        }
        // Fallback classpath
        URL url = Gestione.class.getResource("/" + DATA_DIR + nomeFile);
        if (url != null) {
            return new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
        }
        throw new IOException("File CSV non trovato: " + f.getAbsolutePath());
    }

    private File csvFile(String nomeFile) {
        if (dataDir == null) {
            dataDir = trovaDataDir().getAbsolutePath();
        }
        return new File(dataDir, nomeFile);
    }


    // ── CARICAMENTO UTENTI ──────────────────────────────────────────────────
    // formato: username,password,admin,squadra
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
                String username  = col[0].trim();
                String password  = col[1].trim();
                boolean isAdmin  = Boolean.parseBoolean(col[2].trim());
                String squadra   = col[3].trim();
                utenti.add(new Utente(username, password, isAdmin,
                        squadra.isEmpty() ? null : squadra));
            }
        } catch (IOException e) {
            System.err.println("Errore caricamento utenti.csv: " + e.getMessage());
        }
    }

    // ── CARICAMENTO SQUADRE ─────────────────────────────────────────────────
    // formato: nome,punti,partiteGiocate,partiteVinte,partitePareg,
    //          partitePerse,golFatti,golSubiti,bilancioIniziale
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
                squadre.add(new Squadra(nome, punti, pg, pv, pp, pl, gf, gs, bil));
            }
        } catch (IOException e) {
            System.err.println("Errore caricamento squadre.csv: " + e.getMessage());
        }
    }

    // ── CARICAMENTO GIOCATORI ───────────────────────────────────────────────
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

    // ── CARICAMENTO TRASFERIMENTI ───────────────────────────────────────────
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
                // Aggiunge lo storico alla squadra di provenienza (se esiste in Serie A)
                Squadra sq = getSquadraByNome(provenienza);
                if (sq != null) sq.getStoricoTrasferimenti().add(t);
                // Aggiunge anche alla squadra destinazione (se è una squadra Serie A)
                Squadra sqDest = getSquadraByNome(dest);
                if (sqDest != null) sqDest.getStoricoTrasferimenti().add(t);
            }
        } catch (IOException e) {
            System.err.println("Errore caricamento trasferimenti.csv: " + e.getMessage());
        }
    }

    // ── API pubblica ────────────────────────────────────────────────────────

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
            pw.println("username,password,admin,squadra");
            for (Utente u : utenti) {
                pw.println(u.getUsername() + ","
                        + u.getPassword() + ","
                        + u.isAdmin() + ","
                        + (u.getSquadraAmministrata() != null ? u.getSquadraAmministrata() : ""));
            }
            System.out.println("[CSV] utenti.csv salvato: " + f.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Errore salvataggio utenti.csv: " + e.getMessage());
        }
    }

    public Squadra getSquadraByNome(String nome) {
        return squadre.stream()
                .filter(s -> s.getNome().equals(nome))
                .findFirst().orElse(null);
    }

    public Squadra getSquadraRandom(String escludi) {
        java.util.List<Squadra> altre = new java.util.ArrayList<>(squadre);
        altre.removeIf(s -> s.getNome().equals(escludi));
        if (altre.isEmpty()) return null;
        return altre.get((int) (Math.random() * altre.size()));
    }

    public boolean offertaAccettata(double offerta, double valore, boolean venditoreInDebito) {
        if (offerta < valore) return false;
        double ratio = offerta / valore;
        double prob  = 0.20 + (ratio - 1.0) * 0.75;
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
        // Salva le modifiche sui CSV
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
            System.out.println("[CSV] giocatori.csv salvato: " + f.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Errore salvataggio giocatori.csv: " + e.getMessage());
        }
    }

    /** Aggiunge in append l'ultimo trasferimento a trasferimenti.csv */
    private void salvaTrasferimenti() {
        // Raccoglie tutti i trasferimenti da tutte le squadre (evita duplicati con un Set)
        java.util.LinkedHashSet<String> righe = new java.util.LinkedHashSet<>();
        for (Squadra s : squadre) {
            for (Trasferimento t : s.getStoricoTrasferimenti()) {
                righe.add(t.getGiocatore() + ","
                        + t.getSquadraProvenienza() + ","
                        + t.getSquadraDestinazione() + ","
                        + t.getImporto() + ","
                        + t.getData());
            }
        }
        File f = csvFile("trasferimenti.csv");
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"))) {
            pw.println("giocatore,squadraProvenienza,squadraDestinazione,importo,data");
            for (String riga : righe) pw.println(riga);
            System.out.println("[CSV] trasferimenti.csv salvato: " + f.getAbsolutePath());
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
            System.out.println("[CSV] squadre.csv salvato: " + f.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Errore salvataggio squadre.csv: " + e.getMessage());
        }
    }
}
