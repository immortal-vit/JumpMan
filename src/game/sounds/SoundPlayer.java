package game.sounds;

import game.GameSettings;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Objects;

/**
 * this is the class for managing the sound
 */
public class SoundPlayer {
    private Clip musicClip;

    /**
     * will play the music indefinitely until the game shut down
     * @param fileName file from which will be the music played
     */
    public void playMusic(String fileName) {
        stopMusic();

        try {
            InputStream bufferedIn = new BufferedInputStream(Objects.requireNonNull(getClass().getResourceAsStream(fileName)));
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);
            musicClip = AudioSystem.getClip();
            musicClip.open(audioInputStream);

            FloatControl gainControl = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
            setVolume(gainControl, GameSettings.getInstance().getMusicVolume());

            musicClip.loop(Clip.LOOP_CONTINUOUSLY);

        }catch (Exception e) {
            System.out.println("something went wrong when playing music");
        }
    }

    /**
     * will stop music
     */
    public void stopMusic() {
        if (musicClip != null && musicClip.isRunning()) {
            musicClip.stop();
            musicClip.close();
        }
    }

    /**
     * this will update music based of the settings
     */
    public void updateMusicVolume() {
        if (musicClip != null) {
            try {
                FloatControl gainControl = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
                setVolume(gainControl, GameSettings.getInstance().getMusicVolume());
            } catch (Exception e) {
                System.out.println("Could not update music volume");
            }
        }
    }

    /**
     * this will play sound effect
     * @param path file from which the sound effect will be played
     */
    public void playSoundEffect(String path) {
        new Thread(() -> {
            try {
                InputStream bufferedIn = new BufferedInputStream(Objects.requireNonNull(getClass().getResourceAsStream(path)));
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);

                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                setVolume(gainControl, GameSettings.getInstance().getSoundEffectsVolume());

                clip.start();
            } catch (Exception e) {
                System.out.println("Error playing sound effect: " + e.getMessage());
            }
        }).start();
    }

    /**
     * chatgpt helped me with this
     * @param gainControl
     * @param volumePercent
     */
    private void setVolume(FloatControl gainControl, int volumePercent) {
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volumePercent / 100f) + gainControl.getMinimum();
        gainControl.setValue(gain);
    }
}
