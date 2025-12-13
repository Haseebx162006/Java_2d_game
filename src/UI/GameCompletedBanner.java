package UI;

import Function.LoadSave;
import State.GameState;
import State.Playing;
import main.game;
import static Function.features.UI.URM.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class GameCompletedBanner {
    private Playing playing;
    private URMButtons menu;
    private BufferedImage image;
    private int backgroundX, backgroundY, backgroundW, backgroundH;
    
    public GameCompletedBanner(Playing playing) {
        this.playing = playing;
        LoadImg();
        LoadButtons();
    }
    
    public void update() {
        menu.update();
    }
    
    private void LoadButtons() {
        int menu_ButtonX = (int)(387 * game.SCALE);
        int y = (int)(250 * game.SCALE);
        menu = new URMButtons(menu_ButtonX, y, URM_BUTTON, URM_BUTTON, 2);
    }
    
    private void LoadImg() {
        // Use the same completed sprite image or create a custom one
        image = LoadSave.GetAtlas(LoadSave.LOAD_COMPLETE_LEVEL);
        backgroundW = (int)(image.getWidth() * game.SCALE);
        backgroundH = (int)(image.getHeight() * game.SCALE);
        backgroundX = game.GAME_WIDTH / 2 - backgroundW / 2;
        backgroundY = (int)(75 * game.SCALE);
    }
    
    public void draw(Graphics g) {
        // Semi-transparent overlay
        g.setColor(new Color(0, 0, 0, 220));
        g.fillRect(0, 0, game.GAME_WIDTH, game.GAME_HEIGHT);
        
        // Draw background image
        g.drawImage(image, backgroundX, backgroundY, backgroundW, backgroundH, null);
        
        // Draw "GAME COMPLETED" text
        g.setColor(new Color(255, 215, 0)); // Gold color
        g.setFont(new Font("Arial", Font.BOLD, 48));
        FontMetrics fm = g.getFontMetrics();
        String gameCompletedText = "GAME COMPLETED!";
        int textX = (game.GAME_WIDTH - fm.stringWidth(gameCompletedText)) / 2;
        int textY = backgroundY + 60;
        // Draw text with shadow for better visibility
        g.setColor(Color.BLACK);
        g.drawString(gameCompletedText, textX + 2, textY + 2);
        g.setColor(new Color(255, 215, 0));
        g.drawString(gameCompletedText, textX, textY);
        
        // Draw congratulations message
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        String congratsText = "Congratulations! You've completed all levels!";
        fm = g.getFontMetrics();
        int congratsX = (game.GAME_WIDTH - fm.stringWidth(congratsText)) / 2;
        int congratsY = backgroundY + 100;
        g.drawString(congratsText, congratsX, congratsY);
        
        // Draw menu button
        menu.draw(g);
        
        // Draw instruction text
        g.setColor(new Color(200, 200, 200));
        g.setFont(new Font("Arial", Font.ITALIC, 16));
        String instructionText = "Click Menu to return to main menu";
        fm = g.getFontMetrics();
        int instX = (game.GAME_WIDTH - fm.stringWidth(instructionText)) / 2;
        int instY = backgroundY + backgroundH - 20;
        g.drawString(instructionText, instX, instY);
    }
    
    public void mouseMoved(MouseEvent e) {
        menu.setMouseOver(false);
        if (isClicked(menu, e)) {
            menu.setMouseOver(true);
        }
    }
    
    public boolean isClicked(URMButtons button, MouseEvent e) {
        return button.getBox().contains(e.getX(), e.getY());
    }
    
    public void mouseReleased(MouseEvent e) {
        if (isClicked(menu, e)) {
            if (menu.isMousePressed()) {
                playing.resetGame();
                GameState.gameState = GameState.MENU;
            }
        }
        menu.reset();
    }
    
    public void mousePressed(MouseEvent e) {
        if (isClicked(menu, e)) {
            menu.setMousePressed(true);
        }
    }
}

