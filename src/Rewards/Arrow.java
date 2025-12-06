package Rewards;

import main.game;

public class Arrow extends GameObject{
    public Arrow(int x, int y, int objType) {
        super(x, y, objType);
        CreateBox(32,12);
        XOffset=0;
        yOffset=(int)(16* game.SCALE);
        box.y+=yOffset;
    }
}
