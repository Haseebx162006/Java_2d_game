package State;

import main.game;

public class State {
    protected game game1;
    public State(game game1){
        this.game1=game1;
    }
    public game Getgame(){
        return game1;
    }
}
