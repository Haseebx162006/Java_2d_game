package Inputes_of_game;

import State.GameState;
import main.game_panel;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;


public class mouse_input implements MouseListener, MouseMotionListener {
    game_panel gamePanel;
   public mouse_input(game_panel gamePanel){
        this.gamePanel= gamePanel;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
       switch (GameState.gameState){
           case SPLASH:
               gamePanel.getGame1().getSplashScreen().mouseClicked(e);
               break;
           case PLAYING:
               gamePanel.getGame1().getPlaying().mouseClicked(e);
               break;
       }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (GameState.gameState) {
            case SPLASH:
                gamePanel.getGame1().getSplashScreen().mousePressed(e);
                break;
            case MENU:
                gamePanel.getGame1().getMenu().mousePressed(e);
                break;
            case PLAYING:
                gamePanel.getGame1().getPlaying().mousePressed(e);
                break;
            case OPTIONS:
                gamePanel.getGame1().getOptions().mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (GameState.gameState) {
            case MENU:
                gamePanel.getGame1().getMenu().mouseReleased(e);
                break;
            case PLAYING:
                gamePanel.getGame1().getPlaying().mouseReleased(e);
                break;
            case OPTIONS:
                gamePanel.getGame1().getOptions().mouseReleased(e);
                break;
        }
    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
       switch (GameState.gameState){
           case PLAYING :
               gamePanel.getGame1().getPlaying().mouseDragged(e);
               break;
           case OPTIONS:
               gamePanel.getGame1().getOptions().mouseDrag(e);
           default:break;
       }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        switch (GameState.gameState) {
            case MENU:
                gamePanel.getGame1().getMenu().mouseMoved(e);
                break;
            case PLAYING:
                gamePanel.getGame1().getPlaying().mouseMoved(e);
                break;
            case OPTIONS:
                gamePanel.getGame1().getOptions().mouseMoved(e);
                break;
        }
    }
}
