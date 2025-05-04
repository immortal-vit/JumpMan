package controls;

import java.awt.*;
import java.awt.event.MouseEvent;

public class CheckBox {

    private float xPercent, yPercent, sizePercent;
    private int x, y, size;
    private boolean checked = false;
    private boolean hovered = false;

    private final Color BORDER_COLOR;
    private final Color FILL_COLOR;
    private final Color CHECK_COLOR;

    private final String label;
    private final Color LABEL_COLOR;

    public CheckBox(float xPercent, float yPercent, float sizePercent, Color borderColor, Color fillColor, Color checkColor, String label, Color labelColor) {
        this.xPercent = xPercent;
        this.yPercent = yPercent;
        this.sizePercent = sizePercent;
        this.BORDER_COLOR = borderColor;
        this.FILL_COLOR = fillColor;
        this.CHECK_COLOR = checkColor;
        this.label = label;
        this.LABEL_COLOR = labelColor;
    }

    public void resize(int panelWidth, int panelHeight) {
        this.size = (int) (Math.min(panelWidth, panelHeight) * sizePercent);
        this.x = (int) (panelWidth * xPercent);
        this.y = (int) (panelHeight * yPercent);
    }
    public void render(Graphics g, int panelWidth, int panelHeight) {
        resize(panelWidth, panelHeight);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(FILL_COLOR);
        g2d.fillRect(x, y, size, size);

        if (checked) {
            g2d.setColor(CHECK_COLOR);
            g2d.fillRect(x , y , size , size );
        }

        g2d.setStroke(new BasicStroke((float) (size * 0.1)));
        g2d.setColor(hovered ? BORDER_COLOR.brighter() : BORDER_COLOR);
        g2d.drawRect(x ,y , size, size);

        g2d.setColor(LABEL_COLOR);
        g2d.setFont(new Font("Arial", Font.BOLD, size / 4));
        g2d.drawString(label, x + size + 10, y + size / 2 + size / 8);
    }

    public void mousePressed(MouseEvent e) {
        if (getBounds().contains(e.getPoint()) && e.getButton() == MouseEvent.BUTTON1) {
            checked = !checked;
        }
    }

    public void updateHover(MouseEvent e) {
        hovered = getBounds().contains(e.getPoint());
    }

    private Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    public boolean isChecked() {
        return checked;
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

}
