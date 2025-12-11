package com.fastcar.ui;

import com.fastcar.dao.ClientDAO;
import com.fastcar.model.Client;
import com.fastcar.ui.components.ModernButton;
import com.fastcar.ui.components.ModernButton.ButtonType;
import com.fastcar.ui.components.ModernTable;
import com.fastcar.ui.theme.ModernTheme;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class PanelClients extends JPanel {

    private ModernTable table;
    private DefaultTableModel tableModel;
    private ClientDAO clientDAO;

    public PanelClients() {
        clientDAO = new ClientDAO();
        setLayout(new BorderLayout());
        setBackground(ModernTheme.BACKGROUND);
        setBorder(null);

        // --- Top Panel: Title, Search, and Buttons ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(ModernTheme.BACKGROUND);
        topPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel lblTitle = new JLabel("Gestion des Clients");
        lblTitle.setFont(ModernTheme.HEADER_FONT);
        lblTitle.setForeground(ModernTheme.TEXT_DARK);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionsPanel.setBackground(ModernTheme.BACKGROUND);

        JTextField txtSearch = new JTextField(20);
        txtSearch.setFont(ModernTheme.MAIN_FONT);

        ModernButton btnSearch = new ModernButton("Rechercher", ButtonType.SECONDARY);
        ModernButton btnAjouter = new ModernButton("+ Ajouter", ButtonType.PRIMARY);
        ModernButton btnSupprimer = new ModernButton("Supprimer", ButtonType.DANGER);
        ModernButton btnActualiser = new ModernButton("Actualiser", ButtonType.SECONDARY);

        actionsPanel.add(txtSearch);
        actionsPanel.add(btnSearch);
        actionsPanel.add(new JLabel("  "));
        actionsPanel.add(btnAjouter);
        actionsPanel.add(btnSupprimer);
        actionsPanel.add(btnActualiser);

        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(actionsPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // --- Center Panel: Table ---
        String[] columnNames = { "CIN", "Nom", "Prénom", "Adresse", "Téléphone", "Email" };
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

        btnAjouter.addActionListener(e -> showAddClientDialog());

        btnSupprimer.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Sélectionnez un client à supprimer", "Erreur",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            int modelRow = table.convertRowIndexToModel(selectedRow);
            String cin = (String) tableModel.getValueAt(modelRow, 0);

            int confirm = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer ce client ?",
                    "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (clientDAO.deleteClient(cin)) {
                    refreshData();
                    JOptionPane.showMessageDialog(this, "Client supprimé avec succès", "Succès",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression", "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnActualiser.addActionListener(e -> refreshData());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(ModernTheme.BACKGROUND);
        add(scrollPane, BorderLayout.CENTER);

        // Initial load
        refreshData();
    }

    private void showAddClientDialog() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtCIN = new JTextField(15);
        JTextField txtNom = new JTextField(15);
        JTextField txtPrenom = new JTextField(15);
        JTextField txtAdresse = new JTextField(15);
        JTextField txtTel = new JTextField(15);
        JTextField txtEmail = new JTextField(15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("CIN:"), gbc);
        gbc.gridx = 1;
        panel.add(txtCIN, gbc);

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

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Adresse:"), gbc);
        gbc.gridx = 1;
        panel.add(txtAdresse, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Téléphone:"), gbc);
        gbc.gridx = 1;
        panel.add(txtTel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel, "Ajouter un Client", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String cin = txtCIN.getText().trim();
            String nom = txtNom.getText().trim();
            String prenom = txtPrenom.getText().trim();
            String adresse = txtAdresse.getText().trim();
            String tel = txtTel.getText().trim();
            String email = txtEmail.getText().trim();

            if (cin.isEmpty() || nom.isEmpty() || prenom.isEmpty()) {
                JOptionPane.showMessageDialog(this, "CIN, Nom et Prénom sont obligatoires", "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Client client = new Client(cin, nom, prenom, adresse, tel, email);
            if (clientDAO.addClient(client)) {
                refreshData();
                JOptionPane.showMessageDialog(this, "Client ajouté avec succès", "Succès",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du client", "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        List<Client> clients = clientDAO.getAllClients();
        for (Client client : clients) {
            Object[] row = {
                    client.getCin(),
                    client.getNom(),
                    client.getPrenom(),
                    client.getAdresse(),
                    client.getTelephone(),
                    client.getEmail()
            };
            tableModel.addRow(row);
        }
    }
}
