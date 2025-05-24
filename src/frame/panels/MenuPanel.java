package frame.panels;

import controls.RelocationButton;
import frame.MainFrame;
import frame.PanelType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;

/**
 * class for menu panel
 */
public class MenuPanel extends JPanel {

    RelocationButton startButton;
    RelocationButton settingsButton;
    RelocationButton quitButton;
    ArrayList<RelocationButton> buttons;
    private BufferedImage background;

    /**
     * this will load background and buttons and will add motion listener
     * @param frame this is for changing panels we need method switch panel and this method is located in main frame
     */
    public MenuPanel(MainFrame frame) {
        loadBackground();
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

    /**
     * initialize buttons
     */
    private void initializeButtons() {
        buttons = new ArrayList<>();

        buttons.add(startButton = new RelocationButton(0.15f,0.15f,0.225f,0.1f,"START", Color.LIGHT_GRAY,Color.WHITE, PanelType.GAME ));
        buttons.add(settingsButton = new RelocationButton(0.15f,0.15f,0.425f,0.1f,"SETTINGS", Color.LIGHT_GRAY,Color.WHITE, PanelType.SETTING ));
        buttons.add(quitButton = new RelocationButton(0.15f,0.15f,0.625f,0.1f,"EXIT", Color.LIGHT_GRAY,Color.WHITE, PanelType.EXIT ));
    }
    private void loadBackground() {
        try {
            background = javax.imageio.ImageIO.read(Objects.requireNonNull(getClass().getResource("/background/menuBackground.jpg")));
        } catch (Exception e) {
            System.out.println("background could not be loaded");
        }

    }

    /**
     * will paint components
     * @param g the specified Graphics window
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (background != null) {
            g2d.drawImage(background,0,0,getWidth(),getHeight(),this);
        }
        super.paintComponents(g);
        for (RelocationButton b : buttons) {
            b.render(g,getWidth(),getHeight());
        }
    }

}
