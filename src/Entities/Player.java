package Entities;

import Function.LoadSave;
import main.game;

import java.awt.*;
import java.awt.image.BufferedImage;
import static Function.features.player_features.*;
import static Function.StaticMethodsforMovement.*;

public class Player extends  Entity{
    // I create a 2D array to store the sprites png and performing actions
    private BufferedImage[][] Animation;
    private static final boolean DEBUG_HITBOX = false;
    private float XOffset;  // these offset variables are for the collision box
    private float YOffset;
    private static final float FOOT_ADJUST = 8f * game.SCALE;
    private float Gravity= 0.04f*game.SCALE;
    private int Animation_tick,Animation_index,Animation_Speed=10;
    private int Playermove=STANDING;
    private boolean Player_is_moving=false, attacking=false;
    private boolean up,down,right,left,jump;
    private float playerSpeed=2.0f*game.SCALE;
    private boolean canJump=true;
    private int[][] DataOfLevel;
    private float airSpeed=0;
    private float jumpSpeed=  (-2.25f* game.SCALE);
    private float fallSpeed= 0.5f*game.SCALE;
    private boolean DuringAir=false;
    public Player(float x, float y,int width,int height) {
        super(x, y,width,height);
        load_Animations();
        CreateBox(x,y,game.SCALE*28,game.SCALE*28);
        XOffset = (width - box.width) / 2f;
        YOffset = Math.max(0, height - box.height - FOOT_ADJUST);
    }
    public void UpdatePlayer(){
        UpdatePosition();
        updateAnimation();
        setAnimation();

    }
    public void RenderPlayer(Graphics g) {

        int drawX = Math.round(box.x - XOffset);
        int drawY = Math.round(box.y - YOffset);
        g.drawImage(Animation[Playermove][Animation_index], drawX, drawY, width, height, null);
        if (DEBUG_HITBOX) {
            drawBox(g, 8);
        }
    }
    public void setAttack(boolean attacking){
        this.attacking=attacking;
    }
    private void UpdatePosition() {
        Player_is_moving=false;
        if (jump && !DuringAir){
            PlayerJumping();
        }
        if (!left && !right && !DuringAir){
            return;
        }
        // i create this method for updating the position
        // whenever the player press a key

        // Temporary variables to hold values only for canMove function and i then pass to original x and y
        float Xspeed=0;
        if (left){
            Xspeed-=playerSpeed;
            Player_is_moving=true;
        }
        if (right) {
            Xspeed+=playerSpeed;
            Player_is_moving=true;
        }
        if (!DuringAir){
            if(!OnFloor(box,DataOfLevel)){
                DuringAir=true;
            }
        }
        if (DuringAir){
            float newY = box.y + airSpeed;
            if (CanMove(box.x,newY,box.width,box.height,DataOfLevel)) {
                box.y=newY;
                airSpeed+=Gravity;
                UpdatePostionX(Xspeed);
            }
            else{
                float correctedY = CheckUnderEnvironmentorAbove(box, airSpeed);
                if (correctedY >= 0 && correctedY < game.GAME_HEIGHT) {
                    box.y = correctedY;
                }
                if (airSpeed>0) {
                    reset();
                }
                else{
                    airSpeed=fallSpeed;  // jab gravity ose neeche khenche ge to Player ke girne ke speed falling speed ke equal hojaye ge jese pehle he mene uper set keya howa
                }
                UpdatePostionX(Xspeed);
            }
        }
        else
            UpdatePostionX(Xspeed);

        Player_is_moving=true;

    }

    private void PlayerJumping() {
        if (DuringAir || !jump || !canJump)
            return;
        DuringAir=true;
        airSpeed=jumpSpeed;
        canJump=false;
        jump=false;
    }

    private void reset() {
        airSpeed=0;
        DuringAir=false;
        canJump=true;
    }

    private void UpdatePostionX(float Xspeed) {
        if (CanMove(box.x+Xspeed,box.y,box.width,box.height,DataOfLevel)) {
            box.x+=Xspeed;
        }
        else{
            box.x=PositionNextToWall(box,Xspeed);
        }
    }

    private void setAnimation() {
        int startAnimation=Playermove;
        if (Player_is_moving){
            Playermove=RUNNING;
        }
        else{
            Playermove=STANDING;
        }
        if (DuringAir){
            if (airSpeed<0)
                Playermove=JUMPING;
            else
                Playermove=FALLING;
        }
        if (attacking){
            Playermove=ATTACK;
            if (startAnimation!=Playermove){
                ResetAnimation();
            }
        }
    }

    private void ResetAnimation() {
        Animation_tick=0;
        Animation_index=0;
    }

    public void setMoving(boolean moving){
        this.Player_is_moving=moving;
    }

    private void updateAnimation() {
        Animation_tick++;
        if (Animation_tick>=Animation_Speed){
            Animation_tick=0;
            Animation_index++;
            if (Animation_index>=GetPlayerMove(Playermove)){
                Animation_index=0;
                attacking=false;
            }
        }
    }
    private void load_Animations() {

            BufferedImage img= LoadSave.GetAtlas(LoadSave.PlayerImgAddress);
            Animation= new BufferedImage[9][6];
            for (int i = 0; i <Animation.length; i++) {
                for (int j = 0; j < Animation[i].length; j++) {
                    Animation[i][j]= img.getSubimage(j*64,i*40,64,40);
                }
            }
    }
    public void LoadlevelData(int[][] DataOfLevel){
        this.DataOfLevel=DataOfLevel;
        if (!OnFloor(box,DataOfLevel)){
            DuringAir=true;
        }
    }
    // Getter and setter
    public boolean getUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean getDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean getRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean getLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setJump(boolean jump) {
        this.jump=jump;
    }
    public void ResetDirection() {
        up=false;
        down=false;
        right=false;
        left=false;
    }
}
