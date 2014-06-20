
package org.spoutcraft.launcher.skin.components;

import net.titanscraft.launchercore.install.user.User;
import net.titanscraft.launchercore.install.user.skins.ISkinListener;
import net.titanscraft.launchercore.install.user.skins.SkinRepository;
import net.titanscraft.launchercore.util.ImageUtils;
import net.titanscraft.launchercore.util.ResourceUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

public class UserCellRenderer extends JLabel implements ListCellRenderer, ISkinListener {
    private Font textFont;
    private Icon addUserIcon;

    private SkinRepository mSkinRepo;

    private static final int ICON_WIDTH = 32;
    private static final int ICON_HEIGHT = 32;

    private HashMap<String, Icon> headMap = new HashMap<String, Icon>();

    public UserCellRenderer(Font font, SkinRepository skinRepo) {
        this.mSkinRepo = skinRepo;
        this.mSkinRepo.addListener(this);
        this.textFont = font;
        setOpaque(true);

        try {
            addUserIcon = new ImageIcon(ImageUtils.scaleImage(ImageIO.read(ResourceUtils.getResourceAsStream("/org/spoutcraft/launcher/resources/add_user.png")), ICON_WIDTH, ICON_HEIGHT));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        this.setFont(textFont);

        if (value instanceof User) {
            User user = (User) value;
            this.setText(user.getDisplayName());
            this.setIconTextGap(8);

            if (!headMap.containsKey(user.getUsername())) {
                headMap.put(user.getUsername(), new ImageIcon(ImageUtils.scaleImage(mSkinRepo.getFaceImage(user), ICON_WIDTH, ICON_HEIGHT)));
            }

            Icon head = headMap.get(user.getUsername());

            if (head != null) {
                this.setIcon(head);
            }
        } else if (value == null) {
            this.setText("Add New User");
            this.setIconTextGap(8);

            if (addUserIcon != null) {
                this.setIcon(addUserIcon);
            }
        } else {
            this.setIconTextGap(0);
            this.setText(value.toString());
        }

        return this;
    }

    @Override
    public void skinReady(User user) {
    }

    @Override
    public void faceReady(User user) {
        if (headMap.containsKey(user.getUsername()))
            headMap.remove(user.getUsername());

        headMap.put(user.getUsername(), new ImageIcon(ImageUtils.scaleImage(mSkinRepo.getFaceImage(user), ICON_WIDTH, ICON_HEIGHT)));

        this.invalidate();
    }
}
