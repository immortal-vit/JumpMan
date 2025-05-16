package game;


import java.io.*;

public class GameSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    private static GameSettings instance;


    private int musicVolume = 50;
    private int soundEffectsVolume = 80;
    private boolean showHitbox = false;
    private boolean isDevModeOn = false;

    private static final String FILE_NAME = "settings.dat";

    private GameSettings() {}

    public static GameSettings getInstance() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    public int getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(int musicVolume) {
        this.musicVolume = musicVolume;
    }

    public int getSoundEffectsVolume() {
        return soundEffectsVolume;
    }

    public void setSoundEffectsVolume(int soundEffectsVolume) {
        this.soundEffectsVolume = soundEffectsVolume;
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
        this.isDevModeOn = devModeOn;
    }

    public void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(this);
        } catch (IOException e) {
            System.out.println("something went wrong when loading");
        }
    }

    private static GameSettings load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (GameSettings) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new GameSettings();
        }
    }
}
