package game.tiles;

import java.awt.image.BufferedImage;

/**
 * this is class for single tile in a tile map
 */
public class Tile {
    private BufferedImage image;
    private TileType type;

    /**
     *
     * @param image which will the tile have
     * @param type this is type of the tile like solid or air
     */
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
