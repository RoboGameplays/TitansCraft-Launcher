
package org.spoutcraft.launcher.skin;

import javax.swing.*;
import java.awt.*;

public class ProgressSplashScreen extends SplashScreen {
    private static final long serialVersionUID = 1L;
    private JProgressBar progressBar = new JProgressBar();

    public ProgressSplashScreen() {
        super(Toolkit.getDefaultToolkit().getImage(SplashScreen.class.getResource("/org/spoutcraft/launcher/resources/splash.png")));

        // Setup the progress bar
        progressBar.setFont(new Font("Arial", Font.PLAIN, 11));
        progressBar.setMaximum(100);
        progressBar.setBounds(0, icon.getIconHeight(), icon.getIconWidth(), 20);
        progressBar.setString("Baixando atualização do launcher...");
        getContentPane().add(progressBar);
        setVisible(true);
    }

    public void updateProgress(int percent) {
        if (percent >= 0 && percent <= 100) {
            progressBar.setValue(percent);
        }
    }
}
