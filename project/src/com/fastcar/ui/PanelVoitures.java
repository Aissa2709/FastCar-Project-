package com.fastcar.ui;

import com.fastcar.dao.VoitureDAO;
import com.fastcar.model.Voiture;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class PanelVoitures extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private VoitureDAO voitureDAO;

    public PanelVoitures() {
        voitureDAO = new VoitureDAO();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- Top Panel: Title and Search ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Gestion des Voitures");
        lblTitle.setFont(lblTitle.getFont().deriveFont(20.0f));

        JTextField txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Rechercher");
        JButton btnAjouter = new JButton("+ Ajouter Voiture");
        JButton btnSupprimer = new JButton("✕ Supprimer");
        JButton btnActualiser = new JButton("Actualiser");

        topPanel.add(lblTitle);
        topPanel.add(new JLabel("   "));
        topPanel.add(txtSearch);
        topPanel.add(btnSearch);
        topPanel.add(new JLabel("   "));
        topPanel.add(btnAjouter);
        topPanel.add(btnSupprimer);
        topPanel.add(btnActualiser);

        add(topPanel, BorderLayout.NORTH);

        // --- Center Panel: Table ---
        String[] columnNames = { "Matricule", "Marque", "Modèle", "Prix/Jour", "Etat" };
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        // --- Enable Sorting and Filtering ---
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

        btnAjouter.addActionListener(e -> showAddVoitureDialog());

        btnSupprimer.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Sélectionnez une voiture à supprimer", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String matricule = (String) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cette voiture ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (voitureDAO.deleteVoiture(matricule)) {
                    refreshData();
                    JOptionPane.showMessageDialog(this, "Voiture supprimée avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnActualiser.addActionListener(e -> refreshData());

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Initial load
        refreshData();
    }

    private void showAddVoitureDialog() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtMatricule = new JTextField(15);
        JTextField txtMarque = new JTextField(15);
        JTextField txtModele = new JTextField(15);
        JTextField txtPrix = new JTextField(15);
        JComboBox<String> cbEtat = new JComboBox<>(new String[]{"Disponible", "Louée", "En maintenance", "Hors service"});

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Matricule:"), gbc);
        gbc.gridx = 1;
        panel.add(txtMatricule, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Marque:"), gbc);
        gbc.gridx = 1;
        panel.add(txtMarque, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Modèle:"), gbc);
        gbc.gridx = 1;
        panel.add(txtModele, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Prix/Jour:"), gbc);
        gbc.gridx = 1;
        panel.add(txtPrix, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Etat:"), gbc);
        gbc.gridx = 1;
        panel.add(cbEtat, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel, "Ajouter une Voiture", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String matricule = txtMatricule.getText().trim();
                String marque = txtMarque.getText().trim();
                String modele = txtModele.getText().trim();
                double prix = Double.parseDouble(txtPrix.getText().trim());
                String etat = (String) cbEtat.getSelectedItem();

                if (matricule.isEmpty() || marque.isEmpty() || modele.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Matricule, Marque et Modèle sont obligatoires", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Voiture voiture = new Voiture(matricule, marque, modele, prix, etat);
                if (voitureDAO.addVoiture(voiture)) {
                    refreshData();
                    JOptionPane.showMessageDialog(this, "Voiture ajoutée avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de la voiture", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Le prix doit être un nombre valide", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        List<Voiture> voitures = voitureDAO.getAllVoitures();
        for (Voiture voiture : voitures) {
            Object[] row = {
                voiture.getMatricule(),
                voiture.getMarque(),
                voiture.getModele(),
                String.format("%.2f DH", voiture.getPrixJournalier()),
                voiture.getEtat()
            };
            tableModel.addRow(row);
        }
    }
}
