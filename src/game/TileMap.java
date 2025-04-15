package game;

import game.tiles.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class TileMap {
    private Tile[][] tiles;
    private float tileSize;
    private int rows, cols;

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
                    int type = Integer.parseInt(line[col]);

                    BufferedImage img = null;
                    boolean isSolid = false;

                    switch (type) {
                        case 0:
                            img = ImageIO.read(new File("src/game/tiles/images/air.png"));
                            break;
                        case 1:
                            img = ImageIO.read(new File("src/game/tiles/images/groundBlock.png"));
                            isSolid = true;
                            break;
                    }

                    tiles[row][col] = new Tile(img, isSolid);
                }
            }
            br.close();
        }catch (Exception e) {
            System.out.println("nepodarilo se nacist mapu");
        }
    }

    public void draw(Graphics2D g ) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Tile tile = tiles[row][col];

                if (tile!= null){
                    float x = col * tileSize;
                    float y = row * tileSize;

                    g.setColor(Color.GREEN);
                    g.drawRect((int) (col * tileSize), (int) (row * tileSize), (int) tileSize, (int) tileSize);
                    g.drawImage(tile.getImage(), Math.round(x), Math.round(y), Math.round(tileSize), Math.round(tileSize), null);
                }

            }
        }


    }
    public boolean isSolid(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) return false;
        return tiles[row][col] != null && tiles[row][col].isSolid();
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
