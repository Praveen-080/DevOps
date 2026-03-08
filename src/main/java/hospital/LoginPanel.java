import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoginPanel extends JPanel {

    private final JTextField user;
    private final JPasswordField pass;
    private final JToggleButton patientRole;
    private final JToggleButton doctorRole;
    private final JToggleButton receptionRole;

    public LoginPanel() {
        setLayout(new GridBagLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(6, 6, 6, 6));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 0;

        int row = 0;

        JLabel panelTitle = UIUtils.sectionTitle("Sign In");
        panelTitle.setFont(panelTitle.getFont().deriveFont(Font.BOLD, panelTitle.getFont().getSize2D() + 4f));
        gbc.gridy = row++;
        add(panelTitle, gbc);

        JLabel panelSub = UIUtils.mutedLabel("Choose your role and continue securely");
        panelSub.setBorder(new EmptyBorder(0, 0, 4, 0));
        gbc.gridy = row++;
        add(panelSub, gbc);

        // Role segmented buttons (top)
        JPanel roleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        roleRow.setOpaque(false);
        JLabel roleLabel = new JLabel("Role");
        roleLabel.setForeground(UIUtils.MUTED_TEXT);
        roleRow.add(roleLabel);

        patientRole = new JToggleButton("Patient");
        doctorRole = new JToggleButton("Doctor");
        receptionRole = new JToggleButton("Reception");

        ButtonGroup group = new ButtonGroup();
        group.add(patientRole);
        group.add(doctorRole);
        group.add(receptionRole);

        patientRole.setSelected(true);

        UIUtils.stylePillToggle(patientRole);
        UIUtils.stylePillToggle(doctorRole);
        UIUtils.stylePillToggle(receptionRole);

        roleRow.add(patientRole);
        roleRow.add(doctorRole);
        roleRow.add(receptionRole);

        gbc.gridy = row++;
        add(roleRow, gbc);

        gbc.gridy = row++;
        JLabel uLabel = new JLabel("Username");
        uLabel.setForeground(UIUtils.MUTED_TEXT);
        add(uLabel, gbc);

        user = new JTextField();
        user.setColumns(22);
        UIUtils.styleTextField(user);
        gbc.gridy = row++;
        add(user, gbc);

        gbc.gridy = row++;
        JLabel pLabel = new JLabel("Password");
        pLabel.setForeground(UIUtils.MUTED_TEXT);
        add(pLabel, gbc);

        pass = new JPasswordField();
        pass.setColumns(22);
        UIUtils.styleTextField(pass);
        gbc.gridy = row++;
        add(pass, gbc);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        actions.setOpaque(false);
        JButton login = new JButton("Login");
        UIUtils.stylePrimaryButton(login);
        actions.add(login);
        gbc.gridy = row++;
        gbc.insets = new Insets(14, 0, 0, 0);
        add(actions, gbc);

        // filler to keep content compact and top-aligned
        gbc.gridy = row;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(Box.createVerticalGlue(), gbc);

        ActionListener doLogin = e -> {
            String username = user.getText().trim();
            String password = new String(pass.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter username and password");
                return;
            }

            String selectedRole;
            if (doctorRole.isSelected()) {
                selectedRole = "Doctor";
            } else if (receptionRole.isSelected()) {
                selectedRole = "Reception";
            } else {
                selectedRole = "Patient";
            }

            boolean authenticated;
            if (selectedRole.equals("Doctor")) {
                authenticated = DatabaseService.validateDoctorLogin(username, password);
            } else if (selectedRole.equals("Reception")) {
                authenticated = DatabaseService.validateReceptionistLogin(username, password);
            } else {
                authenticated = DatabaseService.validatePatientLogin(username, password);
            }

            if (!authenticated) {
                JOptionPane.showMessageDialog(this, "Invalid credentials for selected role");
                return;
            }

            if (selectedRole.equals("Doctor")) {
                DoctorDashboard.open();
                closeAncestorWindow();
                return;
            }

            if (selectedRole.equals("Patient")) {
                PatientBooking.open();
                closeAncestorWindow();
                return;
            }

            if (selectedRole.equals("Reception")) {
                ReceptionPanel.open();
                closeAncestorWindow();
            }
        };

        login.addActionListener(doLogin);
        user.addActionListener(doLogin);
        pass.addActionListener(doLogin);
    }

    private void closeAncestorWindow() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
    }
}
