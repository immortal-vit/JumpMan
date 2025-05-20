package game.player;
import frame.PanelType;
import game.tiles.TileMap;

import java.awt.*;

public class PlayerPhysicsHandler {
    private final float GRAVITY = 0.8f;

    private final Player player;
    private TileMap tileMap;

    public PlayerPhysicsHandler(Player player, TileMap tileMap) {
        this.player = player;
        this.tileMap = tileMap;
    }

    /**
     * this will apply gravity
     * it will set the y velocity to velocity y + gravity
     * if the velocity y is faster than max velocity y it will set the max velocity y
     */
    public void applyGravity() {
        if (!player.isOnGround()) {
            player.setVelocityY(Math.min(player.getVelocityY() + GRAVITY, player.getMAX_VELOCITY_Y()));
        }
    }

    /**
     * this will apply velocities to x and y
     */
    public void applyVelocity() {
        player.setX(player.getX() + player.getVelocityX());
        player.setY(player.getY() + player.getVelocityY());
    }

    /**
     * this will handle collisions and based of that it will move
     * this will predict the next movement and based of the next movement it will change the final velocity
     */
    public void handleCollisionAndMove() {
        Rectangle hitbox = player.getHitbox();

        int nextX = (int) (player.getHitbox().x + player.getVelocityX());
        float topY = player.getHitbox().y;
        float bottomY = player.getHitbox().y + player.getHitbox().height - 1;

        checkCollisionLeft(nextX, topY, bottomY);
        checkCollisionRight(nextX, topY, bottomY,hitbox);



        int nextY = (int) (player.getHitbox().y + player.getVelocityY());
        float leftX = player.getHitbox().x;
        float rightX = player.getHitbox().x + player.getHitbox().width - 1;


        checkCollisionBelow(nextY,leftX,rightX, hitbox);
        checkCollisionAbove(nextY,leftX,rightX);

        if (player.isPlayerTouchVictoryPoint()){
            player.getGamePanel().triggerVictory();
        }

    }

    /**
     * this will check predicted collision bellow
     * @param nextY this is next y
     * @param leftX left x of a hit box
     * @param rightX right x of a hit box
     * @param hitbox player hit box
     */
    private void checkCollisionBelow(int nextY, float leftX, float rightX,Rectangle hitbox) {
        float feetY = nextY + hitbox.height;

        boolean solidLeft = isSolid(feetY, leftX);
        boolean solidRight = isSolid(feetY, rightX);


        if ((solidLeft || solidRight) && player.getVelocityY() >= 0) {
            player.setY(getTileRow(feetY) * tileMap.getTileSize() - player.getHEIGHT());
            player.setVelocityY(0);
            player.setVelocityX(0);
            player.setOnGround(true);
        } else {
            player.setOnGround(false);
        }
        checkForWin(feetY,leftX,rightX);

        if (isBelowMap()) {
            player.moveDown();
        }

    }

    /**
     * this will check predicted collision above
     * @param nextY this is next y
     * @param leftX left x of a hit box
     * @param rightX right x of a hit box
     */
    private void checkCollisionAbove(int nextY, float leftX, float rightX) {
        float headY = nextY;

        boolean solidLeft = isSolid(headY, leftX);
        boolean solidRight = isSolid(headY, rightX);


        if ((solidLeft || solidRight) && player.getVelocityY() < 0) {
            player.setY((getTileRow(headY) + 1) * tileMap.getTileSize() - player.getHITBOX_OFFSET_Y());
            player.setVelocityY(0);

            if (player.getVelocityX() > 0) player.setVelocityX(slowDownAfterCollision());
            else if (player.getVelocityX() < 0) player.setVelocityX(-slowDownAfterCollision());
        }
        checkForWin(headY,leftX,rightX);

        if (isOnMap()) {
            player.moveUp();
        }

    }

    /**
     * this will check predicted left collisions
     * @param newLeftX this is the predicted left x
     * @param topY player hit box top y
     * @param bottomY player hit box bottom y
     */
    private void checkCollisionLeft(int newLeftX, float topY, float bottomY) {
        if (player.getVelocityX() >= 0) return;

        boolean solidTop = isSolid(topY, newLeftX);
        boolean solidBottom = isSolid(bottomY - 1, newLeftX);

        if ((solidTop || solidBottom)) {
            int tileCol = getTileCol(newLeftX);
            float correctedX = (tileCol + 1) * tileMap.getTileSize() - player.getHITBOX_OFFSET_X();
            player.setX(correctedX);
            player.setVelocityX(slowDownAfterCollision());
        }
        checkForWin(topY,newLeftX,bottomY);
    }

    /**
     * this will check predicted right collisions
     * @param rightX this is the predicted right x
     * @param topY player hit box top y
     * @param bottomY player hit box bottom y
     */
    private void checkCollisionRight(int rightX, float topY, float bottomY, Rectangle hitbox) {
        if (player.getVelocityX() <= 0) return;

        float newRightX = hitbox.width + rightX;

        boolean solidTop = isSolid(topY, newRightX);
        boolean solidBottom = isSolid(bottomY - 1, newRightX);

        if ((solidTop || solidBottom)) {
            int tileCol = getTileCol(newRightX);
            float correctedX = tileCol * tileMap.getTileSize() - player.getWIDTH() + player.getHITBOX_OFFSET_X();
            player.setX(correctedX);
            player.setVelocityX(-slowDownAfterCollision());
        }
        checkForWin(topY,newRightX,bottomY);
    }

    /**
     * this will check screen borders for the player
     */
    public void checkScreenBorders() {
        if (player.getHitbox().x < 0) {
            player.setX(-player.getHITBOX_OFFSET_X());

            player.setVelocityX(slowDownAfterCollision());
        }
        if (player.getHitbox().x + player.getHitbox().width > tileMap.getCols() * tileMap.getTileSize()) {
            player.setX(tileMap.getCols() * tileMap.getTileSize() - player.getHitbox().width - player.getHITBOX_OFFSET_X());
            player.setVelocityX(-slowDownAfterCollision());
        }
    }

    /**
     * this will check movement if the player is in
     */
    public void checkMovementForDevMode(){
        if (isBelowMap()) {
            player.moveDown();
        }
        if (isOnMap()) {
            player.moveUp();
        }
        checkScreenBorders();
    }
    public void checkForWin(float NextYOrX, float position1, float position2) {
        boolean winPoint1 = isWin(NextYOrX, position1);
        boolean winPoint2 = isWin(NextYOrX, position2);

        if (winPoint1 || winPoint2) {
            player.setPlayerTouchVictoryPoint(true);
        }

    }

    /**
     * this will make player x velocity slower
     * @return new player x velocity
     * I am using it after the player hit something
     */
    private float slowDownAfterCollision(){
        return Math.abs(player.getVelocityX()) * 0.65f;
    }

    /**
     * is checking if the player is oon top map screen or not
     * @return bool if the player is on top of the map
     */
    private boolean isOnMap() {
        return player.getY() < 1;
    }

    /**
     * is checking if player is bellow map or not
     * @return bool if the player is bellow the map or not
     */
    private boolean isBelowMap() {
        return player.getY() + player.getHEIGHT() > tileMap.getRows() * tileMap.getTileSize();
    }
    public void setTileMap(TileMap tileMap) {
        this.tileMap = tileMap;
    }
    private int getTileRow(float y) {
        return (int)(y / tileMap.getTileSize());
    }

    private int getTileCol(float x) {
        return (int)(x / tileMap.getTileSize());
    }
    private boolean isSolid(float y, float x) {
        return tileMap.isSolid(getTileRow(y), getTileCol(x));
    }
    private boolean isWin(float y, float x) {
        return tileMap.isWin(getTileRow(y), getTileCol(x));
    }
}
