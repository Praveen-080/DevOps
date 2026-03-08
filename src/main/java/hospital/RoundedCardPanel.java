import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class RoundedCardPanel extends JPanel {

    private final int arc;

    public RoundedCardPanel() {
        this(18);
    }

    public RoundedCardPanel(int arc) {
        super(new BorderLayout());
        this.arc = arc;
        setOpaque(false);
        setBorder(new EmptyBorder(16, 16, 16, 16));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // Shadow
            g2.setColor(new Color(0, 0, 0, 18));
            g2.fill(new RoundRectangle2D.Float(4, 6, w - 8, h - 8, arc, arc));

            // Card
            g2.setColor(Color.WHITE);
            g2.fill(new RoundRectangle2D.Float(0, 0, w - 8, h - 8, arc, arc));

            // Border
            g2.setColor(UIUtils.BORDER);
            g2.setStroke(new BasicStroke(1.2f));
            g2.draw(new RoundRectangle2D.Float(0, 0, w - 8, h - 8, arc, arc));
        } finally {
            g2.dispose();
        }

        super.paintComponent(g);
    }
}
