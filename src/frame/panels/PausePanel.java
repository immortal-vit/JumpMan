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

/**
 * class for a pause panel
 */
public class PausePanel extends JPanel {

    private ArrayList<RelocationButton> buttons;
    private final MainFrame frame;
    private final String PAUSE = "PAUSED";

    /**
     * constructor with a mouse listener functions for buttons
     * @param frame main frame where will be changed the panel after clicking on relocation button
     */
    public PausePanel(MainFrame frame) {
        this.frame = frame;
        this.setBackground(Color.DARK_GRAY);
        this.setOpaque(true);

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

    /**
     * will create buttons and add them to button array
     */
    private void initializeButtons() {
        buttons = new ArrayList<>();

        RelocationButton resumeButton = new RelocationButton(0.15f, 0.2f, 0.25f, 0.1f, "RESUME", Color.LIGHT_GRAY, Color.GREEN, PanelType.GAME);
        RelocationButton settingsButton = new RelocationButton(0.15f, 0.2f, 0.25f, 0.4f, "SETTINGS", Color.LIGHT_GRAY, Color.ORANGE, PanelType.SETTING);
        RelocationButton menuButton = new RelocationButton(0.15f, 0.2f, 0.25f, 0.7f, "MAIN MENU", Color.LIGHT_GRAY, Color.RED, PanelType.MENU);

        buttons.add(resumeButton);
        buttons.add(settingsButton);
        buttons.add(menuButton);
    }

    /**
     * will paint every component
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);


        paintText(g);


        for (RelocationButton b : buttons) {
            b.render(g, getWidth(), getHeight());
        }
    }

    /**
     * will paint paused text
     * @param g graphics which will be used
     */
    private void paintText(Graphics g) {
        float fontSize = getHeight() * 0.1f;
        g.setFont(g.getFont().deriveFont(Font.BOLD, fontSize));

        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(PAUSE);
        int textHeight = fm.getAscent();

        int x = (getWidth() - textWidth) / 2;
        int y = (int) (getHeight() * 0.8f) + textHeight / 2;

        g.setColor(Color.WHITE);
        g.drawString(PAUSE, x, y);

    }
}
