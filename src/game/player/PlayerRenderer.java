package game.player;

import game.GameSettings;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;

/**
 * this is class for rendering the player
 * it will select right image to render based of player state
 */
public class PlayerRenderer {

    private final EnumMap<PlayerState, BufferedImage> playerSprites = new EnumMap<>(PlayerState.class);

    /**
     * will load all sprites
     */
    public PlayerRenderer() {
        loadSprites();
    }

    /**
     * this will load all sprites and put them to enum map.
     * enum map is a special map balanced for enums
     */
    private void loadSprites(){
        try {
            playerSprites.put(PlayerState.IDLE, ImageIO.read(new File("src/game/player/sprites/playerStanding.png")));
            playerSprites.put(PlayerState.CHARGING, ImageIO.read(new File("src/game/player/sprites/playerCharging.png")));
            playerSprites.put(PlayerState.JUMPING, ImageIO.read(new File("src/game/player/sprites/playerJumping.png")));
            playerSprites.put(PlayerState.FALLING, ImageIO.read(new File("src/game/player/sprites/playerFalling.png")));
        } catch (IOException e) {
            System.out.println("something went wrong when loading images");
        }

    }

    /**
     *
     * @param g graphic for rendering
     * @param player player which this class will be rendering pictures for
     * this will set where the picture should appear and render it based of the state
     */
    public void render(Graphics2D g, Player player) {
        BufferedImage sprite = getSpriteForState(player);

        int x = Math.round(player.getX());
        int y = Math.round(player.getY());
        int width = Math.round(player.getWIDTH());
        int height = Math.round(player.getHEIGHT());

        if (GameSettings.getInstance().isDevModeOn()){
            sprite = playerSprites.get(PlayerState.IDLE);
        }

        if (player.getDirection() == Direction.LEFT) {
            sprite = flipImageHorizontally(sprite);
        }

        g.drawImage(sprite, x, y, width, height, null);

        drawHitbox(g, player);


    }

    /**
     * this will select right picture based of the player
     * @param player player from we will be getting state
     * @return image which will be rendered in the render method
     */
    private BufferedImage getSpriteForState(Player player) {

        if (player.isChargingJump()) return playerSprites.get(PlayerState.CHARGING);

        if (!player.isOnGround()) {
            return player.getVelocityY() < 0 ? playerSprites.get(PlayerState.JUMPING) : playerSprites.get(PlayerState.FALLING);
        }

        return playerSprites.get(PlayerState.IDLE);
    }

    /**
     * chatgpt helped me generate the flipping
     * this will flip the image horizontally based of the direction the player is looking
     * @param image which image will be flipped
     * @return flipped image
     */
    public BufferedImage flipImageHorizontally(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage flippedImage = new BufferedImage(w, h, image.getType());

        Graphics2D g = flippedImage.createGraphics();
        g.drawImage(image, 0, 0,w,h,w,0,0,h, null);
        g.dispose();
        return flippedImage;
    }

    /**
     * this is the method which will draw hit box based of the settings
     * @param g grpahics which will be used
     * @param player player where the hit box will be rendered
     */
    private void drawHitbox(Graphics2D g, Player player) {
        if (GameSettings.getInstance().isShowHitbox()){
            g.setColor(Color.BLUE);
            g.drawRect(player.getHitbox().x, player.getHitbox().y, player.getHitbox().width, player.getHitbox().height);
        }
    }
}
