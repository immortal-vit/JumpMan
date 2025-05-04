package game.player;

import frame.panels.GamePanel;
import game.GameSettings;
import game.tiles.TileMap;

import java.awt.*;

/**
 * class for player
 * in this class there is everything about the player
 */
public class Player {

    /**
     * there I have some crucial variables which manages things like jump, jump rate, power of jump etc.
     */
    private float x, y;
    private final float WIDTH;
    private final float HEIGHT;
    private final float HITBOX_OFFSET_X = 12;
    private final float HITBOX_OFFSET_Y = 5;
    private Rectangle hitbox;

    private float velocityX = 0;
    private float velocityY = 0;
    private final float MAX_VELOCITY_Y = 15f;

    private boolean onGround = true;
    private boolean chargingJump = false;

    private float chargePower;
    private final float MAX_CHARGE = 16f;
    private final float CHARGE_RATE = 0.5f;
    private final float X_STRENGTH = 0.4f;

    private Direction direction = Direction.RIGHT;

    private TileMap tileMap;
    private PlayerPhysicsHandler physicsHandler;
    private PlayerControls playerControls;
    private PlayerRenderer renderer;

    private GamePanel gamePanel;
    private FloorChangeCallback floorChangeCallback;



    /**
     *
     * @param startX starting x point
     * @param startY  starting y point
     * @param size size of the player this should some multiple of tile size for resizing properly
     * @param tileMap tile map for checking collisions
     */
    public Player(float startX, float startY, float size, TileMap tileMap, GamePanel gamePanel) {
        this.x = startX;
        this.y = startY;
        this.WIDTH = size;
        this.HEIGHT = size;
        this.hitbox = new Rectangle((int) x, (int) y, (int) WIDTH, (int) HEIGHT);
        this.tileMap = tileMap;
        this.physicsHandler = new PlayerPhysicsHandler(this, tileMap);
        this.playerControls = new PlayerControls(this,5f);
        this.renderer = new PlayerRenderer();
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

            float force = Math.min(chargePower, MAX_CHARGE);

            velocityY = -force * 1.2f;
            switch (direction){
                case LEFT:
                    velocityX = -force * X_STRENGTH;
                    break;
                case RIGHT:
                    velocityX = force * X_STRENGTH;
                    break;
            }
        }
    }

    /**
     * this is the method for run in game panel
     * this is managing all things for the player that have to be checked and done
     */
    public void update() {
        if (GameSettings.getInstance().isDevModeOn()){
            setOnGround(false);
            physicsHandler.applyVelocity();
            physicsHandler.checkMovementForDevMode();
            updateHitbox();
        } else {
            manageCharging();
            physicsHandler.applyGravity();

            physicsHandler.handleCollisionAndMove();
            physicsHandler.checkScreenBorders();
            physicsHandler.applyVelocity();
            updateHitbox();
        }




    }

    /**
     * this checks based of the boolean if the player is charging it will slowly add power for the jump
     */
    private void manageCharging() {
        if (chargingJump) {
            chargePower += CHARGE_RATE;
            if (chargePower > MAX_CHARGE) chargePower = MAX_CHARGE;
        }
    }
    /**
     * will render the player
     * @param g graphics for rendering
     */
    public void render(Graphics2D g) {

        renderer.render(g,this);

    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    /**
     * this will move floor up
     */
    public void moveUp() {
        if (floorChangeCallback != null) {
            floorChangeCallback.onFloorChange(FloorDirection.UP);
        }
    }

    /**
     * this will move floor down
     */
    public void moveDown() {
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
        physicsHandler.setTileMap(tileMap);
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getHEIGHT() {
        return HEIGHT;
    }
    public void resetChargingJump() {
        chargePower = 0;
        chargingJump = false;
    }

    private float slowDownAfterCollision(){
        return Math.abs(velocityX) * 0.5f;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public float getWIDTH() {
        return WIDTH;
    }

    public float getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public float getMAX_VELOCITY_Y() {
        return MAX_VELOCITY_Y;
    }
    public void updateHitbox() {
            hitbox.setBounds(
                    (int) (x + HITBOX_OFFSET_X),
                    (int) (y + HITBOX_OFFSET_Y),
                    (int) (WIDTH - 2 * HITBOX_OFFSET_X),
                    (int) (HEIGHT - HITBOX_OFFSET_Y)
            );
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public float getHITBOX_OFFSET_X() {
        return HITBOX_OFFSET_X;
    }

    public float getHITBOX_OFFSET_Y() {
        return HITBOX_OFFSET_Y;
    }

    public boolean isChargingJump() {
        return chargingJump;
    }

    public PlayerControls getPlayerControls() {
        return playerControls;
    }
}