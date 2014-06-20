
package org.spoutcraft.launcher;

import net.titanscraft.launchercore.launch.MinecraftExitListener;
import net.titanscraft.launchercore.util.LaunchAction;
import net.titanscraft.launchercore.util.Settings;
import org.spoutcraft.launcher.launcher.Launcher;

public class LauncherUnhider implements MinecraftExitListener {
    @Override
    public void onMinecraftExit() {
        LaunchAction action = Settings.getLaunchAction();
        if (action == null || action == LaunchAction.HIDE) {
            Launcher.getFrame().setVisible(true);
        }
    }
}
