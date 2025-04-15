package frame.panels;

import controls.Button;
import frame.MainFrame;
import frame.PanelType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

public class MenuPanel extends JPanel {

    controls.Button startButton;
    controls.Button settingsButton;
    controls.Button quitButton;
    ArrayList<Button> buttons;

    public MenuPanel(MainFrame frame) {
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
                for (Button b : buttons) {
                    b.updateSelected(e);
                    repaint();
                }
            }
        });
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (Button b : buttons) {
                    b.updateSelected(e);
                    repaint();
                    if (b.isSelected(e)){
                        frame.switchPanel(b.getWhereToRelocate());
                    }
                }
            }
            @Override public void mousePressed(MouseEvent e) {}
            @Override public void mouseReleased(MouseEvent e) {}
            @Override public void mouseEntered(MouseEvent e) {}
            @Override public void mouseExited(MouseEvent e) {}
        });

    }

    private void initializeButtons() {
        buttons = new ArrayList<>();

        buttons.add(startButton = new Button(0.15f,0.15f,0.225f,0.1f,"START", Color.DARK_GRAY, Color.LIGHT_GRAY, PanelType.GAME ));
        buttons.add(settingsButton = new Button(0.15f,0.15f,0.425f,0.1f,"SETTINGS", Color.DARK_GRAY, Color.LIGHT_GRAY, PanelType.SETTING ));
        buttons.add(quitButton = new Button(0.15f,0.15f,0.625f,0.1f,"EXIT", Color.DARK_GRAY, Color.LIGHT_GRAY, PanelType.EXIT ));
    }

    /**
     * will paint components
     * @param g the specified Graphics window
     */
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponents(g);
        for (Button b : buttons) {
            b.render(g,getWidth(),getHeight());
        }
    }

}
