import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class ArabaGUI2 extends JFrame {
    private Araba araba;
    private JLabel durumLabel;
    private JLabel hizLabel;
    private JLabel yakitLabel;
    private JProgressBar hizBar;
    private JProgressBar yakitBar;
    private ArabaPanel arabaPanel;
    private Timer animasyonTimer;
    private int arabaX = 50;

    public ArabaGUI2() {
        Motor motor = new Motor(3.0, 200, "Turbo");
        araba = new Araba("Ferrari", "F8", 2024, motor);

        setTitle("Araba Simülasyonu - " + araba.getMarka() + " " + araba.getModel());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 240, 240));

        JPanel ustPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        ustPanel.setBackground(new Color(240, 240, 240));

        durumLabel = new JLabel("Durum: Durmuş", SwingConstants.CENTER);
        durumLabel.setFont(new Font("Arial", Font.BOLD, 24));
        durumLabel.setForeground(Color.RED);

        hizLabel = new JLabel("Hız: 0 km/h", SwingConstants.CENTER);
        hizLabel.setFont(new Font("Arial", Font.BOLD, 20));

        yakitLabel = new JLabel("Yakıt: %100", SwingConstants.CENTER);
        yakitLabel.setFont(new Font("Arial", Font.BOLD, 20));

        ustPanel.add(durumLabel);
        ustPanel.add(hizLabel);
        ustPanel.add(yakitLabel);

        arabaPanel = new ArabaPanel();
        arabaPanel.setPreferredSize(new Dimension(800, 300));

        JPanel gostergePanel = new JPanel(new GridLayout(2, 2, 10, 10));
        gostergePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        hizBar = new JProgressBar(0, araba.getMaxHiz());
        hizBar.setStringPainted(true);
        hizBar.setForeground(new Color(0, 150, 255));
        hizBar.setFont(new Font("Arial", Font.BOLD, 14));

        yakitBar = new JProgressBar(0, 100);
        yakitBar.setValue(100);
        yakitBar.setStringPainted(true);
        yakitBar.setForeground(new Color(50, 200, 50));
        yakitBar.setFont(new Font("Arial", Font.BOLD, 14));

        gostergePanel.add(new JLabel("Hız Göstergesi:", SwingConstants.RIGHT));
        gostergePanel.add(hizBar);
        gostergePanel.add(new JLabel("Yakıt Göstergesi:", SwingConstants.RIGHT));
        gostergePanel.add(yakitBar);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));

        JButton calistirBtn = createButton("🔑 Çalıştır", new Color(50, 200, 50));
        JButton durdurBtn = createButton("⏹ Durdur", new Color(255, 100, 100));
        JButton hizlanBtn = createButton("⬆ Hızlan", new Color(0, 150, 255));
        JButton yavaslaBtn = createButton("⬇ Yavaşla", new Color(255, 200, 0));
        JButton yakitBtn = createButton("⛽ Yakıt Doldur", new Color(150, 150, 255));

        calistirBtn.addActionListener(e -> {
            araba.calistir();
            baslatAnimasyon();
            guncelle();
        });

        durdurBtn.addActionListener(e -> {
            araba.durdur();
            durdurAnimasyon();
            guncelle();
        });

        hizlanBtn.addActionListener(e -> {
            araba.hizlan();
            guncelle();
        });

        yavaslaBtn.addActionListener(e -> {
            araba.yavasla();
            guncelle();
        });

        yakitBtn.addActionListener(e -> {
            araba.yakitDoldur();
            guncelle();
        });

        buttonPanel.add(calistirBtn);
        buttonPanel.add(hizlanBtn);
        buttonPanel.add(yavaslaBtn);
        buttonPanel.add(durdurBtn);
        buttonPanel.add(yakitBtn);

        JPanel anaPanel = new JPanel(new BorderLayout());
        anaPanel.add(ustPanel, BorderLayout.NORTH);
        anaPanel.add(arabaPanel, BorderLayout.CENTER);
        anaPanel.add(gostergePanel, BorderLayout.SOUTH);

        add(anaPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return btn;
    }

    private void baslatAnimasyon() {
        if (animasyonTimer == null) {
            animasyonTimer = new Timer(50, e -> {
                if (araba.getCalisiyorMu() && araba.getHiz() > 0) {
                    arabaX += araba.getHiz() / 20;
                    if (arabaX > 800) arabaX = -150;
                    arabaPanel.repaint();
                }
            });
            animasyonTimer.start();
        }
    }

    private void durdurAnimasyon() {
        if (animasyonTimer != null) {
            animasyonTimer.stop();
        }
    }

    private void guncelle() {
        durumLabel.setText("Durum: " + (araba.getCalisiyorMu() ? "Çalışıyor ✓" : "Durmuş ✗"));
        durumLabel.setForeground(araba.getCalisiyorMu() ? new Color(0, 150, 0) : Color.RED);
        
        hizLabel.setText("Hız: " + araba.getHiz() + " km/h");
        yakitLabel.setText("Yakıt: %" + araba.getYakit());
        
        hizBar.setValue(araba.getHiz());
        yakitBar.setValue(araba.getYakit());
        
        if (araba.getYakit() < 20) {
            yakitBar.setForeground(Color.RED);
        } else if (araba.getYakit() < 50) {
            yakitBar.setForeground(Color.ORANGE);
        } else {
            yakitBar.setForeground(new Color(50, 200, 50));
        }
        
        arabaPanel.repaint();
    }

    class ArabaPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(new Color(100, 200, 255));
            g2d.fillRect(0, 0, getWidth(), getHeight() / 2);
            
            g2d.setColor(new Color(100, 100, 100));
            g2d.fillRect(0, getHeight() / 2, getWidth(), getHeight() / 2);
            
            g2d.setColor(Color.YELLOW);
            for (int i = 0; i < getWidth(); i += 60) {
                g2d.fillRect(i, getHeight() / 2 - 5, 40, 10);
            }

            int carY = getHeight() / 2 - 60;
            
            Color carColor = araba.getCalisiyorMu() ? new Color(220, 20, 20) : new Color(150, 150, 150);
            g2d.setColor(carColor);
            g2d.fillRoundRect(arabaX, carY, 120, 50, 15, 15);
            
            g2d.setColor(carColor.darker());
            g2d.fillRoundRect(arabaX + 20, carY - 20, 80, 25, 10, 10);
            
            g2d.setColor(new Color(100, 200, 255, 150));
            g2d.fillRect(arabaX + 25, carY - 18, 30, 20);
            g2d.fillRect(arabaX + 65, carY - 18, 30, 20);
            
            g2d.setColor(Color.BLACK);
            g2d.fillOval(arabaX + 15, carY + 40, 30, 30);
            g2d.fillOval(arabaX + 75, carY + 40, 30, 30);
            
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillOval(arabaX + 20, carY + 45, 20, 20);
            g2d.fillOval(arabaX + 80, carY + 45, 20, 20);
            
            if (araba.getCalisiyorMu()) {
                g2d.setColor(Color.YELLOW);
                g2d.fillOval(arabaX + 5, carY + 20, 8, 8);
                g2d.fillOval(arabaX + 107, carY + 20, 8, 8);
            }
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString(araba.getMarka(), arabaX + 30, carY + 30);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ArabaGUI2());
    }
}
