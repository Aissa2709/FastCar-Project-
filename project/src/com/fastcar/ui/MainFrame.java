package com.fastcar.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;

    public MainFrame() {
        setTitle("FastCar Location - Gestion d'Agence");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Sidebar (West) ---
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(Color.DARK_GRAY);
        sidebar.setPreferredSize(new Dimension(220, 0));

        // Sidebar Header
        JLabel lblHeader = new JLabel("FastCar Location", JLabel.CENTER);
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 18));
        lblHeader.setBorder(new EmptyBorder(20, 10, 20, 10));
        sidebar.add(lblHeader, BorderLayout.NORTH);

        // Sidebar Buttons Panel
        JPanel buttonPanel = new JPanel(new GridLayout(7, 1, 0, 10));
        buttonPanel.setBackground(Color.DARK_GRAY);
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton btnVoitures = createSidebarButton("Gérer Voitures");
        JButton btnClients = createSidebarButton("Gérer Clients");
        JButton btnAgents = createSidebarButton("Gérer Agents");
        JButton btnContrats = createSidebarButton("Gérer Contrats");
        JButton btnNouveauContrat = createSidebarButton("Nouveau Contrat");

        buttonPanel.add(btnVoitures);
        buttonPanel.add(btnClients);
        buttonPanel.add(btnAgents);
        buttonPanel.add(btnContrats);
        buttonPanel.add(btnNouveauContrat);

        sidebar.add(buttonPanel, BorderLayout.CENTER);

        add(sidebar, BorderLayout.WEST);

        // --- Content Area (Center) ---
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        // Panels
        PanelVoitures panelVoitures = new PanelVoitures();
        PanelClients panelClients = new PanelClients();
        PanelAgents panelAgents = new PanelAgents();
        FormContrat formContrat = new FormContrat();
        PanelContrats panelContrats = new PanelContrats();

        contentPanel.add(panelVoitures, "VOITURES");
        contentPanel.add(panelClients, "CLIENTS");
        contentPanel.add(panelAgents, "AGENTS");
        contentPanel.add(panelContrats, "CONTRATS_LIST");
        contentPanel.add(formContrat, "CONTRAT_FORM");

        add(contentPanel, BorderLayout.CENTER);

        // --- Action Listeners ---
        btnVoitures.addActionListener(e -> {
            panelVoitures.refreshData();
            cardLayout.show(contentPanel, "VOITURES");
        });
        
        btnClients.addActionListener(e -> {
            panelClients.refreshData();
            cardLayout.show(contentPanel, "CLIENTS");
        });
        
        btnAgents.addActionListener(e -> {
            panelAgents.refreshData();
            cardLayout.show(contentPanel, "AGENTS");
        });
        
        btnContrats.addActionListener(e -> {
            panelContrats.refreshData();
            cardLayout.show(contentPanel, "CONTRATS_LIST");
        });
        
        btnNouveauContrat.addActionListener(e -> {
            formContrat.refreshData();
            cardLayout.show(contentPanel, "CONTRAT_FORM");
        });
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setForeground(Color.WHITE);
        btn.setBackground(Color.GRAY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MainFrame().setVisible(true);
        });
    }
}
