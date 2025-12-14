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
        // Center the menu button horizontally
        // Position will be calculated based on background image
        int menu_ButtonX = game.GAME_WIDTH / 2 - URM_BUTTON / 2;
        // Position button below the congratulations text, centered vertically in the lower part of banner
        int y = backgroundY + backgroundH / 2 + 50;
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
        
        // Calculate center positions for better alignment
        int centerX = game.GAME_WIDTH / 2;
        int contentStartY = backgroundY + 30;
        
        // Draw "GAME COMPLETED" text - larger and more prominent
        g.setFont(new Font("Arial", Font.BOLD, 52));
        FontMetrics fm = g.getFontMetrics();
        String gameCompletedText = "GAME COMPLETED!";
        int textX = centerX - fm.stringWidth(gameCompletedText) / 2;
        int textY = contentStartY + 50;
        
        // Draw text with shadow for better visibility
        g.setColor(new Color(0, 0, 0, 180));
        g.drawString(gameCompletedText, textX + 3, textY + 3);
        g.setColor(new Color(255, 215, 0)); // Gold color
        g.drawString(gameCompletedText, textX, textY);
        
        // Draw congratulations message - properly spaced
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 22));
        String congratsText = "Congratulations! You've completed all levels!";
        fm = g.getFontMetrics();
        int congratsX = centerX - fm.stringWidth(congratsText) / 2;
        int congratsY = textY + 55;
        g.drawString(congratsText, congratsX, congratsY);
        
        // Draw menu button - centered below congratulations text
        menu.draw(g);
        
        // Draw instruction text - below the button with proper spacing
        g.setColor(new Color(200, 200, 200));
        g.setFont(new Font("Arial", Font.ITALIC, 16));
        String instructionText = "Click Menu to return to main menu";
        fm = g.getFontMetrics();
        int instX = centerX - fm.stringWidth(instructionText) / 2;
        int instY = menu.getY() + URM_BUTTON + 30;
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

