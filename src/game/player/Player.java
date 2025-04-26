package game.player;

import frame.panels.GamePanel;
import game.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * class for player
 * in this class there is everything about the player
 */
public class Player {

    /**
     * there I have some crucial variables which manages things like jump, jump rate, power of jump etc.
     */
    private float x, y;
    private final float width;
    private final float height;

    private float velocityX = 0;
    private float velocityY = 0;

    private int floor = 0;

    private boolean onGround = true;

    private boolean chargingJump = false;
    private float chargePower;
    private final float maxCharge = 16f;
    private final float chargeRate = 0.5f;
    private final float xStrength = 0.4f;

    private Direction direction = Direction.RIGHT;

    private final float gravity = 0.8f;

    private BufferedImage sprite;

    private TileMap tileMap;
    private GamePanel gamePanel;
    private FloorChangeCallback floorChangeCallback;


    /**
     *
     * @param startX starting x point
     * @param startY  starting y point
     * @param size size of the player this should some multiple of tile size for resizing properly
     * @param sprite there wew can add image which we will be using
     * @param tileMap tile map for checking collisions
     */
    public Player(float startX, float startY, float size, BufferedImage sprite, TileMap tileMap, GamePanel gamePanel) {
        this.x = startX;
        this.y = startY;
        this.width = size;
        this.height = size;
        this.sprite = sprite;
        this.tileMap = tileMap;
        this.gamePanel = gamePanel;
    }

    /**
     * this will check if the player can start charging
     */
    public void startCharging() {
        if (onGround && !chargingJump) {
            chargingJump = true;
            chargePower = 0;
        }
    }

    /**
     * stop charging and will launch our player into the air
     * will check direction and based of it, it will send the player either left,right or middle
     */
    public void stopChargingAndJump() {
        if (chargingJump) {
            chargingJump = false;
            onGround = false;

            float force = Math.min(chargePower, maxCharge);

            velocityY = -force * 1.2f;
            switch (direction){
                case LEFT:
                    velocityX = -force * xStrength;
                    break;
                case RIGHT:
                    velocityX = force * xStrength;
                    break;
            }
        }
    }

    /**
     * this is the method for run in game panel
     * this is managing all things for the player that habe to be checked
     */
    public void update() {
       manageCharging();
       applyGravity();
       applyVelocity();

       checkCollisionBelow();
       checkCollisionAbove();

       checkCollisionLeft();
       checkCollisionRight();

       checkScreenBorders();

    }

    /**
     * this checks based of the boolean if the player is charging it will slowly add power for the jump
     */
    private void manageCharging() {
        if (chargingJump) {
            chargePower += chargeRate;
            if (chargePower > maxCharge) chargePower = maxCharge;
        }
    }

    /**
     * method for falling
     * if the player is not touching ground he will fall
     */
    private void applyGravity() {
        if (!onGround) {
            velocityY += gravity;
        }
    }

    /**
     * this will change the location based of the velocity
     */
    private void applyVelocity() {
        x += velocityX;
        y += velocityY;
    }

    /**
     * will create two hit box on
     * each corner below
     * the rowBelow, colLeft, colRight is int, and we will later check the tiles int the tile map thanks to it
     * if we are standing on a ground we will set velocityX and velocityY to zero
     */

    private void checkCollisionBelow() {
        float leftFootX = x + width * 0.1f;
        float rightFootX = x + width * 0.9f;
        float feetY = y + height;

        int rowBelow = (int)(feetY / tileMap.getTileSize());
        int colLeft = (int)(leftFootX / tileMap.getTileSize());
        int colRight = (int)(rightFootX / tileMap.getTileSize());

        boolean solidLeft = tileMap.isSolid(rowBelow, colLeft);
        boolean solidRight = tileMap.isSolid(rowBelow, colRight);

        if ((solidLeft || solidRight) && velocityY >= 0) {
            y = rowBelow * tileMap.getTileSize() - height;
            velocityY = 0;
            velocityX = 0;
            onGround = true;
        } else {
            onGround = false;
        }
        if (isBelowMap()){
            moveDown();
        }
    }

    /**
     * this is the same as the last method but for above for the player
     * will stop tha y for moving up
     * also will check if player touches the top of the map if yes it will call moveUp
     */
    private void checkCollisionAbove() {
        float leftHeadX = x + width * 0.1f;
        float rightHeadX = x + width * 0.9f;
        float headY = y;

        int rowAbove = (int)(headY / tileMap.getTileSize());
        int colLeft = (int)(leftHeadX / tileMap.getTileSize());
        int colRight = (int)(rightHeadX / tileMap.getTileSize());

        boolean solidLeft = tileMap.isSolid(rowAbove, colLeft);
        boolean solidRight = tileMap.isSolid(rowAbove, colRight);

        if ((solidLeft || solidRight) && velocityY < 0) {
            y = (rowAbove + 1) * tileMap.getTileSize();
            velocityY = 0;
        }
        if (isOnMap()) { // hrubá detekce nárazu do horního okraje
            moveUp();
        }
    }

    /**
     * same as last methods but for left side
     * will bounce the player to right side if it detects collision
     * also will check if player touches the bottom of the map if yes it will call moveDown
     */
    private void checkCollisionLeft() {
        float leftX = x;
        float topY = y + 5;
        float bottomY = y + height - 1;

        int colLeft = (int)(leftX / tileMap.getTileSize());
        int rowTop = (int)(topY / tileMap.getTileSize());
        int rowBottom = (int)(bottomY / tileMap.getTileSize());

        boolean solidTop = tileMap.isSolid(rowTop, colLeft);
        boolean solidBottom = tileMap.isSolid(rowBottom, colLeft);

        if (solidTop || solidBottom) {
            x = (colLeft + 1) * tileMap.getTileSize();
            velocityX = -velocityX * 0.5f;
        }
    }

    /**
     * same as last methods but for right side
     * will bounce the player to left side if it detects collision
     */
    private void checkCollisionRight() {
        float rightX = x + width;
        float topY = y + 5;
        float bottomY = y + height - 1;

        int colRight = (int)(rightX / tileMap.getTileSize());
        int rowTop = (int)(topY / tileMap.getTileSize());
        int rowBottom = (int)(bottomY / tileMap.getTileSize());

        boolean solidTop = tileMap.isSolid(rowTop, colRight);
        boolean solidBottom = tileMap.isSolid(rowBottom, colRight);

        if (solidTop || solidBottom) {
            x = colRight * tileMap.getTileSize() - width;
            velocityX = -velocityX * 0.5f;
        }
    }

    /**
     * this wil check screen borders and if the player  touch the border it will bounce him of
     */
    private void checkScreenBorders() {
        if (x < 0) {
            x = 0;
            velocityX = Math.abs(velocityX);
        }
        if (x + width > tileMap.getCols() * tileMap.getTileSize()) {
            x = tileMap.getCols() * tileMap.getTileSize() - width;
            velocityX = -Math.abs(velocityX);
        }
    }

    /**
     * will render the player
     * @param g graphics for rendering
     */
    public void render(Graphics2D g) {
        g.setColor(Color.RED);
        g.drawRect(Math.round(x), Math.round(y), Math.round(width), Math.round(height));

        g.drawImage(sprite, Math.round(x) , Math.round(y),Math.round(width),Math.round(height), null);

    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * this will move floor up
     */
    private void moveUp() {
        if (floorChangeCallback != null) {
            floorChangeCallback.onFloorChange(FloorDirection.UP);
        }
    }

    /**
     * this will move floor down
     */
    private void moveDown() {
        if (floorChangeCallback != null) {
            floorChangeCallback.onFloorChange(FloorDirection.DOWN);
        }
    }

    /**
     * because of this the runnable will know that he has something to do
     * @param floorChangeCallback this will call callback for runnable in game panel
     *                            chatGPT helped me with this
     */
    public void setFloorChangeCallback(FloorChangeCallback floorChangeCallback) {
        this.floorChangeCallback = floorChangeCallback;
    }

    public void setTileMap(TileMap tileMap) {
        this.tileMap = tileMap;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getHeight() {
        return height;
    }
    private boolean isBelowMap() {
        return y + height > tileMap.getRows() * tileMap.getTileSize();
    }
    private boolean isOnMap() {
        return y < 1;
    }

    public void resetChargingJump() {
        chargePower = 0;
        chargingJump = false;
    }
}
