
package org.spoutcraft.launcher.log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerOutputStream extends ByteArrayOutputStream {
    private final String separator = System.getProperty("line.separator");
    private final Console console;
    private final Level level;
    private final Logger log;

    public LoggerOutputStream(Console console, Level level, Logger log) {
        super();
        this.console = console;
        this.level = level;
        this.log = log;
    }

    @Override
    public synchronized void flush() throws IOException {
        super.flush();
        String record = this.toString();
        super.reset();

        if (record.length() > 0 && !record.equals(separator)) {
            log.logp(level, "LoggerOutputStream", "log" + level, record);
            if (console.getFrame() != null) {
                console.getFrame().log(record + "\n");
            }
        }
    }
}
