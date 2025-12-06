package Rewards;

import main.game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import static Function.features.ANI_SPEED;
import static Function.features.Objects.*;
public class GameObject {
    protected  int x,y,objType;
    protected Rectangle2D.Float box;
    protected boolean Animation,Active=true;
    protected int AnimationTick,AnimationIndex;
    protected int XOffset,yOffset;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getObjType() {
        return objType;
    }

    public void setObjType(int objType) {
        this.objType = objType;
    }

    public Rectangle2D.Float getBox() {
        return box;
    }

    public void setBox(Rectangle2D.Float box) {
        this.box = box;
    }

    public boolean isAnimation() {
        return Animation;
    }

    public void setAnimation(boolean animation) {
        Animation = animation;
    }

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }

    public int getAnimationTick() {
        return AnimationTick;
    }

    public void setAnimationTick(int animationTick) {
        AnimationTick = animationTick;
    }

    public int getAnimationIndex() {
        return AnimationIndex;
    }

    public void setAnimationIndex(int animationIndex) {
        AnimationIndex = animationIndex;
    }

    public int getXOffset() {
        return XOffset;
    }

    public void setXOffset(int XOffset) {
        this.XOffset = XOffset;
    }

    public int getyOffset() {
        return yOffset;
    }

    public void setyOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    public GameObject(int x, int y, int objType){
        this.x=x;
        this.y=y;
        this.objType=objType;
    }
    public void CreateBox(int w, int h){
        box= new Rectangle2D.Float(x,y,(int) (w* game.SCALE),(int)(h*game.SCALE));
    }
    public void drawBox(Graphics g, int xlevelOffset){
        g.setColor(Color.PINK);
        g.drawRect((int)box.x-xlevelOffset,(int)box.y,(int)box.width,(int)box.height);
    }
    protected void updateAnimationTick() {
        AnimationTick++;
        if (AnimationTick >= ANI_SPEED) {
            AnimationTick = 0;
            AnimationIndex++;
            if (AnimationIndex>= GetSpriteAmount(objType)) {
                AnimationIndex = 0;
                if (objType == BARREL || objType == BOX) {
                    Animation= false;
                    Active= false;
                } else if (objType == CANNON_LEFT || objType == CANNON_RIGHT) {
                    // Reset cannon animation after firing cycle completes
                    Animation = false;
                }
            }
        }
    }
    public void reset(){
        AnimationIndex=0;
        AnimationTick=0;
        Active=true;
        if(objType==BARREL || objType ==BOX||objType ==CANNON_LEFT|| objType==CANNON_RIGHT)
            Animation=false;
        else Animation=true;
    }
}
