
package org.spoutcraft.launcher.log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.StreamHandler;

public class RotatingFileHandler extends StreamHandler {
    private final SimpleDateFormat date;
    private final String logFile;
    private String filename;

    public RotatingFileHandler(String logFile) {
        this.logFile = logFile;
        date = new SimpleDateFormat("yyyy-MM-dd");
        filename = calculateFilename();
        try {
            setOutputStream(new FileOutputStream(filename, true));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private String calculateFilename() {
        return logFile.replace("%D", date.format(new Date()));
    }

    @Override
    public synchronized void flush() {
        if (!filename.equals(calculateFilename())) {
            filename = calculateFilename();
            try {
                this.close();
                setOutputStream(new FileOutputStream(filename, true));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        super.flush();
    }
}
