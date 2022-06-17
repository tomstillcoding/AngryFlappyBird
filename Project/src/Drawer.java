import java.awt.*;
import java.awt.geom.AffineTransform;

public class Drawer {
    public int x;
    public int y;
    public Image image;
    public AffineTransform transform;

    public Drawer() {
    }

    public Drawer(int x, int y, String imagePath) {
        this.x = x;
        this.y = y;
        this.image = ImageLoader.loadImage(imagePath);
    }
}
