import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class ContactPage extends JFrame {

    private final JTextField nameField;
    private final JTextField emailField;
    private final JTextField phoneField;
    private final JTextArea messageArea;

    public ContactPage() {
        UIUtils.initLookAndFeel();

        setTitle("Contact Hospital");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = UIUtils.pageRoot();

        JPanel barActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton homeBtn = new JButton("Home");
        JButton closeBtn = new JButton("Close");
        UIUtils.styleSecondaryButton(homeBtn);
        UIUtils.styleSecondaryButton(closeBtn);
        barActions.add(homeBtn);
        barActions.add(closeBtn);
        root.add(UIUtils.appBar("Contact Us", "Reach the hospital support desk", barActions), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(0, 14));
        body.setOpaque(false);
        body.add(UIUtils.header("Get In Touch", "We're here to answer any questions you may have."), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 0, 7, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 0;

        int row = 0;
        gbc.gridy = row++;
        form.add(UIUtils.mutedLabel("Name:"), gbc);

        nameField = new JTextField();
        UIUtils.styleTextField(nameField);
        gbc.gridy = row++;
        form.add(nameField, gbc);

        gbc.gridy = row++;
        form.add(UIUtils.mutedLabel("Email:"), gbc);

        emailField = new JTextField();
        UIUtils.styleTextField(emailField);
        gbc.gridy = row++;
        form.add(emailField, gbc);

        gbc.gridy = row++;
        form.add(UIUtils.mutedLabel("Phone:"), gbc);

        phoneField = new JTextField();
        UIUtils.styleTextField(phoneField);
        gbc.gridy = row++;
        form.add(phoneField, gbc);

        gbc.gridy = row++;
        form.add(UIUtils.mutedLabel("Message:"), gbc);

        messageArea = new JTextArea(5, 20);
        UIUtils.styleTextArea(messageArea);
        JScrollPane messageScroll = new JScrollPane(messageArea);
        messageScroll.setBorder(new EmptyBorder(0, 0, 0, 0));
        messageScroll.getViewport().setBackground(UIUtils.FIELD_BG);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0.2;
        gbc.gridy = row++;
        form.add(messageScroll, gbc);

        JPanel submitRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        submitRow.setOpaque(false);
        JButton sendBtn = new JButton("Send Message");
        UIUtils.stylePrimaryButton(sendBtn);
        sendBtn.addActionListener(e -> onSend());
        submitRow.add(sendBtn);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        gbc.gridy = row++;
        form.add(submitRow, gbc);

        JPanel card = new RoundedCardPanel();
        JPanel cardInner = new JPanel(new BorderLayout(0, 10));
        cardInner.setOpaque(false);
        JLabel formTitle = UIUtils.sectionTitle("Send Enquiry");
        formTitle.setBorder(new EmptyBorder(0, 0, 2, 0));
        cardInner.add(formTitle, BorderLayout.NORTH);
        cardInner.add(form, BorderLayout.CENTER);
        card.add(cardInner, BorderLayout.CENTER);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        GridBagConstraints centerGbc = new GridBagConstraints();
        centerGbc.gridx = 0;
        centerGbc.gridy = 0;
        centerGbc.fill = GridBagConstraints.HORIZONTAL;
        centerGbc.weightx = 1;
        centerGbc.weighty = 1;
        centerGbc.anchor = GridBagConstraints.NORTH;
        center.add(UIUtils.maxWidth(card, 620), centerGbc);
        body.add(center, BorderLayout.CENTER);

        // Keep form block compact and visually aligned with login card behavior.
        JPanel footerSpacer = new JPanel();
        footerSpacer.setOpaque(false);
        footerSpacer.setLayout(new BoxLayout(footerSpacer, BoxLayout.Y_AXIS));
        footerSpacer.add(Box.createVerticalStrut(4));
        body.add(footerSpacer, BorderLayout.SOUTH);

        root.add(body, BorderLayout.CENTER);

        homeBtn.addActionListener(e -> {
            MainHome.open();
            dispose();
        });
        closeBtn.addActionListener(e -> dispose());

        setContentPane(root);
        setMinimumSize(new Dimension(960, 640));
        pack();
    }

    public static void open() {
        SwingUtilities.invokeLater(() -> {
            ContactPage page = new ContactPage();
            UIUtils.showFullScreen(page);
        });
    }

    private void onSend() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String message = messageArea.getText().trim();
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || message.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address");
            return;
        }

        if (!phone.matches("^[0-9]{10}$")) {
            JOptionPane.showMessageDialog(this, "Phone number must be 10 digits");
            return;
        }

        boolean saved = DatabaseService.insertContact(name, email, phone, message);
        if (saved) {
            JOptionPane.showMessageDialog(this, "Message sent successfully");
        } else {
            JOptionPane.showMessageDialog(this, "Saved locally failed. Please check DB connection.");
        }
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        messageArea.setText("");
    }
}