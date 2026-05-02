# ⚽ Serie A Manager — JavaFX

Progetto JavaFX che simula la gestione della Serie A italiana.

---

## 🗂️ Struttura del Progetto

```
SerieA/
└── src/
    ├── module-info.java
    └── serieA/
        ├── Main.java                          ← Entry point
        ├── model/
        │   ├── Utente.java
        │   ├── Giocatore.java
        │   ├── Squadra.java
        │   ├── Trasferimento.java
        │   └── Gestione.java                  ← Dati + logica di business
        ├── controller/
        │   ├── LoginController.java           ← Finestra 1
        │   ├── RegistrazioneController.java   ← Finestra extra
        │   ├── ClassificaController.java      ← Finestra 2
        │   ├── StatisticheSquadraController.java ← Finestra 3
        │   ├── RosaController.java            ← Finestra 4
        │   ├── DettaglioGiocatoreController.java ← Finestra 5
        │   ├── StoricoTrasferimentiController.java ← Finestra 6
        │   └── BilancioController.java        ← Finestra 8
        └── view/
            ├── Login.fxml
            ├── Registrazione.fxml
            ├── Classifica.fxml
            ├── StatisticheSquadra.fxml
            ├── Rosa.fxml
            ├── DettaglioGiocatore.fxml
            ├── StoricoTrasferimenti.fxml
            └── Bilancio.fxml
```

---

## 🚀 Requisiti

- Java 17+
- JavaFX 17+ (SDK scaricabile da https://gluonhq.com/products/javafx/)
- IDE consigliato: **IntelliJ IDEA**

---

## ⚙️ Setup in IntelliJ

1. **Apri** il progetto in IntelliJ (File → Open → cartella `SerieA`)
2. **Imposta SDK**: File → Project Structure → SDK → Java 17+
3. **Aggiungi JavaFX al classpath**:
   - File → Project Structure → Libraries → `+` → Java
   - Seleziona la cartella `lib` del tuo JavaFX SDK
4. **VM Options per Run Configuration**:
   ```
   --module-path /percorso/al/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
   ```
   Oppure, se usi il `module-info.java`, basta impostare il modulo correttamente.
5. **Main class**: `serieA.Main`

---

## 👤 Account di Accesso

| Username       | Password  | Ruolo              |
|----------------|-----------|--------------------|
| user           | user123   | Utente normale     |
| admin_inter    | admin123  | Admin Inter        |
| admin_milan    | admin123  | Admin Milan        |
| admin_juve     | admin123  | Admin Juventus     |
| admin_napoli   | admin123  | Admin Napoli       |
| admin_roma     | admin123  | Admin Roma         |
| admin_lazio    | admin123  | Admin Lazio        |
| admin_atalanta | admin123  | Admin Atalanta     |
| admin_fiorentina | admin123 | Admin Fiorentina  |
| admin_bologna  | admin123  | Admin Bologna      |
| admin_torino   | admin123  | Admin Torino       |

Puoi registrare nuovi utenti (solo ruolo utente) dalla schermata di login.

---

## 🖥️ Descrizione Finestre

### 1. Login
- Campo username e password
- Hyperlink "Registrati" per la registrazione
- Differenzia tra utente e admin

### Registrazione (extra)
- Crea account utente normale
- Validazione campi e unicità username

### 2. Classifica
- Tutte le squadre in ordine **alfabetico** (requisito del progetto)
- Colonne: Punti, PG, V, P, S, GF, GS
- Click su squadra → statistiche

### 3. Statistiche Squadra
- Punti, record vittorie/pareggi/sconfitte, differenza gol
- Pulsanti: Rosa, Storico Trasferimenti
- Pulsante **Bilancio** visibile solo all'admin della propria squadra

### 4. Rosa
- Tutti i giocatori con **barra di ricerca** (nome, cognome, nazionalità)
- Colonne: Nome, Cognome, Nazionalità, Età, Altezza, Gol, Assist, Valore
- Click su giocatore → dettaglio

### 5. Dettaglio Giocatore
- Valore di mercato, G/A, nazionalità, età, altezza
- Pulsante **COMPRA** (se giocatore di altra squadra, solo admin)
- Pulsante **VENDI** (se giocatore della tua squadra, solo admin)

#### Logica COMPRA:
- Se offerta < valore → rifiuto automatico
- Se offerta = valore → 20% probabilità
- Se offerta > valore → probabilità crescente (fino al 95% a +100%)
- Se la squadra venditrice è in debito → +20% di probabilità
- Se la tua squadra è in debito → messaggio di avviso prima di procedere

#### Logica VENDI:
- Una squadra random fa un'offerta tra -20% e +50% del valore
- Se rifiuti → 30% fine trattative, 70% seconda offerta
- La seconda offerta è sempre maggiore della prima
- Se rifiuti anche la seconda → fine trattative

### 6. Storico Trasferimenti
- Tabella con giocatore, provenienza, destinazione, importo, data
- Aggiornato automaticamente ad ogni trasferimento

### 8. Bilancio (solo admin)
- Bilancio iniziale, corrente
- Entrate (vendite) e uscite (acquisti)
- Indicatore stato debito

---

## 🎨 Design
- Tema scuro **#1a1a2e / #16213e / #0f3460**
- Accent color rosso **#e94560**
- Consistente su tutte le schermate

---

## 📂 File CSV (cartella `data/`)

Tutti i dati sono caricati da file CSV nella cartella `data/`. Puoi modificarli senza toccare il codice.

| File | Contenuto |
|---|---|
| `utenti.csv` | Credenziali di accesso (username, password, ruolo, squadra) |
| `squadre.csv` | 20 squadre Serie A con statistiche finali 2024-25 |
| `giocatori.csv` | 200 giocatori con statistiche (gol, assist, valore, ecc.) |
| `trasferimenti.csv` | Storico trasferimenti di esempio |

### Dove mettere la cartella `data/`

La cartella `data/` deve trovarsi nella **working directory** del programma, cioè la stessa cartella da cui viene avviato il JAR o da IntelliJ la root del progetto (`SerieA/`).

In IntelliJ: Run → Edit Configurations → Working directory → imposta su `$MODULE_WORKING_DIR$` oppure sul percorso assoluto della cartella `SerieA/`.

### Aggiungere utenti, squadre o giocatori

Basta aprire il CSV corrispondente con Excel o un editor di testo e aggiungere righe seguendo il formato esistente. Al prossimo avvio il programma caricherà automaticamente i nuovi dati.
