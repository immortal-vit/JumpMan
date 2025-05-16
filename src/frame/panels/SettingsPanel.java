package frame.panels;

import controls.RelocationButton;
import controls.CheckBox;
import controls.Slider;
import frame.MainFrame;
import frame.PanelType;
import game.GameSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

public class SettingsPanel extends JPanel {


    private ArrayList<RelocationButton> buttons;
    private ArrayList<Slider> sliders;

    private ArrayList<CheckBox> checkBoxes;

    private RelocationButton backButton;
    private Slider volumeSlider;
    private Slider soundEffectSlider;
    private CheckBox hitboxCheckBox;
    private CheckBox devModCheckBox;
    private Image tutorialImage;
    private Image tutorialImageFly;

    private MainFrame frame;

    public SettingsPanel(MainFrame frame) {
        this.frame = frame;
        setBackground(Color.GRAY);
        this.setOpaque(true);
        initializeComponents();
        loadPictures();


        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                for (RelocationButton b : buttons) {
                    b.updateSelected(e);
                }
                for (CheckBox c : checkBoxes) {
                    c.updateHover(e);
                }
                repaint();
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                for (Slider slider : sliders) {
                    slider.mouseDragged(e);
                    frame.setMusicVolume(volumeSlider.getValue());
                }
                repaint();
            }
        });

        addMouseListener(new MouseListener() {

            @Override public void mouseClicked(MouseEvent e) {}
            @Override public void mousePressed(MouseEvent e) {
                for (Slider slider : sliders) {
                    slider.mousePressed(e);
                }
            }
            @Override public void mouseReleased(MouseEvent e) {
                for (RelocationButton b : buttons) {
                    if (b.isSelected(e)){
                        frame.switchPanel(b.getWhereToRelocate());
                        GameSettings.getInstance().save();
                    } else {
                        b.updateSelected(e);
                    }
                }
                for (CheckBox c : checkBoxes) {
                    if (c.isHovered()){
                        c.mousePressed(e);
                    } else {
                        c.updateHover(e);
                    }

                }
                for (Slider slider : sliders) {
                    slider.mouseReleased(e);
                }

                updateGameSettings();
                repaint();
            }
            @Override public void mouseEntered(MouseEvent e) {}
            @Override public void mouseExited(MouseEvent e) {}
        });

    }

    private void initializeComponents() {
        buttons = new ArrayList<>();
        sliders = new ArrayList<>();
        checkBoxes = new ArrayList<>();

        backButton = new RelocationButton(0.15f,0.15f,0.75f,0.425f,"BACK", Color.DARK_GRAY,Color.WHITE, PanelType.MENU);

        buttons.add(backButton);

        volumeSlider = new Slider(0.05f, 0.25f, 0.2f, 0.05f, Color.BLACK, Color.RED, "MUSIC");
        soundEffectSlider = new Slider(0.05f, 0.45f, 0.2f, 0.05f, Color.BLACK, Color.RED, "SOUND EFFECTS");

        sliders.add(volumeSlider);
        sliders.add(soundEffectSlider);

        hitboxCheckBox = new CheckBox(0.7f,0.2f,0.1f,Color.DARK_GRAY,Color.RED,Color.GREEN, "SHOW HITBOX", Color.DARK_GRAY);
        devModCheckBox = new CheckBox(0.7f, 0.4f,0.1f,Color.DARK_GRAY,Color.RED,Color.GREEN, "FLY MODE", Color.DARK_GRAY);

        checkBoxes.add(hitboxCheckBox);
        checkBoxes.add(devModCheckBox);

        loadSettings();

    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        paintPictures(g);

        for (RelocationButton b : buttons) {
            b.render(g,getWidth(),getHeight());
        }
        for (Slider slider : sliders) {
            slider.render(g,getWidth(),getHeight());
        }
        for (CheckBox c : checkBoxes) {
            c.render(g,getWidth(),getHeight());
        }

    }

    private void updateGameSettings(){
        GameSettings.getInstance().setShowHitbox(hitboxCheckBox.isChecked());
        GameSettings.getInstance().setDevModeOn(devModCheckBox.isChecked());
        GameSettings.getInstance().setMusicVolume(volumeSlider.getValue());
        GameSettings.getInstance().setSoundEffectsVolume(soundEffectSlider.getValue());
    }
    public void updateRelocation(){
        if (frame.getLastPanelType() == PanelType.PAUSE){
            backButton.setWhereToRelocate(PanelType.PAUSE);
        } else {
            backButton.setWhereToRelocate(PanelType.MENU);
        }
    }
    private void loadPictures(){
        tutorialImage = new ImageIcon("src/frame/tutorialPics/jumpKingTutorial.png").getImage();
        tutorialImageFly = new ImageIcon("src/frame/tutorialPics/jumpKingFlyTutorial.png").getImage();
    }
    private void paintPictures(Graphics g){
        int imageWidth = getWidth() / 4;
        int imageHeight = getHeight() / 4;

        g.drawImage(tutorialImage, getWidth() - imageWidth/2 - getWidth()/2, 50, imageWidth, imageHeight, this);
        g.drawImage(tutorialImageFly, getWidth() - imageWidth/2 - getWidth()/2, 100 + imageHeight, imageWidth, imageHeight, this);
    }

    public void loadSettings() {
        GameSettings settings = GameSettings.getInstance();
        hitboxCheckBox.setChecked(settings.isShowHitbox());
        devModCheckBox.setChecked(settings.isDevModeOn());
        volumeSlider.setValue(settings.getMusicVolume());
        soundEffectSlider.setValue(settings.getSoundEffectsVolume());
    }


}
