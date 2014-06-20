
package org.spoutcraft.launcher.settings;

import org.spoutcraft.launcher.util.yml.YAMLNode;
import org.spoutcraft.launcher.util.yml.YAMLProcessor;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OldSettings {
    private static YAMLProcessor yaml;

    public static synchronized void setYAML(YAMLProcessor settings) {
        if (OldSettings.yaml != null) {
            throw new IllegalArgumentException("settings is already set!");
        }
        OldSettings.yaml = settings;
        try {
            OldSettings.yaml.load();
        } catch (FileNotFoundException ignore) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public static synchronized YAMLProcessor getYAML() {
        return yaml;
    }

    @Deprecated
    public static synchronized int getLauncherBuild() {
        return yaml.getInt("launcher.buildNumber", -1);
    }

    @Deprecated
    public static synchronized int getMemory() {
        return yaml.getInt("launcher.memory", 0);
    }

    @Deprecated
    public static synchronized void setPermGen(boolean permgen) {
        yaml.setProperty("launcher.permgen", permgen);
    }

    @Deprecated
    public static synchronized boolean getPermGen() {
        return yaml.getBoolean("launcher.permgen", false);
    }

    @Deprecated
    public static synchronized String getModpackBuild(String modpack) {
        return yaml.getString("modpacks." + modpack + ".build");
    }

    @Deprecated
    public static synchronized String getLastModpack() {
        return yaml.getString("launcher.lastmodpack");
    }

    @Deprecated
    public static synchronized String getPackDirectory(String modpack) {
        return yaml.getString("modpacks." + modpack + ".directory");
    }

    @Deprecated
    public static synchronized boolean isPackCustom(String modpack) {
        return yaml.getBoolean("modpacks." + modpack + ".custom", false);
    }

    @Deprecated
    public static synchronized void removePack(String modpack) {
        yaml.removeProperty("modpacks." + modpack);
    }

    @Deprecated
    public static synchronized List<String> getInstalledPacks() {
        YAMLNode node = yaml.getNode("modpacks");
        if (node == null) {
            return Collections.emptyList();
        } else {
            return new ArrayList<String>(node.getMap().keySet());
        }
    }

    @Deprecated
    public static synchronized String getLauncherDir() {
        return yaml.getString("launcher.directory");
    }

    @Deprecated
    public static synchronized boolean getMigrate() {
        return yaml.getBoolean("launcher.migrate", false);
    }

    @Deprecated
    public static synchronized String getMigrateDir() {
        return yaml.getString("launcher.migratedir");
    }

    @Deprecated
    public static synchronized String getBuildStream() {
        if (yaml.getString("launcher.buildstream") != null) {
            return yaml.getString("launcher.buildstream");
        }
        return "stable";
    }

    @Deprecated
    public static boolean getShowLauncherConsole() {
        return yaml.getBoolean("launcher.showConsole", false);
    }
}
