import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoginPage extends JFrame {

    LoginPage() {
        UIUtils.initLookAndFeel();

        setTitle("Login");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel shell = new JPanel(new BorderLayout());
        shell.setBackground(UIUtils.SURFACE);

        JPanel topActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton aboutTop = new JButton("About");
        JButton contactTop = new JButton("Contact");
        UIUtils.styleSecondaryButton(aboutTop);
        UIUtils.styleSecondaryButton(contactTop);
        aboutTop.addActionListener(e -> AboutPage.open());
        contactTop.addActionListener(e -> ContactPage.open());
        topActions.add(aboutTop);
        topActions.add(contactTop);
        shell.add(UIUtils.appBar("User Login", "Authenticate and access your workspace", topActions), BorderLayout.NORTH);

        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(UIUtils.SURFACE);
        right.setBorder(new EmptyBorder(24, 24, 24, 24));
        right.add(UIUtils.header("Access Portal", "Select your role and continue"), BorderLayout.NORTH);

        RoundedCardPanel card = new RoundedCardPanel();
        card.add(UIUtils.maxWidth(new LoginPanel(), 520), BorderLayout.CENTER);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        center.add(card, gbc);

        right.add(center, BorderLayout.CENTER);

        JButton back = new JButton("Back to Home");
        UIUtils.styleSecondaryButton(back);
        back.addActionListener(e -> {
            MainHome.open();
            dispose();
        });
        JPanel bottomActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bottomActions.setOpaque(false);
        bottomActions.setBorder(new EmptyBorder(14, 0, 0, 0));
        bottomActions.add(back);
        right.add(bottomActions, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new HospitalHeroPanel(), right);
        split.setResizeWeight(0.62);
        split.setBorder(null);
        split.setDividerSize(6);
        shell.add(split, BorderLayout.CENTER);
        setContentPane(shell);

        setMinimumSize(new Dimension(960, 600));
        pack();
    }

    public static void open() {
        SwingUtilities.invokeLater(() -> {
            LoginPage page = new LoginPage();
            UIUtils.showFullScreen(page);
        });
    }
}