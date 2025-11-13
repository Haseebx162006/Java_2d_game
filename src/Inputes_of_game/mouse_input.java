package Inputes_of_game;

import main.game_panel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class mouse_input implements MouseListener, MouseMotionListener {
    game_panel gamePanel;
   public mouse_input(game_panel gamePanel){
        this.gamePanel= gamePanel;
    }
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
       if (e.getButton()==MouseEvent.BUTTON1){
           gamePanel.getGame1().getPlayer().setAttack(true);
       }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
