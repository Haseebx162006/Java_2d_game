package Rewards;

import main.game;

public class CannonGun extends GameObject{
    private int TileYaxis;
    public CannonGun(int x, int y, int objType) {
        super(x, y, objType);
        TileYaxis=y/ game.TILE_SIZE;
        CreateBox(40,60);
        box.x-=(int)(4*game.SCALE);
        box.y+=(int)(6*game.SCALE);
    }

    public int getTileYaxis() {
        return TileYaxis;
    }

    public void update(){
        if (Animation){
            updateAnimationTick();
        }
    }
}
