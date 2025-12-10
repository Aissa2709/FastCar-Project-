package com.fastcar.ui;

import com.fastcar.dao.ContratDAO;
import com.fastcar.dao.ClientDAO;
import com.fastcar.dao.VoitureDAO;
import com.fastcar.dao.AgentDAO;
import com.fastcar.model.Contrat;
import com.fastcar.model.Client;
import com.fastcar.model.Voiture;
import com.fastcar.model.Agent;
import com.fastcar.util.PDFInvoiceGenerator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class PanelContrats extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private ContratDAO contratDAO;

    public PanelContrats() {
        contratDAO = new ContratDAO();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- Top Panel: Title, Search, and Buttons ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Liste des Contrats");
        lblTitle.setFont(lblTitle.getFont().deriveFont(20.0f));

        JTextField txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Rechercher");
        JButton btnGenererFacture = new JButton("📄 Générer Facture");
        JButton btnSupprimer = new JButton("✕ Supprimer");
        JButton btnActualiser = new JButton("Actualiser");

        topPanel.add(lblTitle);
        topPanel.add(new JLabel("   "));
        topPanel.add(txtSearch);
        topPanel.add(btnSearch);
        topPanel.add(new JLabel("   "));
        topPanel.add(btnGenererFacture);
        topPanel.add(btnSupprimer);
        topPanel.add(btnActualiser);

        add(topPanel, BorderLayout.NORTH);

        // --- Table ---
        String[] columnNames = { "Numéro", "Client", "Voiture", "Début", "Fin", "Montant", "Paiement", "Statut" };
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        // Enable Sorting
        javax.swing.table.TableRowSorter<DefaultTableModel> sorter = new javax.swing.table.TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Search Logic
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filter();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filter();
            }

            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filter();
            }

            public void filter() {
                String text = txtSearch.getText();
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    try {
                        sorter.setRowFilter(javax.swing.RowFilter.regexFilter("(?i)" + text));
                    } catch (java.util.regex.PatternSyntaxException pse) {
                        System.err.println("Bad regex pattern");
                    }
                }
            }
        });

        btnSearch.addActionListener(e -> {
            String text = txtSearch.getText();
            if (text.trim().length() == 0) {
                sorter.setRowFilter(null);
            } else {
                try {
                    sorter.setRowFilter(javax.swing.RowFilter.regexFilter("(?i)" + text));
                } catch (java.util.regex.PatternSyntaxException pse) {
                    System.err.println("Bad regex pattern");
                }
            }
        });

        btnSupprimer.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Sélectionnez un contrat à supprimer", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String numero = (String) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer ce contrat ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (contratDAO.deleteContrat(numero)) {
                    refreshData();
                    JOptionPane.showMessageDialog(this, "Contrat supprimé avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnActualiser.addActionListener(e -> refreshData());
        
        btnGenererFacture.addActionListener(e -> generateFactureAction());

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Initial load
        refreshData();
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        List<Contrat> contrats = contratDAO.getAllContrats();
        for (Contrat contrat : contrats) {
            String statut = contrat.getStatut() != null ? contrat.getStatut() : "Actif";
            Object[] row = {
                contrat.getId(),
                contrat.getCinClient(),
                contrat.getMatVoiture(),
                contrat.getDateDebut(),
                contrat.getDateFin(),
                String.format("%.2f DH", contrat.getMontant()),
                contrat.getModePaiement(),
                statut
            };
            tableModel.addRow(row);
        }
    }
    
    private void generateFactureAction() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un contrat", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String contratId = (String) tableModel.getValueAt(selectedRow, 0);
        Contrat contrat = new ContratDAO().getContratByNumero(contratId);
        
        if (contrat == null) {
            JOptionPane.showMessageDialog(this, "Erreur : Contrat introuvable", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Charger les objets associés
        Client client = new ClientDAO().getClientByCin(contrat.getCinClient());
        Voiture voiture = new VoitureDAO().getVoitureByMatricule(contrat.getMatVoiture());
        Agent agent = new AgentDAO().getAgentByNumero(contrat.getNumAgent());
        
        if (client == null || voiture == null || agent == null) {
            JOptionPane.showMessageDialog(this, "Erreur : Données incomplètes", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Générer facture HTML (imprimable/sauvegardable en PDF)
        java.io.File htmlFile = PDFInvoiceGenerator.generateHTMLInvoice(contrat, client, voiture, agent);
        
        if (htmlFile != null) {
            // Mettre à jour statut
            contrat.setStatut("Facturée");
            new ContratDAO().updateContratStatut(contrat.getId(), "Facturée");
            
            // Marquer voiture comme louée
            new VoitureDAO().updateVoitureEtat(voiture.getMatricule(), "Louée");
            
            // Rafraîchir
            refreshData();
            
            // Ouvrir fichier
            JOptionPane.showMessageDialog(this, "Facture HTML générée !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            try {
                Desktop.getDesktop().open(htmlFile);
            } catch (Exception ex) {
                // Ignorer si impossible d'ouvrir
            }
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de la génération de la facture", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
