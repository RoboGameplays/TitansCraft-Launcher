
package org.spoutcraft.launcher.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class DateOutputFormatter extends Formatter {
    private final SimpleDateFormat date;

    public DateOutputFormatter(SimpleDateFormat date) {
        this.date = date;
    }

    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder();

        builder.append(date.format(record.getMillis()));
        builder.append(" [");
        builder.append(record.getLevel().getLocalizedName().toUpperCase());
        builder.append("] ");
        builder.append(formatMessage(record));
        builder.append('\n');

        if (record.getThrown() != null) {
            StringWriter writer = new StringWriter();
            record.getThrown().printStackTrace(new PrintWriter(writer));
            builder.append(writer.toString());
        }

        return builder.toString();
    }
}
