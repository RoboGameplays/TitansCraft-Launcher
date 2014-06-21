
package org.spoutcraft.launcher.skin.options;

import net.titanscraft.launchercore.install.AvailablePackList;
import net.titanscraft.launchercore.install.InstalledPack;
import net.titanscraft.launchercore.util.ResourceUtils;
import net.titanscraft.launchercore.util.Utils;
import net.titanscraft.launchercore.util.ZipUtils;
import org.spoutcraft.launcher.skin.LauncherFrame;
import org.spoutcraft.launcher.skin.components.ImageButton;
import org.spoutcraft.launcher.skin.components.LiteButton;
import org.spoutcraft.launcher.skin.components.LiteTextBox;
import org.spoutcraft.launcher.util.DesktopUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class ModpackOptions extends JDialog implements ActionListener, MouseListener, MouseMotionListener {
    private static final long serialVersionUID = 1L;
    private static final int FRAME_WIDTH = 300;
    private static final int FRAME_HEIGHT = 300;
    private static final String QUIT_ACTION = "quit";
    private static final String SAVE_ACTION = "save";
    private static final String BUILD_ACTION = "build";
    private static final String REC_ACTION = "rec";
    private static final String LATEST_ACTION = "latest";
    private static final String MANUAL_ACTION = "manual";
    private static final String CHANGEFOLDER_ACTION = "changefolder";
    private static final String OPENFOLDER_ACTION = "openfolder";
    private static final String CLEAN_BIN_ACTION = "cleanbin";
    private static final String ESCAPE_ACTION = "escape";
    private String build;
    private JLabel buildLabel;
    private JLabel background;
    private InstalledPack installedPack;
    private JComboBox buildSelector;
    private LiteTextBox packLocation;
    private LiteButton openFolder;
    private LiteButton cleanBin;
    private File installedDirectory;
    private JFileChooser fileChooser;
    private boolean directoryChanged = false;
    private int mouseX = 0, mouseY = 0;

    private AvailablePackList mPackList;

    public ModpackOptions(InstalledPack installedPack, AvailablePackList packList) {
        this.mPackList = packList;
        this.installedPack = installedPack;
        setTitle("Opções do Modpack");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        addMouseListener(this);
        addMouseMotionListener(this);
        setResizable(false);
        setUndecorated(true);
        initComponents();
    }

    private void initComponents() {
        Font minecraft = LauncherFrame.getMinecraftFont(12);

        KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        Action escapeAction = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        };

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, ESCAPE_ACTION);
        getRootPane().getActionMap().put(ESCAPE_ACTION, escapeAction);

        background = new JLabel();
        background.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        LauncherFrame.setIcon(background, "optionsBackground.png", background.getWidth(), background.getHeight());

        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        JLabel optionsTitle = new JLabel();
        optionsTitle.setBounds(10, 10, FRAME_WIDTH, 25);
        optionsTitle.setText(installedPack.getDisplayName() + " Opções");
        optionsTitle.setForeground(Color.white);
        optionsTitle.setFont(minecraft.deriveFont(14F));

        ImageButton optionsQuit = new ImageButton(ResourceUtils.getIcon("quit.png", 28, 28), ResourceUtils.getIcon("quit.png", 28, 28));
        optionsQuit.setRolloverIcon(ResourceUtils.getIcon("quitHover.png", 28, 28));
        optionsQuit.setBounds(FRAME_WIDTH - 38, 10, 28, 28);
        optionsQuit.setActionCommand(QUIT_ACTION);
        optionsQuit.addActionListener(this);

        buildLabel = new JLabel();
        buildLabel.setBounds(10, 50, 140, 25);
        buildLabel.setText("Selecionar Build");
        buildLabel.setForeground(Color.white);
        buildLabel.setFont(minecraft);

        buildSelector = new JComboBox();
        buildSelector.setBounds(FRAME_WIDTH / 2, 50, 140, 25);
        buildSelector.setActionCommand(BUILD_ACTION);
        buildSelector.addActionListener(this);
        populateBuilds(buildSelector);

        build = installedPack.getRawBuild();
        if (build == null) {
            build = InstalledPack.RECOMMENDED;
        }

        ButtonGroup group = new ButtonGroup();

        JRadioButton versionRec = new JRadioButton("Sempre usar builds recomendadas");
        versionRec.setBounds(10, buildLabel.getY() + buildLabel.getHeight() + 10, FRAME_WIDTH - 20, 30);
        versionRec.setFont(minecraft);
        versionRec.setForeground(Color.white);
        versionRec.setContentAreaFilled(false);
        versionRec.setActionCommand(REC_ACTION);
        versionRec.addActionListener(this);
        group.add(versionRec);

        JRadioButton versionLatest = new JRadioButton("Sempre usar builds mais atuais");
        versionLatest.setBounds(10, versionRec.getY() + versionRec.getHeight(), FRAME_WIDTH - 20, 30);
        versionLatest.setFont(minecraft);
        versionLatest.setForeground(Color.white);
        versionLatest.setContentAreaFilled(false);
        versionLatest.setActionCommand(LATEST_ACTION);
        versionLatest.addActionListener(this);
        group.add(versionLatest);

        JRadioButton versionManual = new JRadioButton("Selecionar manualmente a build");
        versionManual.setBounds(10, versionLatest.getY() + versionLatest.getHeight(), FRAME_WIDTH - 20, 30);
        versionManual.setFont(minecraft);
        versionManual.setForeground(Color.white);
        versionManual.setContentAreaFilled(false);
        versionManual.setActionCommand(MANUAL_ACTION);
        versionManual.addActionListener(this);
        group.add(versionManual);

        if (installedPack.isLocalOnly()) {
            buildSelector.setEnabled(false);

            if (build.equals(InstalledPack.LATEST))
                versionLatest.setSelected(true);
            else if (build.equals(InstalledPack.RECOMMENDED))
                versionRec.setSelected(true);
            else
                versionManual.setSelected(true);

            versionLatest.setEnabled(false);
            versionRec.setEnabled(false);
            versionManual.setEnabled(false);
        } else if (build.equals(InstalledPack.LATEST)) {
            buildSelector.setEnabled(false);
            buildSelector.setSelectedItem(new BuildLabel(installedPack.getInfo().getLatest()));
            versionLatest.setSelected(true);
            build = InstalledPack.LATEST;
        } else if (build.equals(InstalledPack.RECOMMENDED)) {
            buildSelector.setEnabled(false);
            buildSelector.setSelectedItem(new BuildLabel(installedPack.getInfo().getRecommended()));
            versionRec.setSelected(true);
            build = InstalledPack.RECOMMENDED;
        } else {
            versionManual.setSelected(true);
            buildSelector.setSelectedItem(new BuildLabel(build));
        }

        installedDirectory = installedPack.getInstalledDirectory();

        packLocation = new LiteTextBox(this, "");
        packLocation.setBounds(10, versionManual.getY() + versionManual.getHeight() + 10, FRAME_WIDTH - 20, 25);
        packLocation.setFont(minecraft.deriveFont(10F));
        packLocation.setText(installedDirectory.getPath());
        packLocation.setEnabled(false);

        fileChooser = new JFileChooser(Utils.getLauncherDirectory());
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        LiteButton changeFolder = new LiteButton("Mudar Pasta");
        changeFolder.setBounds(FRAME_WIDTH / 2 + 10, packLocation.getY() + packLocation.getHeight() + 10, FRAME_WIDTH / 2 - 20, 25);
        changeFolder.setFont(minecraft);
        changeFolder.setActionCommand(CHANGEFOLDER_ACTION);
        changeFolder.addActionListener(this);

        openFolder = new LiteButton("Abrir Pasta");
        openFolder.setBounds(10, packLocation.getY() + packLocation.getHeight() + 10, FRAME_WIDTH / 2 - 20, 25);
        openFolder.setFont(minecraft);
        openFolder.setActionCommand(OPENFOLDER_ACTION);
        openFolder.addActionListener(this);

        if (!installedDirectory.exists()) {
            openFolder.setVisible(false);
        }

        LiteButton save = new LiteButton("Salvar");
        save.setFont(minecraft.deriveFont(14F));
        save.setBounds(FRAME_WIDTH / 2 + 10, FRAME_HEIGHT - 40, FRAME_WIDTH / 2 - 20, 25);
        save.setActionCommand(SAVE_ACTION);
        save.addActionListener(this);

        cleanBin = new LiteButton("Redefinir Modpack");
        cleanBin.setFont(minecraft.deriveFont(14F));
        cleanBin.setBounds(10, FRAME_HEIGHT - 40, FRAME_WIDTH / 2 - 20, 25);
        cleanBin.setActionCommand(CLEAN_BIN_ACTION);
        cleanBin.addActionListener(this);

        if (installedPack.isLocalOnly())
            cleanBin.setEnabled(false);

        contentPane.add(optionsTitle);
        contentPane.add(optionsQuit);
        contentPane.add(buildLabel);
        contentPane.add(buildSelector);
        contentPane.add(versionRec);
        contentPane.add(versionLatest);
        contentPane.add(versionManual);
        contentPane.add(packLocation);
        contentPane.add(changeFolder);
        contentPane.add(openFolder);
        contentPane.add(save);
        contentPane.add(cleanBin);
        contentPane.add(background);

        setLocationRelativeTo(this.getOwner());
    }

    private void populateBuilds(JComboBox buildSelector) {

        if (installedPack.getInfo() != null) {
            for (String build : installedPack.getInfo().getBuilds()) {
                System.out.println(installedPack.getInfo());
                String display = build;
                if (build.equals(installedPack.getInfo().getLatest())) {
                    display += " - Mais Recente";
                } else if (build.equals(installedPack.getInfo().getRecommended())) {
                    display += " - Recomendado";
                }
                BuildLabel label = new BuildLabel(build, display);
                buildSelector.addItem(label);
            }

            if (installedPack.getInfo().getBuilds().size() == 0) {
                if (!installedPack.getInfo().getRecommended().equals(installedPack.getInfo().getLatest()))
                    buildSelector.addItem(new BuildLabel(installedPack.getInfo().getRecommended(), installedPack.getInfo().getRecommended() + " - Recommended"));

                buildSelector.addItem(new BuildLabel(installedPack.getInfo().getLatest(), installedPack.getInfo().getLatest() + " - Latest"));
            }
        } else {
            BuildLabel label = new BuildLabel(installedPack.getBuild(), installedPack.getBuild());
            buildSelector.addItem(label);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JComponent) {
            action(e.getActionCommand(), (JComponent) e.getSource());
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        this.setLocation(e.getXOnScreen() - mouseX, e.getYOnScreen() - mouseY);
    }

    private void action(String action, JComponent c) {
        if (action.equals(QUIT_ACTION)) {
            dispose();
        } else if (action.equals(SAVE_ACTION)) {
            if (directoryChanged) {
                directoryChanged = false;
                installedPack.setPackDirectory(installedDirectory);
            }
            installedPack.setBuild(build);
            mPackList.save();
            dispose();
        } else if (action.equals(BUILD_ACTION)) {
            build = ((BuildLabel) buildSelector.getSelectedItem()).getBuild();
        } else if (action.equals(REC_ACTION)) {
            buildSelector.setEnabled(false);
            buildSelector.setSelectedItem(new BuildLabel(installedPack.getInfo().getRecommended()));
            build = InstalledPack.RECOMMENDED;
        } else if (action.equals(LATEST_ACTION)) {
            buildSelector.setEnabled(false);
            buildSelector.setSelectedItem(new BuildLabel(installedPack.getInfo().getLatest()));
            build = InstalledPack.LATEST;
        } else if (action.equals(MANUAL_ACTION)) {
            buildSelector.setEnabled(true);
            build = ((BuildLabel) buildSelector.getSelectedItem()).getBuild();
        } else if (action.equals(OPENFOLDER_ACTION)) {
            if (installedDirectory.exists()) {
                DesktopUtils.open(installedDirectory);
            }
        } else if (action.equals(CHANGEFOLDER_ACTION)) {
            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!ZipUtils.checkEmpty(file)) {
                    JOptionPane.showMessageDialog(c, "Por favor selecione uma pasta vazia", "Localização Invalida", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                packLocation.setText(file.getPath());
                installedDirectory = file;
                directoryChanged = true;
                if (file.exists()) {
                    openFolder.setVisible(true);
                }
            }
        } else if (action.equals(CLEAN_BIN_ACTION)) {
            int result = JOptionPane.showConfirmDialog(c, "Tem certeza que deseja remover o modpack?", "Remover", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
                cleanBin();
                dispose();
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    private void cleanBin() {
        File versionFile = new File(installedPack.getBinDir(), "version");
        if (versionFile.exists()) {
            versionFile.delete();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    private class BuildLabel {
        private final String build;
        private final String display;

        public BuildLabel(String build) {
            this(build, build);
        }

        public BuildLabel(String build, String display) {
            this.build = build;
            this.display = display;
        }

        @Override
        public int hashCode() {
            return build.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof BuildLabel) {
                BuildLabel label = (BuildLabel) obj;
                return (getBuild().equals(label.getBuild()));
            }
            return false;
        }

        @Override
        public String toString() {
            return display;
        }

        public String getBuild() {
            return build;
        }
    }


}
