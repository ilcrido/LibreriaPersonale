package gui;

import modello.Genere;
import modello.Libro;
import modello.StatoLettura;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class EditorLibro extends JDialog {

    private JTextField txtTitolo, txtAutore, txtIsbn, txtValutazione;
    private JComboBox<Genere> cmbGenere;
    private JComboBox<StatoLettura> cmbStato;
    private boolean confermato = false;
    private boolean modificaMode = false;

    public EditorLibro(Frame parent) {
        this(parent, null);
    }

    public EditorLibro(Frame parent, Libro libro) {
        super(parent, libro == null ? "Nuovo Libro" : "Modifica Libro", true);
        this.modificaMode = (libro != null);

        initGUI();

        if (libro != null) {
            caricaDatiLibro(libro);
        }

        pack();
        setLocationRelativeTo(parent);
    }

    private void initGUI() {
        setLayout(new BorderLayout());

        // Panel principale con i campi
        JPanel panelCampi = new JPanel(new GridBagLayout());
        panelCampi.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panelCampi.add(new JLabel("Titolo:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtTitolo = new JTextField(20);
        panelCampi.add(txtTitolo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        panelCampi.add(new JLabel("Autore:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtAutore = new JTextField(20);
        panelCampi.add(txtAutore, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        panelCampi.add(new JLabel("ISBN:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtIsbn = new JTextField(20);
        panelCampi.add(txtIsbn, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        panelCampi.add(new JLabel("Genere:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        cmbGenere = new JComboBox<>(Genere.values());
        panelCampi.add(cmbGenere, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        panelCampi.add(new JLabel("Stato:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        cmbStato = new JComboBox<>(StatoLettura.values());
        cmbStato.setSelectedItem(StatoLettura.DA_LEGGERE); // Default

        // Listener per gestire automaticamente la valutazione quando cambia lo stato
        cmbStato.addActionListener(e -> {
            StatoLettura statoSelezionato = (StatoLettura) cmbStato.getSelectedItem();
            String valutazioneAttuale = txtValutazione.getText().trim();

            if (statoSelezionato == StatoLettura.DA_LEGGERE) {
                // Se cambio a DA_LEGGERE, imposta valutazione a 0
                txtValutazione.setText("0");
            } else if (statoSelezionato == StatoLettura.LETTO) {
                // Se cambio a LETTO e la valutazione è 0, imposta 1
                if ("0".equals(valutazioneAttuale)) {
                    txtValutazione.setText("1");
                }
            }
        });
        panelCampi.add(cmbStato, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        panelCampi.add(new JLabel("Valutazione (0-5):"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtValutazione = new JTextField("0", 5);
        txtValutazione.setToolTipText("DA_LEGGERE: solo 0 | IN_LETTURA: 0-5 | LETTO: 1-5");
        panelCampi.add(txtValutazione, gbc);

        add(panelCampi, BorderLayout.CENTER);

        // Panel bottoni
        JPanel panelBottoni = new JPanel(new FlowLayout());

        JButton btnOk = new JButton("OK");
        btnOk.addActionListener(this::conferma);
        getRootPane().setDefaultButton(btnOk);

        JButton btnAnnulla = new JButton("Annulla");
        btnAnnulla.addActionListener(this::annulla);

        panelBottoni.add(btnOk);
        panelBottoni.add(btnAnnulla);

        add(panelBottoni, BorderLayout.SOUTH);

        txtTitolo.requestFocusInWindow();

        // Se è in modalità modifica, disabilita la modifica dell'ISBN
        if (modificaMode) {
            txtIsbn.setEditable(false);
            txtIsbn.setBackground(Color.LIGHT_GRAY);
        }
    }

    private void caricaDatiLibro(Libro libro) {
        txtTitolo.setText(libro.getTitolo());
        txtAutore.setText(libro.getAutore());
        txtIsbn.setText(libro.getIsbn());
        cmbGenere.setSelectedItem(libro.getGenere());
        if (libro.getStatoLettura() != null) {
            cmbStato.setSelectedItem(libro.getStatoLettura());
        }
        txtValutazione.setText(String.valueOf(libro.getValutazione()));
    }

    private void conferma(ActionEvent e) {
        if (validaCampi()) {
            confermato = true;
            dispose();
        }
    }

    private void annulla(ActionEvent e) {
        confermato = false;
        dispose();
    }

    private boolean validaCampi() {
        StringBuilder errori = new StringBuilder();

        // Controllo campi vuoti
        if (txtTitolo.getText().trim().isEmpty()) {
            errori.append("- Il titolo è obbligatorio\n");
        }
        if (txtAutore.getText().trim().isEmpty()) {
            errori.append("- L'autore è obbligatorio\n");
        }

        String isbn = txtIsbn.getText().trim();
        if (isbn.isEmpty()) {
            errori.append("- L'ISBN è obbligatorio\n");
        } else if (!isbn.matches("\\d{13}")) {
            errori.append("- L'ISBN deve essere di esattamente 13 cifre numeriche\n");
        }

        if (cmbGenere.getSelectedItem() == null) {
            errori.append("- Seleziona un genere\n");
        }
        if (cmbStato.getSelectedItem() == null) {
            errori.append("- Seleziona uno stato\n");
        }

        // controllo della valutazione in base allo stato
        try {
            int valutazione = Integer.parseInt(txtValutazione.getText().trim());
            StatoLettura stato = (StatoLettura) cmbStato.getSelectedItem();

            if (valutazione < 0 || valutazione > 5) {
                errori.append("- La valutazione deve essere tra 0 e 5\n");
            } else if (stato == StatoLettura.DA_LEGGERE && valutazione != 0) {
                errori.append("- I libri 'DA_LEGGERE' devono avere valutazione 0\n");
            } else if (stato == StatoLettura.LETTO && valutazione == 0) {
                errori.append("- I libri 'LETTO' devono avere valutazione da 1 a 5\n");
            }
            // IN_LETTURA può avere qualsiasi valutazione (0-5)
        } catch (NumberFormatException ex) {
            errori.append("- Inserisci un numero valido per la valutazione\n");
        }

        // Se ci sono errori, mostrali
        if (errori.length() > 0) {
            JOptionPane.showMessageDialog(
                    this,
                    errori.toString(),
                    "Errori di validazione",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        return true;
    }

    public boolean showDialog() {
        setVisible(true);
        return confermato;
    }

    public Libro getLibro() {
        if (!confermato) {
            return null;
        }
        return new Libro(
                txtTitolo.getText().trim(),
                txtAutore.getText().trim(),
                txtIsbn.getText().trim(),
                (Genere) cmbGenere.getSelectedItem(),
                (StatoLettura) cmbStato.getSelectedItem(),
                Integer.parseInt(txtValutazione.getText().trim())
        );
    }
}