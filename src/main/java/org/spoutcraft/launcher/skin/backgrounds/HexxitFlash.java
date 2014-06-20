
package org.spoutcraft.launcher.skin.backgrounds;

import net.titanscraft.launchercore.util.ResourceUtils;
import org.spoutcraft.launcher.skin.components.AnimatedImage;

import java.awt.event.ActionEvent;

public class HexxitFlash extends AnimatedImage {
    private int counter = 0;

    public HexxitFlash() {
        super(ResourceUtils.getIcon("flash.jpg"), 75);
        this.setBounds(0, 0, 0, 0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        counter++;

        if (counter == 41 || counter == 42 || counter == 47 || counter == 49) {
            this.setBounds(0, 0, 880, 520);
        } else {
            this.setBounds(0, 0, 0, 0);
        }

        if (counter >= 100) {
            counter = 0;
        }
    }
}
