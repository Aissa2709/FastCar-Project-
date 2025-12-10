package com.fastcar.ui;

import com.fastcar.dao.AgentDAO;
import com.fastcar.dao.ClientDAO;
import com.fastcar.dao.ContratDAO;
import com.fastcar.dao.VoitureDAO;
import com.fastcar.model.Agent;
import com.fastcar.model.Client;
import com.fastcar.model.Contrat;
import com.fastcar.model.Voiture;
import com.fastcar.util.PDFInvoiceGenerator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class FormContrat extends JPanel {

    private JComboBox<Client> cbClient;
    private JComboBox<Voiture> cbVoiture;
    private JComboBox<Agent> cbAgent;
    private JTextField txtDateDebut;
    private JTextField txtDateFin;
    private JRadioButton rbEspece;
    private JRadioButton rbCarte;
    private JRadioButton rbVirement;
    private JRadioButton rbCheque;
    private JLabel lblTotal;

    public FormContrat() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- Title ---
        JLabel lblTitle = new JLabel("Nouveau Contrat de Location");
        lblTitle.setFont(lblTitle.getFont().deriveFont(20.0f));
        
        // --- Top Panel with Title and Refresh Button ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(lblTitle);
        topPanel.add(new JLabel("   ")); // Spacer
        
        JButton btnRefresh = new JButton("🔄 Actualiser les listes");
        btnRefresh.addActionListener(e -> refreshData());
        topPanel.add(btnRefresh);
        
        add(topPanel, BorderLayout.NORTH);

        // --- Form Panel ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Client
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Client:"), gbc);

        gbc.gridx = 1;
        cbClient = new JComboBox<>();
        loadClients();
        formPanel.add(cbClient, gbc);

        // Voiture
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Voiture:"), gbc);

        gbc.gridx = 1;
        cbVoiture = new JComboBox<>();
        loadVoitures();
        formPanel.add(cbVoiture, gbc);

        // Agent
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Agent:"), gbc);

        gbc.gridx = 1;
        cbAgent = new JComboBox<>();
        loadAgents();
        formPanel.add(cbAgent, gbc);

        // Dates
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Date Début (DD/MM/YYYY):"), gbc);
        gbc.gridx = 1;
        txtDateDebut = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        formPanel.add(txtDateDebut, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Date Fin (DD/MM/YYYY):"), gbc);
        gbc.gridx = 1;
        txtDateFin = new JTextField(LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        formPanel.add(txtDateFin, gbc);

        // Payment
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Mode de Paiement:"), gbc);

        gbc.gridx = 1;
        JPanel panelPayment = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelPayment.setBackground(Color.WHITE);
        rbEspece = new JRadioButton("Espèces");
        rbEspece.setBackground(Color.WHITE);
        rbEspece.setSelected(true);
        rbCarte = new JRadioButton("Carte");
        rbCarte.setBackground(Color.WHITE);
        rbVirement = new JRadioButton("Virement");
        rbVirement.setBackground(Color.WHITE);
        rbCheque = new JRadioButton("Chèque");
        rbCheque.setBackground(Color.WHITE);

        ButtonGroup bg = new ButtonGroup();
        bg.add(rbEspece);
        bg.add(rbCarte);
        bg.add(rbVirement);
        bg.add(rbCheque);

        panelPayment.add(rbEspece);
        panelPayment.add(new JLabel("  "));
        panelPayment.add(rbCarte);
        panelPayment.add(new JLabel("  "));
        panelPayment.add(rbVirement);
        panelPayment.add(new JLabel("  "));
        panelPayment.add(rbCheque);
        formPanel.add(panelPayment, gbc);

        // Total
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Total Estimé:"), gbc);
        gbc.gridx = 1;
        lblTotal = new JLabel("0.00 DH");
        lblTotal.setFont(lblTotal.getFont().deriveFont(java.awt.Font.BOLD, 14f));
        formPanel.add(lblTotal, gbc);

        // Recalculate total when voiture or dates change
        cbVoiture.addActionListener(e -> calculateTotal());
        txtDateDebut.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { calculateTotal(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { calculateTotal(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { calculateTotal(); }
        });
        txtDateFin.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { calculateTotal(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { calculateTotal(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { calculateTotal(); }
        });

        add(formPanel, BorderLayout.CENTER);

        // --- Buttons ---
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southPanel.setBackground(Color.WHITE);

        JButton btnSave = new JButton("Enregistrer Contrat");
        btnSave.setBackground(new Color(40, 167, 69));
        btnSave.setForeground(Color.WHITE);

        JButton btnInvoice = new JButton("Générer Facture");
        btnInvoice.setBackground(new Color(23, 162, 184));
        btnInvoice.setForeground(Color.WHITE);

        // Save action uses DAO
        btnSave.addActionListener(e -> saveContract());

        // Generate Invoice also saves first
        btnInvoice.addActionListener(e -> generateInvoiceAction());

        southPanel.add(btnSave);
        southPanel.add(btnInvoice);

        add(southPanel, BorderLayout.SOUTH);

        calculateTotal(); // Initial calc
    }

    public void refreshData() {
        loadClients();
        loadVoitures();
        loadAgents();
        calculateTotal();
        JOptionPane.showMessageDialog(this, "Listes actualisées !", "Succès", JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadClients() {
        ClientDAO dao = new ClientDAO();
        List<Client> clients = dao.getAllClients();
        cbClient.removeAllItems();
        for (Client c : clients) {
            cbClient.addItem(c);
        }
    }

    private void loadVoitures() {
        VoitureDAO dao = new VoitureDAO();
        List<Voiture> voitures = dao.getAllVoitures();
        cbVoiture.removeAllItems();
        for (Voiture v : voitures) {
            cbVoiture.addItem(v);
        }
    }

    private void loadAgents() {
        AgentDAO dao = new AgentDAO();
        List<Agent> agents = dao.getAllAgents();
        cbAgent.removeAllItems();
        for (Agent a : agents) {
            cbAgent.addItem(a);
        }
    }

    private void calculateTotal() {
        try {
            Voiture v = (Voiture) cbVoiture.getSelectedItem();
            if (v != null) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate dStart = LocalDate.parse(txtDateDebut.getText(), dtf);
                LocalDate dEnd = LocalDate.parse(txtDateFin.getText(), dtf);
                
                long nbJours = java.time.temporal.ChronoUnit.DAYS.between(dStart, dEnd);
                if (nbJours < 1) nbJours = 1;
                
                double price = v.getPrixJournalier() * nbJours;
                lblTotal.setText(String.format("%.2f DH", price));
            }
        } catch (Exception e) {
            lblTotal.setText("Erreur calcul");
        }
    }

    private Contrat createContractFromForm() {
        Client client = (Client) cbClient.getSelectedItem();
        Voiture voiture = (Voiture) cbVoiture.getSelectedItem();
        Agent agent = (Agent) cbAgent.getSelectedItem();

        if (client == null || voiture == null || agent == null) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un client, une voiture et un agent", "Erreur", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // Date Parsing
        Date dStart = new Date();
        Date dEnd = new Date();
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            dStart = java.util.Date.from(
                    LocalDate.parse(txtDateDebut.getText(), dtf).atStartOfDay(ZoneId.systemDefault()).toInstant());
            dEnd = java.util.Date
                    .from(LocalDate.parse(txtDateFin.getText(), dtf).atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Format de date invalide (DD/MM/YYYY)", "Erreur", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        String mode = rbEspece.isSelected() ? "Espèce" : 
                      (rbCarte.isSelected() ? "Carte bancaire" : 
                       (rbVirement.isSelected() ? "Virement" : "Chèque"));
        
        double montant = 0;
        try {
            String totalStr = lblTotal.getText().replace(" DH", "").replace(",", ".");
            montant = Double.parseDouble(totalStr);
        } catch (Exception e) {
        }

        String id = "LOC-" + System.currentTimeMillis();

        return new Contrat(id, dStart, dEnd, montant, mode, 0, client.getCin(), voiture.getMatricule(), agent.getNumAgent());
    }

    private void saveContract() {
        Contrat c = createContractFromForm();
        if (c == null) return;
        
        ContratDAO dao = new ContratDAO();
        if (dao.addContrat(c)) {
            // Mettre à jour l'état de la voiture à "Louée"
            Voiture voiture = (Voiture) cbVoiture.getSelectedItem();
            if (voiture != null) {
                new VoitureDAO().updateVoitureEtat(voiture.getMatricule(), "Louée");
            }
            JOptionPane.showMessageDialog(this, "Contrat enregistré et voiture marquée louée !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generateInvoiceAction() {
        Contrat c = createContractFromForm();
        if (c == null) return;
        
        Client client = (Client) cbClient.getSelectedItem();
        Voiture voiture = (Voiture) cbVoiture.getSelectedItem();
        Agent agent = (Agent) cbAgent.getSelectedItem();

        // Save first
        new ContratDAO().addContrat(c);
        
        // Mettre à jour l'état de la voiture à "Louée"
        if (voiture != null) {
            new VoitureDAO().updateVoitureEtat(voiture.getMatricule(), "Louée");
        }
        
        // Mettre à jour le statut du contrat à "Facturée"
        c.setStatut("Facturée");
        new ContratDAO().updateContratStatut(c.getId(), "Facturée");

        // Générer facture HTML (imprimable/sauvegardable en PDF)
        java.io.File htmlFile = PDFInvoiceGenerator.generateHTMLInvoice(c, client, voiture, agent);

        if (htmlFile != null) {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Facture HTML générée et enregistrée.\nVoulez-vous l'ouvrir ?\n\n(Utilisez Imprimer/Enregistrer en PDF depuis le navigateur)",
                    "Facture",
                    JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                try {
                    Desktop.getDesktop().open(htmlFile);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Impossible d'ouvrir le fichier: " + htmlFile.getAbsolutePath());
                }
            }
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur génération facture", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        txtDateDebut.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        txtDateFin.setText(LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        rbEspece.setSelected(true);
        calculateTotal();
    }
}