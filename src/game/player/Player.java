package game.player;

import game.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player {

    private float x, y;
    private float width, height;

    private float velocityX = 0;
    private float velocityY = 0;

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

    public Player(float startX, float startY, float size, BufferedImage sprite, TileMap tileMap) {
        this.x = startX;
        this.y = startY;
        this.width = size;
        this.height = size;
        this.sprite = sprite;
        this.tileMap = tileMap;
    }
    public void startCharging() {
        if (onGround && !chargingJump) {
            chargingJump = true;
            chargePower = 0;
        }
    }
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
                case STANDING:
                    velocityX = 0;
                    break;
            }
        }
    }
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

    private void manageCharging() {
        if (chargingJump) {
            chargePower += chargeRate;
            if (chargePower > maxCharge) chargePower = maxCharge;
        }
    }
    private void applyGravity() {
        if (!onGround) {
            velocityY += gravity;
        }
    }

    private void applyVelocity() {
        x += velocityX;
        y += velocityY;
    }

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
    }

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
    }

    private void checkCollisionLeft() {
        float leftX = x;
        float topY = y + 2;
        float bottomY = y + height - 2;

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

    private void checkCollisionRight() {
        float rightX = x + width;
        float topY = y + 2;
        float bottomY = y + height - 2;

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

    public void render(Graphics2D g) {
        g.setColor(Color.RED);
        g.drawRect(Math.round(x), Math.round(y), Math.round(width), Math.round(height));

        g.drawImage(sprite, Math.round(x) , Math.round(y),Math.round(width),Math.round(height), null);

    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
