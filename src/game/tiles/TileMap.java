package game.tiles;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class TileMap {
    private Tile[][] tiles;
    private float tileSize;
    private int rows, cols;

    private static final int TILE_COUNT = 20;
    private static Tile[] tileTypes = new Tile[TILE_COUNT];

    /**
     * chatgpt helped me with this.
     * when we will load objects from a tilemap it will just load this preloaded objects instead of loading it every time when tile map changes
     * it will load it to an array of tiles
     * so its faster and it saves memory
     */
    static {
        try {
            tileTypes[0] = new Tile(ImageIO.read(new File("src/game/tiles/images/air.png")), TileType.AIR);
            tileTypes[1] = new Tile(ImageIO.read(new File("src/game/tiles/images/grassBlock.png")), TileType.SOLID);
            tileTypes[9] = new Tile(ImageIO.read(new File("src/game/tiles/images/grassBlock.png")), TileType.WIN);

        } catch (Exception e) {
            System.out.println("error when loading blocks");
        }
    }

    public TileMap(String fileName, float titleSize) {
        this.tileSize = titleSize;
        loadMap(fileName);
    }


    private void loadMap(String fileName) {
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            rows = Integer.parseInt(br.readLine());
            cols = Integer.parseInt(br.readLine());

            tiles = new Tile[rows][cols];

            for (int row = 0; row < rows; row++) {
                String[] line = br.readLine().split(" ");
                for (int col = 0; col < cols; col++) {
                    String type = String.valueOf(Integer.parseInt(line[col]));

                    tiles[row][col] = tileTypes[Integer.parseInt(type)];

                }
            }
        }catch (Exception e) {
            System.out.println("error when loading a map");
        }
    }

    public void draw(Graphics2D g ) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Tile tile = tiles[row][col];

                if (tile!= null){
                    float x = col * tileSize;
                    float y = row * tileSize;

                    g.drawImage(tile.getImage(), Math.round(x), Math.round(y), Math.round(tileSize), Math.round(tileSize), null);
                }

            }
        }


    }
    public boolean isSolid(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) return false;
        return tiles[row][col] != null && tiles[row][col].getType() == TileType.SOLID;
    }
    public boolean isWin(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) return false;
        return tiles[row][col] != null && tiles[row][col].getType() == TileType.WIN;
    }

    public float getTileSize() {
        return tileSize;
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }
}
