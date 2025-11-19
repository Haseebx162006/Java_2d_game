package Inputes_of_game;

import Entities.Player;
import main.game_panel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import static Function.features.PlayerDirectons.*;

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
        switch (e.getKeyCode()){
            case KeyEvent.VK_W:
                gamePanel.getGame1().getPlayer().setUp(true);

                break;
            case KeyEvent.VK_A:
                gamePanel.getGame1().getPlayer().setLeft(true);

                break;
            case KeyEvent.VK_S:
                gamePanel.getGame1().getPlayer().setDown(true);

                break;
            case KeyEvent.VK_D:
                gamePanel.getGame1().getPlayer().setRight(true);

                break;
            case KeyEvent.VK_SPACE:
                gamePanel.getGame1().getPlayer().setJump(true);
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        gamePanel.getGame1().getPlayer().setMoving(false);
        switch (e.getKeyCode()){
            case KeyEvent.VK_W:
                gamePanel.getGame1().getPlayer().setUp(false);
                break;
            case KeyEvent.VK_A:
                gamePanel.getGame1().getPlayer().setLeft(false);
                break;
            case KeyEvent.VK_S:
                gamePanel.getGame1().getPlayer().setDown(false);
                break;
            case KeyEvent.VK_D:
                gamePanel.getGame1().getPlayer().setRight(false);
                break;
            case KeyEvent.VK_SPACE:
                gamePanel.getGame1().getPlayer().setJump(false);
                break;
        }
    }
}
