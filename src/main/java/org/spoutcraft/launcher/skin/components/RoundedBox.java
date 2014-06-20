
package org.spoutcraft.launcher.skin.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedBox extends JComponent {
    private static final long serialVersionUID = 1L;

    public RoundedBox(Color color) {
        this.setBackground(color);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // TODO Auto-generated method stub
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        RoundRectangle2D rect = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(this.getBackground());
        g2d.fill(rect);
        g2d.dispose();
    }
}
