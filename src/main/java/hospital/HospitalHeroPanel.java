import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class HospitalHeroPanel extends JPanel {

    private final Image image;

    public HospitalHeroPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(24, 24, 24, 24));
        this.image = tryLoadImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // Blue hero gradient background (matches the requested look)
            Color left = new Color(18, 78, 200);
            Color right = new Color(52, 162, 255);
            g2.setPaint(new GradientPaint(0, 0, left, w, 0, right));
            g2.fillRect(0, 0, w, h);

            // Subtle overlay glow
            g2.setColor(new Color(255, 255, 255, 18));
            g2.fillOval((int) (w * -0.25), (int) (h * -0.15), (int) (w * 0.9), (int) (h * 0.7));
            g2.fillOval((int) (w * 0.45), (int) (h * 0.55), (int) (w * 0.85), (int) (h * 0.6));

            if (image != null) {
                drawScaledImage(g2, w, h);
            } else {
                drawGeneratedHero(g2, w, h);
            }

            drawOverlayText(g2, w, h);
        } finally {
            g2.dispose();
        }
    }

    private void drawScaledImage(Graphics2D g2, int panelWidth, int panelHeight) {
        int imgWidth = image.getWidth(this);
        int imgHeight = image.getHeight(this);
        if (imgWidth <= 0 || imgHeight <= 0) {
            return;
        }

        double scale = Math.min((double) panelWidth / imgWidth, (double) panelHeight / imgHeight);
        int drawW = (int) (imgWidth * scale);
        int drawH = (int) (imgHeight * scale);
        int x = (panelWidth - drawW) / 2;
        int y = (panelHeight - drawH) / 2;

        g2.drawImage(image, x, y, drawW, drawH, this);
    }

    // Procedurally generated illustration (no external image required)
    // Styled to look closer to the reference: icon + welcome on blue hero.
    private void drawGeneratedHero(Graphics2D g2, int w, int h) {
        int size = Math.min(w, h);

        int iconCenterX = (int) (w * 0.26);
        int iconCenterY = (int) (h * 0.42);
        int iconSize = (int) (size * 0.18);

        // Soft icon backdrop
        g2.setColor(new Color(255, 255, 255, 22));
        g2.fill(new Ellipse2D.Float(iconCenterX - iconSize, iconCenterY - iconSize, iconSize * 2f, iconSize * 2f));
        g2.setColor(new Color(255, 255, 255, 45));
        g2.setStroke(new BasicStroke(2f));
        g2.draw(new Ellipse2D.Float(iconCenterX - iconSize, iconCenterY - iconSize, iconSize * 2f, iconSize * 2f));

        // Rocket-like icon (simple vector)
        int bodyW = (int) (iconSize * 0.42);
        int bodyH = (int) (iconSize * 0.62);
        int bodyX = iconCenterX - bodyW / 2;
        int bodyY = iconCenterY - bodyH / 2;

        g2.setColor(new Color(255, 255, 255, 235));
        g2.fill(new RoundRectangle2D.Float(bodyX, bodyY, bodyW, bodyH, bodyW, bodyW));
        g2.setColor(new Color(255, 255, 255, 80));
        g2.draw(new RoundRectangle2D.Float(bodyX, bodyY, bodyW, bodyH, bodyW, bodyW));

        // Window
        int win = (int) (bodyW * 0.32);
        int winX = iconCenterX - win / 2;
        int winY = bodyY + (int) (bodyH * 0.22);
        g2.setColor(new Color(18, 78, 200));
        g2.fill(new Ellipse2D.Float(winX, winY, win, win));
        g2.setColor(new Color(255, 255, 255, 220));
        g2.draw(new Ellipse2D.Float(winX, winY, win, win));

        // Fins
        g2.setColor(new Color(255, 255, 255, 220));
        Path2D leftFin = new Path2D.Float();
        leftFin.moveTo(bodyX, bodyY + (bodyH * 0.62));
        leftFin.lineTo(bodyX - (bodyW * 0.26), bodyY + bodyH);
        leftFin.lineTo(bodyX, bodyY + bodyH);
        leftFin.closePath();
        g2.fill(leftFin);

        Path2D rightFin = new Path2D.Float();
        rightFin.moveTo(bodyX + bodyW, bodyY + (bodyH * 0.62));
        rightFin.lineTo(bodyX + bodyW + (bodyW * 0.26), bodyY + bodyH);
        rightFin.lineTo(bodyX + bodyW, bodyY + bodyH);
        rightFin.closePath();
        g2.fill(rightFin);

        // Flame
        Path2D flame = new Path2D.Float();
        int flameTopX = iconCenterX;
        int flameTopY = bodyY + bodyH;
        flame.moveTo(flameTopX, flameTopY);
        flame.curveTo(flameTopX - bodyW * 0.22, flameTopY + bodyH * 0.18,
                flameTopX - bodyW * 0.10, flameTopY + bodyH * 0.46,
                flameTopX, flameTopY + bodyH * 0.55);
        flame.curveTo(flameTopX + bodyW * 0.10, flameTopY + bodyH * 0.46,
                flameTopX + bodyW * 0.22, flameTopY + bodyH * 0.18,
                flameTopX, flameTopY);
        flame.closePath();
        g2.setColor(new Color(255, 210, 90, 230));
        g2.fill(flame);

        // A few decorative dots
        g2.setColor(new Color(255, 255, 255, 55));
        for (int i = 0; i < 12; i++) {
            int rx = (int) (w * 0.08 + (w * 0.42) * Math.random());
            int ry = (int) (h * 0.10 + (h * 0.75) * Math.random());
            int r = 2 + (int) (3 * Math.random());
            g2.fillOval(rx, ry, r, r);
        }
    }

    private void drawOverlayText(Graphics2D g2, int w, int h) {
        Shape oldClip = g2.getClip();
        g2.setClip(0, 0, w, h);
        g2.setColor(new Color(255, 255, 255, 230));

        // Brand (top-left)
        Font brandFont = getFont().deriveFont(Font.BOLD, getFont().getSize2D() + 4f);
        g2.setFont(brandFont);
        g2.drawString("HMS", 26, 40);

        // Welcome text (mid-left)
        Font welcomeFont = getFont().deriveFont(Font.BOLD, getFont().getSize2D() + 16f);
        g2.setFont(welcomeFont);
        g2.drawString("Welcome", 26, (int) (h * 0.58));

        g2.setColor(new Color(255, 255, 255, 190));
        Font sub = getFont().deriveFont(getFont().getSize2D() + 2f);
        g2.setFont(sub);
        g2.drawString("Manage patients, appointments, and records", 26, (int) (h * 0.58) + 28);

        g2.setClip(oldClip);
    }

    private Image tryLoadImage() {
        String[] candidates = {
                "hospital.png",
                "hospital.jpg",
                "hms.png",
                "assets/hospital.png",
                "images/hospital.png"
        };

        for (String path : candidates) {
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                ImageIcon icon = new ImageIcon(path);
                if (icon.getIconWidth() > 0 && icon.getIconHeight() > 0) {
                    return icon.getImage();
                }
            }
        }

        return null;
    }
}
