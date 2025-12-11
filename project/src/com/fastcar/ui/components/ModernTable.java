package com.fastcar.ui.components;

import com.fastcar.ui.theme.ModernTheme;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class ModernTable extends JTable {

    public ModernTable(DefaultTableModel model) {
        super(model);
        setupStyle();
    }

    private void setupStyle() {
        setRowHeight(35);
        setShowVerticalLines(false);
        setIntercellSpacing(new Dimension(0, 0));
        setFont(ModernTheme.MAIN_FONT);
        setSelectionBackground(ModernTheme.PRIMARY.brighter());
        setSelectionForeground(Color.WHITE);

        // Header Style
        JTableHeader header = getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(ModernTheme.SIDEBAR);
                setForeground(Color.WHITE);
                setFont(ModernTheme.BOLD_FONT);
                setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                return this;
            }
        });
        header.setPreferredSize(new Dimension(0, 40));

        // Cell Style
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(248, 249, 250)); // Very light gray
                    }
                }

                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); // Padding left/right
                return c;
            }
        });
    }
}
