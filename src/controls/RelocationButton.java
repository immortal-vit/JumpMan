package controls;

import frame.PanelType;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * class for button for relocation
 */
public class RelocationButton {
    private final float xPercent, yPercent, widthPercent, heightPercent;
    private int x, y, width, height;
    private final String text;
    private boolean hovered = false;
    private final Color SELECTED_COLOR;
    private final Color FONT_COLOR;
    private PanelType whereToRelocate;

    /**
     *
     * @param heightPercent height of a button
     * @param widthPercent width of a button
     * @param yPercent y location in percent
     * @param xPercent x location in percent
     * @param text text inside the button
     * @param selectedcolor color when player is hovering a curson on it
     * @param fontColor font color
     * @param panelType panel where player will be relocated
     */
    public RelocationButton(float heightPercent, float widthPercent, float yPercent, float xPercent, String text, Color selectedcolor, Color fontColor, PanelType panelType) {
        this.heightPercent = heightPercent;
        this.widthPercent = widthPercent;
        this.yPercent = yPercent;
        this.xPercent = xPercent;
        this.text = text;
        this.SELECTED_COLOR = selectedcolor;
        this.FONT_COLOR = fontColor;
        this.whereToRelocate = panelType;
    }

    /**
     * method for resizing
     * @param panelWidth based of this the button will be resized
     * @param panelHeight based of this the button will be resized
     */

    public void resize(int panelWidth, int panelHeight) {
        this.x = (int) (panelWidth * xPercent);
        this.y = (int) (panelHeight * yPercent);
        this.width = (int) (widthPercent * panelWidth);
        this.height = (int) (heightPercent * panelHeight);
    }

    /**
     * this will render the button
     * @param g graphics
     * @param panelWidth used for resizing
     * @param panelHeight used for resizing
     */
    public void render (Graphics g,int panelWidth, int panelHeight) {
        Graphics2D g2d = (Graphics2D) g;

        resize(panelWidth, panelHeight);

        float alpha = 0.4f;


        if (hovered){
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.setColor(SELECTED_COLOR);
            g2d.fillRect(x, y, width, height);

            alpha = 0.75f;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.setColor(SELECTED_COLOR);
            g2d.drawRect(x, y, width, height);
        }


        alpha = 1f;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        int fontSize = (int) (height * 0.2);
        Font font = new Font("Arial", Font.BOLD, fontSize);
        g2d.setFont(font);
        g2d.setColor(FONT_COLOR);

        FontMetrics fm = g2d.getFontMetrics();

        int textX = x + (width - fm.stringWidth(text)) / 2;
        int textY = y + (height + fm.getAscent() - fm.getDescent()) / 2;
        g2d.drawString(text, textX, textY);
    }


    /**
     * will be updated if the cursor is on the button
     * @param e mouse event
     */
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
