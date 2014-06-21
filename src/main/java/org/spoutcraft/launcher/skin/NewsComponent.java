
package org.spoutcraft.launcher.skin;

import net.titanscraft.launchercore.exception.RestfulAPIException;
import net.titanscraft.launchercore.restful.PlatformConstants;
import net.titanscraft.launchercore.restful.RestObject;
import net.titanscraft.launchercore.restful.platform.Article;
import net.titanscraft.launchercore.restful.platform.News;
import net.titanscraft.launchercore.util.Utils;
import org.spoutcraft.launcher.skin.components.HyperlinkJTextPane;
import org.spoutcraft.launcher.skin.components.RoundedBox;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.logging.Level;

public class NewsComponent extends JComponent {
    private static final long serialVersionUID = 1L;

    public NewsComponent() {
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(LauncherFrame.getMinecraftFont(10));
    }

    public void loadArticles() {
        try {
            List<Article> articles = RestObject.getRestObject(News.class, PlatformConstants.NEWS).getNews();
            setupArticles(articles);
        } catch (RestfulAPIException e) {
            Utils.getLogger().log(Level.WARNING, "Impossivel carregar noticias, escondendo a seção de noticias", e);
            this.setVisible(false);
            this.setEnabled(false);
        }
    }

    private void setupArticles(List<Article> articles) {
        Font articleFont = LauncherFrame.getMinecraftFont(10);
        int width = getWidth() - 16;
        int height = getHeight() / 2 - 16;

        for (int i = 0; i < 2; i++) {
            Article article = articles.get(i);
            String date = article.getDate();
            String title = article.getDisplayTitle();
            HyperlinkJTextPane link = new HyperlinkJTextPane(date + "\n" + title, article.getUrl());
            link.setFont(articleFont);
            link.setForeground(Color.WHITE);
            link.setBackground(new Color(255, 255, 255, 0));
            link.setBounds(8, 8 + ((height + 8) * i), width, height);
            this.add(link);
        }

        RoundedBox background = new RoundedBox(LauncherFrame.TRANSPARENT);
        background.setBounds(0, 0, getWidth(), getHeight());
        this.add(background);
        this.repaint();
    }
}
