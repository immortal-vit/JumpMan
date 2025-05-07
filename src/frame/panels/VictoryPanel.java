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

public class VictoryPanel extends JPanel {

    private ArrayList<RelocationButton> buttons;
    private final MainFrame frame;

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
                    }

                }
            }
            @Override public void mouseEntered(MouseEvent e) {}
            @Override public void mouseExited(MouseEvent e) {}
        });
    }

    private void initializeButtons() {
        buttons = new ArrayList<>();

        RelocationButton backToMenu = new RelocationButton(0.15f, 0.2f, 0.65f, 0.4f, "BACK TO MENU", Color.LIGHT_GRAY, Color.WHITE, PanelType.MENU);

        buttons.add(backToMenu);
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);


        paintText(g);


        for (RelocationButton b : buttons) {
            b.render(g, getWidth(), getHeight());
        }
    }

    private void paintText(Graphics g) {
        float fontSize = getHeight() * 0.1f;
        g.setFont(g.getFont().deriveFont(Font.BOLD, fontSize));

        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth("VICTORY");
        int textHeight = fm.getAscent();

        int x = (getWidth() - textWidth) / 2;
        int y = (int) (getHeight() * 0.2f) + textHeight / 2;

        g.setColor(Color.ORANGE);
        g.drawString("VICTORY", x, y);

    }
}
