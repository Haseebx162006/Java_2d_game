package Rewards;

import main.game;

public class Potions extends GameObject{
    private float Hoverset;
    private int maxHover, hoverdirecrction=1;
    public Potions(int x, int y, int objType) {
        super(x, y, objType);
        Animation=true;
        CreateBox(7,14);
        XOffset=(int)(3* game.SCALE);
        yOffset=(int)(2*game.SCALE);
        maxHover=(int)(10*game.SCALE);
    }
    public void update(){
        updateAnimationTick();
        updateHover();
    }

    private void updateHover() {
        Hoverset+=(0.070f*game.SCALE*hoverdirecrction);
        if (Hoverset>=maxHover){
            hoverdirecrction=-1;
        } else if (Hoverset<0) {
            hoverdirecrction=1;
        }
        box.y=y+Hoverset;
    }
}
