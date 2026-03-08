import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class AboutPage extends JFrame {

    public AboutPage() {
        UIUtils.initLookAndFeel();

        setTitle("About Hospital");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = UIUtils.pageRoot();

        JPanel barActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton homeBtn = new JButton("Home");
        JButton contactBtn = new JButton("Contact");
        JButton closeBtn = new JButton("Close");
        UIUtils.styleSecondaryButton(homeBtn);
        UIUtils.styleSecondaryButton(contactBtn);
        UIUtils.styleSecondaryButton(closeBtn);
        barActions.add(homeBtn);
        barActions.add(contactBtn);
        barActions.add(closeBtn);
        root.add(UIUtils.appBar("About Hospital", "Compassionate care powered by modern workflows", barActions), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 14));
        content.setOpaque(false);
        content.add(UIUtils.header("Our Story", "Trusted care for families with digital-first hospital operations"), BorderLayout.NORTH);

        JPanel metrics = new JPanel(new GridLayout(1, 4, 10, 0));
        metrics.setOpaque(false);
        metrics.add(UIUtils.kpiCard("Doctors", "48"));
        metrics.add(UIUtils.kpiCard("Beds", "220"));
        metrics.add(UIUtils.kpiCard("Patients/Day", "350+"));
        metrics.add(UIUtils.kpiCard("Emergency", "24/7"));

        JPanel missionCard = UIUtils.sectionPanel(new BorderLayout(0, 10));
        missionCard.add(UIUtils.sectionTitle("Mission"), BorderLayout.NORTH);
        JTextArea missionText = UIUtils.readOnlyText(
            "We deliver safe, timely, and affordable treatment with integrated departments, " +
            "clear communication, and patient-focused care pathways."
        );
        missionCard.add(missionText, BorderLayout.CENTER);

        JPanel valuesCard = UIUtils.sectionPanel(new BorderLayout(0, 10));
        valuesCard.add(UIUtils.sectionTitle("Core Values"), BorderLayout.NORTH);
        JTextArea valuesText = UIUtils.readOnlyText(
            "- Respect every patient and family\n" +
            "- Clinical quality and accountability\n" +
            "- Fast response with transparent updates\n" +
            "- Technology that reduces waiting time"
        );
        valuesCard.add(valuesText, BorderLayout.CENTER);

        JPanel servicesCard = UIUtils.sectionPanel(new BorderLayout(0, 10));
        servicesCard.add(UIUtils.sectionTitle("Key Services"), BorderLayout.NORTH);
        JTextArea servicesText = UIUtils.readOnlyText(
            "General medicine, diagnostics, emergency care, surgery support, outpatient follow-up, " +
            "and continuous reception-to-doctor coordination."
        );
        servicesCard.add(servicesText, BorderLayout.CENTER);

        JPanel cardsGrid = new JPanel(new GridLayout(1, 3, 12, 0));
        cardsGrid.setOpaque(false);
        cardsGrid.add(missionCard);
        cardsGrid.add(valuesCard);
        cardsGrid.add(servicesCard);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        center.add(UIUtils.maxWidth(metrics, 1180), gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(12, 0, 0, 0);
        center.add(UIUtils.maxWidth(cardsGrid, 1180), gbc);

        JPanel centerWrap = new JPanel(new BorderLayout());
        centerWrap.setOpaque(false);
        centerWrap.add(center, BorderLayout.NORTH);
        content.add(centerWrap, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(8, 0, 0, 0));
        JButton backBtn = new JButton("Back");
        UIUtils.styleSecondaryButton(backBtn);
        footer.add(backBtn);
        content.add(footer, BorderLayout.SOUTH);

        root.add(content, BorderLayout.CENTER);

        homeBtn.addActionListener(e -> {
            MainHome.open();
            dispose();
        });
        contactBtn.addActionListener(e -> ContactPage.open());
        closeBtn.addActionListener(e -> dispose());
        backBtn.addActionListener(e -> dispose());

        setContentPane(root);
        setMinimumSize(new Dimension(1000, 660));
        pack();
    }

    public static void open() {
        SwingUtilities.invokeLater(() -> {
            AboutPage page = new AboutPage();
            UIUtils.showFullScreen(page);
        });
    }
}
