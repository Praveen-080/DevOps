import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class PatientBooking extends JFrame {

    private final JTextField nameField;
    private final JTextField ageField;
    private final JTextField diseaseField;
    private final JTextField dateField;

    public PatientBooking() {
        UIUtils.initLookAndFeel();

        setTitle("Book Appointment");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = UIUtils.pageRoot();

        JPanel barActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton backTop = new JButton("Dashboard");
        UIUtils.styleSecondaryButton(backTop);
        backTop.addActionListener(e -> {
            MainHome.open();
            dispose();
        });
        barActions.add(backTop);
        root.add(UIUtils.appBar("Appointment Booking", "Create and confirm patient appointments", barActions), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 14));
        content.setOpaque(false);

        JPanel metrics = new JPanel(new GridLayout(1, 3, 10, 0));
        metrics.setOpaque(false);
        metrics.add(UIUtils.kpiCard("Today Slots", "24"));
        metrics.add(UIUtils.kpiCard("Booked", "16"));
        metrics.add(UIUtils.kpiCard("Available", "8"));
        content.add(metrics, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 0;

        int row = 0;

        gbc.gridy = row++;
        form.add(UIUtils.sectionTitle("Patient Details"), gbc);

        gbc.gridy = row++;
        form.add(UIUtils.mutedLabel("Name"), gbc);
        nameField = new JTextField();
        UIUtils.styleTextField(nameField);
        gbc.gridy = row++;
        form.add(nameField, gbc);

        gbc.gridy = row++;
        form.add(UIUtils.mutedLabel("Age"), gbc);
        ageField = new JTextField();
        UIUtils.styleTextField(ageField);
        gbc.gridy = row++;
        form.add(ageField, gbc);

        gbc.gridy = row++;
        form.add(UIUtils.mutedLabel("Disease"), gbc);
        diseaseField = new JTextField();
        UIUtils.styleTextField(diseaseField);
        gbc.gridy = row++;
        form.add(diseaseField, gbc);

        gbc.gridy = row++;
        form.add(UIUtils.mutedLabel("Date"), gbc);
        dateField = new JTextField();
        dateField.setToolTipText("Example: 12/03");
        UIUtils.styleTextField(dateField);
        gbc.gridy = row++;
        form.add(dateField, gbc);

        JButton backBtn = new JButton("Back");
        JButton bookBtn = new JButton("Book Slot");
        UIUtils.styleSecondaryButton(backBtn);
        UIUtils.stylePrimaryButton(bookBtn);
        backBtn.addActionListener(e -> {
            MainHome.open();
            dispose();
        });
        bookBtn.addActionListener(e -> onBook());

        JPanel formActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        formActions.setOpaque(false);
        formActions.add(backBtn);
        formActions.add(bookBtn);
        gbc.gridy = row++;
        gbc.insets = new Insets(12, 0, 0, 0);
        form.add(formActions, gbc);

        JPanel formCardHost = new JPanel(new GridBagLayout());
        formCardHost.setOpaque(false);
        GridBagConstraints formHostGbc = new GridBagConstraints();
        formHostGbc.gridx = 0;
        formHostGbc.gridy = 0;
        formHostGbc.fill = GridBagConstraints.HORIZONTAL;
        formHostGbc.weightx = 1;
        formHostGbc.anchor = GridBagConstraints.NORTH;
        formCardHost.add(UIUtils.maxWidth(UIUtils.wrapInCard(form), 740), formHostGbc);

        JPanel tips = UIUtils.sectionPanel(new BorderLayout());
        JLabel tipsTitle = UIUtils.sectionTitle("Booking Tips");
        tips.add(tipsTitle, BorderLayout.NORTH);
        JTextArea tipsText = UIUtils.readOnlyText(
                "• Use patient full name\n" +
                        "• Enter valid age and disease details\n" +
                        "• Confirm date format as DD/MM\n" +
                        "• Keep emergency cases prioritized"
        );
        tipsText.setBorder(new EmptyBorder(8, 0, 0, 0));
        tips.add(tipsText, BorderLayout.CENTER);

        JPanel quickCard = UIUtils.sectionPanel(new BorderLayout());
        quickCard.add(UIUtils.sectionTitle("Quick Guidance"), BorderLayout.NORTH);
        JTextArea quickText = UIUtils.readOnlyText(
            "• Default date format: DD/MM\n" +
                "• Save booking to DB instantly\n" +
                "• Recheck age before submit"
        );
        quickText.setBorder(new EmptyBorder(8, 0, 0, 0));
        quickCard.add(quickText, BorderLayout.CENTER);

        JPanel rightRail = new JPanel();
        rightRail.setOpaque(false);
        rightRail.setLayout(new BoxLayout(rightRail, BoxLayout.Y_AXIS));
        rightRail.add(tips);
        rightRail.add(Box.createVerticalStrut(12));
        rightRail.add(quickCard);

        JPanel mid = new JPanel(new BorderLayout(12, 0));
        mid.setOpaque(false);
        mid.add(formCardHost, BorderLayout.CENTER);
        mid.add(UIUtils.maxWidth(rightRail, 420), BorderLayout.EAST);
        content.add(mid, BorderLayout.CENTER);

        root.add(content, BorderLayout.CENTER);

        setContentPane(root);
        setMinimumSize(new Dimension(960, 640));
        pack();
    }

    public static void open() {
        SwingUtilities.invokeLater(() -> {
            PatientBooking page = new PatientBooking();
            UIUtils.showFullScreen(page);
        });
    }

    private void onBook() {
        String name = nameField.getText().trim();
        String ageText = ageField.getText().trim();
        String disease = diseaseField.getText().trim();
        String date = dateField.getText().trim();

        if (name.isEmpty() || ageText.isEmpty() || disease.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please complete all booking details");
            return;
        }

        if (!name.matches("^[A-Za-z ]{2,}$")) {
            JOptionPane.showMessageDialog(this, "Name must contain only letters and spaces");
            return;
        }

        if (!ageText.matches("^[0-9]{1,3}$")) {
            JOptionPane.showMessageDialog(this, "Age must be a number");
            return;
        }

        int age = Integer.parseInt(ageText);
        if (age < 1 || age > 120) {
            JOptionPane.showMessageDialog(this, "Age should be between 1 and 120");
            return;
        }

        if (!date.matches("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])$")) {
            JOptionPane.showMessageDialog(this, "Date format must be DD/MM");
            return;
        }

        boolean saved = DatabaseService.insertAppointment(name, age, disease, date);
        if (saved) {
            JOptionPane.showMessageDialog(this, "Appointment booked successfully");
        } else {
            JOptionPane.showMessageDialog(this, "Could not save booking. Check database connection.");
        }
        nameField.setText("");
        ageField.setText("");
        diseaseField.setText("");
        dateField.setText("");
    }
}