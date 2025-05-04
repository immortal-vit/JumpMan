package game.player;

import game.GameSettings;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;

public class PlayerRenderer {

    private final EnumMap<PlayerState, BufferedImage> playerSprites = new EnumMap<>(PlayerState.class);

    public PlayerRenderer() {
        loadSprites();
    }

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

    private BufferedImage getSpriteForState(Player player) {

        if (player.isChargingJump()) return playerSprites.get(PlayerState.CHARGING);

        if (!player.isOnGround()) {
            return player.getVelocityY() < 0 ? playerSprites.get(PlayerState.JUMPING) : playerSprites.get(PlayerState.FALLING);
        }

        return playerSprites.get(PlayerState.IDLE);
    }

    public BufferedImage flipImageHorizontally(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage flippedImage = new BufferedImage(w, h, image.getType());

        Graphics2D g = flippedImage.createGraphics();
        g.drawImage(image, 0, 0,w,h,w,0,0,h, null);
        g.dispose();
        return flippedImage;
    }

    private void drawHitbox(Graphics2D g, Player player) {
        if (GameSettings.getInstance().isShowHitbox()){
            g.setColor(Color.BLUE);
            g.drawRect(player.getHitbox().x, player.getHitbox().y, player.getHitbox().width, player.getHitbox().height);
        }
    }
}
