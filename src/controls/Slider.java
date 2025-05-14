package controls;

import java.awt.event.MouseEvent;

import java.awt.*;

/**
 * custom slide bar for sound
 * it scales with panel, and it is based of the percents
 */
public class Slider {

    private float xPercent, yPercent, widthPercent, heightPercent;
    private int x, y, width, height;

    private int value = 50;
    private boolean dragging = false;
    private Color primaryColor;
    private Color secondaryColor;
    private String label;

    /**
     *  this will set position width and colors
     * @param xPercent location on x based on percent
     * @param yPercent location on y based on percent
     * @param widthPercent width based of percent
     * @param heightPercent height based of percent
     * @param primaryColor primary color for slider and text
     * @param secondaryColor secondary color for knob
     */
    public Slider(float xPercent, float yPercent, float widthPercent, float heightPercent, Color primaryColor, Color secondaryColor, String label) {
        this.xPercent = xPercent;
        this.yPercent = yPercent;
        this.widthPercent = widthPercent;
        this.heightPercent = heightPercent;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.label = label;
    }

    /**
     * will resize the button based of panel width and height
     * @param panelWidth width of panel
     * @param panelHeight height of panel
     */
    public void resize(int panelWidth, int panelHeight) {
        this.x = (int) (panelWidth * xPercent);
        this.y = (int) (panelHeight * yPercent);
        this.width = (int) (panelWidth * widthPercent);
        this.height = (int) (panelHeight * heightPercent);
    }

    /**
     * this will firstly call resize, and then it will paint all the components
     * then it will draw slider bar and knob and lastly the text
     * @param g graphics
     * @param panelWidth panel width
     * @param panelHeight panel height
     */
    public void render(Graphics g, int panelWidth, int panelHeight) {

        resize(panelWidth, panelHeight);
        Graphics2D g2d = (Graphics2D) g;

        int barHeight = (int)(height * 0.3);
        int barY = y + height / 2 - barHeight / 2;

        int knobWidth = (int)(height * 0.4);
        int knobHeight = height;
        int knobX = x + (int) ((width - knobWidth) * (value / 100f));

        g2d.setColor(primaryColor);
        g2d.fillRoundRect(x, barY, width, barHeight, 10, 10);

        g2d.setColor(secondaryColor);
        g2d.fillRoundRect(knobX, y, knobWidth, knobHeight, 10, 10);

        g2d.setColor(primaryColor);
        g2d.drawRoundRect(knobX, y, knobWidth, knobHeight, 10, 10);

        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        String text = label + ": " + value + "%";
        FontMetrics fm = g2d.getFontMetrics();
        int textX = x + (width - fm.stringWidth(text)) / 2;
        int textY = y - 10;
        g2d.drawString(text, textX, textY);

    }

    /**
     * if mouse is pressed on the knob this will set dragging to true
     * @param e mouse event
     */
    public void mousePressed(MouseEvent e) {
        Rectangle knob = getKnobRect();
        if (knob.contains(e.getPoint())) {
            dragging = true;
        }
    }

    /**
     * if mouse is released while dragging the knob it will turn dragging to false
     * @param e mouse event
     */
    public void mouseReleased(MouseEvent e) {
        dragging = false;
    }

    /**
     * will change the knob width and set new x location for the knob if player is dragging
     * @param e mouse event
     */
    public void mouseDragged(MouseEvent e) {
        if (dragging) {
            int knobWidth = getKnobRect().width;
            int newX = Math.max(x, Math.min(e.getX() - knobWidth / 2, x + width - knobWidth));
            float percent = (float)(newX - x) / (width - knobWidth);
            value = Math.round(percent * 100);
        }
    }

    /**
     *
     * @return rectangle of knob
     */
    private Rectangle getKnobRect() {
        int knobWidth = (int)(height * 0.6);
        int knobX = x + (int) ((width - knobWidth) * (value / 100f));
        return new Rectangle(knobX, y, knobWidth, height);
    }

    /**
     *
     * @return volume for soundtrack
     */
    public int getValue() {
        return value;
    }

    /**
     * volume is between 0 - 100
     * @param v new v which will be selected for vol
     */
    public void setValue(int v) {
        value = Math.max(0, Math.min(v, 100));
    }
}
