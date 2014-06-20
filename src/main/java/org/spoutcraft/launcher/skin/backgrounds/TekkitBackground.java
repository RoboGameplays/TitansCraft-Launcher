
package org.spoutcraft.launcher.skin.backgrounds;

import net.titanscraft.launchercore.util.ResourceUtils;
import org.spoutcraft.launcher.skin.components.AnimatedImage;
import org.spoutcraft.launcher.skin.components.EnhancedBackground;

public class TekkitBackground extends EnhancedBackground {
    private AnimatedImage tekkit;

    public TekkitBackground() {
        super("tekkitmain");
        tekkit = new TekkitCreeper(650, 100, ResourceUtils.getIcon("creeper.png", 107, 69));
        tekkit.setBounds(500, 100, 107, 69);
        tekkit.setVisible(false);
        this.add(tekkit);
    }


    @Override
    public void setVisible(boolean aFlag) {
        tekkit.setAnimating(aFlag);
        tekkit.setVisible(aFlag);
        super.setVisible(aFlag);
    }
}
