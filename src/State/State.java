package State;

import UI.MenuButton;
import main.game;

import java.awt.event.MouseEvent;

public class State {
    protected game game1;
    public State(game game1){
        this.game1=game1;
    }
    public game Getgame(){
        return game1;
    }
    public boolean isPlayerPressing(MouseEvent e , MenuButton m){
        return m.getButtonBox().contains(e.getX(),e.getY());
    }
}
