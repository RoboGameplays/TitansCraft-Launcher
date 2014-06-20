
package org.spoutcraft.launcher.skin.components;

import javax.swing.*;
import java.awt.*;

public class TransparentButton extends JButton implements Transparent {
    private static final long serialVersionUID = 1L;
    private final TransparentComponent transparency = new TransparentComponent(this);

    public TransparentButton() {
        this.setBorder(null);
        setRolloverEnabled(true);
        setFocusable(false);
        setContentAreaFilled(false);
        setOpaque(false);
    }

    @Override
    public void setIcon(Icon icon) {
        super.setIcon(icon);
        setRolloverIcon(getIcon());
        setSelectedIcon(getIcon());
        setDisabledIcon(getIcon());
        setPressedIcon(getIcon());
    }

    @Override
    public void paint(Graphics g) {
        g = transparency.setup(g);
        super.paint(g);
        transparency.cleanup(g);
    }

    @Override
    public float getTransparency() {
        return transparency.getTransparency();
    }

    @Override
    public void setTransparency(float t) {
        transparency.setTransparency(t);
    }

    @Override
    public float getHoverTransparency() {
        return transparency.getHoverTransparency();
    }

    @Override
    public void setHoverTransparency(float t) {
        transparency.setHoverTransparency(t);
    }
}
