
package org.spoutcraft.launcher.skin.components;

import org.spoutcraft.launcher.util.DesktopUtils;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;

public class HyperlinkJLabel extends TransparentJLabel implements MouseListener {
    private static final long CLICK_DELAY = 250L;
    private static final long serialVersionUID = 1L;
    private String url;
    private long lastClick = System.currentTimeMillis();

    public HyperlinkJLabel(String text, String url) {
        super(text);
        this.url = url;
        super.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (lastClick + CLICK_DELAY > System.currentTimeMillis()) {
            return;
        }
        lastClick = System.currentTimeMillis();
        try {
            URI uri = new java.net.URI(url);
            HyperlinkJLabel.browse(uri);
        } catch (Exception ex) {
            System.err.println("Unable to open browser to " + url);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    public static void browse(URI uri) {
        DesktopUtils.browse(uri);
    }
}
