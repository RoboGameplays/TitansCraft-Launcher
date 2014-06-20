
package org.spoutcraft.launcher;

import net.titanscraft.launchercore.exception.BuildInaccessibleException;
import net.titanscraft.launchercore.exception.CacheDeleteException;
import net.titanscraft.launchercore.exception.DownloadException;
import net.titanscraft.launchercore.exception.PackNotAvailableOfflineException;
import net.titanscraft.launchercore.install.InstalledPack;
import net.titanscraft.launchercore.install.ModpackInstaller;
import net.titanscraft.launchercore.install.user.UserModel;
import net.titanscraft.launchercore.launch.LaunchOptions;
import net.titanscraft.launchercore.launch.MinecraftLauncher;
import net.titanscraft.launchercore.minecraft.CompleteVersion;
import net.titanscraft.launchercore.mirror.MirrorStore;
import net.titanscraft.launchercore.util.LaunchAction;
import net.titanscraft.launchercore.util.Settings;
import org.spoutcraft.launcher.entrypoint.SpoutcraftLauncher;
import org.spoutcraft.launcher.launcher.Launcher;

import javax.swing.*;
import java.io.IOException;
import java.util.zip.ZipException;

public class InstallThread extends Thread {
    private final InstalledPack pack;
    private final ModpackInstaller modpackInstaller;
    private final UserModel userModel;
    private final MirrorStore mirrorStore;
    private boolean finished = false;

    public InstallThread(InstalledPack pack, String build, UserModel userModel, MirrorStore mirrorStore) {
        super("InstallThread");
        this.pack = pack;
        this.modpackInstaller = new ModpackInstaller(Launcher.getFrame(), pack, build, mirrorStore);
        this.userModel = userModel;
        this.mirrorStore = mirrorStore;
    }

    @Override
    public void run() {
        try {
            Launcher.getFrame().getProgressBar().setVisible(true);
            CompleteVersion version = null;
            if (!pack.isLocalOnly()) {
                version = modpackInstaller.installPack(Launcher.getFrame(), userModel.getCurrentUser());
            } else {
                version = modpackInstaller.prepareOfflinePack();
            }

            int memory = Memory.getMemoryFromId(Settings.getMemory()).getMemoryMB();
            MinecraftLauncher minecraftLauncher = new MinecraftLauncher(memory, pack, version);

            StartupParameters params = SpoutcraftLauncher.params;
            LaunchOptions options = new LaunchOptions(pack.getDisplayName(), pack.getIconPath(), params.getWidth(), params.getHeight(), params.getFullscreen());
            LauncherUnhider unhider = new LauncherUnhider();
            minecraftLauncher.launch(userModel.getCurrentUser(), options, unhider, mirrorStore);

            LaunchAction launchAction = Settings.getLaunchAction();

            if (launchAction == null || launchAction == LaunchAction.HIDE) {
                Launcher.getFrame().setVisible(false);
            } else if (launchAction == LaunchAction.CLOSE) {
                System.exit(0);
            }
        } catch (PackNotAvailableOfflineException e) {
            JOptionPane.showMessageDialog(Launcher.getFrame(), e.getMessage(), "Cannot Start Modpack", JOptionPane.WARNING_MESSAGE);
        } catch (DownloadException e) {
            JOptionPane.showMessageDialog(Launcher.getFrame(), "Error downloading file for the following pack: " + pack.getDisplayName() + " \n\n" + e.getMessage() + "\n\nPlease consult the modpack author.", "Error", JOptionPane.WARNING_MESSAGE);
        } catch (ZipException e) {
            JOptionPane.showMessageDialog(Launcher.getFrame(), "Error unzipping a file for the following pack: " + pack.getDisplayName() + " \n\n" + e.getMessage() + "\n\nPlease consult the modpack author.", "Error", JOptionPane.WARNING_MESSAGE);
        } catch (CacheDeleteException e) {
            JOptionPane.showMessageDialog(Launcher.getFrame(), "Error installing the following pack: " + pack.getDisplayName() + " \n\n" + e.getMessage() + "\n\nPlease check your system settings.", "Error", JOptionPane.WARNING_MESSAGE);
        } catch (BuildInaccessibleException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(Launcher.getFrame(), e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Launcher.getFrame().getProgressBar().setVisible(false);

            finished = true;
        }
    }

    public boolean isFinished() {
        return modpackInstaller.isFinished() || finished;
    }
}
