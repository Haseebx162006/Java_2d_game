package Entities;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {
    protected  float x;
    protected float y;

    public Rectangle2D.Float getBox() {
        return box;
    }

    public void setBox(Rectangle2D.Float box) {
        this.box = box;
    }

    protected Rectangle2D.Float box; // this is the hitbox that will be drawn outsde of player
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
    public void drawBox(Graphics g, int padding){
        Graphics2D g2 = (Graphics2D) g.create();
        int drawX = Math.round(box.x) + padding;
        int drawY = Math.round(box.y) + padding;
        int drawWidth = Math.max(1, Math.round(box.width) - padding * 2);
        int drawHeight = Math.max(1, Math.round(box.height) - padding * 2);
        g2.setColor(new Color(0, 255, 0, 90));
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1f, new float[]{4f, 4f}, 0f));
        g2.drawRect(drawX, drawY, drawWidth, drawHeight);
        g2.setStroke(oldStroke);
        g2.dispose();
    }
}
