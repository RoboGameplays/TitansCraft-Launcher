
package org.spoutcraft.launcher.log;

import net.titanscraft.launchercore.util.Settings;
import net.titanscraft.launchercore.util.Utils;
import org.spoutcraft.launcher.skin.ConsoleFrame;

public class Console {
    private ConsoleFrame frame = null;
    private RotatingFileHandler handler = null;

    public Console(boolean isConsole) {
        if (isConsole || Settings.getShowConsole()) {
            setupConsole();
            Utils.getLogger().info("Console Mode Activated");
        }

        Thread logThread = new LogFlushThread(this);
        logThread.start();
    }

    public void setupConsole() {
        if (frame != null) {
            frame.dispose();
        }
        frame = new ConsoleFrame(2500, true);
        frame.setVisible(true);
    }

    public ConsoleFrame getFrame() {
        return frame;
    }

    public void setRotatingFileHandler(RotatingFileHandler handler) {
        this.handler = handler;
    }

    public RotatingFileHandler getHandler() {
        return handler;
    }

    public void destroyConsole() {
        if (frame != null) {
            frame.setVisible(false);
            frame.dispose();
        }
    }
}
