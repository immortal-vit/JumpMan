package frame.panels;

import controls.RelocationButton;
import frame.MainFrame;
import frame.PanelType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

public class MenuPanel extends JPanel {

    RelocationButton startButton;
    RelocationButton settingsButton;
    RelocationButton quitButton;
    ArrayList<RelocationButton> buttons;

    public MenuPanel(MainFrame frame) {
        initializeButtons();

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                for (RelocationButton b : buttons) {
                    b.updateSelected(e);
                    repaint();
                }
            }
        });
        addMouseListener(new MouseListener() {

            @Override public void mouseClicked(MouseEvent e) {}
            @Override public void mousePressed(MouseEvent e) {}
            @Override public void mouseReleased(MouseEvent e) {
                for (RelocationButton b : buttons) {
                    if (b.isSelected(e)){
                        frame.switchPanel(b.getWhereToRelocate());
                    } else {
                        b.updateSelected(e);
                        repaint();
                    }
                }
            }
            @Override public void mouseEntered(MouseEvent e) {}
            @Override public void mouseExited(MouseEvent e) {}
        });

    }

    private void initializeButtons() {
        buttons = new ArrayList<>();

        buttons.add(startButton = new RelocationButton(0.15f,0.15f,0.225f,0.1f,"START", Color.DARK_GRAY, Color.LIGHT_GRAY,Color.GREEN, PanelType.GAME ));
        buttons.add(settingsButton = new RelocationButton(0.15f,0.15f,0.425f,0.1f,"SETTINGS", Color.DARK_GRAY, Color.LIGHT_GRAY,Color.BLUE, PanelType.SETTING ));
        buttons.add(quitButton = new RelocationButton(0.15f,0.15f,0.625f,0.1f,"EXIT", Color.DARK_GRAY, Color.LIGHT_GRAY,Color.RED, PanelType.EXIT ));
    }

    /**
     * will paint components
     * @param g the specified Graphics window
     */
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponents(g);
        for (RelocationButton b : buttons) {
            b.render(g,getWidth(),getHeight());
        }
    }

}
