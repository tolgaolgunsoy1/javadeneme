import java.awt.*;

public class CarCustomization {
    private Color bodyColor;
    private Color windowColor;
    private boolean hasSpoiler;
    private boolean hasNeonLights;
    
    public CarCustomization() {
        bodyColor = new Color(255, 50, 0);
        windowColor = new Color(100, 200, 255, 180);
        hasSpoiler = false;
        hasNeonLights = false;
    }
    
    public void setBodyColor(Color color) {
        this.bodyColor = color;
    }
    
    public Color getBodyColor() {
        return bodyColor;
    }
    
    public Color getWindowColor() {
        return windowColor;
    }
    
    public void toggleSpoiler() {
        hasSpoiler = !hasSpoiler;
    }
    
    public void toggleNeonLights() {
        hasNeonLights = !hasNeonLights;
    }
    
    public boolean hasSpoiler() {
        return hasSpoiler;
    }
    
    public boolean hasNeonLights() {
        return hasNeonLights;
    }
    
    public static Color[] getPresetColors() {
        return new Color[] {
            new Color(255, 50, 0),    // Red
            new Color(0, 100, 255),   // Blue
            new Color(255, 215, 0),   // Gold
            new Color(50, 255, 50),   // Green
            new Color(148, 0, 211),   // Purple
            new Color(255, 255, 255), // White
            new Color(0, 0, 0)        // Black
        };
    }
}
