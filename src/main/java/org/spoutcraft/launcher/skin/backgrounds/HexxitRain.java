
package org.spoutcraft.launcher.skin.backgrounds;

import net.titanscraft.launchercore.util.ResourceUtils;
import org.spoutcraft.launcher.skin.components.AnimatedImage;

import java.awt.event.ActionEvent;

public class HexxitRain extends AnimatedImage {
    private int x = 0;
    private int y = 0;

    public HexxitRain(int x, int startY) {
        super(ResourceUtils.getIcon("rain.png"), 5);
        this.x = x;
        this.y = startY;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        y += 5;

        if (y >= 520) {
            y = -520;
        }

        this.setBounds(x, y, 880, 520);
        this.repaint();
    }
}
