
package org.spoutcraft.launcher.util;

import org.spoutcraft.launcher.entrypoint.SpoutcraftLauncher;
import org.spoutcraft.launcher.log.DateOutputFormatter;

import java.text.SimpleDateFormat;
import java.util.logging.LogRecord;

public class TechnicLogFormatter extends DateOutputFormatter {

    private String launcherBuild;

    public TechnicLogFormatter() {
        super(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));
        launcherBuild = SpoutcraftLauncher.getLauncherBuild();
    }

    @Override
    public String format(LogRecord record) {
        return "[B#" + launcherBuild + "] " + super.format(record);
    }
}
