import java.awt.*;
import java.util.Random;

public class WeatherSystem {
    private WeatherType currentWeather;
    private Random random = new Random();
    
    public enum WeatherType {
        SUNNY, RAINY, SNOWY, FOGGY
    }
    
    public WeatherSystem() {
        currentWeather = WeatherType.SUNNY;
    }
    
    public void changeWeather() {
        currentWeather = WeatherType.values()[random.nextInt(4)];
    }
    
    public WeatherType getCurrentWeather() {
        return currentWeather;
    }
    
    public void drawWeather(Graphics2D g2d, int width, int height, int offset) {
        switch (currentWeather) {
            case RAINY:
                drawRain(g2d, width, height, offset);
                break;
            case SNOWY:
                drawSnow(g2d, width, height, offset);
                break;
            case FOGGY:
                drawFog(g2d, width, height);
                break;
        }
    }
    
    private void drawRain(Graphics2D g2d, int width, int height, int offset) {
        g2d.setColor(new Color(100, 100, 255, 150));
        g2d.setStroke(new BasicStroke(2));
        for (int i = 0; i < 200; i++) {
            int x = random.nextInt(width);
            int y = (random.nextInt(height) + offset) % height;
            g2d.drawLine(x, y, x - 5, y + 20);
        }
    }
    
    private void drawSnow(Graphics2D g2d, int width, int height, int offset) {
        g2d.setColor(Color.WHITE);
        for (int i = 0; i < 150; i++) {
            int x = random.nextInt(width);
            int y = (random.nextInt(height) + offset / 2) % height;
            g2d.fillOval(x, y, 5, 5);
        }
    }
    
    private void drawFog(Graphics2D g2d, int width, int height) {
        g2d.setColor(new Color(200, 200, 200, 100));
        g2d.fillRect(0, 0, width, height);
    }
    
    public double getSpeedModifier() {
        switch (currentWeather) {
            case RAINY: return 0.8;
            case SNOWY: return 0.6;
            case FOGGY: return 0.9;
            default: return 1.0;
        }
    }
}
