package com.fastcar.ui;

import com.fastcar.dao.AgentDAO;
import com.fastcar.model.Agent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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

public class PanelAgents extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private AgentDAO agentDAO;

    public PanelAgents() {
        agentDAO = new AgentDAO();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- Top Panel: Title, Search, and Buttons ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Gestion des Agents");
        lblTitle.setFont(lblTitle.getFont().deriveFont(20.0f));

        JTextField txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Rechercher");
        JButton btnAjouter = new JButton("+ Ajouter Agent");
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
        String[] columnNames = { "Numéro", "Nom", "Prénom" };
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

        btnAjouter.addActionListener(e -> showAddAgentDialog());

        btnSupprimer.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Sélectionnez un agent à supprimer", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String numero = (String) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cet agent ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (agentDAO.deleteAgent(numero)) {
                    refreshData();
                    JOptionPane.showMessageDialog(this, "Agent supprimé avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnActualiser.addActionListener(e -> refreshData());

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Initial load
        refreshData();
    }

    private void showAddAgentDialog() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtNumero = new JTextField(15);
        JTextField txtNom = new JTextField(15);
        JTextField txtPrenom = new JTextField(15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Numéro:"), gbc);
        gbc.gridx = 1;
        panel.add(txtNumero, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Nom:"), gbc);
        gbc.gridx = 1;
        panel.add(txtNom, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Prénom:"), gbc);
        gbc.gridx = 1;
        panel.add(txtPrenom, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel, "Ajouter un Agent", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String numero = txtNumero.getText().trim();
            String nom = txtNom.getText().trim();
            String prenom = txtPrenom.getText().trim();

            if (numero.isEmpty() || nom.isEmpty() || prenom.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Agent agent = new Agent(numero, nom, prenom);
            if (agentDAO.addAgent(agent)) {
                refreshData();
                JOptionPane.showMessageDialog(this, "Agent ajouté avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de l'agent", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        List<Agent> agents = agentDAO.getAllAgents();
        for (Agent agent : agents) {
            Object[] row = {
                agent.getNumAgent(),
                agent.getNom(),
                agent.getPrenom()
            };
            tableModel.addRow(row);
        }
    }
}
