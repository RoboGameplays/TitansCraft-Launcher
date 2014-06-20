
package org.spoutcraft.launcher.skin.backgrounds;

import net.titanscraft.launchercore.util.ResourceUtils;
import org.spoutcraft.launcher.skin.components.EnhancedBackground;

import javax.swing.*;

public class HexxitBackground extends EnhancedBackground {
    private JLabel foreground;
    private HexxitClouds firstBackgroundClouds;
    private HexxitClouds secondBackgroundClouds;
    private HexxitClouds firstForegroundClouds;
    private HexxitClouds secondForegroundClouds;
    private HexxitFlash flash;
    private HexxitRain firstRain;
    private HexxitRain secondRain;

    public HexxitBackground() {
        super("hexxit");
        foreground = new JLabel();
        foreground.setBounds(0, 0, 880, 520);
        foreground.setIcon(ResourceUtils.getIcon("foreground.png"));
        firstBackgroundClouds = new HexxitClouds(0, -50, "clouds.png", 100);
        secondBackgroundClouds = new HexxitClouds(880, -50, "clouds.png", 100);
        firstForegroundClouds = new HexxitClouds(0, -200, "foregroundClouds.png", 50);
        secondForegroundClouds = new HexxitClouds(880, -200, "foregroundClouds.png", 50);
        flash = new HexxitFlash();
        firstRain = new HexxitRain(0, 0);
        secondRain = new HexxitRain(0, 520);

        this.add(firstForegroundClouds);
        this.add(secondForegroundClouds);
        this.add(firstRain);
        this.add(secondRain);
        this.add(foreground);
        this.add(firstBackgroundClouds);
        this.add(secondBackgroundClouds);
        this.add(flash);
    }

    @Override
    public void setVisible(boolean aFlag) {
        firstBackgroundClouds.setAnimating(aFlag);
        secondBackgroundClouds.setAnimating(aFlag);
        firstForegroundClouds.setAnimating(aFlag);
        secondForegroundClouds.setAnimating(aFlag);
        firstRain.setAnimating(aFlag);
        secondRain.setAnimating(aFlag);
        flash.setAnimating(aFlag);
        super.setVisible(aFlag);
    }
}
