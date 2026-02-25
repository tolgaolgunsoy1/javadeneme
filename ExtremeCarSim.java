import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

public class ExtremeCarSim extends JFrame {
    private Araba araba;
    private GamePanel gamePanel;
    private Timer gameTimer;
    private ArrayList<Particle> particles = new ArrayList<>();
    private ArrayList<Cloud> clouds = new ArrayList<>();
    private ArrayList<Tree> trees = new ArrayList<>();
    private Random random = new Random();
    private int arabaX = 100;
    private int arabaY = 400;
    private double arabaAngle = 0;
    private boolean isNight = false;
    private boolean isRaining = false;
    private int roadOffset = 0;
    private JPanel controlPanel;
    private JLabel speedLabel, rpmLabel, gearLabel, tempLabel;
    private SpeedoMeter speedoMeter;
    private int rpm = 0;
    private int gear = 0;
    private int engineTemp = 20;

    public ExtremeCarSim() {
        Motor motor = new Motor(5.0, 300, "V8 Twin Turbo");
        araba = new Araba("Lamborghini", "Aventador", 2024, motor);

        setTitle("🏎️ EXTREME CAR SIMULATOR - " + araba.getMarka() + " " + araba.getModel());
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initTrees();
        initClouds();

        gamePanel = new GamePanel();
        speedoMeter = new SpeedoMeter();

        controlPanel = createControlPanel();

        add(gamePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        add(speedoMeter, BorderLayout.EAST);

        gameTimer = new Timer(16, e -> {
            update();
            gamePanel.repaint();
            speedoMeter.repaint();
        });
        gameTimer.start();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initTrees() {
        for (int i = 0; i < 20; i++) {
            trees.add(new Tree(random.nextInt(1200), 100 + random.nextInt(200)));
        }
    }

    private void initClouds() {
        for (int i = 0; i < 8; i++) {
            clouds.add(new Cloud(random.nextInt(1200), random.nextInt(150)));
        }
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));
        panel.setBackground(new Color(20, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel infoPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        infoPanel.setBackground(new Color(20, 20, 20));

        speedLabel = createInfoLabel("SPEED: 0 km/h");
        rpmLabel = createInfoLabel("RPM: 0");
        gearLabel = createInfoLabel("GEAR: N");
        tempLabel = createInfoLabel("TEMP: 20°C");

        infoPanel.add(speedLabel);
        infoPanel.add(rpmLabel);
        infoPanel.add(gearLabel);
        infoPanel.add(tempLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.setBackground(new Color(20, 20, 20));

        JButton startBtn = createStyledButton("🔑 START", new Color(0, 200, 0));
        JButton stopBtn = createStyledButton("⏹ STOP", new Color(200, 0, 0));
        JButton gasBtn = createStyledButton("⚡ GAS", new Color(255, 150, 0));
        JButton brakeBtn = createStyledButton("🛑 BRAKE", new Color(200, 0, 0));
        JButton turboBtn = createStyledButton("🚀 TURBO", new Color(0, 150, 255));
        JButton nightBtn = createStyledButton("🌙 NIGHT", new Color(50, 50, 150));
        JButton rainBtn = createStyledButton("🌧️ RAIN", new Color(100, 100, 200));
        JButton refuelBtn = createStyledButton("⛽ REFUEL", new Color(150, 150, 0));

        startBtn.addActionListener(e -> {
            araba.calistir();
            rpm = 1000;
        });

        stopBtn.addActionListener(e -> {
            araba.durdur();
            rpm = 0;
            gear = 0;
        });

        gasBtn.addActionListener(e -> {
            if (araba.getCalisiyorMu()) {
                araba.hizlan();
                rpm = Math.min(rpm + 500, 8000);
                engineTemp = Math.min(engineTemp + 5, 120);
                if (araba.getHiz() > 0) {
                    gear = Math.min((araba.getHiz() / 40) + 1, 6);
                    createExhaust();
                }
            }
        });

        brakeBtn.addActionListener(e -> {
            araba.yavasla();
            rpm = Math.max(rpm - 300, araba.getCalisiyorMu() ? 1000 : 0);
            createBrakeSmoke();
        });

        turboBtn.addActionListener(e -> {
            if (araba.getCalisiyorMu() && araba.getYakit() > 10) {
                for (int i = 0; i < 3; i++) {
                    araba.hizlan();
                }
                rpm = 8000;
                engineTemp = Math.min(engineTemp + 15, 120);
                createTurboFlame();
            }
        });

        nightBtn.addActionListener(e -> {
            isNight = !isNight;
            nightBtn.setText(isNight ? "☀️ DAY" : "🌙 NIGHT");
        });

        rainBtn.addActionListener(e -> {
            isRaining = !isRaining;
            rainBtn.setText(isRaining ? "☀️ CLEAR" : "🌧️ RAIN");
        });

        refuelBtn.addActionListener(e -> {
            araba.yakitDoldur();
            engineTemp = Math.max(engineTemp - 10, 20);
        });

        buttonPanel.add(startBtn);
        buttonPanel.add(gasBtn);
        buttonPanel.add(brakeBtn);
        buttonPanel.add(turboBtn);
        buttonPanel.add(stopBtn);
        buttonPanel.add(nightBtn);
        buttonPanel.add(rainBtn);
        buttonPanel.add(refuelBtn);

        panel.add(infoPanel);
        panel.add(buttonPanel);

        return panel;
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Consolas", Font.BOLD, 16));
        label.setForeground(new Color(0, 255, 0));
        label.setOpaque(true);
        label.setBackground(new Color(40, 40, 40));
        label.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 0), 2));
        return label;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(120, 35));
        return btn;
    }

    private void createExhaust() {
        for (int i = 0; i < 3; i++) {
            particles.add(new Particle(arabaX - 20, arabaY + 40, Color.GRAY, 1));
        }
    }

    private void createBrakeSmoke() {
        for (int i = 0; i < 5; i++) {
            particles.add(new Particle(arabaX + 20, arabaY + 60, Color.WHITE, 2));
        }
    }

    private void createTurboFlame() {
        for (int i = 0; i < 10; i++) {
            particles.add(new Particle(arabaX - 30, arabaY + 40, 
                new Color(255, random.nextInt(100), 0), 3));
        }
    }

    private void update() {
        if (araba.getCalisiyorMu() && araba.getHiz() > 0) {
            roadOffset += araba.getHiz() / 10;
            arabaAngle = Math.sin(System.currentTimeMillis() / 200.0) * 2;
            
            for (Cloud cloud : clouds) {
                cloud.x -= 0.5;
                if (cloud.x < -100) cloud.x = 1200;
            }
            
            for (Tree tree : trees) {
                tree.x -= araba.getHiz() / 15.0;
                if (tree.x < -50) tree.x = 1200;
            }
        }

        if (araba.getCalisiyorMu()) {
            rpm = Math.max(1000, rpm - 10);
        } else {
            rpm = Math.max(0, rpm - 50);
        }

        if (engineTemp > 20) {
            engineTemp = Math.max(20, engineTemp - 1);
        }

        particles.removeIf(p -> !p.update());

        if (isRaining && random.nextInt(3) == 0) {
            particles.add(new Particle(random.nextInt(1200), 0, 
                new Color(150, 150, 255, 200), 4));
        }

        speedLabel.setText("SPEED: " + araba.getHiz() + " km/h");
        rpmLabel.setText("RPM: " + rpm);
        gearLabel.setText("GEAR: " + (gear == 0 ? "N" : gear));
        tempLabel.setText("TEMP: " + engineTemp + "°C");

        if (engineTemp > 100) {
            tempLabel.setForeground(Color.RED);
        } else if (engineTemp > 80) {
            tempLabel.setForeground(Color.ORANGE);
        } else {
            tempLabel.setForeground(new Color(0, 255, 0));
        }
    }

    class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color skyColor = isNight ? new Color(10, 10, 40) : new Color(135, 206, 250);
            GradientPaint skyGradient = new GradientPaint(0, 0, skyColor, 
                0, getHeight() / 2, isNight ? new Color(20, 20, 60) : new Color(200, 230, 255));
            g2d.setPaint(skyGradient);
            g2d.fillRect(0, 0, getWidth(), getHeight() / 2);

            if (isNight) {
                g2d.setColor(Color.YELLOW);
                g2d.fillOval(1000, 50, 60, 60);
                for (int i = 0; i < 50; i++) {
                    g2d.fillOval(random.nextInt(1200), random.nextInt(300), 2, 2);
                }
            } else {
                g2d.setColor(Color.YELLOW);
                g2d.fillOval(1000, 50, 80, 80);
                g2d.setColor(new Color(255, 255, 0, 100));
                g2d.fillOval(990, 40, 100, 100);
            }

            for (Cloud cloud : clouds) {
                cloud.draw(g2d);
            }

            Color grassColor = isNight ? new Color(20, 60, 20) : new Color(50, 150, 50);
            g2d.setColor(grassColor);
            g2d.fillRect(0, getHeight() / 2, getWidth(), getHeight() / 2);

            for (Tree tree : trees) {
                tree.draw(g2d, isNight);
            }

            Color roadColor = isNight ? new Color(40, 40, 40) : new Color(80, 80, 80);
            g2d.setColor(roadColor);
            g2d.fillRect(0, getHeight() / 2 + 100, getWidth(), 200);

            g2d.setColor(Color.YELLOW);
            for (int i = -roadOffset % 80; i < getWidth(); i += 80) {
                g2d.fillRect(i, getHeight() / 2 + 195, 50, 10);
            }

            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, getHeight() / 2 + 100, getWidth(), 5);
            g2d.fillRect(0, getHeight() / 2 + 295, getWidth(), 5);

            for (Particle p : particles) {
                p.draw(g2d);
            }

            drawCar(g2d);

            if (araba.getYakit() < 20) {
                g2d.setColor(Color.RED);
                g2d.setFont(new Font("Arial", Font.BOLD, 24));
                g2d.drawString("⚠️ LOW FUEL!", 500, 50);
            }

            if (engineTemp > 100) {
                g2d.setColor(Color.RED);
                g2d.setFont(new Font("Arial", Font.BOLD, 24));
                g2d.drawString("⚠️ OVERHEATING!", 450, 80);
            }
        }

        private void drawCar(Graphics2D g2d) {
            g2d.translate(arabaX, arabaY);
            g2d.rotate(Math.toRadians(arabaAngle));

            GradientPaint carPaint = new GradientPaint(0, -30, new Color(255, 50, 0), 
                0, 30, new Color(200, 0, 0));
            g2d.setPaint(carPaint);

            int[] bodyX = {-60, -50, 0, 50, 60, 60, -60};
            int[] bodyY = {20, -10, -30, -10, 20, 40, 40};
            g2d.fillPolygon(bodyX, bodyY, 7);

            g2d.setColor(new Color(150, 0, 0));
            g2d.fillRoundRect(-40, -25, 80, 20, 10, 10);

            GradientPaint windowPaint = new GradientPaint(0, -25, new Color(100, 150, 200, 200), 
                0, -10, new Color(150, 200, 255, 150));
            g2d.setPaint(windowPaint);
            g2d.fillPolygon(new int[]{-35, -25, 25, 35}, new int[]{-20, -25, -25, -20}, 4);

            g2d.setColor(Color.BLACK);
            g2d.fillOval(-50, 30, 25, 25);
            g2d.fillOval(25, 30, 25, 25);

            g2d.setColor(Color.DARK_GRAY);
            g2d.fillOval(-45, 35, 15, 15);
            g2d.fillOval(30, 35, 15, 15);

            if (araba.getCalisiyorMu()) {
                g2d.setColor(Color.YELLOW);
                g2d.fillOval(-65, 10, 10, 8);
                g2d.fillOval(55, 10, 10, 8);

                if (araba.getHiz() > 0) {
                    g2d.setColor(Color.RED);
                    g2d.fillOval(-62, 25, 6, 6);
                    g2d.fillOval(56, 25, 6, 6);
                }
            }

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            g2d.drawString(araba.getMarka(), -30, 5);

            g2d.rotate(-Math.toRadians(arabaAngle));
            g2d.translate(-arabaX, -arabaY);
        }
    }

    class SpeedoMeter extends JPanel {
        SpeedoMeter() {
            setPreferredSize(new Dimension(250, 800));
            setBackground(new Color(20, 20, 20));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            drawGauge(g2d, 125, 150, 100, "SPEED", araba.getHiz(), araba.getMaxHiz(), Color.GREEN);
            drawGauge(g2d, 125, 350, 100, "RPM", rpm / 100, 80, Color.RED);
            drawFuelGauge(g2d, 125, 550, 100);
        }

        private void drawGauge(Graphics2D g2d, int cx, int cy, int radius, String label, 
                               int value, int max, Color color) {
            g2d.setColor(new Color(40, 40, 40));
            g2d.fillOval(cx - radius, cy - radius, radius * 2, radius * 2);

            g2d.setColor(new Color(60, 60, 60));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawOval(cx - radius, cy - radius, radius * 2, radius * 2);

            for (int i = 0; i <= 10; i++) {
                double angle = Math.PI * 0.75 + (Math.PI * 1.5 * i / 10.0);
                int x1 = cx + (int) ((radius - 10) * Math.cos(angle));
                int y1 = cy + (int) ((radius - 10) * Math.sin(angle));
                int x2 = cx + (int) ((radius - 20) * Math.cos(angle));
                int y2 = cy + (int) ((radius - 20) * Math.sin(angle));
                
                g2d.setColor(Color.WHITE);
                g2d.drawLine(x1, y1, x2, y2);
            }

            double needleAngle = Math.PI * 0.75 + (Math.PI * 1.5 * value / (double) max);
            int needleX = cx + (int) ((radius - 25) * Math.cos(needleAngle));
            int needleY = cy + (int) ((radius - 25) * Math.sin(needleAngle));

            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(4));
            g2d.drawLine(cx, cy, needleX, needleY);

            g2d.setColor(color);
            g2d.fillOval(cx - 8, cy - 8, 16, 16);

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(label, cx - fm.stringWidth(label) / 2, cy + radius - 30);

            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            String valueStr = String.valueOf(value);
            g2d.drawString(valueStr, cx - fm.stringWidth(valueStr) / 2, cy + 30);
        }

        private void drawFuelGauge(Graphics2D g2d, int cx, int cy, int radius) {
            g2d.setColor(new Color(40, 40, 40));
            g2d.fillOval(cx - radius, cy - radius, radius * 2, radius * 2);

            g2d.setColor(new Color(60, 60, 60));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawOval(cx - radius, cy - radius, radius * 2, radius * 2);

            int fuelLevel = araba.getYakit();
            Color fuelColor = fuelLevel > 50 ? Color.GREEN : fuelLevel > 20 ? Color.ORANGE : Color.RED;
            
            double angle = Math.PI * 1.5 * fuelLevel / 100.0;
            g2d.setColor(fuelColor);
            g2d.fillArc(cx - radius + 10, cy - radius + 10, (radius - 10) * 2, (radius - 10) * 2, 
                135, -(int) Math.toDegrees(angle));

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString("FUEL", cx - fm.stringWidth("FUEL") / 2, cy + radius - 30);

            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            String fuelStr = fuelLevel + "%";
            g2d.drawString(fuelStr, cx - fm.stringWidth(fuelStr) / 2, cy + 5);
        }
    }

    class Particle {
        double x, y, vx, vy;
        Color color;
        int life, maxLife;
        int type;

        Particle(double x, double y, Color color, int type) {
            this.x = x;
            this.y = y;
            this.color = color;
            this.type = type;
            this.vx = (random.nextDouble() - 0.5) * 4;
            this.vy = type == 4 ? random.nextDouble() * 5 + 5 : -random.nextDouble() * 3;
            this.maxLife = 30 + random.nextInt(30);
            this.life = maxLife;
        }

        boolean update() {
            x += vx;
            y += vy;
            vy += 0.2;
            life--;
            return life > 0;
        }

        void draw(Graphics2D g2d) {
            int alpha = (int) (255 * life / (double) maxLife);
            g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
            int size = type == 3 ? 8 : type == 4 ? 3 : 5;
            g2d.fillOval((int) x, (int) y, size, size);
        }
    }

    class Cloud {
        double x, y;
        int size;

        Cloud(double x, double y) {
            this.x = x;
            this.y = y;
            this.size = 40 + random.nextInt(40);
        }

        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(255, 255, 255, 150));
            g2d.fillOval((int) x, (int) y, size, size / 2);
            g2d.fillOval((int) x + size / 3, (int) y - size / 4, size, size / 2);
            g2d.fillOval((int) x + size / 2, (int) y, size, size / 2);
        }
    }

    class Tree {
        double x, y;

        Tree(double x, double y) {
            this.x = x;
            this.y = y;
        }

        void draw(Graphics2D g2d, boolean night) {
            g2d.setColor(night ? new Color(60, 40, 20) : new Color(101, 67, 33));
            g2d.fillRect((int) x, (int) y, 15, 40);
            
            g2d.setColor(night ? new Color(20, 80, 20) : new Color(34, 139, 34));
            g2d.fillOval((int) x - 20, (int) y - 30, 55, 50);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExtremeCarSim());
    }
}
