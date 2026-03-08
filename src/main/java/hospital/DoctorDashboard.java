import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class DoctorDashboard extends JFrame {

    private JTable appointmentsTable;
    private JTable receptionTable;
    private DefaultTableModel appointmentsModel;
    private DefaultTableModel receptionModel;

    private JButton logoutBtn;
    private JButton refreshBtn;
    private JButton addBtn;
    private JButton deleteBtn;

    private JTextField searchField;
    private JButton searchBtn;
    private JTextField nameField;
    private JTextField ageField;
    private JTextField diseaseField;
    private JTextField dateField;
    private JLabel countLabel;
    private JTabbedPane tabs;
    private TableRowSorter<DefaultTableModel> appointmentsSorter;
    private TableRowSorter<DefaultTableModel> receptionSorter;

    public DoctorDashboard() {
        UIUtils.initLookAndFeel();

        setTitle("Doctor Dashboard");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = UIUtils.pageRoot();

        JPanel barActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        refreshBtn = new JButton("Refresh");
        logoutBtn = new JButton("Logout");
        JButton homeBtn = new JButton("Home");
        UIUtils.styleSecondaryButton(refreshBtn);
        UIUtils.styleSecondaryButton(logoutBtn);
        UIUtils.styleSecondaryButton(homeBtn);
        barActions.add(refreshBtn);
        barActions.add(logoutBtn);
        homeBtn.addActionListener(e -> {
            MainHome.open();
            dispose();
        });
        barActions.add(homeBtn);
        root.add(UIUtils.appBar("Doctor Dashboard", "Clinical record review and follow-up", barActions), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 14));
        content.setOpaque(false);

        JPanel metrics = new JPanel(new GridLayout(1, 4, 10, 0));
        metrics.setOpaque(false);
        metrics.add(UIUtils.kpiCard("Patients", "128"));
        metrics.add(UIUtils.kpiCard("Appointments", "42"));
        metrics.add(UIUtils.kpiCard("Critical", "5"));
        metrics.add(UIUtils.kpiCard("Follow-ups", "19"));
        content.add(metrics, BorderLayout.NORTH);

        appointmentsModel = new DefaultTableModel(new String[]{"Patient ID", "Name", "Age", "Disease", "Date"}, 0);
        receptionModel = new DefaultTableModel(new String[]{"Name", "Age", "Disease"}, 0);

        appointmentsTable = new JTable(appointmentsModel);
        receptionTable = new JTable(receptionModel);
        UIUtils.styleTable(appointmentsTable);
        UIUtils.styleTable(receptionTable);

        appointmentsSorter = new TableRowSorter<>(appointmentsModel);
        receptionSorter = new TableRowSorter<>(receptionModel);
        appointmentsTable.setRowSorter(appointmentsSorter);
        receptionTable.setRowSorter(receptionSorter);

        JScrollPane appointmentsScroll = new JScrollPane(appointmentsTable);
        appointmentsScroll.setBorder(UIUtils.cardBorder());
        appointmentsScroll.getViewport().setBackground(UIUtils.CARD_BG);

        JScrollPane receptionScroll = new JScrollPane(receptionTable);
        receptionScroll.setBorder(UIUtils.cardBorder());
        receptionScroll.getViewport().setBackground(UIUtils.CARD_BG);

        tabs = new JTabbedPane();
        tabs.addTab("Appointments", appointmentsScroll);
        tabs.addTab("Reception Patients", receptionScroll);

        JPanel recordsPanel = new RoundedCardPanel();
        JPanel recordsInner = new JPanel(new BorderLayout(0, 10));
        recordsInner.setOpaque(false);
        JPanel recordsHeader = new JPanel(new BorderLayout());
        recordsHeader.setOpaque(false);
        recordsHeader.add(UIUtils.sectionTitle("Clinical Data"), BorderLayout.WEST);
        countLabel = UIUtils.mutedLabel("Rows: 0");
        recordsHeader.add(countLabel, BorderLayout.EAST);
        recordsInner.add(recordsHeader, BorderLayout.NORTH);
        recordsInner.add(tabs, BorderLayout.CENTER);
        recordsPanel.add(recordsInner, BorderLayout.CENTER);

        JPanel rightRail = new JPanel();
        rightRail.setOpaque(false);
        rightRail.setLayout(new BoxLayout(rightRail, BoxLayout.Y_AXIS));

        JPanel searchCard = UIUtils.sectionPanel(new BorderLayout(0, 8));
        searchCard.add(UIUtils.sectionTitle("Quick Search"), BorderLayout.NORTH);
        JPanel searchArea = new JPanel(new BorderLayout(8, 0));
        searchArea.setOpaque(false);
        searchField = new JTextField(18);
        UIUtils.styleTextField(searchField);
        searchArea.add(searchField, BorderLayout.CENTER);
        searchBtn = new JButton("Search");
        UIUtils.styleSecondaryButton(searchBtn);
        searchArea.add(searchBtn, BorderLayout.EAST);
        searchCard.add(searchArea, BorderLayout.CENTER);

        JPanel formCard = UIUtils.sectionPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 0;

        int row = 0;
        gbc.gridy = row++;
        formCard.add(UIUtils.sectionTitle("Data Controls"), gbc);

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

        gbc.gridy = row++;
        formCard.add(UIUtils.mutedLabel("Date (DD/MM, for Appointments)"), gbc);
        dateField = new JTextField();
        UIUtils.styleTextField(dateField);
        gbc.gridy = row++;
        formCard.add(dateField, gbc);

        JPanel formActions = new JPanel(new GridLayout(1, 2, 8, 0));
        formActions.setOpaque(false);
        addBtn = new JButton("Add Data");
        deleteBtn = new JButton("Delete Selected");
        UIUtils.stylePrimaryButton(addBtn);
        UIUtils.styleSecondaryButton(deleteBtn);
        formActions.add(addBtn);
        formActions.add(deleteBtn);
        gbc.gridy = row++;
        gbc.insets = new Insets(10, 0, 0, 0);
        formCard.add(formActions, gbc);

        JPanel notesCard = UIUtils.sectionPanel(new BorderLayout());
        notesCard.add(UIUtils.sectionTitle("Clinical Notes"), BorderLayout.NORTH);
        JTextArea noteText = UIUtils.readOnlyText(
            "• Tab 1 shows doctor appointments\n" +
            "• Tab 2 shows reception queue\n" +
            "• Add/Delete works on the active tab"
        );
        noteText.setBorder(new EmptyBorder(8, 0, 0, 0));
        notesCard.add(noteText, BorderLayout.CENTER);

        rightRail.add(searchCard);
        rightRail.add(Box.createVerticalStrut(12));
        rightRail.add(formCard);
        rightRail.add(Box.createVerticalStrut(12));
        rightRail.add(notesCard);

        JPanel centerBody = new JPanel(new BorderLayout(12, 0));
        centerBody.setOpaque(false);
        centerBody.add(recordsPanel, BorderLayout.CENTER);
        centerBody.add(UIUtils.maxWidth(rightRail, 360), BorderLayout.EAST);
        content.add(centerBody, BorderLayout.CENTER);

        root.add(content, BorderLayout.CENTER);

        loadTables();
        updateCountLabel();

        searchBtn.addActionListener(e -> handleSearch());
        refreshBtn.addActionListener(e -> {
            loadTables();
            clearSearch();
            updateCountLabel();
            JOptionPane.showMessageDialog(this, "Records refreshed");
        });
        addBtn.addActionListener(e -> addDataForActiveTab());
        deleteBtn.addActionListener(e -> deleteSelectedForActiveTab());
        tabs.addChangeListener(e -> updateCountLabel());
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
            DoctorDashboard page = new DoctorDashboard();
            UIUtils.showFullScreen(page);
        });
    }

    private void handleSearch() {
        String text = searchField.getText().trim();
        if (text.isEmpty()) {
            clearSearch();
            JOptionPane.showMessageDialog(this, "Showing all patients");
            return;
        }

        int tab = tabs.getSelectedIndex();
        if (tab == 0) {
            appointmentsSorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(text), 1));
        } else {
            receptionSorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(text), 0));
        }
    }

    private void clearSearch() {
        appointmentsSorter.setRowFilter(null);
        receptionSorter.setRowFilter(null);
    }

    private void addDataForActiveTab() {
        String name = nameField.getText().trim();
        String ageText = ageField.getText().trim();
        String disease = diseaseField.getText().trim();
        String date = dateField.getText().trim();

        if (name.isEmpty() || ageText.isEmpty() || disease.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill Name, Age, and Disease");
            return;
        }
        if (!ageText.matches("^[0-9]{1,3}$")) {
            JOptionPane.showMessageDialog(this, "Age must be numeric");
            return;
        }

        int age = Integer.parseInt(ageText);
        boolean saved;

        if (tabs.getSelectedIndex() == 0) {
            if (!date.matches("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])$")) {
                JOptionPane.showMessageDialog(this, "Date format must be DD/MM for appointments");
                return;
            }
            saved = DatabaseService.insertAppointment(name, age, disease, date);
            if (saved) {
                loadAppointments();
            }
        } else {
            saved = DatabaseService.insertReceptionPatient(name, age, disease);
            if (saved) {
                loadReceptionPatients();
            }
        }

        if (!saved) {
            JOptionPane.showMessageDialog(this, "Could not save data. Check database connection.");
            return;
        }

        updateCountLabel();
        clearForm();
    }

    private void deleteSelectedForActiveTab() {
        if (tabs.getSelectedIndex() == 0) {
            int viewRow = appointmentsTable.getSelectedRow();
            if (viewRow == -1) {
                JOptionPane.showMessageDialog(this, "Select an appointment row to delete");
                return;
            }

            int modelRow = appointmentsTable.convertRowIndexToModel(viewRow);
            int appointmentId = Integer.parseInt(appointmentsModel.getValueAt(modelRow, 0).toString());
            boolean deleted = DatabaseService.deleteAppointmentById(appointmentId);
            if (!deleted) {
                JOptionPane.showMessageDialog(this, "Could not delete appointment from database.");
                return;
            }
            loadAppointments();
            updateCountLabel();
            return;
        }

        int viewRow = receptionTable.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a reception row to delete");
            return;
        }

        int modelRow = receptionTable.convertRowIndexToModel(viewRow);
        String name = receptionModel.getValueAt(modelRow, 0).toString();
        int age = Integer.parseInt(receptionModel.getValueAt(modelRow, 1).toString());
        String disease = receptionModel.getValueAt(modelRow, 2).toString();

        boolean deleted = DatabaseService.deleteReceptionPatient(name, age, disease);
        if (!deleted) {
            JOptionPane.showMessageDialog(this, "Could not delete reception patient from database.");
            return;
        }

        loadReceptionPatients();
        updateCountLabel();
    }

    private void clearForm() {
        nameField.setText("");
        ageField.setText("");
        diseaseField.setText("");
        dateField.setText("");
        nameField.requestFocusInWindow();
    }

    private void loadTables() {
        loadAppointments();
        loadReceptionPatients();
    }

    private void loadAppointments() {
        appointmentsModel.setRowCount(0);
        for (String[] row : DatabaseService.fetchAppointments()) {
            appointmentsModel.addRow(row);
        }
    }

    private void loadReceptionPatients() {
        receptionModel.setRowCount(0);
        for (String[] row : DatabaseService.fetchReceptionPatients()) {
            receptionModel.addRow(row);
        }
    }

    private void updateCountLabel() {
        if (tabs.getSelectedIndex() == 0) {
            countLabel.setText("Rows: " + appointmentsModel.getRowCount());
        } else {
            countLabel.setText("Rows: " + receptionModel.getRowCount());
        }
    }
}