
package org.spoutcraft.launcher.entrypoint;

import net.titanscraft.launchercore.util.Directories;
import net.titanscraft.launchercore.util.OperatingSystem;
import net.titanscraft.launchercore.util.Utils;
import org.apache.commons.io.IOUtils;
import org.spoutcraft.launcher.settings.LauncherDirectories;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Mover {
    public static void main(String[] args) {
        main(args, false);
    }

    public static void main(String[] args, boolean exe) {
        try {
            Directories.instance = new LauncherDirectories();
            SpoutcraftLauncher.setupLogger();
            execute(args, exe);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private static void execute(String[] args, boolean exe) throws Exception {
        File temp;
        if (exe) {
            temp = new File(Utils.getSettingsDirectory(), "temp.exe");
        } else {
            temp = new File(Utils.getSettingsDirectory(), "temp.jar");
        }
        File codeSource = new File(args[0]);
        codeSource.delete();
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(temp);
            fos = new FileOutputStream(codeSource);
            IOUtils.copy(fis, fos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fis);
            IOUtils.closeQuietly(fos);
        }

        codeSource.setExecutable(true, true);

        ProcessBuilder processBuilder = new ProcessBuilder();
        ArrayList<String> commands = new ArrayList<String>();
        if (!exe) {
            if (OperatingSystem.getOperatingSystem().equals(OperatingSystem.WINDOWS)) {
                commands.add("javaw");
            } else {
                commands.add("java");
            }
            commands.add("-Xmx256m");
            commands.add("-cp");
            commands.add(codeSource.getAbsolutePath());
            commands.add(SpoutcraftLauncher.class.getName());
        } else {
            commands.add(temp.getAbsolutePath());
            commands.add("-Launcher");
        }
        commands.addAll(Arrays.asList(args));
        processBuilder.command(commands);

        processBuilder.start();
    }
}
