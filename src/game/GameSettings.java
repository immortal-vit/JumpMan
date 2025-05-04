package game;

public class GameSettings {
    private static final GameSettings INSTANCE = new GameSettings();

    private boolean showHitbox = false;
    private boolean isDevModeOn = false;

    private GameSettings() {}

    public static GameSettings getInstance() {
        return INSTANCE;
    }

    public boolean isShowHitbox() {
        return showHitbox;
    }

    public void setShowHitbox(boolean showHitbox) {
        this.showHitbox = showHitbox;
    }

    public boolean isDevModeOn() {
        return isDevModeOn;
    }

    public void setDevModeOn(boolean devModeOn) {
        isDevModeOn = devModeOn;
    }
}
