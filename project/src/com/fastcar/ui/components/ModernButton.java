package com.fastcar.ui.components;

import com.fastcar.ui.theme.ModernTheme;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

public class ModernButton extends JButton {
    public enum ButtonType {
        PRIMARY, SECONDARY, DANGER, SIDEBAR, SUCCESS
    }

    private ButtonType type;
    private Color normalColor;
    private Color hoverColor;
    private Color selectedColor;
    private boolean isHovered = false;
    private boolean isSelected = false;

    public ModernButton(String text) {
        this(text, ButtonType.PRIMARY);
    }

    public ModernButton(String text, ButtonType type) {
        super(text);
        this.type = type;
        setupColors();

        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setFont(ModernTheme.BUTTON_FONT);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Padding
        if (type == ButtonType.SIDEBAR) {
            setBorder(new EmptyBorder(10, 20, 10, 20)); // Left align padding
            setHorizontalAlignment(LEFT);
        } else {
            setBorder(new EmptyBorder(8, 15, 8, 15));
            setHorizontalAlignment(CENTER);
        }

        // Hover Effect
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    public void setIsSelected(boolean selected) {
        this.isSelected = selected;
        repaint();
    }

    private void setupColors() {
        switch (type) {
            case PRIMARY:
                normalColor = ModernTheme.PRIMARY;
                hoverColor = ModernTheme.PRIMARY_HOVER;
                setForeground(Color.WHITE);
                break;
            case SECONDARY:
                normalColor = ModernTheme.GRAY_LIGHT;
                hoverColor = new Color(200, 200, 200);
                setForeground(ModernTheme.TEXT_DARK);
                break;
            case DANGER:
                normalColor = ModernTheme.DANGER;
                hoverColor = new Color(192, 57, 43);
                setForeground(Color.WHITE);
                break;
            case SUCCESS:
                normalColor = new Color(40, 167, 69); // Green
                hoverColor = new Color(33, 136, 56);
                setForeground(Color.WHITE);
                break;
            case SIDEBAR:
                normalColor = ModernTheme.SIDEBAR;
                hoverColor = new Color(52, 73, 94); // Lighter shade of sidebar
                selectedColor = ModernTheme.PRIMARY; // Highlight color for active tab
                setForeground(ModernTheme.TEXT_LIGHT);
                break;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int arcs = (type == ButtonType.SIDEBAR) ? 0 : 10;

        if (isSelected && type == ButtonType.SIDEBAR) {
            g2.setColor(selectedColor);
        } else if (isHovered) {
            g2.setColor(hoverColor);
        } else {
            g2.setColor(normalColor);
        }

        // Fill background logic
        // For Sidebar: Only paint if hovered or selected (otherwise it's
        // transparent/same as sidebar bg)
        if (type == ButtonType.SIDEBAR) {
            if (isHovered || isSelected) {
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcs, arcs);
            }
        } else {
            // Standard buttons always painted
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcs, arcs);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
