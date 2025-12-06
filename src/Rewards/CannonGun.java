package Rewards;

import main.game;

public class CannonGun extends GameObject{
    private int TileYaxis;
    private boolean hasFired = false; // Track if cannon has fired in current animation cycle
    
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
    
    public boolean getHasFired() {
        return hasFired;
    }
    
    public void setHasFired(boolean fired) {
        hasFired = fired;
    }

    public void update(){
        if (Animation){
            updateAnimationTick();
            // Reset fire flag when animation completes (loops back to frame 0)
            if (AnimationIndex == 0 && AnimationTick == 0) {
                hasFired = false;
            }
        }
    }
    
    @Override
    public void reset(){
        super.reset();
        hasFired = false;
    }
}