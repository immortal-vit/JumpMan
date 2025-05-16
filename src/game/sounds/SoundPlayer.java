package game.sounds;

import game.GameSettings;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

public class SoundPlayer {
    private Clip musicClip;

    public void playMusic(String fileName) {
        stopMusic();

        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(fileName));
            musicClip = AudioSystem.getClip();
            musicClip.open(audioInputStream);

            FloatControl gainControl = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
            setVolume(gainControl, GameSettings.getInstance().getMusicVolume());

            musicClip.loop(Clip.LOOP_CONTINUOUSLY);

        }catch (Exception e) {
            System.out.println("something went wrong when playing music");
        }
    }

    public void stopMusic() {
        if (musicClip != null && musicClip.isRunning()) {
            musicClip.stop();
            musicClip.close();
        }
    }

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

    public void playSoundEffect(String path) {
        new Thread(() -> {
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path));
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

    private void setVolume(FloatControl gainControl, int volumePercent) {
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volumePercent / 100f) + gainControl.getMinimum();
        gainControl.setValue(gain);
    }
}
