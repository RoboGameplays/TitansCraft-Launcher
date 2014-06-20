
package org.spoutcraft.launcher.skin.backgrounds;

import org.spoutcraft.launcher.skin.components.AnimatedImage;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class TekkitCreeper extends AnimatedImage {
    private static final long serialVersionUID = 1;

    private final int x;
    private final int y;
    private static final int delay = 50;
    private final int distance = 30;
    private int modX = 0;
    private boolean xReverse = false;
    private int modY = 0;
    private boolean yReverse = false;


    public TekkitCreeper(int x, int y, Icon image) {
        super(image, delay);
        this.x = x;
        this.y = y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (modX == distance) {
            xReverse = true;
        }
        if (modX == 0) {
            xReverse = false;
        }
        if (modY == distance) {
            yReverse = true;
        }
        if (modY == 0) {
            yReverse = false;
        }

        if (xReverse) {
            modX--;
        } else {
            modX++;
        }

        if (yReverse) {
            modY--;
        } else {
            modY++;
        }

        int delayChange = 0;
        if (modX < distance / 2) {
            delayChange = distance - modX - (distance / 2);
        } else {
            delayChange = modX - (distance / 2);
        }
        getTimer().setDelay(delay + (delayChange * 10));

        this.setBounds(x + modX, y + modY, getWidth(), getHeight());
        this.repaint();
    }
}
