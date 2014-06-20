
package org.spoutcraft.launcher.util;

import org.spoutcraft.launcher.log.Console;

public class ShutdownThread extends Thread {
    private Console console;

    public ShutdownThread(Console console) {
        super("Shutdown Thread");
        this.console = console;
        this.setDaemon(true);
    }

    @Override
    public void run() {
        if (console.getHandler() != null) {
            console.getHandler().flush();
        }
    }
}
