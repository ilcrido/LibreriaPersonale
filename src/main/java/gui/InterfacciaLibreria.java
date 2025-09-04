package gui;

import command.*;
import modello.Libro;
import observer.Libreria;
import observer.Observer;
import strategy.*;
import template.GestoreFileCsv;
import template.GestoreFileJson;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InterfacciaLibreria extends JFrame implements Observer {


    private JTable tabellaLibri;
    private DefaultTableModel modelloTabella;
    private JButton btnNuovo, btnModifica, btnRimuovi;
    private JButton btnUndo, btnRedo;
    private JButton btnSalva, btnCarica;
    private JTextField txtCerca, txtFiltroValutazione;
    private JComboBox<String> cmbFiltroGenere, cmbFiltroStato;
    private JComboBox<String> cmbTipoRicerca;
    private JComboBox<String> cmbOrdinamento;
    private JLabel lblStato;

    private Libreria libreria;
    private GestoreComandi gestoreComandi;

    public InterfacciaLibreria() {
        initModello();
        initGUI();
        aggiornaTabella();
        aggiornaControlli();
    }

    private void initModello() {
        libreria = new Libreria();
        libreria.aggiungiObserver(this);
        gestoreComandi = new GestoreComandi();
    }

    private void initGUI() {
        setTitle("Gestione Libreria Personale");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Crea e aggiungi i componenti
        add(createToolBar(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);

        // Configurazione finestra
        pack();
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 700));

        // per poter utilizzare i comandi da tastiera per undo e redo
        createKeyBindings();
    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        btnNuovo = new JButton("Aggiungi");
        btnNuovo.addActionListener(this::nuovoLibro);
        toolBar.add(btnNuovo);

        btnModifica = new JButton("Modifica");
        btnModifica.addActionListener(this::modificaLibro);
        toolBar.add(btnModifica);

        btnRimuovi = new JButton("Rimuovi");
        btnRimuovi.addActionListener(this::rimuoviLibro);
        toolBar.add(btnRimuovi);

        toolBar.addSeparator();

        // Bottoni Undo/Redo
        btnUndo = new JButton("↶");
        btnUndo.addActionListener(this::undo);
        toolBar.add(btnUndo);

        btnRedo = new JButton("↷");
        btnRedo.addActionListener(this::redo);
        toolBar.add(btnRedo);


        toolBar.addSeparator();

        // Bottoni per salvare e caricare i file
        btnSalva = new JButton("💾"); // dischetto = salva
        btnSalva.addActionListener(this::salvaFile);
        toolBar.add(btnSalva);

        btnCarica = new JButton("📥"); // inbox tray = carica
        btnCarica.addActionListener(this::caricaFile);
        toolBar.add(btnCarica);


        return toolBar;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());

        // Panel filtri sopra la tabella
        centerPanel.add(createFilterPanel(), BorderLayout.NORTH);

        createTable();
        JScrollPane scrollPane = new JScrollPane(tabellaLibri);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        return centerPanel;
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Ricerca, Filtri e Ordinamento"));

        // Ricerca
        filterPanel.add(new JLabel("Cerca:"));
        cmbTipoRicerca = new JComboBox<>();
        cmbTipoRicerca.addItem("Titolo");
        cmbTipoRicerca.addItem("Autore");
        cmbTipoRicerca.addItem("ISBN");
        cmbTipoRicerca.setPreferredSize(new Dimension(80, 25));
        filterPanel.add(cmbTipoRicerca);

        txtCerca = new JTextField(12);

        // Ricerca in tempo reale mentre scrivi
        txtCerca.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { applicaFiltri(null); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { applicaFiltri(null); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { applicaFiltri(null); }
        });

        // Anche quando cambia il tipo di ricerca
        cmbTipoRicerca.addActionListener(this::applicaFiltri);
        filterPanel.add(txtCerca);

        filterPanel.add(Box.createHorizontalStrut(15));

        // Filtri
        filterPanel.add(new JLabel("Genere:"));
        cmbFiltroGenere = new JComboBox<>();
        cmbFiltroGenere.addItem("Tutti");
        for (var genere : modello.Genere.values()) {
            cmbFiltroGenere.addItem(genere.toString());
        }
        cmbFiltroGenere.setPreferredSize(new Dimension(100, 25));
        // Filtro automatico quando cambia selezione
        cmbFiltroGenere.addActionListener(this::applicaFiltri);
        filterPanel.add(cmbFiltroGenere);

        filterPanel.add(new JLabel("Stato:"));
        cmbFiltroStato = new JComboBox<>();
        cmbFiltroStato.addItem("Tutti");
        for (var stato : modello.StatoLettura.values()) {
            cmbFiltroStato.addItem(stato.toString());
        }
        cmbFiltroStato.setPreferredSize(new Dimension(100, 25));
        // Filtro automatico quando cambia selezione
        cmbFiltroStato.addActionListener(this::applicaFiltri);
        filterPanel.add(cmbFiltroStato);

        filterPanel.add(new JLabel("Voto:"));
        txtFiltroValutazione = new JTextField(2);
        txtFiltroValutazione.setToolTipText("Inserisci un numero da 0 a 5");

        // Filtro automatico con validazione
        txtFiltroValutazione.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                if (validaInputValutazione()) {
                    applicaFiltri(null);
                }
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { applicaFiltri(null); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                if (validaInputValutazione()) {
                    applicaFiltri(null);
                }
            }
        });

        txtFiltroValutazione.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                String currentText = txtFiltroValutazione.getText();

                // Permetti solo numeri da 0 a 5 e backspace/delete
                if (!Character.isDigit(c) && c != java.awt.event.KeyEvent.VK_BACK_SPACE && c != java.awt.event.KeyEvent.VK_DELETE) {
                    e.consume();
                    return;
                }

                // Se è una cifra, controlla che sia tra 0-5
                if (Character.isDigit(c)) {
                    int digit = Character.getNumericValue(c);
                    if (digit < 0 || digit > 5) {
                        e.consume();
                        return;
                    }

                    // Permetti solo un carattere
                    if (currentText.length() >= 1) {
                        e.consume();
                    }
                }
            }
        });
        filterPanel.add(txtFiltroValutazione);

        filterPanel.add(Box.createHorizontalStrut(15));

        filterPanel.add(new JLabel("Ordina:"));
        cmbOrdinamento = new JComboBox<>();
        cmbOrdinamento.addItem("Nessuno");
        cmbOrdinamento.addItem("Titolo A-Z");
        cmbOrdinamento.addItem("Titolo Z-A");
        cmbOrdinamento.addItem("Autore A-Z");
        cmbOrdinamento.addItem("Autore Z-A");
        cmbOrdinamento.addItem("Genere A-Z");
        cmbOrdinamento.addItem("Genere Z-A");
        cmbOrdinamento.addItem("Valutazione 0-5");
        cmbOrdinamento.addItem("Valutazione 5-0");
        cmbOrdinamento.setPreferredSize(new Dimension(120, 25));
        // Ordinamento automatico quando cambia selezione
        cmbOrdinamento.addActionListener(this::applicaFiltri);
        filterPanel.add(cmbOrdinamento);

        JButton btnReset = new JButton("Reset");
        btnReset.addActionListener(this::resetFiltri);
        filterPanel.add(btnReset);

        return filterPanel;
    }

    private void createTable() {
        String[] colonne = {"Titolo", "Autore", "ISBN", "Genere", "Stato", "Valutazione"};
        modelloTabella = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabella non modificabile
            }
        };

        tabellaLibri = new JTable(modelloTabella);
        tabellaLibri.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabellaLibri.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                aggiornaBottoniSelezione();
            }
        });

        // Imposta larghezze colonne
        tabellaLibri.getColumnModel().getColumn(0).setPreferredWidth(200); // Titolo
        tabellaLibri.getColumnModel().getColumn(1).setPreferredWidth(150); // Autore
        tabellaLibri.getColumnModel().getColumn(2).setPreferredWidth(120); // ISBN
        tabellaLibri.getColumnModel().getColumn(3).setPreferredWidth(100); // Genere
        tabellaLibri.getColumnModel().getColumn(4).setPreferredWidth(100); // Stato
        tabellaLibri.getColumnModel().getColumn(5).setPreferredWidth(80);  // Valutazione
    }

    private JPanel createStatusBar() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createEtchedBorder());

        lblStato = new JLabel(" Pronto");
        lblStato.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        statusPanel.add(lblStato, BorderLayout.WEST);

        return statusPanel;
    }

    private void createKeyBindings() {
        // Ctrl+Z per Undo
        KeyStroke ctrlZ = KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlZ, "undo");
        getRootPane().getActionMap().put("undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undo(e);
            }
        });

        // Ctrl+Y per Redo
        KeyStroke ctrlY = KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlY, "redo");
        getRootPane().getActionMap().put("redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redo(e);
            }
        });
    }

    private void nuovoLibro(ActionEvent e) {
        EditorLibro editor = new EditorLibro(this);
        if (editor.showDialog()) {
            try {
                Libro nuovoLibro = editor.getLibro();
                Command comando = new AggiungiLibro(libreria, nuovoLibro);
                eseguiComando(comando);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Errore ISBN duplicato",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void modificaLibro(ActionEvent e) {
        int rigaSelezionata = tabellaLibri.getSelectedRow();
        if (rigaSelezionata == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona un libro da modificare");
            return;
        }

        String isbn = (String) modelloTabella.getValueAt(rigaSelezionata, 2);
        Libro libroEsistente = libreria.trovaLibroPerIsbn(isbn);

        EditorLibro editor = new EditorLibro(this, libroEsistente);
        if (editor.showDialog()) {
            try {
                Libro libroModificato = editor.getLibro();
                Command comando = new ModificaLibro(libreria, libroModificato);
                eseguiComando(comando);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Errore",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void rimuoviLibro(ActionEvent e) {
        int rigaSelezionata = tabellaLibri.getSelectedRow();
        if (rigaSelezionata == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona un libro da rimuovere");
            return;
        }

        String isbn = (String) modelloTabella.getValueAt(rigaSelezionata, 2);
        Libro libroDaRimuovere = libreria.trovaLibroPerIsbn(isbn);

        int conferma = JOptionPane.showConfirmDialog(
                this,
                "Rimuovere il libro '" + libroDaRimuovere.getTitolo() + "'?",
                "Conferma rimozione",
                JOptionPane.YES_NO_OPTION
        );

        if (conferma == JOptionPane.YES_OPTION) {
            Command comando = new RimuoviLibro(libreria, libroDaRimuovere);
            eseguiComando(comando);
        }
    }

    private void undo(ActionEvent e) {
        if (gestoreComandi.undo()) {
            aggiornaControlli();
            lblStato.setText(" Annullato: " + gestoreComandi.getUltimaAzioneRedo());
        }
    }

    private void redo(ActionEvent e) {
        if (gestoreComandi.redo()) {
            aggiornaControlli();
            lblStato.setText(" Ripetuto: " + gestoreComandi.getUltimaAzioneUndo());
        }
    }

    private void applicaFiltri(ActionEvent e) {
        List<Libro> risultato = libreria.getTuttiLibri();

        // Applica filtro di ricerca (per tipo specifico)
        String termine = txtCerca.getText().trim();
        if (!termine.isEmpty()) {
            String tipoRicerca = (String) cmbTipoRicerca.getSelectedItem();
            List<Libro> trovati = new ArrayList<>();

            for (Libro libro : risultato) {
                boolean match = false;
                switch (tipoRicerca) {
                    case "Titolo":
                        match = libro.getTitolo().toLowerCase().contains(termine.toLowerCase());
                        break;
                    case "Autore":
                        match = libro.getAutore().toLowerCase().contains(termine.toLowerCase());
                        break;
                    case "ISBN":
                        match = libro.getIsbn().contains(termine);
                        break;
                }
                if (match) {
                    trovati.add(libro);
                }
            }
            risultato = trovati;
        }

        // Applica filtro genere
        String genereSelezionato = (String) cmbFiltroGenere.getSelectedItem();
        if (genereSelezionato != null && !"Tutti".equals(genereSelezionato)) {
            modello.Genere genere = modello.Genere.valueOf(genereSelezionato);
            Strategy filtro = new FiltroPerGenere(genere);
            risultato = filtro.filtra(risultato);
        }

        // Applica filtro stato
        String statoSelezionato = (String) cmbFiltroStato.getSelectedItem();
        if (statoSelezionato != null && !"Tutti".equals(statoSelezionato)) {
            modello.StatoLettura stato = modello.StatoLettura.valueOf(statoSelezionato);
            Strategy filtro = new FiltroPerStato(stato);
            risultato = filtro.filtra(risultato);
        }

        // Applica filtro valutazione
        String valutazioneStr = txtFiltroValutazione.getText().trim();
        if (!valutazioneStr.isEmpty()) {
            try {
                int valutazione = Integer.parseInt(valutazioneStr);
                if (valutazione >= 0 && valutazione <= 5) {
                    Strategy filtro = new FiltroPerValutazione(valutazione);
                    risultato = filtro.filtra(risultato);
                }
            } catch (NumberFormatException ex) {
                // Ignora input non valido
            }
        }

        // Applica ordinamento se selezionato
        String ordinamentoSelezionato = (String) cmbOrdinamento.getSelectedItem();
        if (ordinamentoSelezionato != null && !"Nessuno".equals(ordinamentoSelezionato)) {
            Strategy strategiaOrdinamento = null;

            switch (ordinamentoSelezionato) {
                case "Titolo A-Z":
                    strategiaOrdinamento = new OrdinamentoPerTitolo(true);
                    break;
                case "Titolo Z-A":
                    strategiaOrdinamento = new OrdinamentoPerTitolo(false);
                    break;
                case "Autore A-Z":
                    strategiaOrdinamento = new OrdinamentoPerAutore(true);
                    break;
                case "Autore Z-A":
                    strategiaOrdinamento = new OrdinamentoPerAutore(false);
                    break;
                case "Genere A-Z":
                    strategiaOrdinamento = new OrdinamentoPerGenere(true);
                    break;
                case "Genere Z-A":
                    strategiaOrdinamento = new OrdinamentoPerGenere(false);
                    break;
                case "Valutazione 0-5":
                    strategiaOrdinamento = new OrdinamentoPerValutazione(true);
                    break;
                case "Valutazione 5-0":
                    strategiaOrdinamento = new OrdinamentoPerValutazione(false);
                    break;
            }

            if (strategiaOrdinamento != null) {
                risultato = strategiaOrdinamento.filtra(risultato);
            }
        }

        aggiornaTabella(risultato);
        lblStato.setText(" Mostrati " + risultato.size() + " di " + libreria.getNumeroLibri() + " libri");
    }

    private void resetFiltri(ActionEvent e) {
        txtCerca.setText("");
        cmbTipoRicerca.setSelectedIndex(0);
        cmbFiltroGenere.setSelectedIndex(0);
        cmbFiltroStato.setSelectedIndex(0);
        txtFiltroValutazione.setText("");
        cmbOrdinamento.setSelectedIndex(0);
        aggiornaTabella();
        lblStato.setText(" Mostrati tutti i libri");
    }

    private void salvaFile(ActionEvent e) {
        // Dialog di selezione formato
        String[] opzioni = {"CSV", "JSON"};
        int scelta = JOptionPane.showOptionDialog(
                this,
                "Seleziona il formato di salvataggio:",
                "Salva libreria",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opzioni,
                opzioni[0]
        );

        if (scelta == -1) return; // Utente ha annullato quindi non fa nulla

        boolean isCSV = (scelta == 0);
        String estensione = isCSV ? "csv" : "json";
        String descrizione = isCSV ? "File CSV" : "File JSON";

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(descrizione, estensione));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().endsWith("." + estensione)) {
                file = new File(file.getAbsolutePath() + "." + estensione);
            }

            if (isCSV) {
                GestoreFileCsv gestore = new GestoreFileCsv();
                gestore.salvaLibreria(libreria.getTuttiLibri(), file.getAbsolutePath());
            } else {
                GestoreFileJson gestore = new GestoreFileJson();
                gestore.salvaLibreria(libreria.getTuttiLibri(), file.getAbsolutePath());
            }

            lblStato.setText(" Salvato in formato " + estensione.toUpperCase() + ": " + file.getName());
        }
    }

    private void caricaFile(ActionEvent e) {
        // Dialog di selezione formato
        String[] opzioni = {"CSV", "JSON"};
        int scelta = JOptionPane.showOptionDialog(
                this,
                "Seleziona il formato del file da caricare:",
                "Carica libreria",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opzioni,
                opzioni[0]
        );

        if (scelta == -1) return; // Utente ha annullato quindi non fa nulla

        boolean isCSV = (scelta == 0);
        String estensione = isCSV ? "csv" : "json";
        String descrizione = isCSV ? "File CSV" : "File JSON";

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(descrizione, estensione));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            List<Libro> libri = null;

            if (isCSV) {
                GestoreFileCsv gestore = new GestoreFileCsv();
                libri = gestore.caricaLibreria(file.getAbsolutePath());
            } else {
                GestoreFileJson gestore = new GestoreFileJson();
                libri = gestore.caricaLibreria(file.getAbsolutePath());
            }

            if (libri != null) {
                libreria = new Libreria();
                libreria.aggiungiObserver(this);
                gestoreComandi.pulisciCronologia();

                int libriCaricati = 0;
                for (Libro libro : libri) {
                    try {
                        libreria.aggiungiLibro(libro);
                        libriCaricati++;
                    } catch (IllegalArgumentException ex) {
                        // Ignora libri con ISBN duplicati durante il caricamento
                        System.out.println("Libro ignorato (ISBN duplicato): " + libro.getTitolo());
                    }
                }

                lblStato.setText(" Caricati " + libriCaricati + " libri da " + file.getName() + " (formato " + estensione.toUpperCase() + ")");
                aggiornaControlli();
            }
        }
    }

    private boolean validaInputValutazione() {
        String testo = txtFiltroValutazione.getText().trim();
        if (testo.isEmpty()) {
            return true; // Campo vuoto è valido
        }

        try {
            int valore = Integer.parseInt(testo);
            return valore >= 0 && valore <= 5;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void eseguiComando(Command comando) {
        try {
            gestoreComandi.eseguiComando(comando);
            aggiornaControlli();
        } catch (IllegalArgumentException ex) {
            // Rilanciamo l'eccezione per gestirla nel chiamante
            throw ex;
        }
    }

    private void aggiornaTabella() {
        aggiornaTabella(libreria.getTuttiLibri());
    }

    private void aggiornaTabella(List<Libro> libri) {
        modelloTabella.setRowCount(0);
        for (Libro libro : libri) {
            Object[] riga = {
                    libro.getTitolo(),
                    libro.getAutore(),
                    libro.getIsbn(),
                    libro.getGenere().toString(),
                    libro.getStatoLettura() != null ? libro.getStatoLettura().toString() : "",
                    libro.getValutazione()
            };
            modelloTabella.addRow(riga);
        }
    }

    private void aggiornaControlli() {
        btnUndo.setEnabled(gestoreComandi.puoiFareUndo());
        btnRedo.setEnabled(gestoreComandi.puoiFareRedo());

        String azioneUndo = gestoreComandi.getUltimaAzioneUndo();
        String azioneRedo = gestoreComandi.getUltimaAzioneRedo();

        btnUndo.setToolTipText(azioneUndo.isEmpty() ? "Niente da annullare" : "Annulla: " + azioneUndo);
        btnRedo.setToolTipText(azioneRedo.isEmpty() ? "Niente da ripetere" : "Ripeti: " + azioneRedo);

        aggiornaBottoniSelezione();
    }

    private void aggiornaBottoniSelezione() {
        boolean hasSelezione = tabellaLibri.getSelectedRow() != -1;
        btnModifica.setEnabled(hasSelezione);
        btnRimuovi.setEnabled(hasSelezione);
    }


    @Override
    public void libroAggiunto(Libro libro) {
        applicaFiltri(null);  // Riapplica i filtri invece di mostrare tutto
    }

    @Override
    public void libroRimosso(Libro libro) {
        applicaFiltri(null);  // Riapplica i filtri invece di mostrare tutto
    }

    @Override
    public void libroModificato(Libro libro) {
        applicaFiltri(null);  // Riapplica i filtri invece di mostrare tutto
    }

}