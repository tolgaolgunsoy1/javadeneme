import java.awt.*;

public class DayNightCycle {
    private double timeOfDay = 0.5;
    private boolean isNight = false;
    
    public void update() {
        timeOfDay += 0.001;
        if (timeOfDay >= 1.0) timeOfDay = 0.0;
        isNight = timeOfDay > 0.7 || timeOfDay < 0.3;
    }
    
    public boolean isNight() {
        return isNight;
    }
    
    public Color getSkyColor() {
        if (timeOfDay < 0.25) {
            return interpolateColor(new Color(10, 10, 30), new Color(135, 206, 250), timeOfDay * 4);
        } else if (timeOfDay < 0.5) {
            return new Color(135, 206, 250);
        } else if (timeOfDay < 0.75) {
            return interpolateColor(new Color(135, 206, 250), new Color(255, 140, 0), (timeOfDay - 0.5) * 4);
        } else {
            return interpolateColor(new Color(255, 140, 0), new Color(10, 10, 30), (timeOfDay - 0.75) * 4);
        }
    }
    
    public Color getGroundColor() {
        if (isNight) {
            return new Color(20, 80, 20);
        } else {
            return new Color(34, 139, 34);
        }
    }
    
    private Color interpolateColor(Color c1, Color c2, double ratio) {
        int r = (int)(c1.getRed() + (c2.getRed() - c1.getRed()) * ratio);
        int g = (int)(c1.getGreen() + (c2.getGreen() - c1.getGreen()) * ratio);
        int b = (int)(c1.getBlue() + (c2.getBlue() - c1.getBlue()) * ratio);
        return new Color(r, g, b);
    }
    
    public void drawSun(Graphics2D g2d, int width) {
        if (!isNight) {
            int sunY = (int)(100 + Math.sin(timeOfDay * Math.PI * 2) * 50);
            g2d.setColor(Color.YELLOW);
            g2d.fillOval(width - 150, sunY, 80, 80);
            g2d.setColor(new Color(255, 255, 0, 100));
            g2d.fillOval(width - 160, sunY - 10, 100, 100);
        }
    }
    
    public void drawMoon(Graphics2D g2d, int width) {
        if (isNight) {
            int moonY = (int)(100 - Math.sin(timeOfDay * Math.PI * 2) * 50);
            g2d.setColor(new Color(240, 240, 255));
            g2d.fillOval(width - 150, moonY, 70, 70);
            g2d.setColor(new Color(200, 200, 220));
            g2d.fillOval(width - 140, moonY + 10, 15, 15);
            g2d.fillOval(width - 125, moonY + 25, 10, 10);
        }
    }
}
