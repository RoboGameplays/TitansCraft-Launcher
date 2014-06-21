
package org.spoutcraft.launcher.skin.options;

import net.titanscraft.launchercore.install.AvailablePackList;
import net.titanscraft.launchercore.install.InstalledPack;
import net.titanscraft.launchercore.install.user.UserModel;
import net.titanscraft.launchercore.mirror.MirrorStore;
import net.titanscraft.launchercore.restful.PackInfo;
import net.titanscraft.launchercore.restful.PlatformConstants;
import net.titanscraft.launchercore.restful.RestObject;
import net.titanscraft.launchercore.restful.platform.PlatformPackInfo;
import net.titanscraft.launchercore.restful.solder.SolderPackInfo;
import net.titanscraft.launchercore.util.ResourceUtils;
import net.titanscraft.launchercore.util.Utils;
import net.titanscraft.launchercore.util.ZipUtils;
import org.spoutcraft.launcher.skin.LauncherFrame;
import org.spoutcraft.launcher.skin.components.ImageButton;
import org.spoutcraft.launcher.skin.components.LiteButton;
import org.spoutcraft.launcher.skin.components.LiteTextBox;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ImportOptions extends JDialog implements ActionListener, MouseListener, MouseMotionListener, DocumentListener {
    private static final long serialVersionUID = 1L;
    private static final String QUIT_ACTION = "quit";
    private static final String IMPORT_ACTION = "import";
    private static final String CHANGE_FOLDER = "folder";
    private static final String PASTE_URL = "paste";
    private static final String ESCAPE_ACTION = "escape";
    private static final int FRAME_WIDTH = 520;
    private static final int FRAME_HEIGHT = 222;

    private JLabel msgLabel;
    private JLabel background;
    private LiteButton save;
    private LiteButton folder;
    private LiteButton paste;
    private LiteTextBox install;
    private JFileChooser fileChooser;
    private int mouseX = 0, mouseY = 0;
    private PackInfo info = null;
    private String url = "";
    private Document urlDoc;
    private File installDir;
    private LiteTextBox urlTextBox;

    private AvailablePackList mPackList;
    private UserModel mUserModel;
    private MirrorStore mirrorStore;

    public ImportOptions(AvailablePackList packList, UserModel userModel, MirrorStore mirrorStore) {
        this.mPackList = packList;
        this.mUserModel = userModel;
        this.mirrorStore = mirrorStore;

        setTitle("Adicionar Modpack");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        addMouseListener(this);
        addMouseMotionListener(this);
        setResizable(false);
        setUndecorated(true);
        initComponents();
    }

    public void initComponents() {
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
        LauncherFrame.setIcon(background, "platformBackground.png", background.getWidth(), background.getHeight());

        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        ImageButton optionsQuit = new ImageButton(ResourceUtils.getIcon("quit.png", 28, 28), ResourceUtils.getIcon("quit.png", 28, 28));
        optionsQuit.setRolloverIcon(ResourceUtils.getIcon("quitHover.png", 28, 28));
        optionsQuit.setBounds(FRAME_WIDTH - 38, 10, 28, 28);
        optionsQuit.setActionCommand(QUIT_ACTION);
        optionsQuit.addActionListener(this);

        msgLabel = new JLabel();
        msgLabel.setBounds(10, 75, FRAME_WIDTH - 20, 25);
        msgLabel.setText("Coloque a URL do Modpack a baixo:");
        msgLabel.setForeground(Color.white);
        msgLabel.setFont(minecraft);

        urlTextBox = new LiteTextBox(this, "Coloque a URL do Modpack aqui");
        urlTextBox.setBounds(10, msgLabel.getY() + msgLabel.getHeight() + 5, FRAME_WIDTH - 115, 30);
        urlTextBox.setFont(minecraft);
        urlTextBox.getDocument().addDocumentListener(this);
        urlDoc = urlTextBox.getDocument();

        save = new LiteButton("Adicionar Modpack");
        save.setFont(minecraft.deriveFont(14F));
        save.setBounds(FRAME_WIDTH - 145, FRAME_HEIGHT - 40, 135, 30);
        save.setActionCommand(IMPORT_ACTION);
        save.addActionListener(this);

        fileChooser = new JFileChooser(Utils.getLauncherDirectory());
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        folder = new LiteButton("Mudar Pasta");
        folder.setFont(minecraft.deriveFont(14F));
        folder.setBounds(FRAME_WIDTH - 290, FRAME_HEIGHT - 40, 135, 30);
        folder.setActionCommand(CHANGE_FOLDER);
        folder.addActionListener(this);

        paste = new LiteButton("Colar");
        paste.setFont(minecraft.deriveFont(14F));
        paste.setBounds(FRAME_WIDTH - 95, msgLabel.getY() + msgLabel.getHeight() + 5, 85, 30);
        paste.setActionCommand(PASTE_URL);
        paste.addActionListener(this);
        paste.setVisible(true);

        install = new LiteTextBox(this, "");
        install.setBounds(10, FRAME_HEIGHT - 75, FRAME_WIDTH - 20, 25);
        install.setFont(minecraft.deriveFont(10F));
        install.setEnabled(false);
        install.setVisible(false);

        enableComponent(save, false);
        enableComponent(folder, false);
        enableComponent(paste, true);

        contentPane.add(install);
        contentPane.add(optionsQuit);
        contentPane.add(msgLabel);
        contentPane.add(folder);
        contentPane.add(paste);
        contentPane.add(urlTextBox);
        contentPane.add(save);
        contentPane.add(background);

        setLocationRelativeTo(this.getOwner());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JComponent) {
            action(e.getActionCommand());
        }
    }

    private void action(String action) {
        if (action.equals(QUIT_ACTION)) {
            dispose();
        } else if (action.equals(CHANGE_FOLDER)) {
            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                if (!ZipUtils.checkEmpty(file)) {
                    install.setText("Por favor use uma pasta vazia.");
                    return;
                }

                if (info.shouldForceDirectory() && file.getAbsolutePath().startsWith(Utils.getSettingsDirectory().getAbsolutePath())) {
                    install.setText("Este pacote requer um diretório fora da " + Utils.getSettingsDirectory().getAbsolutePath());
                    return;
                }
                installDir = file;

                install.setText("Localização: " + installDir.getPath());
                folder.setText("Mudar Pasta");
                folder.setLocation(FRAME_WIDTH - 290, FRAME_HEIGHT - 40);
                enableComponent(save, true);
            }
        } else if (action.equals(IMPORT_ACTION)) {
            if (info != null) {
                InstalledPack pack = new InstalledPack(mirrorStore, info.getName(), true);
                pack.setInfo(info);
                mPackList.add(pack);
                dispose();
            }
        } else if (action.equals(PASTE_URL)) {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable clipData = clipboard.getContents(clipboard);
            if (clipData != null) {
                try {
                    if (clipData.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                        String s = (String) (clipData.getTransferData(DataFlavor.stringFlavor));
                        urlDoc.remove(0, urlDoc.getLength());
                        urlDoc.insertString(0, s, new SimpleAttributeSet());
                        urlTextBox.setLabelVisible(false);
                    }
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void urlUpdated(Document doc) {
        try {
            final String url = doc.getText(0, doc.getLength()).trim();

            if (url.isEmpty()) {
                msgLabel.setText("Coloque a URL da plataforma TitansCraft");
                enableComponent(save, false);
                enableComponent(folder, false);
                enableComponent(install, false);
                info = null;
                this.url = "";
                return;
            }

            if (matchUrl(url)) {
                msgLabel.setText("Tentando encontrar informações do Modpack...");
                // Turn everything off while the data is being fetched
                enableComponent(urlTextBox, false);
                enableComponent(paste, false);
                enableComponent(install, false);
                enableComponent(folder, false);
                enableComponent(save, false);
                // fetch the info asynchronously
                SwingWorker<PlatformPackInfo, Void> worker = new SwingWorker<PlatformPackInfo, Void>() {

                    @Override
                    protected PlatformPackInfo doInBackground() throws Exception {
                        return RestObject.getRestObject(PlatformPackInfo.class, url);
                    }

                    @Override
                    public void done() {
                        try {
                            PlatformPackInfo result = get();
                            if (!result.hasSolder() && !(result.getUrl().startsWith("http://") || result.getUrl().startsWith("https://"))) {
                                msgLabel.setText("Link de download do modpack invalido, procure informar esse erro ao dono do modpack.");
                                return;
                            } else {
                                if (result.hasSolder()) {
                                    info = SolderPackInfo.getSolderPackInfo(result.getSolder(), result.getName(), mUserModel.getCurrentUser());
                                } else {
                                    info = result;
                                }
                            }
                            System.out.println(info.getName());
                            msgLabel.setText("Modpack: " + info.getDisplayName());
                            ImportOptions.this.url = url;
                            enableComponent(folder, true);
                            enableComponent(install, true);
                            if (info.shouldForceDirectory()) {
                                install.setText("Este pacote requer um diretório fora da " + Utils.getSettingsDirectory().getAbsolutePath());
                                folder.setText("Selecionar");
                                folder.setLocation(FRAME_WIDTH - 145, FRAME_HEIGHT - 40);
                                enableComponent(save, false);
                            } else {
                                installDir = new File(Utils.getLauncherDirectory(), info.getName());
                                install.setText("Localização: " + installDir.getPath());
                                enableComponent(save, true);
                            }
                        } catch (ExecutionException e) {
                            msgLabel.setText("Resposta da plataforma a análise de erros");
                            enableComponent(save, false);
                            enableComponent(folder, false);
                            enableComponent(install, false);
                            info = null;
                            ImportOptions.this.url = "";
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            // TODO Interrupted exception?
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                            msgLabel.setText("Modpack tem Link solder inválido. Consulte autor do modpack.");
                        } finally {
                            // always turn these back on
                            enableComponent(urlTextBox, true);
                            enableComponent(paste, true);
                        }
                    }
                };
                worker.execute();
            } else {
                msgLabel.setText("URL fornecido pela Plataforma TitansCraft é inválido");
                enableComponent(save, false);
                enableComponent(folder, false);
                enableComponent(install, false);
                info = null;
                this.url = "";
            }

        } catch (BadLocationException e) {
            // This should never ever happen.
            // Java is stupid for not having a getAllText of some kind on the
            // Document class
            e.printStackTrace();
        }
    }

    public boolean matchUrl(String url) {
        boolean result = false;
        result = (url.matches(PlatformConstants.MODPACK + "([a-zA-Z0-9-]+)") || result);
        return result;
    }

    public void enableComponent(JComponent component, boolean enable) {
        component.setEnabled(enable);
        component.setVisible(enable);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        this.setLocation(e.getXOnScreen() - mouseX, e.getYOnScreen() - mouseY);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub

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

    @Override
    public void changedUpdate(DocumentEvent e) {
        urlUpdated(e.getDocument());
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        urlUpdated(e.getDocument());
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        urlUpdated(e.getDocument());
    }
}
