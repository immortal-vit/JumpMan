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

import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseMotionListener;

public class SettingsPanel extends JPanel {

    Button backButon;
    ArrayList<Button> buttons;

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

        buttons.add(backButon = new Button(0.15f,0.15f,0.75f,0.425f,"BACK", Color.DARK_GRAY, Color.LIGHT_GRAY, PanelType.MENU));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (Button b : buttons) {
            b.render(g,getWidth(),getHeight());
        }
    }


}
