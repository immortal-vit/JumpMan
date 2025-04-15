package controls;

import frame.PanelType;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Button {
    private float xPercent, yPercent, widthPercent, heightPercent;
    private int x, y, width, height;
    private String text;
    private boolean selected = false;
    private final Color SELECTED_COLOR;
    private final Color DEFAULT_COLOR;
    private final PanelType whereToRelocate;

    public Button(float heightPercent, float widthPercent, float yPercent, float xPercent, String text, Color selectedcolor, Color defaultcolor, PanelType panelType) {
        this.heightPercent = heightPercent;
        this.widthPercent = widthPercent;
        this.yPercent = yPercent;
        this.xPercent = xPercent;
        this.text = text;
        this.SELECTED_COLOR = selectedcolor;
        this.DEFAULT_COLOR = defaultcolor;
        this.whereToRelocate = panelType;
    }

    public void resize(int panelWidth, int panelHeight) {
        this.x = (int) (panelWidth * xPercent);
        this.y = (int) (panelHeight * yPercent);
        this.width = (int) (widthPercent * panelWidth);
        this.height = (int) (heightPercent * panelHeight);
    }

    public void render (Graphics g,int panelWidth, int panelHeight) {
        Graphics2D g2d = (Graphics2D) g;

        resize(panelWidth, panelHeight);
        g2d.setColor(selected ? DEFAULT_COLOR : SELECTED_COLOR);
        g2d.fillRect(x, y, width, height);

        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, width, height);

        FontMetrics fm = g2d.getFontMetrics();

        int textX = x + (width - fm.stringWidth(text)) / 2;
        int textY = y + (height + fm.getAscent() - fm.getDescent()) / 2;
        g2d.drawString(text, textX, textY);
    }

    public void updateSelected(MouseEvent e) {
        selected = e.getX() >= x && e.getX() <= x + width && e.getY() >= y && e.getY() <= y + height;
    }

    public boolean isSelected(MouseEvent e) {
        return selected && e.getButton() == MouseEvent.BUTTON1;
    }

    public PanelType getWhereToRelocate() {
        selected = false;
        return whereToRelocate;
    }
}
