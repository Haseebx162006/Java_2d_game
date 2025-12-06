package Rewards;

import main.game;

import java.awt.geom.Rectangle2D;

import static Function.features.Projectiles.*;

public class Ball {
    private Rectangle2D.Float box;
    private int direction;
    private boolean active=true;
    private boolean canDamage = false; // Prevent instant damage on creation

    public Ball(int x, int y, int direction){
        int xOff=(int)(-3* game.SCALE);
        int yOff=(int)(5* game.SCALE);

        if (direction==1){
            xOff=(int)(29*game.SCALE);
        }
        box= new Rectangle2D.Float(x+xOff,y+yOff,CANNON_BALL_WIDTH,CANNON_BALL_HEIGHT);
        this.direction=direction;
    }
    public void setPosition(int x, int y){
        box.x=x;

        box.y=y;
    }
    public void updatePos(int[][] levelData) {
        box.x += direction * SPEED;
        // Enable damage after first movement to prevent instant collision
        canDamage = true;
        
        // Don't deactivate based on screen boundaries - balls use world coordinates
        // Balls will be deactivated by level collision or hitting player
        // Only deactivate if ball falls way below level (safety check)
        if (levelData != null && levelData.length > 0) {
            int maxLevelHeight = levelData.length * game.TILE_SIZE;
            // Only deactivate if ball falls way below the level
            if (box.y > maxLevelHeight + game.TILE_SIZE * 5) {
                active = false;
            }
        }
    }
    
    public boolean canDamage() {
        return canDamage;
    }

    public Rectangle2D.Float getBox() {
        return box;
    }

    public void setBox(Rectangle2D.Float box) {
        this.box = box;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
