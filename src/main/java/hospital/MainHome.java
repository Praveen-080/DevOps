import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MainHome extends JFrame {

    JButton aboutBtn;
    JButton contactBtn;
    JButton exitBtn;

    public MainHome() {
        UIUtils.initLookAndFeel();
        setTitle("Hospital Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel shell = new JPanel(new BorderLayout());
        shell.setBackground(UIUtils.SURFACE);

        shell.add(UIUtils.appBar("Hospital Management System", "Modern hospital operations dashboard", null), BorderLayout.NORTH);

        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(UIUtils.SURFACE);
        right.setBorder(new EmptyBorder(22, 22, 22, 22));

        JPanel header = UIUtils.header("Secure Login", "Role-based access for patient, doctor, and reception");
        right.add(header, BorderLayout.NORTH);

        RoundedCardPanel loginCard = new RoundedCardPanel();
        loginCard.add(UIUtils.maxWidth(new LoginPanel(), 520), BorderLayout.CENTER);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        center.add(loginCard, gbc);

        right.add(center, BorderLayout.CENTER);

        JPanel nav = new JPanel(new GridLayout(1, 3, 10, 0));
        nav.setOpaque(false);
        nav.setBorder(new EmptyBorder(8, 0, 0, 0));
        aboutBtn = new JButton("About Us");
        contactBtn = new JButton("Contact");
        exitBtn = new JButton("Exit");

        UIUtils.styleSecondaryButton(aboutBtn);
        UIUtils.styleSecondaryButton(contactBtn);
        UIUtils.styleSecondaryButton(exitBtn);

        nav.add(aboutBtn);
        nav.add(contactBtn);
        nav.add(exitBtn);

        JPanel navWrap = UIUtils.sectionPanel(new BorderLayout());
        navWrap.add(nav, BorderLayout.CENTER);
        right.add(navWrap, BorderLayout.SOUTH);

        aboutBtn.addActionListener(e -> AboutPage.open());
        contactBtn.addActionListener(e -> ContactPage.open());
        exitBtn.addActionListener(e -> handleExit());

        JPanel left = new HospitalHeroPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        splitPane.setResizeWeight(0.62);
        splitPane.setBorder(null);
        splitPane.setDividerSize(6);

        shell.add(splitPane, BorderLayout.CENTER);
        setContentPane(shell);
        setMinimumSize(new Dimension(960, 600));
        pack();
    }

    public static void open() {
        SwingUtilities.invokeLater(() -> {
            MainHome home = new MainHome();
            UIUtils.showFullScreen(home);
        });
    }

    private void handleExit() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Exit",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        UIUtils.initLookAndFeel();
        boolean dbReady = DatabaseService.initializeDatabase();
        if (!dbReady) {
            JOptionPane.showMessageDialog(
                null,
                "Database not connected.\nReason: " + DatabaseService.getLastError()
                    + "\n\nUse: mvn package and run java -jar target/hospital.jar",
                "Database Connection",
                JOptionPane.WARNING_MESSAGE
            );
        }
        open();
    }

}