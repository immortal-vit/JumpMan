package controls;

import frame.PanelType;

import java.awt.*;
import java.awt.event.MouseEvent;

public class RelocationButton {
    private final float xPercent, yPercent, widthPercent, heightPercent;
    private int x, y, width, height;
    private final String text;
    private boolean hovered = false;
    private final Color SELECTED_COLOR;
    private final Color DEFAULT_COLOR;
    private final Color FONT_COLOR;
    private PanelType whereToRelocate;

    public RelocationButton(float heightPercent, float widthPercent, float yPercent, float xPercent, String text, Color selectedcolor, Color defaultcolor, Color fontColor, PanelType panelType) {
        this.heightPercent = heightPercent;
        this.widthPercent = widthPercent;
        this.yPercent = yPercent;
        this.xPercent = xPercent;
        this.text = text;
        this.SELECTED_COLOR = selectedcolor;
        this.DEFAULT_COLOR = defaultcolor;
        this.FONT_COLOR = fontColor;
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
        g2d.setColor(hovered ? DEFAULT_COLOR : SELECTED_COLOR);
        g2d.fillRect(x, y, width, height);

        g2d.setColor(hovered ? SELECTED_COLOR : DEFAULT_COLOR);
        g2d.drawRect(x, y, width, height);

        int fontSize = (int) (height * 0.2);
        Font font = new Font("Arial", Font.BOLD, fontSize);
        g2d.setFont(font);
        g2d.setColor(FONT_COLOR);

        FontMetrics fm = g2d.getFontMetrics();

        int textX = x + (width - fm.stringWidth(text)) / 2;
        int textY = y + (height + fm.getAscent() - fm.getDescent()) / 2;
        g2d.drawString(text, textX, textY);
    }

    public void updateSelected(MouseEvent e) {
        hovered = e.getX() >= x && e.getX() <= x + width && e.getY() >= y && e.getY() <= y + height;
    }

    public boolean isSelected(MouseEvent e) {
        return hovered && e.getButton() == MouseEvent.BUTTON1;
    }

    public PanelType getWhereToRelocate() {
        hovered = false;
        return whereToRelocate;
    }

    public void setWhereToRelocate(PanelType whereToRelocate) {
        this.whereToRelocate = whereToRelocate;
    }
}
