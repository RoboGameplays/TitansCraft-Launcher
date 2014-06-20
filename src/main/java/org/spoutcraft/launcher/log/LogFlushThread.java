
package org.spoutcraft.launcher.log;

public class LogFlushThread extends Thread {
    private final Console console;

    public LogFlushThread(Console console) {
        super("Log Flush Thread");
        this.console = console;
        this.setDaemon(true);
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            if (console.getHandler() != null) {
                console.getHandler().flush();
            }
            try {
                sleep(60000);
            } catch (InterruptedException e) {
            }
        }
    }
}
