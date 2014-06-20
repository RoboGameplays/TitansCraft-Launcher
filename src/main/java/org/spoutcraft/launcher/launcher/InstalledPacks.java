
package org.spoutcraft.launcher.launcher;

import com.google.gson.JsonSyntaxException;
import net.titanscraft.launchercore.install.AddPack;
import net.titanscraft.launchercore.install.IPackStore;
import net.titanscraft.launchercore.install.InstalledPack;
import net.titanscraft.launchercore.mirror.MirrorStore;
import net.titanscraft.launchercore.util.Utils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class InstalledPacks implements IPackStore {
    private final Map<String, InstalledPack> installedPacks = new HashMap<String, InstalledPack>();
    private final List<String> byIndex = new ArrayList<String>();
    private String selected = null;
    private int selectedIndex = 0;

    public static InstalledPacks load(MirrorStore mirrorStore) {
        File installedPacks = new File(Utils.getSettingsDirectory(), "installedPacks");

        InstalledPacks emptyList = new InstalledPacks();
        emptyList.add(new AddPack());

        if (!installedPacks.exists()) {
            Utils.getLogger().log(Level.WARNING, "Unable to load installedPacks from " + installedPacks + " because it does not exist.");
            return emptyList;
        }

        try {
            String json = FileUtils.readFileToString(installedPacks, Charset.forName("UTF-8"));
            InstalledPacks parsedList = Utils.getGson().fromJson(json, InstalledPacks.class);

            if (parsedList != null) {
                for (String packName : parsedList.getPackNames()) {
                    InstalledPack pack = parsedList.getInstalledPacks().get(packName);

                    if (pack != null) {
                        pack.setMirrorStore(mirrorStore);
                    }
                }

                return parsedList;
            } else
                return emptyList;
        } catch (JsonSyntaxException e) {
            Utils.getLogger().log(Level.WARNING, "Unable to load installedPacks from " + installedPacks);
            return emptyList;
        } catch (IOException e) {
            Utils.getLogger().log(Level.WARNING, "Unable to load installedPacks from " + installedPacks);
            return emptyList;
        }
    }

    public Map<String, InstalledPack> getInstalledPacks() {
        return installedPacks;
    }

    public List<String> getPackNames() {
        return byIndex;
    }

    public void reorder(int index, String pack) {
        if (byIndex.remove(pack)) {
            byIndex.add(index, pack);
        }
    }

    public int getSelectedIndex() {
        return this.selectedIndex;
    }

    public void setSelectedIndex(int index) {
        this.selectedIndex = index;
    }

    public InstalledPack add(InstalledPack installedPack) {
        InstalledPack pack = installedPacks.put(installedPack.getName(), installedPack);
        if (pack == null) {
            int loc = byIndex.size();
            byIndex.add(loc, installedPack.getName());
        }
        return pack;
    }

    public InstalledPack put(InstalledPack inputPack) {
        InstalledPack pack = installedPacks.put(inputPack.getName(), inputPack);
        if (pack == null) {
            byIndex.add(inputPack.getName());
        }
        save();
        return pack;
    }

    public InstalledPack remove(String key) {
        InstalledPack pack = installedPacks.remove(key);
        if (pack != null) {
            byIndex.remove(key);
        }
        save();
        return pack;
    }

    public void save() {
        File settings = new File(Utils.getSettingsDirectory(), "installedPacks");
        String json = Utils.getGson().toJson(this);

        try {
            FileUtils.writeStringToFile(settings, json, Charset.forName("UTF-8"));
        } catch (IOException e) {
            Utils.getLogger().log(Level.WARNING, "Unable to save settings " + settings, e);
        }
    }

    @Override
    public String toString() {
        return "InstalledPacks{" +
                "installedPacks=" + installedPacks +
                ", byIndex=" + byIndex +
                ", selectedIndex=" + selectedIndex +
                '}';
    }
}
