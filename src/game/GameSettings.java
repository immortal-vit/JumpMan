package game;


import java.io.*;

/**
 * chatgpt helped me with the basics
 * this is singleton
 * it is static instance which is the only one in the whole game
 * this class is managing all settings
 */
public class GameSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    private static GameSettings instance;


    private int musicVolume = 50;
    private int soundEffectsVolume = 80;
    private boolean showHitbox = false;
    private boolean isDevModeOn = false;

    private static final String FILE_NAME = "settings.dat";

    private GameSettings() {}

    /**
     *
     * @return the instance
     */
    public static GameSettings getInstance() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    /**
     * this will serialize the instance
     */
    public void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(this);
        } catch (IOException e) {
            System.out.println("something went wrong when loading");
        }
    }

    /**
     * this will load settings
     * @return loaded instance from "settings.dat"
     */
    private static GameSettings load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (GameSettings) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new GameSettings();
        }
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


}
