import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

public class UltraCarGame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private GameState gameState = GameState.MENU;
    private Araba selectedCar;
    private String[] carNames = {"Ferrari F8", "Lamborghini Aventador", "Porsche 911", "McLaren 720S", "Bugatti Chiron"};
    private int selectedCarIndex = 0;
    
    enum GameState { MENU, PLAYING, PAUSED, GAME_OVER }
    
    public UltraCarGame() {
        setTitle("🏎️ ULTRA CAR RACING SIMULATOR");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        mainPanel.add(new MenuPanel(), "MENU");
        mainPanel.add(new GamePanel(), "GAME");
        
        add(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    class MenuPanel extends JPanel {
        private int animOffset = 0;
        private Timer animTimer;
        
        public MenuPanel() {
            setLayout(null);
            setBackground(new Color(20, 20, 40));
            
            animTimer = new Timer(50, e -> {
                animOffset = (animOffset + 2) % 800;
                repaint();
            });
            animTimer.start();
            
            JLabel title = new JLabel("ULTRA CAR RACING", SwingConstants.CENTER);
            title.setFont(new Font("Arial", Font.BOLD, 60));
            title.setForeground(new Color(255, 215, 0));
            title.setBounds(300, 100, 800, 80);
            add(title);
            
            JLabel subtitle = new JLabel("Extreme Speed • Ultimate Control", SwingConstants.CENTER);
            subtitle.setFont(new Font("Arial", Font.ITALIC, 24));
            subtitle.setForeground(Color.CYAN);
            subtitle.setBounds(300, 180, 800, 40);
            add(subtitle);
            
            JPanel carSelectPanel = new JPanel();
            carSelectPanel.setLayout(null);
            carSelectPanel.setBackground(new Color(40, 40, 60, 200));
            carSelectPanel.setBounds(350, 300, 700, 250);
            carSelectPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 3));
            
            JLabel carLabel = new JLabel(carNames[selectedCarIndex], SwingConstants.CENTER);
            carLabel.setFont(new Font("Arial", Font.BOLD, 36));
            carLabel.setForeground(Color.WHITE);
            carLabel.setBounds(150, 30, 400, 50);
            carSelectPanel.add(carLabel);
            
            JButton prevBtn = createStyledButton("◀ PREV", 50, 100, 150, 60);
            prevBtn.addActionListener(e -> {
                selectedCarIndex = (selectedCarIndex - 1 + carNames.length) % carNames.length;
                carLabel.setText(carNames[selectedCarIndex]);
            });
            carSelectPanel.add(prevBtn);
            
            JButton nextBtn = createStyledButton("NEXT ▶", 500, 100, 150, 60);
            nextBtn.addActionListener(e -> {
                selectedCarIndex = (selectedCarIndex + 1) % carNames.length;
                carLabel.setText(carNames[selectedCarIndex]);
            });
            carSelectPanel.add(nextBtn);
            
            JLabel statsLabel = new JLabel("<html>Speed: ★★★★★<br>Control: ★★★★☆<br>Nitro: ★★★★★</html>", SwingConstants.CENTER);
            statsLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            statsLabel.setForeground(Color.YELLOW);
            statsLabel.setBounds(250, 120, 200, 100);
            carSelectPanel.add(statsLabel);
            
            add(carSelectPanel);
            
            JButton startBtn = createStyledButton("🏁 START RACE", 500, 600, 400, 80);
            startBtn.setFont(new Font("Arial", Font.BOLD, 32));
            startBtn.addActionListener(e -> {
                animTimer.stop();
                createSelectedCar();
                cardLayout.show(mainPanel, "GAME");
                ((GamePanel) mainPanel.getComponent(1)).startGame();
            });
            add(startBtn);
            
            JLabel controls = new JLabel("<html><center>CONTROLS:<br>W/↑ - Accelerate | S/↓ - Brake<br>A/← - Left | D/→ - Right<br>SHIFT - Nitro Boost | SPACE - Drift<br>F - Refuel | P - Pause</center></html>", SwingConstants.CENTER);
            controls.setFont(new Font("Arial", Font.PLAIN, 16));
            controls.setForeground(new Color(200, 200, 200));
            controls.setBounds(450, 720, 500, 120);
            add(controls);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            for (int i = 0; i < 5; i++) {
                int alpha = 50 - i * 10;
                g2d.setColor(new Color(255, 215, 0, alpha));
                g2d.drawRoundRect(280 - i * 10 + animOffset % 100, 80 - i * 5, 840 + i * 20, 120 + i * 10, 30, 30);
            }
        }
        
        private JButton createStyledButton(String text, int x, int y, int w, int h) {
            JButton btn = new JButton(text);
            btn.setBounds(x, y, w, h);
            btn.setFont(new Font("Arial", Font.BOLD, 20));
            btn.setBackground(new Color(255, 100, 0));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 3));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(new Color(255, 150, 0));
                }
                public void mouseExited(MouseEvent e) {
                    btn.setBackground(new Color(255, 100, 0));
                }
            });
            return btn;
        }
    }
    
    private void createSelectedCar() {
        Motor motor = new Motor(5.0, 250 + selectedCarIndex * 50, "V8 Turbo");
        String[] brands = {"Ferrari", "Lamborghini", "Porsche", "McLaren", "Bugatti"};
        String[] models = {"F8", "Aventador", "911", "720S", "Chiron"};
        selectedCar = new Araba(brands[selectedCarIndex], models[selectedCarIndex], 2024, motor);
    }
    
    class GamePanel extends JPanel {
        private Timer gameTimer, particleTimer, powerUpTimer;
        private ArrayList<Particle> particles = new ArrayList<>();
        private ArrayList<Obstacle> obstacles = new ArrayList<>();
        private ArrayList<PowerUp> powerUps = new ArrayList<>();
        private Random random = new Random();
        private int arabaX = 700, arabaY = 600;
        private double rotation = 0, driftAngle = 0;
        private int roadOffset = 0, score = 0, combo = 0;
        private boolean isDrifting = false, isPaused = false;
        private long lastObstacleTime = 0, lastPowerUpTime = 0;
        private int[] roadLanes = {450, 600, 750, 900};
        
        public GamePanel() {
            setFocusable(true);
            setBackground(Color.BLACK);
            setupControls();
        }
        
        public void startGame() {
            requestFocusInWindow();
            selectedCar.calistir();
            
            gameTimer = new Timer(20, e -> {
                if (!isPaused) {
                    updateGame();
                    repaint();
                }
            });
            
            particleTimer = new Timer(30, e -> {
                if (!isPaused) updateParticles();
            });
            
            powerUpTimer = new Timer(100, e -> {
                if (!isPaused && System.currentTimeMillis() - lastPowerUpTime > 5000 && random.nextInt(50) == 0) {
                    powerUps.add(new PowerUp(roadLanes[random.nextInt(4)], -50, PowerUpType.values()[random.nextInt(3)]));
                    lastPowerUpTime = System.currentTimeMillis();
                }
            });
            
            gameTimer.start();
            particleTimer.start();
            powerUpTimer.start();
        }
        
        private void setupControls() {
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (selectedCar.getCan() <= 0) return;
                    
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_P:
                            isPaused = !isPaused;
                            break;
                        case KeyEvent.VK_ESCAPE:
                            cardLayout.show(mainPanel, "MENU");
                            stopGame();
                            break;
                        case KeyEvent.VK_UP:
                        case KeyEvent.VK_W:
                            selectedCar.hizlan();
                            createExhaustParticles();
                            break;
                        case KeyEvent.VK_DOWN:
                        case KeyEvent.VK_S:
                            selectedCar.yavasla();
                            break;
                        case KeyEvent.VK_LEFT:
                        case KeyEvent.VK_A:
                            if (arabaX > 400) {
                                arabaX -= 20;
                                rotation = -0.15;
                            }
                            break;
                        case KeyEvent.VK_RIGHT:
                        case KeyEvent.VK_D:
                            if (arabaX < 950) {
                                arabaX += 20;
                                rotation = 0.15;
                            }
                            break;
                        case KeyEvent.VK_SHIFT:
                            selectedCar.nitroAktif(true);
                            break;
                        case KeyEvent.VK_SPACE:
                            isDrifting = true;
                            driftAngle = rotation * 3;
                            break;
                        case KeyEvent.VK_F:
                            selectedCar.yakitDoldur();
                            break;
                    }
                }
                
                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A ||
                        e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
                        rotation = 0;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                        selectedCar.nitroAktif(false);
                    }
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        isDrifting = false;
                        driftAngle = 0;
                    }
                }
            });
        }
        
        private void updateGame() {
            if (selectedCar.getCalisiyorMu() && selectedCar.getHiz() > 0) {
                roadOffset += selectedCar.getHiz() / 8;
                score += selectedCar.getHiz() / 30;
                
                if (isDrifting && selectedCar.getHiz() > 50) {
                    combo++;
                    score += combo / 10;
                    createDriftParticles();
                } else {
                    combo = 0;
                }
                
                if (System.currentTimeMillis() - lastObstacleTime > 1500 - selectedCar.getHiz() * 2) {
                    obstacles.add(new Obstacle(roadLanes[random.nextInt(4)], -50));
                    lastObstacleTime = System.currentTimeMillis();
                }
                
                for (int i = obstacles.size() - 1; i >= 0; i--) {
                    Obstacle obs = obstacles.get(i);
                    obs.y += selectedCar.getHiz() / 4 + 5;
                    
                    if (obs.y > 900) {
                        obstacles.remove(i);
                        score += 50;
                    } else if (checkCollision(obs.x, obs.y, 40)) {
                        selectedCar.hasarAl();
                        obstacles.remove(i);
                        createExplosion(obs.x, obs.y);
                        if (selectedCar.getCan() <= 0) {
                            gameOver();
                        }
                    }
                }
                
                for (int i = powerUps.size() - 1; i >= 0; i--) {
                    PowerUp pu = powerUps.get(i);
                    pu.y += selectedCar.getHiz() / 4 + 3;
                    
                    if (pu.y > 900) {
                        powerUps.remove(i);
                    } else if (checkCollision(pu.x, pu.y, 30)) {
                        applyPowerUp(pu.type);
                        powerUps.remove(i);
                        score += 100;
                    }
                }
            }
        }
        
        private boolean checkCollision(int x, int y, int size) {
            Rectangle carBounds = new Rectangle(arabaX - 35, arabaY - 50, 70, 100);
            Rectangle objBounds = new Rectangle(x - size/2, y - size/2, size, size);
            return carBounds.intersects(objBounds);
        }
        
        private void applyPowerUp(PowerUpType type) {
            switch (type) {
                case NITRO:
                    selectedCar.nitroYukle();
                    break;
                case FUEL:
                    selectedCar.yakitDoldur();
                    break;
                case HEALTH:
                    selectedCar.canYukle();
                    break;
            }
        }
        
        private void createExhaustParticles() {
            if (random.nextInt(2) == 0) {
                particles.add(new Particle(arabaX - 40, arabaY + 50, new Color(100, 100, 100), 2));
                particles.add(new Particle(arabaX + 40, arabaY + 50, new Color(100, 100, 100), 2));
            }
            if (selectedCar.isNitro()) {
                particles.add(new Particle(arabaX, arabaY + 60, new Color(255, 100, 0), 3));
            }
        }
        
        private void createDriftParticles() {
            if (random.nextInt(2) == 0) {
                particles.add(new Particle(arabaX - 30, arabaY + 40, new Color(200, 200, 200), 2));
                particles.add(new Particle(arabaX + 30, arabaY + 40, new Color(200, 200, 200), 2));
            }
        }
        
        private void createExplosion(int x, int y) {
            for (int i = 0; i < 20; i++) {
                particles.add(new Particle(x, y, new Color(255, random.nextInt(100), 0), 4));
            }
        }
        
        private void updateParticles() {
            for (int i = particles.size() - 1; i >= 0; i--) {
                Particle p = particles.get(i);
                p.update();
                if (p.isDead()) particles.remove(i);
            }
        }
        
        private void gameOver() {
            gameTimer.stop();
            particleTimer.stop();
            powerUpTimer.stop();
            selectedCar.durdur();
        }
        
        private void stopGame() {
            if (gameTimer != null) gameTimer.stop();
            if (particleTimer != null) particleTimer.stop();
            if (powerUpTimer != null) powerUpTimer.stop();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            drawBackground(g2d);
            drawRoad(g2d);
            drawPowerUps(g2d);
            drawObstacles(g2d);
            drawParticles(g2d);
            drawCar(g2d);
            drawHUD(g2d);
            
            if (isPaused) drawPauseScreen(g2d);
            if (selectedCar.getCan() <= 0) drawGameOver(g2d);
        }
        
        private void drawBackground(Graphics2D g2d) {
            GradientPaint gp = new GradientPaint(0, 0, new Color(20, 20, 50), 0, 450, new Color(50, 50, 100));
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            g2d.setColor(new Color(34, 139, 34));
            g2d.fillRect(0, 0, 350, getHeight());
            g2d.fillRect(1050, 0, 350, getHeight());
        }
        
        private void drawRoad(Graphics2D g2d) {
            g2d.setColor(new Color(40, 40, 40));
            g2d.fillRect(350, 0, 700, getHeight());
            
            g2d.setColor(Color.YELLOW);
            g2d.setStroke(new BasicStroke(4));
            for (int i = -100; i < getHeight(); i += 60) {
                int y = (i + roadOffset) % getHeight();
                for (int lane = 1; lane < 4; lane++) {
                    g2d.drawLine(350 + lane * 175, y, 350 + lane * 175, y + 40);
                }
            }
            
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(10));
            g2d.drawLine(350, 0, 350, getHeight());
            g2d.drawLine(1050, 0, 1050, getHeight());
        }
        
        private void drawPowerUps(Graphics2D g2d) {
            for (PowerUp pu : powerUps) {
                g2d.setColor(pu.type.color);
                g2d.fillOval(pu.x - 25, pu.y - 25, 50, 50);
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(3));
                g2d.drawOval(pu.x - 25, pu.y - 25, 50, 50);
                g2d.setFont(new Font("Arial", Font.BOLD, 20));
                g2d.drawString(pu.type.symbol, pu.x - 10, pu.y + 8);
            }
        }
        
        private void drawObstacles(Graphics2D g2d) {
            for (Obstacle obs : obstacles) {
                g2d.setColor(new Color(150, 75, 0));
                g2d.fillRect(obs.x - 20, obs.y - 20, 40, 40);
                g2d.setColor(new Color(100, 50, 0));
                g2d.fillRect(obs.x - 15, obs.y - 15, 30, 30);
                g2d.setColor(Color.RED);
                g2d.setStroke(new BasicStroke(4));
                g2d.drawLine(obs.x - 15, obs.y - 15, obs.x + 15, obs.y + 15);
                g2d.drawLine(obs.x + 15, obs.y - 15, obs.x - 15, obs.y + 15);
            }
        }
        
        private void drawParticles(Graphics2D g2d) {
            for (Particle p : particles) p.draw(g2d);
        }
        
        private void drawCar(Graphics2D g2d) {
            g2d.translate(arabaX, arabaY);
            g2d.rotate(rotation + driftAngle);
            
            Color carColor = new Color(255, 50, 0);
            if (selectedCar.isNitro()) {
                carColor = new Color(255, 150, 0);
            }
            
            GradientPaint bodyGradient = new GradientPaint(-35, -50, carColor.brighter(), 35, 50, carColor.darker());
            g2d.setPaint(bodyGradient);
            g2d.fillRoundRect(-35, -50, 70, 100, 15, 15);
            
            g2d.setColor(carColor.darker().darker());
            int[] xp = {-30, 30, 20, -20};
            int[] yp = {-35, -35, -10, -10};
            g2d.fillPolygon(xp, yp, 4);
            
            g2d.setColor(new Color(100, 200, 255, 150));
            g2d.fillRect(-25, -30, 20, 15);
            g2d.fillRect(5, -30, 20, 15);
            
            g2d.setColor(Color.BLACK);
            g2d.fillOval(-30, -40, 18, 18);
            g2d.fillOval(12, -40, 18, 18);
            g2d.fillOval(-30, 22, 18, 18);
            g2d.fillOval(12, 22, 18, 18);
            
            if (selectedCar.getCalisiyorMu()) {
                g2d.setColor(Color.YELLOW);
                g2d.fillOval(-38, -5, 8, 8);
                g2d.fillOval(30, -5, 8, 8);
            }
            
            g2d.rotate(-(rotation + driftAngle));
            g2d.translate(-arabaX, -arabaY);
        }
        
        private void drawHUD(Graphics2D g2d) {
            g2d.setColor(new Color(0, 0, 0, 180));
            g2d.fillRoundRect(20, 20, 350, 200, 15, 15);
            
            g2d.setColor(Color.YELLOW);
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            g2d.drawString("🏆 SCORE: " + score, 40, 50);
            
            if (combo > 10) {
                g2d.setColor(Color.CYAN);
                g2d.drawString("🔥 COMBO x" + combo, 40, 80);
            }
            
            drawBar(g2d, 40, 100, 300, 20, selectedCar.getHiz(), selectedCar.getMaxHiz(), "SPEED", Color.CYAN);
            drawBar(g2d, 40, 130, 300, 20, selectedCar.getYakit(), 100, "FUEL", Color.GREEN);
            drawBar(g2d, 40, 160, 300, 20, selectedCar.getNitroMiktar(), 100, "NITRO", Color.ORANGE);
            
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            String hearts = "";
            for (int i = 0; i < selectedCar.getCan(); i++) hearts += "❤ ";
            g2d.drawString("LIVES: " + hearts, 40, 195);
            
            g2d.setColor(new Color(0, 0, 0, 180));
            g2d.fillRoundRect(1030, 20, 350, 100, 15, 15);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 18));
            g2d.drawString("🏎️ " + selectedCar.getMarka() + " " + selectedCar.getModel(), 1050, 50);
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            g2d.drawString("SHIFT - Nitro | SPACE - Drift", 1050, 75);
            g2d.drawString("P - Pause | ESC - Menu", 1050, 95);
        }
        
        private void drawBar(Graphics2D g2d, int x, int y, int w, int h, int val, int max, String label, Color color) {
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(x, y, w, h);
            g2d.setColor(color);
            g2d.fillRect(x, y, (int)(w * val / (double)max), h);
            g2d.setColor(Color.WHITE);
            g2d.drawRect(x, y, w, h);
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.drawString(label + ": " + val, x + 5, y + 15);
        }
        
        private void drawPauseScreen(Graphics2D g2d) {
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.setColor(Color.YELLOW);
            g2d.setFont(new Font("Arial", Font.BOLD, 80));
            g2d.drawString("PAUSED", 550, 400);
            g2d.setFont(new Font("Arial", Font.PLAIN, 30));
            g2d.setColor(Color.WHITE);
            g2d.drawString("Press P to Resume", 550, 500);
        }
        
        private void drawGameOver(Graphics2D g2d) {
            g2d.setColor(new Color(0, 0, 0, 200));
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, 100));
            g2d.drawString("GAME OVER", 400, 350);
            g2d.setColor(Color.YELLOW);
            g2d.setFont(new Font("Arial", Font.BOLD, 50));
            g2d.drawString("Final Score: " + score, 500, 450);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.PLAIN, 30));
            g2d.drawString("Press ESC for Menu", 530, 550);
        }
    }
    
    enum PowerUpType {
        NITRO("N", new Color(255, 150, 0)),
        FUEL("F", new Color(50, 200, 50)),
        HEALTH("H", new Color(255, 50, 50));
        
        String symbol;
        Color color;
        PowerUpType(String symbol, Color color) {
            this.symbol = symbol;
            this.color = color;
        }
    }
    
    class PowerUp {
        int x, y;
        PowerUpType type;
        PowerUp(int x, int y, PowerUpType type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }
    }
    
    class Obstacle {
        int x, y;
        Obstacle(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    class Particle {
        int x, y, vx, vy, life, maxLife, size;
        Color color;
        
        Particle(int x, int y, Color color, int size) {
            this.x = x;
            this.y = y;
            this.color = color;
            this.size = size;
            this.vx = new Random().nextInt(10) - 5;
            this.vy = new Random().nextInt(8) + 2;
            this.maxLife = this.life = 30 + new Random().nextInt(20);
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
            int alpha = (int)(255 * life / (double)maxLife);
            g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, alpha)));
            g2d.fillOval(x, y, size * 3, size * 3);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UltraCarGame());
    }
}
