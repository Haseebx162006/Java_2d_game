package Entities;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {
    protected  float x;
    protected float y;
    protected Rectangle2D.Float box;// this is the hitbox that will be drawn outsde of player
    protected int width;
    protected int height;
    public Entity(float x, float y, int width, int height){
        // in this method values are initialized for the derived entities like player and
        // i create a box here for collision
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;

    }

    protected void CreateBox(float x, float y, float width, float height) {
        box= new Rectangle2D.Float(x, y,width,height);  // have done typcasting here
    }
//    public void UpdateBox(){
//        box.x=(int )x;
//        box.y=(int ) y;
//    }

//    public Rectangle getBox() {
//        return box;
//    }
    public void drawBox(Graphics g){
        g.setColor(Color.green);
        g.drawRect((int)box.x,(int)box.y,(int)box.width,(int)box.height);
    }
}
