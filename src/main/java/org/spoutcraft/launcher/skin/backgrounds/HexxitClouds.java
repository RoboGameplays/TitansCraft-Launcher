
package org.spoutcraft.launcher.skin.backgrounds;

import net.titanscraft.launchercore.util.ResourceUtils;
import org.spoutcraft.launcher.skin.components.AnimatedImage;

import java.awt.event.ActionEvent;

public class HexxitClouds extends AnimatedImage {
    private int x = 0;
    private int y = 0;

    public HexxitClouds(int startX, int y, String image, int delay) {
        super(ResourceUtils.getIcon(image), delay);
        this.x = startX;
        this.y = y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        x -= 1;

        if (x <= -880) {
            x = 880;
        }

        this.setBounds(x, y, 880, 520);
        this.repaint();
    }
}
