package frame.panels;

import controls.Button;
import controls.VolumeSlider;
import frame.MainFrame;
import frame.PanelType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseMotionListener;

public class SettingsPanel extends JPanel {

    private ArrayList<Button> buttons;
    private ArrayList<VolumeSlider> sliders;

    public SettingsPanel(MainFrame frame) {
        initializeButtons();

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                for (Button b : buttons) {
                    b.updateSelected(e);
                    repaint();
                }
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                for (VolumeSlider slider : sliders) {
                    slider.mouseDragged(e);
                    repaint();
                }
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
                for (Button b : buttons) {
                    if (b.isSelected(e)){
                        frame.switchPanel(b.getWhereToRelocate());
                    } else {
                        b.updateSelected(e);
                        repaint();
                    }
                }
                for (VolumeSlider slider : sliders) {
                    slider.mouseReleased(e);
                }
            }
            @Override public void mouseEntered(MouseEvent e) {}
            @Override public void mouseExited(MouseEvent e) {}
        });

    }

    private void initializeButtons() {
        buttons = new ArrayList<>();
        sliders = new ArrayList<>();

        buttons.add(new Button(0.15f,0.15f,0.75f,0.425f,"BACK", Color.DARK_GRAY, Color.LIGHT_GRAY,Color.WHITE, PanelType.MENU));

        sliders.add(new VolumeSlider(0.05f,0.25f,0.2f,0.05f,Color.BLACK,Color.ORANGE));

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (Button b : buttons) {
            b.render(g,getWidth(),getHeight());
        }
        for (VolumeSlider slider : sliders) {
            slider.render(g,getWidth(),getHeight());
        }
    }


}
