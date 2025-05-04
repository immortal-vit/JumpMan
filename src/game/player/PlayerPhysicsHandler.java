package game.player;
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

    public void applyGravity() {
        if (!player.isOnGround()) {
            player.setVelocityY(Math.min(player.getVelocityY() + GRAVITY, player.getMAX_VELOCITY_Y()));
        }
    }

    public void applyVelocity() {
        player.setX(player.getX() + player.getVelocityX());
        player.setY(player.getY() + player.getVelocityY());
    }
    public void handleCollisionAndMove() {
        Rectangle hitbox = player.getHitbox();

        int nextX = (int) (player.getHitbox().x + player.getVelocityX());
        float topY = player.getHitbox().y;
        float bottomY = player.getHitbox().y + player.getHitbox().height;

        checkCollisionLeft(nextX, topY, bottomY,hitbox);
        checkCollisionRight(nextX, topY, bottomY,hitbox);



        int nextY = (int) (player.getHitbox().y + player.getVelocityY());
        float leftX = player.getHitbox().x;
        float rightX = player.getHitbox().x + player.getHitbox().width - 1;


        checkCollisionBelow(nextY,leftX,rightX, hitbox);
        checkCollisionAbove(nextY,leftX,rightX);


    }

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

    private void checkCollisionLeft(int newLeftX, float topY, float bottomY, Rectangle hitbox) {
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


    private void checkCollisionRight(int nextX, float topY, float bottomY, Rectangle hitbox) {
        if (player.getVelocityX() <= 0) return;

        float newRightX = hitbox.width + nextX;

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
            System.out.println("you won");
        }

    }

    private float slowDownAfterCollision(){
        return Math.abs(player.getVelocityX()) * 0.5f;
    }
    private boolean isOnMap() {
        return player.getY() < 1;
    }
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
