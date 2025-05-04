package frame.panels;

import controls.RelocationButton;
import controls.CheckBox;
import controls.VolumeSlider;
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
    private ArrayList<VolumeSlider> sliders;

    private ArrayList<CheckBox> checkBoxes;

    private RelocationButton backButton;
    private VolumeSlider volumeSlider;
    private CheckBox hitboxCheckBox;
    private CheckBox devModCheckBox;

    private MainFrame frame;

    public SettingsPanel(MainFrame frame) {
        this.frame = frame;
        initializeComponents();

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
                for (VolumeSlider slider : sliders) {
                    slider.mouseDragged(e);

                }
                repaint();
            }
        });

        addMouseListener(new MouseListener() {

            @Override public void mouseClicked(MouseEvent e) {}
            @Override public void mousePressed(MouseEvent e) {
                for (VolumeSlider slider : sliders) {
                    slider.mousePressed(e);
                }
            }
            @Override public void mouseReleased(MouseEvent e) {
                for (RelocationButton b : buttons) {
                    if (b.isSelected(e)){
                        frame.switchPanel(b.getWhereToRelocate());
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
                for (VolumeSlider slider : sliders) {
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

        backButton = new RelocationButton(0.15f,0.15f,0.75f,0.425f,"BACK", Color.DARK_GRAY, Color.LIGHT_GRAY,Color.WHITE, PanelType.MENU);

        buttons.add(backButton);

        volumeSlider = new VolumeSlider(0.05f,0.25f,0.2f,0.05f,Color.BLACK,Color.ORANGE);

        sliders.add(volumeSlider);

        hitboxCheckBox = new CheckBox(0.7f,0.2f,0.1f,Color.DARK_GRAY,Color.RED,Color.GREEN, "show hitbox", Color.DARK_GRAY);
        devModCheckBox = new CheckBox(0.7f, 0.4f,0.1f,Color.DARK_GRAY,Color.RED,Color.GREEN, "enable dev mode", Color.DARK_GRAY);

        checkBoxes.add(hitboxCheckBox);
        checkBoxes.add(devModCheckBox);

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (RelocationButton b : buttons) {
            b.render(g,getWidth(),getHeight());
        }
        for (VolumeSlider slider : sliders) {
            slider.render(g,getWidth(),getHeight());
        }
        for (CheckBox c : checkBoxes) {
            c.render(g,getWidth(),getHeight());
        }
    }

    private void updateGameSettings(){
        GameSettings.getInstance().setShowHitbox(hitboxCheckBox.isChecked());
        GameSettings.getInstance().setDevModeOn(devModCheckBox.isChecked());
    }
    public void updateRelocation(){
        if (frame.getLastPanelType() == PanelType.PAUSE){
            backButton.setWhereToRelocate(PanelType.PAUSE);
        } else {
            backButton.setWhereToRelocate(PanelType.MENU);
        }
    }


}
