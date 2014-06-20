
package org.spoutcraft.launcher.entrypoint;

import net.titanscraft.launchercore.exception.RestfulAPIException;
import net.titanscraft.launchercore.mirror.download.Download;
import net.titanscraft.launchercore.util.*;
import org.spoutcraft.launcher.settings.LauncherDirectories;
import org.spoutcraft.launcher.skin.ProgressSplashScreen;
import org.spoutcraft.launcher.updater.LauncherInfo;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;

public class Start {
    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void launch(String[] args) throws Exception {
        // Text for local build (not official build)
        if (SpoutcraftLauncher.getLauncherBuild().equals("0")) {
            SpoutcraftLauncher.main(args);
            return;
        }

        Directories.instance = new LauncherDirectories();

        // Test for exe relaunch
        SpoutcraftLauncher.setupLogger().info("Args: " + Arrays.toString(args));
        if (args.length > 0 && (args[0].equals("-Mover") || args[0].equals("-Launcher"))) {
            String[] argsCopy = new String[args.length - 1];

            System.arraycopy(args, 1, argsCopy, 0, args.length - 1);

            if (args[0].equals("-Mover")) {
                Mover.main(argsCopy, true);
            } else {
                SpoutcraftLauncher.main(argsCopy);
            }
            return;
        }

        Utils.getLauncherDirectory();
        boolean update = false;

        int version = Integer.parseInt(SpoutcraftLauncher.getLauncherBuild());
        String buildStream = Settings.getBuildStream();
        int latest = version;

        try {
            latest = LauncherInfo.getLatestBuild(buildStream);
            if (buildStream.equals("beta") && version < latest) {
                update = true;
            } else if (buildStream.equals("stable") && version != latest) {
                update = true;
            }
        } catch (RestfulAPIException e) {
            e.printStackTrace();
        }

        if (update) {
            File codeSource = new File(URLDecoder.decode(Start.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8"));
            File temp;
            if (codeSource.getName().endsWith(".exe")) {
                temp = new File(Utils.getSettingsDirectory(), "temp.exe");
            } else {
                temp = new File(Utils.getSettingsDirectory(), "temp.jar");
            }

            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            ProgressSplashScreen splash = new ProgressSplashScreen();
            Download download = new Download(new URL(LauncherInfo.getDownloadURL(latest, !codeSource.getName().endsWith(".exe"))), temp.getName(), temp.getPath());
            download.setListener(new LauncherDownloadListener(splash));
            download.run();

            ProcessBuilder processBuilder = new ProcessBuilder();
            ArrayList<String> commands = new ArrayList<String>();
            if (!codeSource.getName().endsWith(".exe")) {
                if (OperatingSystem.getOperatingSystem().equals(OperatingSystem.WINDOWS)) {
                    commands.add("javaw");
                } else {
                    commands.add("java");
                }
                commands.add("-Xmx256m");
                commands.add("-cp");
                commands.add(temp.getAbsolutePath());
                commands.add(Mover.class.getName());
            } else {
                commands.add(temp.getAbsolutePath());
                commands.add("-Mover");
            }
            commands.add(codeSource.getAbsolutePath());
            commands.addAll(Arrays.asList(args));
            processBuilder.command(commands);

            try {
                processBuilder.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.exit(0);
        } else {
            SpoutcraftLauncher.main(args);
        }
    }

    private static class LauncherDownloadListener implements DownloadListener {
        private final ProgressSplashScreen screen;

        LauncherDownloadListener(ProgressSplashScreen screen) {
            this.screen = screen;
        }

        @Override
        public void stateChanged(String text, float progress) {
            screen.updateProgress((int) progress);
        }
    }
}
