package UI;

import State.GameState;
import main.game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class SplashScreen {

    private int splashTimer = 0;
    private static final int SPLASH_DISPLAY_TIME = 480;
    private boolean skipRequested = false;
    private float alpha = 0.0f;
    private float fadeSpeed = 0.02f;
    private boolean fadingIn = true;

    public SplashScreen(game Game) {
    }

    public void update() {
        splashTimer++;

        if (fadingIn) {
            alpha += fadeSpeed;
            if (alpha >= 1.0f) {
                alpha = 1.0f;
                fadingIn = false;
            }
        } else if (splashTimer >= SPLASH_DISPLAY_TIME - 60) {
            alpha -= fadeSpeed;
            if (alpha < 0.0f) {
                alpha = 0.0f;
            }
        }

        if (skipRequested || splashTimer >= SPLASH_DISPLAY_TIME) {
            GameState.gameState = GameState.MENU;
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        drawGradientBackground(g2d);
        drawWelcomeText(g2d);

        if (splashTimer > 60) {
            drawContinueHint(g2d);
        }

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    private void drawGradientBackground(Graphics2D g2d) {
        Color color1 = new Color(21, 40, 20);
        Color color2 = new Color(40, 80, 41);
        Color color3 = new Color(87, 120, 60);

        for (int y = 0; y < game.GAME_HEIGHT; y++) {
            float ratio = (float) y / game.GAME_HEIGHT;
            Color color;

            if (ratio < 0.5f) {
                color = blend(color1, color2, ratio * 2);
            } else {
                color = blend(color2, color3, (ratio - 0.5f) * 2);
            }

            g2d.setColor(color);
            g2d.drawLine(0, y, game.GAME_WIDTH, y);
        }

        g2d.setColor(new Color(255, 255, 255, 100));
        for (int i = 0; i < 50; i++) {
            int x = (int) (Math.random() * game.GAME_WIDTH);
            int y = (int) (Math.random() * game.GAME_HEIGHT);
            int size = (int) (Math.random() * 3) + 1;
            g2d.fillOval(x, y, size, size);
        }
    }

    private Color blend(Color c1, Color c2, float ratio) {
        int r = (int) (c1.getRed() * (1 - ratio) + c2.getRed() * ratio);
        int g = (int) (c1.getGreen() * (1 - ratio) + c2.getGreen() * ratio);
        int b = (int) (c1.getBlue() * (1 - ratio) + c2.getBlue() * ratio);
        return new Color(r, g, b);
    }

    private void drawWelcomeText(Graphics2D g2d) {
        int centerX = game.GAME_WIDTH / 2;
        int textY = 150;

        g2d.setFont(new Font("Arial", Font.BOLD, 56));
        FontMetrics fm = g2d.getFontMetrics();
        String welcomeText = "Welcome to";
        int textX = centerX - fm.stringWidth(welcomeText) / 2;

        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.drawString(welcomeText, textX + 3, textY + 3);
        g2d.setColor(Color.WHITE);
        g2d.drawString(welcomeText, textX, textY);

        g2d.setFont(new Font("Arial", Font.BOLD, 72));
        fm = g2d.getFontMetrics();
        String gameName = "HOPMAN";
        textX = centerX - fm.stringWidth(gameName) / 2;
        textY += 80;

        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.drawString(gameName, textX + 4, textY + 4);
        g2d.setColor(Color.BLACK);
        g2d.drawString(gameName, textX, textY);

        g2d.setFont(new Font("Arial", Font.ITALIC, 20));
        fm = g2d.getFontMetrics();
        String subtitle = "An Epic Adventure Awaits";
        textX = centerX - fm.stringWidth(subtitle) / 2;
        textY += 50;
        g2d.setColor(new Color(200, 200, 200));
        g2d.drawString(subtitle, textX, textY);
    }

    private void drawContinueHint(Graphics2D g2d) {
        int centerX = game.GAME_WIDTH / 2;
        int hintY = game.GAME_HEIGHT - 80;

        float blinkAlpha = (float) (Math.sin(splashTimer * 0.1) * 0.5 + 0.5);
        g2d.setColor(new Color(255, 255, 255, (int) (blinkAlpha * 255)));
        g2d.setFont(new Font("Arial", Font.PLAIN, 18));

        FontMetrics fm = g2d.getFontMetrics();
        String hintText = "Press any key or click to continue...";
        int textX = centerX - fm.stringWidth(hintText) / 2;
        g2d.drawString(hintText, textX, hintY);
    }

    public void keyPressed(KeyEvent e) {
        skipRequested = true;
    }

    public void mousePressed(MouseEvent e) {
        skipRequested = true;
    }

    public void mouseClicked(MouseEvent e) {
        skipRequested = true;
    }

    public boolean isComplete() {
        return skipRequested || splashTimer >= SPLASH_DISPLAY_TIME;
    }
}
