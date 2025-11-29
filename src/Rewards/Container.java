package Rewards;

import main.game;

import static Function.features.Objects.BOX;

public class Container extends GameObject {
    public Container(int x, int y, int objType) {
        super(x, y, objType);
        CreateDiffBox();
    }

    private void CreateDiffBox() {
        if (objType == BOX) {
            CreateBox(25, 18);

            XOffset = (int) (7 * game.SCALE);
            yOffset = (int) (12 * game.SCALE);
        }else{
            CreateBox(23,25);
            XOffset=(int)(8*game.SCALE);
            yOffset=(int)(5*game.SCALE);
        }
        box.y+=yOffset+(int) (game.SCALE*2);
        box.x+=XOffset/2;
    }
    public void update() {
        if (Animation)
            updateAnimationTick();
    }
}