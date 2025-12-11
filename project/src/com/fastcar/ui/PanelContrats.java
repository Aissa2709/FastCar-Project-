package com.fastcar.ui;

import com.fastcar.dao.ContratDAO;
import com.fastcar.dao.ClientDAO;
import com.fastcar.dao.VoitureDAO;
import com.fastcar.dao.AgentDAO;
import com.fastcar.model.Contrat;
import com.fastcar.model.Client;
import com.fastcar.model.Voiture;
import com.fastcar.model.Agent;
import com.fastcar.ui.components.ModernButton;
import com.fastcar.ui.components.ModernButton.ButtonType;
import com.fastcar.ui.components.ModernTable;
import com.fastcar.ui.theme.ModernTheme;
import com.fastcar.util.PDFInvoiceGenerator;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class PanelContrats extends JPanel {

    private ModernTable table;
    private DefaultTableModel tableModel;
    private ContratDAO contratDAO;

    public PanelContrats() {
        contratDAO = new ContratDAO();
        setLayout(new BorderLayout());
        setBackground(ModernTheme.BACKGROUND);
        setBorder(null);

        // --- Top Panel: Title, Search, and Buttons ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(ModernTheme.BACKGROUND);
        topPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel lblTitle = new JLabel("Liste des Contrats");
        lblTitle.setFont(ModernTheme.HEADER_FONT);
        lblTitle.setForeground(ModernTheme.TEXT_DARK);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionsPanel.setBackground(ModernTheme.BACKGROUND);

        JTextField txtSearch = new JTextField(20);
        txtSearch.setFont(ModernTheme.MAIN_FONT);

        ModernButton btnSearch = new ModernButton("Rechercher", ButtonType.SECONDARY);
        ModernButton btnGenererFacture = new ModernButton("📄 Générer Facture", ButtonType.PRIMARY);
        ModernButton btnSupprimer = new ModernButton("Supprimer", ButtonType.DANGER);
        ModernButton btnActualiser = new ModernButton("Actualiser", ButtonType.SECONDARY);

        actionsPanel.add(txtSearch);
        actionsPanel.add(btnSearch);
        actionsPanel.add(new JLabel("  "));
        actionsPanel.add(btnGenererFacture);
        actionsPanel.add(btnSupprimer);
        actionsPanel.add(btnActualiser);

        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(actionsPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // --- Table ---
        String[] columnNames = { "Numéro", "Client", "Voiture", "Début", "Fin", "Montant", "Paiement", "Statut" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new ModernTable(tableModel);

        // Enable Sorting
        javax.swing.table.TableRowSorter<DefaultTableModel> sorter = new javax.swing.table.TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        table.setFillsViewportHeight(true);

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
                if (text.trim().length() == 0)
                    sorter.setRowFilter(null);
                else {
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
            if (text.trim().length() == 0)
                sorter.setRowFilter(null);
            else
                sorter.setRowFilter(javax.swing.RowFilter.regexFilter("(?i)" + text));
        });

        btnSupprimer.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Sélectionnez un contrat à supprimer", "Erreur",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            int modelRow = table.convertRowIndexToModel(selectedRow);
            String numero = (String) tableModel.getValueAt(modelRow, 0);

            int confirm = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer ce contrat ?",
                    "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (contratDAO.deleteContrat(numero)) {
                    refreshData();
                    JOptionPane.showMessageDialog(this, "Contrat supprimé avec succès", "Succès",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression", "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnActualiser.addActionListener(e -> refreshData());

        btnGenererFacture.addActionListener(e -> generateFactureAction());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(ModernTheme.BACKGROUND);
        add(scrollPane, BorderLayout.CENTER);

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
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un contrat", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow); // Safety check
        String contratId = (String) tableModel.getValueAt(modelRow, 0);
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
            JOptionPane.showMessageDialog(this, "Erreur lors de la génération de la facture", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
