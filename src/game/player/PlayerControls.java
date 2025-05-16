package game.player;

import game.GameSettings;

import java.awt.event.KeyEvent;

public class PlayerControls {
    private Player player;
    private float devModeSpeed;

    public PlayerControls(Player player, float devModeSpeed) {
        this.player = player;
        this.devModeSpeed = devModeSpeed;
    }


    public void normalSettingsPressed(KeyEvent keyEvent){
        switch (keyEvent.getKeyCode()){
            case KeyEvent.VK_A -> player.setDirection(Direction.LEFT);
            case KeyEvent.VK_D -> player.setDirection(Direction.RIGHT);
            case KeyEvent.VK_SPACE -> player.startCharging();
        }
    }
    public void devSettingsPressed(KeyEvent keyEvent){
        switch (keyEvent.getKeyCode()){
            case KeyEvent.VK_W -> player.setVelocityY(-devModeSpeed);
            case KeyEvent.VK_S -> player.setVelocityY(devModeSpeed);
            case KeyEvent.VK_A -> player.setVelocityX(-devModeSpeed);
            case KeyEvent.VK_D -> player.setVelocityX(devModeSpeed);
            default -> {
                player.setVelocityX(0);
                player.setVelocityY(0);
            }

        }
    }
    public void normalSettingsReleased(KeyEvent keyEvent){
        if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            player.stopChargingAndJump();
        }
    }
    public void devSettingsReleased(KeyEvent keyEvent){
        switch (keyEvent.getKeyCode()){
            case KeyEvent.VK_W, KeyEvent.VK_S -> player.setVelocityY(0);
            case KeyEvent.VK_A, KeyEvent.VK_D -> player.setVelocityX(0);
        }
    }
}
