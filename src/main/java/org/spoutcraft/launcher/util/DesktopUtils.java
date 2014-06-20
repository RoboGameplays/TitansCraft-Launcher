
package org.spoutcraft.launcher.util;

import org.spoutcraft.launcher.entrypoint.SpoutcraftLauncher;

import java.awt.*;
import java.io.File;
import java.net.URI;

/**
 * Static utility class to preventing checking for IOExceptions everywhere you would like to open a folder or open the browser
 */
public class DesktopUtils {
    /**
     * Replaces Desktop.getDesktop().browse(uri)
     *
     * @param uri
     */
    public static void browse(URI uri) {
        try {
            Desktop.getDesktop().browse(uri);
        } catch (Exception e) {
            if (SpoutcraftLauncher.params.isDebugMode()) {
                e.printStackTrace();
            }
        }
    }

    public static void open(File file) {
        try {
            Desktop.getDesktop().open(file);
        } catch (Exception e) {
            if (SpoutcraftLauncher.params.isDebugMode()) {
                e.printStackTrace();
            }
        }
    }
}
