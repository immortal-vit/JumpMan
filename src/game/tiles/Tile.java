package game.tiles;

import java.awt.image.BufferedImage;

public class Tile {
    private BufferedImage image;
    private boolean isSolid;

    public Tile(BufferedImage image, boolean isSolid) {
        this.image = image;
        this.isSolid = isSolid;
    }

    public BufferedImage getImage() {
        return image;
    }

    public boolean isSolid() {
        return isSolid;
    }
}
