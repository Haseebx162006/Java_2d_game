package Rewards;

import main.game;

import java.awt.geom.Rectangle2D;

import static Function.features.Projectiles.*;

public class Ball {
    private Rectangle2D.Float box;
    private int direction;
    private boolean active=true;

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
    public void updatePos() {
        box.x += direction * SPEED;
        // Ball ko inactive karo agar screen se bahar chali gayi
        if (box.x < -CANNON_BALL_WIDTH || box.x > game.GAME_WIDTH) {
            active = false;
        }

        // Optional: Y-axis check bhi (agar ball neeche gir sakti hai)
        if (box.y > game.GAME_HEIGHT) {
            active = false;
        }
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
