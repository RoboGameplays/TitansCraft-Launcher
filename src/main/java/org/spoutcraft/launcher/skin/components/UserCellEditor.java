
package org.spoutcraft.launcher.skin.components;

import net.titanscraft.launchercore.install.user.User;
import net.titanscraft.launchercore.install.user.skins.ISkinListener;
import net.titanscraft.launchercore.install.user.skins.SkinRepository;
import net.titanscraft.launchercore.util.ImageUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class UserCellEditor implements ComboBoxEditor, DocumentListener, ISkinListener {
    private Font textFont;

    private static final int ICON_WIDTH = 32;
    private static final int ICON_HEIGHT = 32;

    private JPanel parentPanel;
    private JLabel userLabel;
    private JTextField textField;
    private CardLayout layout;

    private Object currentObject;
    private HashMap<String, Icon> headMap = new HashMap<String, Icon>();

    Collection<ActionListener> actionListeners = new HashSet<ActionListener>();
    private SkinRepository mSkinRepo;

    private static final String USER = "user";
    private static final String STRING = "string";

    public UserCellEditor(Font font, SkinRepository skinRepo) {
        this.textFont = font;
        this.mSkinRepo = skinRepo;
        this.mSkinRepo.addListener(this);

        layout = new CardLayout();

        parentPanel = new JPanel();
        parentPanel.setLayout(layout);

        userLabel = new JLabel();
        userLabel.setOpaque(true);
        userLabel.setFont(textFont);
        userLabel.setBackground(Color.white);
        parentPanel.add(userLabel, USER);

        textField = new JTextField();
        textField.setOpaque(true);
        textField.setFont(textFont);
        textField.setBackground(Color.white);
        textField.setBorder(null);
        textField.getDocument().addDocumentListener(this);
        parentPanel.add(textField, STRING);
    }

    @Override
    public Component getEditorComponent() {
        return parentPanel;
    }

    @Override
    public void setItem(Object anObject) {
        currentObject = anObject;

        if (anObject instanceof User) {
            User user = (User) anObject;
            userLabel.setText(user.getDisplayName());
            userLabel.setIconTextGap(8);

            if (!headMap.containsKey(user.getUsername())) {
                headMap.put(user.getUsername(), new ImageIcon(ImageUtils.scaleImage(mSkinRepo.getFaceImage(user), ICON_WIDTH, ICON_HEIGHT)));
            }

            Icon head = headMap.get(user.getUsername());
            userLabel.setIcon(head);

            layout.show(parentPanel, USER);
        } else {
            String newText = "";

            if (anObject != null) {
                newText = anObject.toString();
            }

            if (!textField.getText().equals(newText))
                textField.setText(newText);

            layout.show(parentPanel, STRING);
            textField.requestFocus();
        }
    }

    @Override
    public Object getItem() {
        return currentObject;
    }

    @Override
    public void selectAll() {
    }

    @Override
    public void addActionListener(ActionListener l) {
        actionListeners.add(l);
    }

    @Override
    public void removeActionListener(ActionListener l) {
        actionListeners.remove(l);
    }

    public void addKeyListener(KeyListener k) {
        userLabel.addKeyListener(k);
    }

    public void removeKeyListener(KeyListener k) {
        userLabel.removeKeyListener(k);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        currentObject = textField.getText();
        for (ActionListener listener : actionListeners) {
            listener.actionPerformed(new ActionEvent(this, 0, "edited"));
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        currentObject = textField.getText();
        for (ActionListener listener : actionListeners) {
            listener.actionPerformed(new ActionEvent(this, 0, "edited"));
        }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        currentObject = textField.getText();
        for (ActionListener listener : actionListeners) {
            listener.actionPerformed(new ActionEvent(this, 0, "edited"));
        }
    }

    @Override
    public void skinReady(User user) {
    }

    @Override
    public void faceReady(User user) {

    }
}
