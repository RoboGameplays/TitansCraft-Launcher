
package org.spoutcraft.launcher.skin.components;

import javax.swing.*;
import java.awt.*;

public class TransparentJLabel extends JLabel implements Transparent {
    private static final long serialVersionUID = 1L;
    TransparentComponent transparency = new TransparentComponent(this);

    public TransparentJLabel() {
        super();
    }

    public TransparentJLabel(String text) {
        super(text);
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

    @Override
    public void paint(Graphics g) {
        g = transparency.setup(g);
        super.paint(g);
        transparency.cleanup(g);
    }
}
