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

public class PausePanel extends JPanel {

    private ArrayList<RelocationButton> buttons;
    private RelocationButton resumeButton;
    private RelocationButton settingsButton;
    private RelocationButton menuButton;

    private final MainFrame frame;

    public PausePanel(MainFrame frame) {
        this.frame = frame;
        setOpaque(false);

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
                    if (b.isSelected(e)) {
                        frame.switchPanel(b.getWhereToRelocate());
                    }

                }
            }
            @Override public void mouseEntered(MouseEvent e) {}
            @Override public void mouseExited(MouseEvent e) {}
        });
    }

    private void initializeButtons() {
        buttons = new ArrayList<>();

        buttons.add(resumeButton = new RelocationButton(0.15f, 0.2f, 0.25f, 0.1f, "RESUME", Color.DARK_GRAY, Color.LIGHT_GRAY, Color.GREEN, PanelType.GAME));
        buttons.add(settingsButton = new RelocationButton(0.15f, 0.2f, 0.25f, 0.4f, "SETTINGS", Color.DARK_GRAY, Color.LIGHT_GRAY, Color.ORANGE, PanelType.SETTING));
        buttons.add(menuButton = new RelocationButton(0.15f, 0.2f, 0.25f, 0.7f, "MAIN MENU", Color.DARK_GRAY, Color.LIGHT_GRAY, Color.RED, PanelType.MENU));
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponents(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        for (RelocationButton b : buttons) {
            b.render(g, getWidth(), getHeight());
        }
    }
}
