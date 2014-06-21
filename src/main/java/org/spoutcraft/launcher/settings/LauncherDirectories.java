
package org.spoutcraft.launcher.settings;

import net.titanscraft.launchercore.util.Directories;
import net.titanscraft.launchercore.util.OperatingSystem;
import net.titanscraft.launchercore.util.Settings;
import net.titanscraft.launchercore.util.ZipUtils;
import org.spoutcraft.launcher.entrypoint.SpoutcraftLauncher;
import org.spoutcraft.launcher.skin.SplashScreen;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class LauncherDirectories extends Directories {
    private File workDir = null;
    private SplashScreen splash = null;
    private File settingsDir = null;

    public File getLauncherDirectory() {
        if (workDir == null) {
            setupLauncherDirectory();
        }
        return workDir;
    }

    public File getSettingsDirectory() {
        if (settingsDir == null) {
            settingsDir = setupSettingsDir("titanscraft");
        }
        return settingsDir;
    }

    public File getCacheDirectory() {
        File cache = new File(getLauncherDirectory(), "cache");
        if (!cache.exists()) {
            cache.mkdir();
        }
        return cache;
    }

    public File getAssetsDirectory() {
        return new File(getLauncherDirectory(), "assets");
    }

    public File getModpacksDirectory() {
        return new File(getLauncherDirectory(), "modpacks");
    }

    private void setupLauncherDirectory() {
        workDir = getSettingsDirectory();
        boolean exists = workDir.exists();
        if (!exists && !workDir.mkdirs()) {
            throw new RuntimeException("A pasta de trabalho nao pode ser criada: " + workDir);
        }

        Settings.load();

        if (SpoutcraftLauncher.params != null && SpoutcraftLauncher.params.isPortable()) {
            return;
        }

        if (Settings.getDirectory() != null) {
            File temp = new File(Settings.getDirectory());
            exists = temp.exists();
            if (exists) {
                workDir = temp;
            }
        }

        if (!exists) {
            workDir = selectInstallDir(workDir);
        } else if (Settings.getMigrate() && Settings.getMigrateDir() != null) {
            File migrate = new File(Settings.getMigrateDir());
            try {
                org.apache.commons.io.FileUtils.copyDirectory(workDir, migrate);
                org.apache.commons.io.FileUtils.cleanDirectory(workDir);
                workDir = migrate;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                Settings.setMigrate(false);
                Settings.setMigrateDir("");
            }
        }

        Settings.setDirectory(workDir.getAbsolutePath());
    }

    public File selectInstallDir(File workDir) {
        int result = JOptionPane.showConfirmDialog(splash, "O launcher nao esta instalado. \n\nE sera instalado no: \n" + workDir.getAbsolutePath() + " \n\nWould you like to change the install directory?", "Install Technic Launcher", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            JFileChooser fileChooser = new JFileChooser(workDir);
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int changeInst = fileChooser.showOpenDialog(splash);

            if (changeInst == JFileChooser.APPROVE_OPTION) {
                workDir = fileChooser.getSelectedFile();
                if (!ZipUtils.checkLaunchDirectory(workDir)) {
                    JOptionPane.showMessageDialog(splash, "Please select an empty directory, or your default install folder with settings.yml in it.", "Invalid Location", JOptionPane.WARNING_MESSAGE);
                    return selectInstallDir(workDir);
                }
            }
            workDir.mkdirs();
        }
        return workDir;
    }

    public void setSplashScreen(SplashScreen splash) {
        this.splash = splash;
    }

    private File setupSettingsDir(String applicationName) {
        if (SpoutcraftLauncher.params != null && SpoutcraftLauncher.params.isPortable()) {
            return new File(applicationName);
        }

        String userHome = System.getProperty("user.home", ".");
        File workingDirectory;

        OperatingSystem os = OperatingSystem.getOperatingSystem();
        switch (os) {
            case LINUX:
                workingDirectory = new File(userHome, '.' + applicationName + '/');
                break;
            case WINDOWS:
                String applicationData = System.getenv("APPDATA");
                if (applicationData != null) {
                    workingDirectory = new File(applicationData, "." + applicationName + '/');
                } else {
                    workingDirectory = new File(userHome, '.' + applicationName + '/');
                }
                break;
            case OSX:
                workingDirectory = new File(userHome, "Library/Application Support/" + applicationName);
                break;
            case UNKNOWN:
                workingDirectory = new File(userHome, applicationName + '/');
                break;
            default:
                workingDirectory = new File(userHome, applicationName + '/');
                break;
        }

        return workingDirectory;
    }

}
