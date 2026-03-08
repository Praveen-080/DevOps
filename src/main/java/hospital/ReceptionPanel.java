import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class ReceptionPanel extends JFrame {

    JTable table;
    DefaultTableModel model;

    JTextField nameField, ageField, diseaseField;
    JLabel queueLabel;

    JButton addBtn, deleteBtn, logoutBtn;

    public ReceptionPanel() {
        UIUtils.initLookAndFeel();

        setTitle("Reception Panel");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = UIUtils.pageRoot();

        JPanel barActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton homeBtn = new JButton("Home");
        UIUtils.styleSecondaryButton(homeBtn);
        homeBtn.addActionListener(e -> {
            MainHome.open();
            dispose();
        });
        barActions.add(homeBtn);
        root.add(UIUtils.appBar("Reception Panel", "Fast patient intake and queue management", barActions), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 14));
        content.setOpaque(false);

        JPanel metrics = new JPanel(new GridLayout(1, 4, 10, 0));
        metrics.setOpaque(false);
        metrics.add(UIUtils.kpiCard("Waiting", "12"));
        metrics.add(UIUtils.kpiCard("Checked In", "37"));
        metrics.add(UIUtils.kpiCard("New Patients", "6"));
        metrics.add(UIUtils.kpiCard("Avg Wait", "18m"));
        content.add(metrics, BorderLayout.NORTH);

        String[] columns = {"Name", "Age", "Disease"};
        model = new DefaultTableModel(columns, 0);
        loadPatients();
        table = new JTable(model);
        UIUtils.styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(UIUtils.cardBorder());
        scroll.getViewport().setBackground(UIUtils.CARD_BG);
        JPanel tableCard = new RoundedCardPanel();
        JPanel tableInner = new JPanel(new BorderLayout(0, 10));
        tableInner.setOpaque(false);
        JPanel tableHead = new JPanel(new BorderLayout());
        tableHead.setOpaque(false);
        tableHead.add(UIUtils.sectionTitle("Patient Queue"), BorderLayout.WEST);
        queueLabel = UIUtils.mutedLabel("Rows: " + model.getRowCount());
        tableHead.add(queueLabel, BorderLayout.EAST);
        tableInner.add(tableHead, BorderLayout.NORTH);
        tableInner.add(scroll, BorderLayout.CENTER);
        tableCard.add(tableInner, BorderLayout.CENTER);

        JPanel formCard = UIUtils.sectionPanel(new GridBagLayout());
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtils.BORDER),
            new EmptyBorder(16, 16, 16, 16)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 0, 7, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 0;

        int row = 0;
        gbc.gridy = row++;
        formCard.add(UIUtils.sectionTitle("Quick Intake"), gbc);

        gbc.gridy = row++;
        formCard.add(UIUtils.mutedLabel("Name"), gbc);
        nameField = new JTextField();
        UIUtils.styleTextField(nameField);
        gbc.gridy = row++;
        formCard.add(nameField, gbc);

        gbc.gridy = row++;
        formCard.add(UIUtils.mutedLabel("Age"), gbc);
        ageField = new JTextField();
        UIUtils.styleTextField(ageField);
        gbc.gridy = row++;
        formCard.add(ageField, gbc);

        gbc.gridy = row++;
        formCard.add(UIUtils.mutedLabel("Disease"), gbc);
        diseaseField = new JTextField();
        UIUtils.styleTextField(diseaseField);
        gbc.gridy = row++;
        formCard.add(diseaseField, gbc);

        JPanel formActions = new JPanel(new GridLayout(1, 3, 8, 0));
        formActions.setOpaque(false);
        addBtn = new JButton("Add Patient");
        deleteBtn = new JButton("Delete");
        logoutBtn = new JButton("Logout");
        UIUtils.stylePrimaryButton(addBtn);
        UIUtils.styleSecondaryButton(deleteBtn);
        UIUtils.styleSecondaryButton(logoutBtn);
        formActions.add(addBtn);
        formActions.add(deleteBtn);
        formActions.add(logoutBtn);
        gbc.gridy = row++;
        gbc.insets = new Insets(12, 0, 0, 0);
        formCard.add(formActions, gbc);

        JPanel centerBody = new JPanel(new BorderLayout(12, 0));
        centerBody.setOpaque(false);
        centerBody.add(tableCard, BorderLayout.CENTER);
        centerBody.add(UIUtils.maxWidth(formCard, 420), BorderLayout.EAST);
        content.add(centerBody, BorderLayout.CENTER);

        root.add(content, BorderLayout.CENTER);

        addBtn.addActionListener(e -> addPatient());
        deleteBtn.addActionListener(e -> deleteSelectedPatient());
        logoutBtn.addActionListener(e -> {
            MainHome.open();
            dispose();
        });

        setContentPane(root);
        setMinimumSize(new Dimension(1024, 680));
        pack();
    }

    public static void open() {
        SwingUtilities.invokeLater(() -> {
            ReceptionPanel page = new ReceptionPanel();
            UIUtils.showFullScreen(page);
        });
    }

    private void addPatient() {
        String name = nameField.getText().trim();
        String age = ageField.getText().trim();
        String disease = diseaseField.getText().trim();

        if (name.isEmpty() || age.isEmpty() || disease.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill name, age, and disease");
            return;
        }
        if (!age.matches("^[0-9]{1,3}$")) {
            JOptionPane.showMessageDialog(this, "Age must be numeric");
            return;
        }

        int parsedAge = Integer.parseInt(age);
        boolean saved = DatabaseService.insertReceptionPatient(name, parsedAge, disease);
        if (saved) {
            model.addRow(new Object[]{name, age, disease});
            queueLabel.setText("Rows: " + model.getRowCount());
        } else {
            JOptionPane.showMessageDialog(this, "Could not save patient to database.");
            return;
        }
        nameField.setText("");
        ageField.setText("");
        diseaseField.setText("");
        nameField.requestFocusInWindow();
    }

    private void deleteSelectedPatient() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int modelRow = table.convertRowIndexToModel(row);
            String name = model.getValueAt(modelRow, 0).toString();
            int age = Integer.parseInt(model.getValueAt(modelRow, 1).toString());
            String disease = model.getValueAt(modelRow, 2).toString();

            boolean deleted = DatabaseService.deleteReceptionPatient(name, age, disease);
            if (deleted) {
                model.removeRow(modelRow);
                queueLabel.setText("Rows: " + model.getRowCount());
            } else {
                JOptionPane.showMessageDialog(this, "Could not delete patient from database.");
            }
        }
    }

    private void loadPatients() {
        model.setRowCount(0);
        for (String[] row : DatabaseService.fetchReceptionPatients()) {
            model.addRow(row);
        }
    }
}