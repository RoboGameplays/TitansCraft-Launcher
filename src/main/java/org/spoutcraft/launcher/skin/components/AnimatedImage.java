
package org.spoutcraft.launcher.skin.components;

import javax.swing.*;
import java.awt.event.ActionListener;

public abstract class AnimatedImage extends JLabel implements ActionListener {
    private static final long serialVersionUID = 1;
    private final Timer timer;

    public AnimatedImage(Icon image, int delay) {
        this.setIcon(image);
        timer = new Timer(delay, this);
    }

    public void setAnimating(boolean animate) {
        if (animate) {
            timer.start();
        } else {
            timer.stop();
        }
    }

    public Timer getTimer() {
        return timer;
    }
}
