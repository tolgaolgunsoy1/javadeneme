import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

public class SuperCarGame extends JFrame {
    private GamePanel gamePanel;
    
    public SuperCarGame() {
        setTitle("🏎️ SUPER CAR RACING - Ultra Graphics");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        gamePanel = new GamePanel();
        add(gamePanel);
        
        setLocationRelativeTo(null);
        setVisible(true);
        gamePanel.startGame();
    }
    
    class GamePanel extends JPanel {
        private Araba araba;
        private Timer gameTimer;
        private ArrayList<Tree> trees = new ArrayList<>();
        private ArrayList<Building> buildings = new ArrayList<>();
        private ArrayList<Cloud> clouds = new ArrayList<>();
        private ArrayList<Particle> particles = new ArrayList<>();
        private Random random = new Random();
        private int arabaX = 700, arabaY = 600;
        private double rotation = 0;
        private int roadOffset = 0;
        private int score = 0;
        private DayNightCycle dayNight = new DayNightCycle();
        
        public GamePanel() {
            setFocusable(true);
            Motor motor = new Motor(5.0, 300, "V8 Twin Turbo");
            araba = new Araba("Lamborghini", "Aventador", 2024, motor);
            araba.calistir();
            
            initEnvironment();
            setupControls();
        }
        
        private void initEnvironment() {
            for (int i = 0; i < 20; i++) {
                trees.add(new Tree(random.nextInt(300), random.nextInt(900)));
                trees.add(new Tree(1100 + random.nextInt(300), random.nextInt(900)));
            }
            for (int i = 0; i < 10; i++) {
                buildings.add(new Building(random.nextInt(250), random.nextInt(600), random.nextInt(3)));
                buildings.add(new Building(1150 + random.nextInt(250), random.nextInt(600), random.nextInt(3)));
            }
            for (int i = 0; i < 8; i++) {
                clouds.add(new Cloud(random.nextInt(1400), random.nextInt(300)));
            }
        }
        
        public void startGame() {
            requestFocusInWindow();
            gameTimer = new Timer(20, e -> {
                updateGame();
                repaint();
            });
            gameTimer.start();
        }
        
        private void setupControls() {
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    switch (e.getKeyCode()) {
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
                            if (arabaX > 400) {
                                arabaX -= 20;
                                rotation = -0.2;
                            }
                            break;
                        case KeyEvent.VK_RIGHT:
                        case KeyEvent.VK_D:
                            if (arabaX < 950) {
                                arabaX += 20;
                                rotation = 0.2;
                            }
                            break;
                        case KeyEvent.VK_SHIFT:
                            araba.nitroAktif(true);
                            break;
                        case KeyEvent.VK_F:
                            araba.yakitDoldur();
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
                        araba.nitroAktif(false);
                    }
                }
            });
        }
        
        private void updateGame() {
            dayNight.update();
            if (araba.getCalisiyorMu() && araba.getHiz() > 0) {
                roadOffset += araba.getHiz() / 8;
                score += araba.getHiz() / 30;
                
                for (Tree tree : trees) {
                    tree.y += araba.getHiz() / 10;
                    if (tree.y > 900) tree.y = -50;
                }
                
                for (Building building : buildings) {
                    building.y += araba.getHiz() / 15;
                    if (building.y > 900) building.y = -200;
                }
                
                for (Cloud cloud : clouds) {
                    cloud.x += 0.5;
                    if (cloud.x > 1400) cloud.x = -100;
                }
            }
            
            for (int i = particles.size() - 1; i >= 0; i--) {
                Particle p = particles.get(i);
                p.update();
                if (p.isDead()) particles.remove(i);
            }
        }
        
        private void createExhaustParticles() {
            if (random.nextInt(2) == 0) {
                Color smokeColor = araba.isNitro() ? new Color(255, 150, 0) : new Color(100, 100, 100);
                particles.add(new Particle(arabaX - 45, arabaY + 55, smokeColor));
                particles.add(new Particle(arabaX + 45, arabaY + 55, smokeColor));
            }
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            
            drawSky(g2d);
            drawClouds(g2d);
            dayNight.drawSun(g2d, getWidth());
            dayNight.drawMoon(g2d, getWidth());
            drawGround(g2d);
            drawBuildings(g2d);
            drawTrees(g2d);
            drawRoad(g2d);
            drawParticles(g2d);
            drawDetailedCar(g2d);
            drawHUD(g2d);
        }
        
        private void drawSky(Graphics2D g2d) {
            Color skyColor = dayNight.getSkyColor();
            GradientPaint gp = new GradientPaint(0, 0, skyColor, 0, 400, skyColor.darker());
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
        
        private void drawClouds(Graphics2D g2d) {
            for (Cloud cloud : clouds) {
                g2d.setColor(new Color(255, 255, 255, dayNight.isNight() ? 50 : 200));
                g2d.fillOval(cloud.x, cloud.y, 80, 40);
                g2d.fillOval(cloud.x + 30, cloud.y - 10, 60, 40);
                g2d.fillOval(cloud.x + 60, cloud.y, 70, 35);
            }
        }
        
        private void drawGround(Graphics2D g2d) {
            Color groundColor = dayNight.getGroundColor();
            g2d.setColor(groundColor);
            g2d.fillRect(0, 400, 350, 500);
            g2d.fillRect(1050, 400, 350, 500);
            
            g2d.setColor(groundColor.darker());
            for (int i = 0; i < 30; i++) {
                int x = random.nextInt(300);
                int y = 400 + random.nextInt(500);
                g2d.fillOval(x, y, 5, 5);
                g2d.fillOval(1100 + random.nextInt(300), y, 5, 5);
            }
        }
        
        private void drawBuildings(Graphics2D g2d) {
            for (Building b : buildings) {
                Color buildingColor = dayNight.isNight() ? new Color(40, 40, 60) : new Color(120, 120, 140);
                GradientPaint gp = new GradientPaint(b.x, b.y, buildingColor.brighter(), b.x + b.width, b.y + b.height, buildingColor.darker());
                g2d.setPaint(gp);
                g2d.fillRect(b.x, b.y, b.width, b.height);
                
                g2d.setColor(buildingColor.darker());
                g2d.drawRect(b.x, b.y, b.width, b.height);
                
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (dayNight.isNight() && random.nextInt(3) == 0) {
                            g2d.setColor(Color.YELLOW);
                        } else {
                            g2d.setColor(new Color(100, 150, 200, 100));
                        }
                        g2d.fillRect(b.x + 10 + i * 20, b.y + 10 + j * 20, 15, 15);
                    }
                }
            }
        }
        
        private void drawTrees(Graphics2D g2d) {
            for (Tree tree : trees) {
                g2d.setColor(new Color(101, 67, 33));
                g2d.fillRect(tree.x - 8, tree.y, 16, 50);
                
                Color leafColor = dayNight.isNight() ? new Color(20, 80, 20) : new Color(34, 139, 34);
                g2d.setColor(leafColor);
                g2d.fillOval(tree.x - 30, tree.y - 40, 60, 60);
                g2d.fillOval(tree.x - 25, tree.y - 50, 50, 50);
                g2d.fillOval(tree.x - 20, tree.y - 30, 40, 40);
                
                g2d.setColor(leafColor.darker());
                g2d.drawOval(tree.x - 30, tree.y - 40, 60, 60);
            }
        }
        
        private void drawRoad(Graphics2D g2d) {
            GradientPaint roadGradient = new GradientPaint(350, 0, new Color(50, 50, 50), 700, 0, new Color(40, 40, 40));
            g2d.setPaint(roadGradient);
            g2d.fillRect(350, 0, 700, getHeight());
            
            g2d.setColor(new Color(60, 60, 60));
            for (int i = 0; i < 20; i++) {
                int y = (i * 50 + roadOffset) % 900;
                g2d.fillRect(350, y, 700, 3);
            }
            
            g2d.setColor(Color.YELLOW);
            g2d.setStroke(new BasicStroke(5));
            for (int i = -100; i < getHeight(); i += 60) {
                int y = (i + roadOffset) % getHeight();
                g2d.drawLine(525, y, 525, y + 40);
                g2d.drawLine(700, y, 700, y + 40);
                g2d.drawLine(875, y, 875, y + 40);
            }
            
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(12));
            g2d.drawLine(350, 0, 350, getHeight());
            g2d.drawLine(1050, 0, 1050, getHeight());
            
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(6));
            g2d.drawLine(355, 0, 355, getHeight());
            g2d.drawLine(1045, 0, 1045, getHeight());
        }
        
        private void drawParticles(Graphics2D g2d) {
            for (Particle p : particles) p.draw(g2d);
        }
        
        private void drawDetailedCar(Graphics2D g2d) {
            g2d.translate(arabaX, arabaY);
            g2d.rotate(rotation);
            
            Color carColor = araba.isNitro() ? new Color(255, 150, 0) : new Color(220, 20, 20);
            
            if (araba.isNitro()) {
                g2d.setColor(new Color(255, 200, 0, 100));
                g2d.fillOval(-50, -70, 100, 140);
            }
            
            g2d.setColor(Color.BLACK);
            g2d.fillOval(-35, -45, 20, 20);
            g2d.fillOval(15, -45, 20, 20);
            g2d.fillOval(-35, 25, 20, 20);
            g2d.fillOval(15, 25, 20, 20);
            
            g2d.setColor(new Color(80, 80, 80));
            g2d.fillOval(-32, -42, 14, 14);
            g2d.fillOval(18, -42, 14, 14);
            g2d.fillOval(-32, 28, 14, 14);
            g2d.fillOval(18, 28, 14, 14);
            
            g2d.setColor(new Color(100, 100, 100));
            for (int i = 0; i < 5; i++) {
                g2d.drawOval(-32 + i, -42 + i, 14 - i * 2, 14 - i * 2);
                g2d.drawOval(18 + i, -42 + i, 14 - i * 2, 14 - i * 2);
            }
            
            GradientPaint bodyGradient = new GradientPaint(-40, -60, carColor.brighter(), 40, 60, carColor.darker());
            g2d.setPaint(bodyGradient);
            g2d.fillRoundRect(-40, -60, 80, 120, 20, 20);
            
            g2d.setColor(carColor.darker());
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(-40, -60, 80, 120, 20, 20);
            
            g2d.setColor(carColor.darker().darker());
            int[] xPoints = {-35, 35, 30, -30};
            int[] yPoints = {-45, -45, -15, -15};
            g2d.fillPolygon(xPoints, yPoints, 4);
            
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(1));
            g2d.drawPolygon(xPoints, yPoints, 4);
            
            GradientPaint windowGradient = new GradientPaint(-30, -40, new Color(150, 200, 255, 200), 0, -20, new Color(100, 150, 200, 150));
            g2d.setPaint(windowGradient);
            g2d.fillRect(-28, -40, 23, 20);
            g2d.fillRect(5, -40, 23, 20);
            
            g2d.setColor(new Color(50, 50, 50));
            g2d.drawRect(-28, -40, 23, 20);
            g2d.drawRect(5, -40, 23, 20);
            
            g2d.setColor(carColor.darker());
            g2d.fillRect(-42, -5, 4, 10);
            g2d.fillRect(38, -5, 4, 10);
            
            if (araba.getCalisiyorMu()) {
                g2d.setColor(Color.YELLOW);
                g2d.fillOval(-45, -8, 10, 10);
                g2d.fillOval(35, -8, 10, 10);
                
                g2d.setColor(new Color(255, 255, 150, 150));
                g2d.fillOval(-48, -11, 16, 16);
                g2d.fillOval(32, -11, 16, 16);
            }
            
            if (araba.getHiz() > 50) {
                g2d.setColor(Color.RED);
                g2d.fillOval(-48, 52, 8, 8);
                g2d.fillOval(40, 52, 8, 8);
            }
            
            g2d.setColor(carColor.darker().darker());
            g2d.fillRect(-45, 45, 90, 8);
            g2d.fillRect(-40, 40, 80, 5);
            
            g2d.setColor(Color.BLACK);
            g2d.fillRect(-5, 10, 10, 30);
            g2d.fillRect(-15, 15, 10, 20);
            g2d.fillRect(5, 15, 10, 20);
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 8));
            g2d.drawString(araba.getMarka(), -25, 5);
            
            g2d.rotate(-rotation);
            g2d.translate(-arabaX, -arabaY);
        }
        
        private void drawHUD(Graphics2D g2d) {
            g2d.setColor(new Color(0, 0, 0, 200));
            g2d.fillRoundRect(20, 20, 380, 220, 20, 20);
            
            g2d.setColor(new Color(255, 215, 0));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRoundRect(20, 20, 380, 220, 20, 20);
            
            g2d.setColor(Color.CYAN);
            g2d.setFont(new Font("Arial", Font.BOLD, 28));
            g2d.drawString("🏆 " + score, 40, 55);
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 18));
            g2d.drawString("🏎️ " + araba.getMarka() + " " + araba.getModel(), 40, 85);
            
            drawProgressBar(g2d, 40, 105, 340, 25, araba.getHiz(), araba.getMaxHiz(), "SPEED", new Color(0, 200, 255));
            drawProgressBar(g2d, 40, 140, 340, 25, araba.getYakit(), 100, "FUEL", new Color(50, 255, 50));
            drawProgressBar(g2d, 40, 175, 340, 25, araba.getNitroMiktar(), 100, "NITRO", new Color(255, 150, 0));
            
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, 22));
            String hearts = "";
            for (int i = 0; i < araba.getCan(); i++) hearts += "❤ ";
            g2d.drawString(hearts, 40, 215);
        }
        
        private void drawProgressBar(Graphics2D g2d, int x, int y, int w, int h, int val, int max, String label, Color color) {
            g2d.setColor(new Color(30, 30, 30));
            g2d.fillRoundRect(x, y, w, h, 10, 10);
            
            GradientPaint gp = new GradientPaint(x, y, color.brighter(), x + w, y, color.darker());
            g2d.setPaint(gp);
            int fillWidth = (int)(w * val / (double)max);
            g2d.fillRoundRect(x, y, fillWidth, h, 10, 10);
            
            g2d.setColor(color.darker().darker());
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(x, y, w, h, 10, 10);
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.drawString(label + ": " + val + "/" + max, x + 10, y + 18);
        }
    }
    
    class Tree {
        int x, y;
        Tree(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    class Building {
        int x, y, width, height, type;
        Building(int x, int y, int type) {
            this.x = x;
            this.y = y;
            this.type = type;
            this.width = 80 + type * 20;
            this.height = 150 + type * 30;
        }
    }
    
    class Cloud {
        int x, y;
        Cloud(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    class Particle {
        int x, y, vx, vy, life, maxLife;
        Color color;
        
        Particle(int x, int y, Color color) {
            this.x = x;
            this.y = y;
            this.color = color;
            Random r = new Random();
            this.vx = r.nextInt(10) - 5;
            this.vy = r.nextInt(8) + 2;
            this.maxLife = this.life = 30 + r.nextInt(20);
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
            g2d.fillOval(x, y, 10, 10);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SuperCarGame());
    }
}
