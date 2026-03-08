import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public final class UIUtils {

    private static boolean initialized = false;

    public static final Color PRIMARY = new Color(18, 78, 200);
    public static final Color PRIMARY_HOVER = new Color(14, 64, 170);
    public static final Color ON_PRIMARY = Color.WHITE;
    public static final Color BORDER = new Color(220, 220, 220);
    public static final Color MUTED_TEXT = new Color(110, 120, 135);
    public static final Color SURFACE = new Color(248, 250, 252);

    public static final Color TEXT = new Color(22, 28, 38);
    public static final Color CARD_BG = Color.WHITE;
    public static final Color FIELD_BG = Color.WHITE;
    public static final Color FIELD_BORDER = new Color(208, 216, 228);
    public static final Color TABLE_ALT = new Color(246, 249, 255);
    public static final Color APP_BAR = new Color(12, 74, 110);
    public static final Color APP_BAR_ALT = new Color(18, 97, 140);

    private UIUtils() {
    }

    public static void initLookAndFeel() {
        if (initialized) {
            return;
        }
        initialized = true;

        try {
            // Prefer Nimbus if available; otherwise use system LAF.
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    applyFontDefaults(new Font("Segoe UI", Font.PLAIN, 14));
                    return;
                }
            }
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            applyFontDefaults(new Font("Segoe UI", Font.PLAIN, 14));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignored) {
        }
    }

    private static void applyFontDefaults(Font font) {
        if (font == null) {
            return;
        }

        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof Font) {
                UIManager.put(key, font);
            }
        }
    }

    public static JPanel pageRoot() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(new EmptyBorder(20, 20, 20, 20));
        root.setOpaque(true);
        root.setBackground(SURFACE);
        return root;
    }

    public static JPanel appBar(String title, String subtitle, JComponent rightActions) {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBorder(new EmptyBorder(16, 18, 16, 18));
        bar.setBackground(APP_BAR);

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel t = new JLabel(title);
        t.setForeground(Color.WHITE);
        t.setFont(t.getFont().deriveFont(Font.BOLD, t.getFont().getSize2D() + 8f));
        left.add(t);

        if (subtitle != null && !subtitle.trim().isEmpty()) {
            JLabel s = new JLabel(subtitle);
            s.setForeground(new Color(229, 242, 255));
            s.setBorder(new EmptyBorder(4, 0, 0, 0));
            left.add(s);
        }

        bar.add(left, BorderLayout.WEST);

        if (rightActions != null) {
            rightActions.setOpaque(false);
            bar.add(rightActions, BorderLayout.EAST);
        }

        return bar;
    }

    public static JPanel kpiCard(String label, String value) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(12, 14, 12, 14)
        ));

        JLabel l = mutedLabel(label);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(l);

        JLabel v = new JLabel(value);
        v.setForeground(TEXT);
        v.setFont(v.getFont().deriveFont(Font.BOLD, v.getFont().getSize2D() + 7f));
        v.setBorder(new EmptyBorder(4, 0, 0, 0));
        v.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(v);

        return panel;
    }

    public static JPanel sectionPanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setOpaque(true);
        panel.setBackground(CARD_BG);
        panel.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(16, 16, 16, 16)
        ));
        return panel;
    }

    public static JPanel header(String title, String subtitle) {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(0, 0, 14, 0));
        header.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, titleLabel.getFont().getSize2D() + 10f));
        titleLabel.setForeground(TEXT);
        header.add(titleLabel);

        if (subtitle != null && !subtitle.trim().isEmpty()) {
            JLabel subtitleLabel = new JLabel(subtitle);
            subtitleLabel.setBorder(new EmptyBorder(6, 0, 0, 0));
            subtitleLabel.setForeground(MUTED_TEXT);
            header.add(subtitleLabel);
        }

        return header;
    }

    public static Border cardBorder() {
        return new CompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(16, 16, 16, 16)
        );
    }

    public static RoundedCardPanel card() {
        return new RoundedCardPanel();
    }

    public static RoundedCardPanel wrapInCard(JComponent content) {
        RoundedCardPanel card = new RoundedCardPanel();
        card.add(content, BorderLayout.CENTER);
        return card;
    }

    public static JLabel mutedLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(MUTED_TEXT);
        return label;
    }

    public static JLabel sectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD, label.getFont().getSize2D() + 3f));
        label.setForeground(TEXT);
        return label;
    }

    public static JTextArea readOnlyText(String text) {
        JTextArea area = new JTextArea(text);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setOpaque(false);
        area.setBorder(null);
        area.setForeground(new Color(55, 65, 80));
        area.setFont(area.getFont().deriveFont(area.getFont().getSize2D() + 0.5f));
        return area;
    }

    public static void styleTextField(JTextField field) {
        if (field == null) {
            return;
        }
        field.setBackground(FIELD_BG);
        field.setForeground(TEXT);
        field.setCaretColor(TEXT);

        Border normal = new CompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER),
                new EmptyBorder(10, 12, 10, 12)
        );
        Border focused = new CompoundBorder(
                BorderFactory.createLineBorder(PRIMARY, 2),
                new EmptyBorder(9, 11, 9, 11)
        );
        installFocusBorder(field, normal, focused);
    }

    public static void styleTextArea(JTextArea area) {
        if (area == null) {
            return;
        }
        area.setBackground(FIELD_BG);
        area.setForeground(TEXT);
        area.setCaretColor(TEXT);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        Border normal = new CompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER),
                new EmptyBorder(10, 12, 10, 12)
        );
        Border focused = new CompoundBorder(
                BorderFactory.createLineBorder(PRIMARY, 2),
                new EmptyBorder(9, 11, 9, 11)
        );
        installFocusBorder(area, normal, focused);
    }

    private static void installFocusBorder(JComponent component, Border normal, Border focused) {
        component.setBorder(normal);
        component.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                component.setBorder(focused);
            }

            @Override
            public void focusLost(FocusEvent e) {
                component.setBorder(normal);
            }
        });
    }

    public static void styleScrollPane(JScrollPane scrollPane) {
        if (scrollPane == null) {
            return;
        }
        scrollPane.getViewport().setBackground(CARD_BG);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER));
    }

    public static void styleTable(JTable table) {
        if (table == null) {
            return;
        }

        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(214, 232, 255));
        table.setSelectionForeground(TEXT);
        table.setForeground(TEXT);
        table.setBackground(CARD_BG);

        JTableHeader header = table.getTableHeader();
        if (header != null) {
            header.setFont(header.getFont().deriveFont(Font.BOLD));
            header.setForeground(new Color(55, 65, 80));
            header.setBackground(new Color(242, 245, 250));
            header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
            header.setReorderingAllowed(false);
        }

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (c == null) {
                    return null;
                }
                if (c instanceof JComponent) {
                    ((JComponent) c).setBorder(new EmptyBorder(0, 12, 0, 12));
                }
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? CARD_BG : TABLE_ALT);
                }
                return c;
            }
        };
        table.setDefaultRenderer(Object.class, renderer);
    }

    public static void styleList(JList<?> list) {
        if (list == null) {
            return;
        }
        list.setBackground(CARD_BG);
        list.setForeground(TEXT);
        list.setSelectionBackground(new Color(214, 232, 255));
        list.setSelectionForeground(TEXT);
        list.setBorder(new EmptyBorder(6, 6, 6, 6));
    }

    public static void stylePrimaryButton(AbstractButton button) {
        if (button == null) {
            return;
        }
        button.putClientProperty("JButton.buttonType", "roundRect");
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setBackground(PRIMARY);
        button.setForeground(ON_PRIMARY);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(11, 20, 11, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFont(button.getFont().deriveFont(Font.BOLD));
    }

    public static void styleSecondaryButton(AbstractButton button) {
        if (button == null) {
            return;
        }
        button.putClientProperty("JButton.buttonType", "roundRect");
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(45, 55, 70));
        button.setFocusPainted(false);
        button.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(BORDER),
            new EmptyBorder(11, 20, 11, 20)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFont(button.getFont().deriveFont(Font.BOLD));
    }

    public static void stylePillToggle(JToggleButton button) {
        if (button == null) {
            return;
        }
        button.putClientProperty("JButton.buttonType", "roundRect");
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorder(new EmptyBorder(8, 14, 8, 14));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        updatePillToggle(button);
        button.addChangeListener(e -> updatePillToggle(button));
    }

    private static void updatePillToggle(JToggleButton button) {
        if (button.isSelected()) {
            button.setBackground(PRIMARY);
            button.setForeground(ON_PRIMARY);
        } else {
            button.setBackground(new Color(235, 240, 248));
            button.setForeground(new Color(45, 55, 70));
        }
        button.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(button.isSelected() ? PRIMARY.darker() : BORDER),
                new EmptyBorder(8, 14, 8, 14)
        ));
    }

    public static JComponent maxWidth(JComponent content, int maxWidth) {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        content.setMaximumSize(new Dimension(maxWidth, Integer.MAX_VALUE));
        content.setPreferredSize(new Dimension(Math.min(maxWidth, content.getPreferredSize().width), content.getPreferredSize().height));
        wrapper.add(content, gbc);
        return wrapper;
    }

    public static void styleSegmentButton(JToggleButton button) {
        if (button == null) {
            return;
        }
        button.setFocusPainted(false);
        button.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 110)),
                new EmptyBorder(6, 14, 6, 14)
        ));
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        updateSegmentButtonColors(button);
        button.addChangeListener(e -> updateSegmentButtonColors(button));
    }

    private static void updateSegmentButtonColors(JToggleButton button) {
        if (button.isSelected()) {
            button.setBackground(Color.WHITE);
            button.setForeground(PRIMARY);
        } else {
            button.setBackground(new Color(255, 255, 255, 26));
            button.setForeground(new Color(255, 255, 255, 230));
        }
    }

    public static void centerOnScreen(Window window) {
        if (window != null) {
            window.setLocationRelativeTo(null);
        }
    }

    public static void showFullScreen(JFrame frame) {
        if (frame == null) {
            return;
        }
        SwingUtilities.invokeLater(() -> {
            frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            centerOnScreen(frame);
            frame.setVisible(true);
        });
    }
}
