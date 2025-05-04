package game.tiles;

import java.awt.image.BufferedImage;

public class Tile {
    private BufferedImage image;
    private TileType type;

    public Tile(BufferedImage image, TileType type) {
        this.image = image;
        this.type = type;
    }

    public BufferedImage getImage() {
        return image;
    }

    public TileType getType() {
        return type;
    }
}
