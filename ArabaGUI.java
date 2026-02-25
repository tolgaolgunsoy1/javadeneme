import javax.swing.*;
import java.awt.*;

public class ArabaGUI extends JFrame {
    private Araba araba;
    private JLabel durumLabel;
    private JButton calistirBtn;
    private JButton durdurBtn;
    private JTextArea bilgiArea;
    private JPanel arabaPanel;

    public ArabaGUI() {
        Motor motor = new Motor(2.0, 150, "Benzinli");
        araba = new Araba("BMW", "M3", 2024, motor);

        setTitle("Araba Simülasyonu");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        arabaPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(araba.getCalisiyorMu() ? Color.GREEN : Color.RED);
                g.fillRect(100, 50, 200, 80);
                g.setColor(Color.BLACK);
                g.fillOval(120, 110, 40, 40);
                g.fillOval(240, 110, 40, 40);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 16));
                g.drawString(araba.getMarka() + " " + araba.getModel(), 130, 90);
            }
        };
        arabaPanel.setPreferredSize(new Dimension(400, 180));
        arabaPanel.setBackground(Color.LIGHT_GRAY);

        durumLabel = new JLabel("Durum: Durmuş", SwingConstants.CENTER);
        durumLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel buttonPanel = new JPanel();
        calistirBtn = new JButton("Çalıştır");
        durdurBtn = new JButton("Durdur");

        calistirBtn.addActionListener(e -> {
            araba.calistir();
            guncelle();
        });

        durdurBtn.addActionListener(e -> {
            araba.durdur();
            guncelle();
        });

        buttonPanel.add(calistirBtn);
        buttonPanel.add(durdurBtn);

        bilgiArea = new JTextArea(5, 30);
        bilgiArea.setEditable(false);
        bilgiArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        guncelleBilgi();

        add(arabaPanel, BorderLayout.NORTH);
        add(durumLabel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(new JScrollPane(bilgiArea), BorderLayout.EAST);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void guncelle() {
        durumLabel.setText("Durum: " + (araba.getCalisiyorMu() ? "Çalışıyor" : "Durmuş"));
        arabaPanel.repaint();
        guncelleBilgi();
    }

    private void guncelleBilgi() {
        bilgiArea.setText(araba.getBilgi());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ArabaGUI());
    }
}
