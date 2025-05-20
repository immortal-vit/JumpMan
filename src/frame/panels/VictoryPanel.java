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
 * panel which will be displayed after a win
 */
public class VictoryPanel extends JPanel {

    private ArrayList<RelocationButton> buttons;
    private final MainFrame frame;
    private int totalJumpsTaken;
    private String winText = "VICTORY";
    private String totalJumpText;

    /**
     * constructor for managing every function in the panel by using mouse listener
     * @param frame frame where will be changed panel
     */
    public VictoryPanel(MainFrame frame) {
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
                        frame.getGamePanel().restartGame();
                    }

                }
            }
            @Override public void mouseEntered(MouseEvent e) {}
            @Override public void mouseExited(MouseEvent e) {}
        });
    }

    /**
     * will initialize buttons
     */
    private void initializeButtons() {
        buttons = new ArrayList<>();

        RelocationButton backToMenu = new RelocationButton(0.15f, 0.2f, 0.65f, 0.4f, "BACK TO MENU", Color.LIGHT_GRAY, Color.WHITE, PanelType.MENU);

        buttons.add(backToMenu);
    }


    /**
     * will paint components
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
     * will paint text
     * @param g graphics which will be used
     */
    private void paintText(Graphics g) {
        updateJumpStatistic();

        float fontSize = getHeight() * 0.1f;
        g.setFont(g.getFont().deriveFont(Font.BOLD, fontSize));

        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(winText);
        int textHeight = fm.getAscent();

        int x = (getWidth() - textWidth) / 2;
        int y1 = (int) (getHeight() * 0.2f) + textHeight / 2;
        int y2 = (int) (getHeight() * 0.4f) + textHeight / 2;

        g.setColor(Color.ORANGE);
        g.drawString(winText, x, y1);

        fontSize = getHeight() * 0.05f;
        g.setFont(g.getFont().deriveFont(Font.BOLD, fontSize));
        fm = g.getFontMetrics();
        textWidth = fm.stringWidth(totalJumpText);
        x = (getWidth() - textWidth) / 2;

        g.drawString(totalJumpText, x, y2);

    }

    /**
     * will update jump statistic after every jump
     */
    private void updateJumpStatistic(){
        totalJumpsTaken = frame.getGamePanel().getPlayer().getTotalJumps();
        totalJumpText = "total jumps = " + totalJumpsTaken;
    }
}
