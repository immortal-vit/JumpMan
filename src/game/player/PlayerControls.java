package game.player;

import java.awt.event.KeyEvent;

/**
 * this is class for managing player controls
 * there are methods which will check which key is pressed and base of that it will call a method
 */
public class PlayerControls {
    private Player player;
    private float flySpeed;

    /**
     *
     * @param player player for which is it going to check settings
     * @param flySpeed this is for fly speed how fast the player will fly in a fly mode
     */
    public PlayerControls(Player player, float flySpeed) {
        this.player = player;
        this.flySpeed = flySpeed;
    }

    /**
     * this will check keys for normal mode
     * @param keyEvent key event for managing which key is pressed in a normal mode
     */
    public void normalSettingsPressed(KeyEvent keyEvent){
        switch (keyEvent.getKeyCode()){
            case KeyEvent.VK_A -> player.setDirection(Direction.LEFT);
            case KeyEvent.VK_D -> player.setDirection(Direction.RIGHT);
            case KeyEvent.VK_SPACE -> player.startCharging();
        }
    }
    /**
     * this will check keys for fly mode
     * @param keyEvent key event for managing which key is pressed in a fly mode
     */
    public void devSettingsPressed(KeyEvent keyEvent){
        switch (keyEvent.getKeyCode()){
            case KeyEvent.VK_W -> player.setVelocityY(-flySpeed);
            case KeyEvent.VK_S -> player.setVelocityY(flySpeed);
            case KeyEvent.VK_A -> player.setVelocityX(-flySpeed);
            case KeyEvent.VK_D -> player.setVelocityX(flySpeed);
            default -> {
                player.setVelocityX(0);
                player.setVelocityY(0);
            }

        }
    }

    /**
     * this will check releasing of keys in normal mode
     * @param keyEvent key event for managing which key is released in a normal mode
     */
    public void normalSettingsReleased(KeyEvent keyEvent){
        if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            player.stopChargingAndJump();
        }
    }
    /**
     * this will check releasing of keys in fly mode
     * @param keyEvent key event for managing which key is released in a fly mode
     */
    public void devSettingsReleased(KeyEvent keyEvent){
        switch (keyEvent.getKeyCode()){
            case KeyEvent.VK_W, KeyEvent.VK_S -> player.setVelocityY(0);
            case KeyEvent.VK_A, KeyEvent.VK_D -> player.setVelocityX(0);
        }
    }
}
