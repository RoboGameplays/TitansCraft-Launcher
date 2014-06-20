
package org.spoutcraft.launcher.skin.components;

import org.spoutcraft.launcher.util.DesktopUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class ImageHyperlinkButton extends JButton {
    private static final long serialVersionUID = 1L;
    private String url;

    public ImageHyperlinkButton(String url) {
        this.url = url;
        this.addActionListener(new ButtonClickHandler());
        setBorder(null);
        setOpaque(false);
        setFocusable(false);
        setContentAreaFilled(false);
    }

    private class ButtonClickHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            try {
                DesktopUtils.browse((new URL(url).toURI()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public void setURL(String url) {
        this.url = url;
    }
}
