
package org.spoutcraft.launcher.skin.components;

import javax.swing.*;

public class EnhancedBackground extends JLabel {
    private final String name;
    private boolean active = false;

    public EnhancedBackground(String name) {
        this.name = name;
        this.setBounds(0, 0, 880, 520);
    }

    @Override
    public String getName() {
        return name;
    }
}
