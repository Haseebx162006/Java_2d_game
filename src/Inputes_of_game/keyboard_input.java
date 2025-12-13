package Inputes_of_game;

import State.GameState;
import main.game_panel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class keyboard_input implements KeyListener {
    private game_panel gamePanel;
    public keyboard_input(game_panel gamePanel){
        this.gamePanel=gamePanel;
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
      switch (GameState.gameState){
          case SPLASH:
              gamePanel.getGame1().getSplashScreen().keyPressed(e);
              break;
          case MENU:
              gamePanel.getGame1().getMenu().KeyPressed(e);
              break;
          case PLAYING:
              gamePanel.getGame1().getPlaying().KeyPressed(e);
              break;
          case OPTIONS:
              gamePanel.getGame1().getOptions().KeyPressed(e);
              break;
      }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (GameState.gameState){
            case MENU:
                gamePanel.getGame1().getMenu().keyReleased(e);
                break;
            case PLAYING:
                gamePanel.getGame1().getPlaying().keyReleased(e);
                break;
        }
    }
}
