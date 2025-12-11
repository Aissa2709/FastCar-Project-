package com.fastcar.ui;

import com.fastcar.ui.components.ModernButton;
import com.fastcar.ui.components.ModernButton.ButtonType;
import com.fastcar.ui.theme.ModernTheme;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;
    private ModernButton currentActiveButton;

    public MainFrame() {
        setTitle("FastCar Location - Gestion d'Agence");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Sidebar (West) ---
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(ModernTheme.SIDEBAR);
        sidebar.setPreferredSize(new Dimension(250, 0));

        // Sidebar Header / Logo Area
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(ModernTheme.SIDEBAR);
        logoPanel.setBorder(new EmptyBorder(30, 0, 30, 0));

        JLabel lblHeader = new JLabel("FastCar Location", JLabel.CENTER);
        lblHeader.setForeground(ModernTheme.TEXT_LIGHT);
        lblHeader.setFont(ModernTheme.HEADER_FONT);
        logoPanel.add(lblHeader);

        sidebar.add(logoPanel, BorderLayout.NORTH);

        // Sidebar Buttons Panel
        JPanel buttonPanel = new JPanel(new GridLayout(8, 1, 0, 5)); // 8 rows to have space
        buttonPanel.setBackground(ModernTheme.SIDEBAR);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Create Navigation Buttons
        ModernButton btnVoitures = new ModernButton("  Gérer Voitures", ButtonType.SIDEBAR);
        ModernButton btnClients = new ModernButton("  Gérer Clients", ButtonType.SIDEBAR);
        ModernButton btnAgents = new ModernButton("  Gérer Agents", ButtonType.SIDEBAR);
        ModernButton btnContrats = new ModernButton("  Gérer Contrats", ButtonType.SIDEBAR);
        ModernButton btnNouveauContrat = new ModernButton("  Nouveau Contrat", ButtonType.SIDEBAR);

        buttonPanel.add(btnVoitures);
        buttonPanel.add(btnClients);
        buttonPanel.add(btnAgents);
        buttonPanel.add(btnContrats);
        buttonPanel.add(btnNouveauContrat);
        // Add spacers/empty panels if needed for visual push

        sidebar.add(buttonPanel, BorderLayout.CENTER);

        // Version/Footer
        JLabel lblVersion = new JLabel("v1.0.0", JLabel.CENTER);
        lblVersion.setForeground(ModernTheme.GRAY_TEXT);
        lblVersion.setBorder(new EmptyBorder(10, 0, 10, 0));
        sidebar.add(lblVersion, BorderLayout.SOUTH);

        add(sidebar, BorderLayout.WEST);

        // --- Content Area (Center) ---
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(ModernTheme.BACKGROUND);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Margin around content

        // Initialize Panels
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
            setActiveButton(btnVoitures);
            panelVoitures.refreshData();
            cardLayout.show(contentPanel, "VOITURES");
        });

        btnClients.addActionListener(e -> {
            setActiveButton(btnClients);
            panelClients.refreshData();
            cardLayout.show(contentPanel, "CLIENTS");
        });

        btnAgents.addActionListener(e -> {
            setActiveButton(btnAgents);
            panelAgents.refreshData();
            cardLayout.show(contentPanel, "AGENTS");
        });

        btnContrats.addActionListener(e -> {
            setActiveButton(btnContrats);
            panelContrats.refreshData();
            cardLayout.show(contentPanel, "CONTRATS_LIST");
        });

        btnNouveauContrat.addActionListener(e -> {
            setActiveButton(btnNouveauContrat);
            formContrat.refreshData();
            cardLayout.show(contentPanel, "CONTRAT_FORM");
        });

        // precise init
        setActiveButton(btnVoitures);
    }

    private void setActiveButton(ModernButton btn) {
        if (currentActiveButton != null) {
            currentActiveButton.setIsSelected(false);
        }
        currentActiveButton = btn;
        if (currentActiveButton != null) {
            currentActiveButton.setIsSelected(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MainFrame().setVisible(true);
        });
    }
}
