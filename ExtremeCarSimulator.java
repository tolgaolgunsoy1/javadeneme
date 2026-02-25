import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

public class ExtremeCarSimulator extends JFrame {
    private Araba araba;
    private GamePanel gamePanel;
    private Timer gameTimer;
    private Timer particleTimer;
    private ArrayList<Particle> particles = new ArrayList<>();
    private Random random = new Random();
    private int arabaX = 400;
    private int arabaY = 400;
    private double rotation = 0;
    private boolean gece = false;
    private int roadOffset = 0;
    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    private int score = 0;
    private boolean gameOver = false;

    public ExtremeCarSimulator() {
        Motor motor = new Motor(5.0, 300, "V8 Twin Turbo");
        araba = new Araba("Lamborghini", "Aventador", 2024, motor);

        setTitle("🏎️ EXTREME CAR SIMULATOR - " + araba.getMarka() + " " + araba.getModel());
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        gamePanel = new GamePanel();
        add(gamePanel);

        setupKeyBindings();
        startGameLoop();
        startParticleSystem();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupKeyBindings() {
        gamePanel.setFocusable(true);
        gamePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameOver) return;
                
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE:
                        if (!araba.getCalisiyorMu()) {
                            araba.calistir();
                        }
                        break;
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_W:
                        araba.hizlan();
                        createExhaustParticles();
                        break;
                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_S:
                        araba.yavasla();
                        break;
                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_A:
                        if (arabaX > 300) arabaX -= 15;
                        rotation = -0.1;
                        break;
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_D:
                        if (arabaX < 700) arabaX += 15;
                        rotation = 0.1;
                        break;
                    case KeyEvent.VK_F:
                        araba.yakitDoldur();
                        break;
                    case KeyEvent.VK_N:
                        gece = !gece;
                        break;
                    case KeyEvent.VK_R:
                        if (gameOver) resetGame();
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT ||
                    e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D) {
                    rotation = 0;
                }
            }
        });
    }

    private void startGameLoop() {
        gameTimer = new Timer(30, e -> {
            if (!gameOver) {
                updateGame();
                gamePanel.repaint();
            }
        });
        gameTimer.start();
    }

    private void startParticleSystem() {
        particleTimer = new Timer(50, e -> updateParticles());
        particleTimer.start();
    }

    private void updateGame() {
        if (araba.getCalisiyorMu() && araba.getHiz() > 0) {
            roadOffset += araba.getHiz() / 10;
            score += araba.getHiz() / 50;
            
            if (random.nextInt(100) < 2) {
                obstacles.add(new Obstacle(random.nextInt(400) + 300, -50));
            }
            
            for (int i = obstacles.size() - 1; i >= 0; i--) {
                Obstacle obs = obstacles.get(i);
                obs.y += araba.getHiz() / 5;
                
                if (obs.y > 800) {
                    obstacles.remove(i);
                } else if (checkCollision(obs)) {
                    gameOver = true;
                    araba.durdur();
                }
            }
        }
    }

    private boolean checkCollision(Obstacle obs) {
        Rectangle carBounds = new Rectangle(arabaX - 40, arabaY - 60, 80, 120);
        Rectangle obsBounds = new Rectangle(obs.x - 30, obs.y - 30, 60, 60);
        return carBounds.intersects(obsBounds);
    }

    private void resetGame() {
        gameOver = false;
        score = 0;
        obstacles.clear();
        particles.clear();
        Motor motor = new Motor(5.0, 300, "V8 Twin Turbo");
        araba = new Araba("Lamborghini", "Aventador", 2024, motor);
        arabaX = 400;
    }

    private void createExhaustParticles() {
        if (araba.getCalisiyorMu() && random.nextInt(3) == 0) {
            particles.add(new Particle(arabaX - 50, arabaY + 40, Color.GRAY));
            particles.add(new Particle(arabaX + 50, arabaY + 40, Color.GRAY));
        }
    }

    private void updateParticles() {
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            p.update();
            if (p.isDead()) {
                particles.remove(i);
            }
        }
    }

    class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            drawBackground(g2d);
            drawRoad(g2d);
            drawObstacles(g2d);
            drawParticles(g2d);
            drawCar(g2d);
            drawDashboard(g2d);
            drawHUD(g2d);
            
            if (gameOver) {
                drawGameOver(g2d);
            }
        }

        private void drawBackground(Graphics2D g2d) {
            if (gece) {
                GradientPaint gp = new GradientPaint(0, 0, new Color(10, 10, 30), 0, 400, new Color(30, 30, 60));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                g2d.setColor(Color.WHITE);
                for (int i = 0; i < 100; i++) {
                    int x = random.nextInt(getWidth());
                    int y = random.nextInt(400);
                    g2d.fillOval(x, y, 2, 2);
                }
            } else {
                GradientPaint gp = new GradientPaint(0, 0, new Color(135, 206, 250), 0, 400, new Color(200, 230, 255));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                g2d.setColor(new Color(255, 255, 100));
                g2d.fillOval(1000, 50, 80, 80);
            }
            
            g2d.setColor(new Color(34, 139, 34));
            g2d.fillRect(0, 400, 300, 400);
            g2d.fillRect(900, 400, 300, 400);
        }

        private void drawRoad(Graphics2D g2d) {
            g2d.setColor(new Color(50, 50, 50));
            g2d.fillRect(300, 0, 600, getHeight());
            
            g2d.setColor(Color.YELLOW);
            g2d.setStroke(new BasicStroke(5));
            for (int i = -100; i < getHeight(); i += 60) {
                int y = (i + roadOffset) % getHeight();
                g2d.drawLine(600, y, 600, y + 40);
            }
            
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(8));
            g2d.drawLine(300, 0, 300, getHeight());
            g2d.drawLine(900, 0, 900, getHeight());
        }

        private void drawObstacles(Graphics2D g2d) {
            for (Obstacle obs : obstacles) {
                g2d.setColor(new Color(139, 69, 19));
                g2d.fillRect(obs.x - 30, obs.y - 30, 60, 60);
                g2d.setColor(new Color(101, 67, 33));
                g2d.fillRect(obs.x - 25, obs.y - 25, 50, 50);
                g2d.setColor(Color.RED);
                g2d.setStroke(new BasicStroke(3));
                g2d.drawLine(obs.x - 20, obs.y - 20, obs.x + 20, obs.y + 20);
                g2d.drawLine(obs.x + 20, obs.y - 20, obs.x - 20, obs.y + 20);
            }
        }

        private void drawParticles(Graphics2D g2d) {
            for (Particle p : particles) {
                p.draw(g2d);
            }
        }

        private void drawCar(Graphics2D g2d) {
            g2d.translate(arabaX, arabaY);
            g2d.rotate(rotation);
            
            Color carColor = araba.getCalisiyorMu() ? new Color(255, 50, 0) : new Color(100, 100, 100);
            
            GradientPaint bodyGradient = new GradientPaint(-40, -60, carColor.brighter(), 40, 60, carColor.darker());
            g2d.setPaint(bodyGradient);
            g2d.fillRoundRect(-40, -60, 80, 120, 20, 20);
            
            g2d.setColor(carColor.darker().darker());
            int[] xPoints = {-35, 35, 25, -25};
            int[] yPoints = {-40, -40, -10, -10};
            g2d.fillPolygon(xPoints, yPoints, 4);
            
            g2d.setColor(new Color(100, 200, 255, 180));
            g2d.fillRect(-30, -35, 25, 20);
            g2d.fillRect(5, -35, 25, 20);
            
            g2d.setColor(Color.BLACK);
            g2d.fillOval(-35, -50, 20, 20);
            g2d.fillOval(15, -50, 20, 20);
            g2d.fillOval(-35, 30, 20, 20);
            g2d.fillOval(15, 30, 20, 20);
            
            g2d.setColor(new Color(80, 80, 80));
            g2d.fillOval(-32, -47, 14, 14);
            g2d.fillOval(18, -47, 14, 14);
            g2d.fillOval(-32, 33, 14, 14);
            g2d.fillOval(18, 33, 14, 14);
            
            if (araba.getCalisiyorMu()) {
                g2d.setColor(Color.YELLOW);
                g2d.fillOval(-42, -10, 10, 10);
                g2d.fillOval(32, -10, 10, 10);
                
                if (araba.getHiz() > 50) {
                    g2d.setColor(new Color(255, 0, 0, 150));
                    g2d.fillOval(-45, 50, 8, 8);
                    g2d.fillOval(37, 50, 8, 8);
                }
            }
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            g2d.drawString(araba.getMarka(), -30, 0);
            
            g2d.rotate(-rotation);
            g2d.translate(-arabaX, -arabaY);
        }

        private void drawDashboard(Graphics2D g2d) {
            g2d.setColor(new Color(0, 0, 0, 200));
            g2d.fillRoundRect(20, 20, 350, 180, 20, 20);
            
            g2d.setColor(new Color(255, 215, 0));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRoundRect(20, 20, 350, 180, 20, 20);
            
            drawSpeedometer(g2d, 100, 120, 60);
            drawFuelGauge(g2d, 250, 120, 60);
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString("🏎️ " + araba.getMarka() + " " + araba.getModel(), 40, 50);
            
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            String status = araba.getCalisiyorMu() ? "✓ Motor Çalışıyor" : "✗ Motor Kapalı";
            g2d.setColor(araba.getCalisiyorMu() ? Color.GREEN : Color.RED);
            g2d.drawString(status, 40, 75);
        }

        private void drawSpeedometer(Graphics2D g2d, int cx, int cy, int radius) {
            g2d.setColor(new Color(50, 50, 50));
            g2d.fillOval(cx - radius, cy - radius, radius * 2, radius * 2);
            
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillOval(cx - radius + 5, cy - radius + 5, radius * 2 - 10, radius * 2 - 10);
            
            g2d.setStroke(new BasicStroke(2));
            for (int i = 0; i <= 10; i++) {
                double angle = Math.PI * 0.75 + (Math.PI * 1.5 * i / 10);
                int x1 = cx + (int) ((radius - 10) * Math.cos(angle));
                int y1 = cy + (int) ((radius - 10) * Math.sin(angle));
                int x2 = cx + (int) ((radius - 20) * Math.cos(angle));
                int y2 = cy + (int) ((radius - 20) * Math.sin(angle));
                g2d.setColor(Color.WHITE);
                g2d.drawLine(x1, y1, x2, y2);
            }
            
            double speedAngle = Math.PI * 0.75 + (Math.PI * 1.5 * araba.getHiz() / araba.getMaxHiz());
            int needleX = cx + (int) ((radius - 15) * Math.cos(speedAngle));
            int needleY = cy + (int) ((radius - 15) * Math.sin(speedAngle));
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawLine(cx, cy, needleX, needleY);
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            String speedText = araba.getHiz() + " km/h";
            g2d.drawString(speedText, cx - 25, cy + 30);
        }

        private void drawFuelGauge(Graphics2D g2d, int cx, int cy, int radius) {
            g2d.setColor(new Color(50, 50, 50));
            g2d.fillOval(cx - radius, cy - radius, radius * 2, radius * 2);
            
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillOval(cx - radius + 5, cy - radius + 5, radius * 2 - 10, radius * 2 - 10);
            
            double fuelAngle = Math.PI * 0.75 + (Math.PI * 1.5 * araba.getYakit() / 100);
            int needleX = cx + (int) ((radius - 15) * Math.cos(fuelAngle));
            int needleY = cy + (int) ((radius - 15) * Math.sin(fuelAngle));
            
            Color fuelColor = araba.getYakit() > 50 ? Color.GREEN : (araba.getYakit() > 20 ? Color.ORANGE : Color.RED);
            g2d.setColor(fuelColor);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawLine(cx, cy, needleX, needleY);
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.drawString("⛽ %" + araba.getYakit(), cx - 20, cy + 30);
        }

        private void drawHUD(Graphics2D g2d) {
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRoundRect(getWidth() - 250, 20, 220, 120, 15, 15);
            
            g2d.setColor(Color.CYAN);
            g2d.setFont(new Font("Arial", Font.BOLD, 18));
            g2d.drawString("🏆 SKOR: " + score, getWidth() - 230, 50);
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            g2d.drawString("⬆ W/↑ - Hızlan", getWidth() - 230, 75);
            g2d.drawString("⬇ S/↓ - Yavaşla", getWidth() - 230, 95);
            g2d.drawString("⬅➡ A/D - Yön", getWidth() - 230, 115);
            g2d.drawString("F - Yakıt | N - Gece", getWidth() - 230, 135);
        }

        private void drawGameOver(Graphics2D g2d) {
            g2d.setColor(new Color(0, 0, 0, 200));
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, 80));
            g2d.drawString("GAME OVER!", 300, 300);
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 40));
            g2d.drawString("Final Skor: " + score, 420, 400);
            
            g2d.setFont(new Font("Arial", Font.PLAIN, 30));
            g2d.drawString("R tuşuna basarak yeniden başla", 320, 500);
        }
    }

    class Particle {
        int x, y;
        Color color;
        int life = 30;
        int vx, vy;

        Particle(int x, int y, Color color) {
            this.x = x;
            this.y = y;
            this.color = color;
            this.vx = random.nextInt(10) - 5;
            this.vy = random.nextInt(5) + 2;
        }

        void update() {
            x += vx;
            y += vy;
            life--;
        }

        boolean isDead() {
            return life <= 0;
        }

        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), life * 8));
            g2d.fillOval(x, y, 8, 8);
        }
    }

    class Obstacle {
        int x, y;

        Obstacle(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExtremeCarSimulator());
    }
}
