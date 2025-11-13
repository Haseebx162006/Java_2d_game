package Entities;

import Function.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static Function.features.player_features.*;

public class Player extends  Entity{
    // I create a 2D array to store the sprites png and performing actions
    private BufferedImage[][] Animation;
    private int Animation_tick,Animation_index,Animation_Speed=10;
    private int Playermove=STANDING;
    private int PlayerDirection=-1;
    private boolean Player_is_moving=false, attacking=false;
    private boolean up,down,right,left;
    private float playerSpeed=2.0f;

    public Player(float x, float y) {
        super(x, y);
        load_Animations();
    }
    public void UpdatePlayer(){
        updateAnimation();
        setAnimation();
        UpdatePosition();
    }
    public void RenderPlayer(Graphics g){
        g.drawImage(Animation[Playermove][Animation_index],(int )x,(int) y,90,60,null);
    }
    public void setAttack(boolean attacking){
        this.attacking=attacking;
    }
    private void UpdatePosition() {
        // i create this method for updating the position
        // whenever the player press a key
        if (left && !right){
            x-=playerSpeed;
           Player_is_moving=true;
        } else if (right && !left) {
            x+=playerSpeed;
        }

        if (up && !down){
            y-=playerSpeed;
            Player_is_moving=true;

        } else if (down && !up) {
            y+=playerSpeed;
            Player_is_moving=true;

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

    public void setdirection(int direction) {
        this.PlayerDirection=direction;
        Player_is_moving=true;
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

    public void ResetDirection() {
        up=false;
        down=false;
        right=false;
        left=false;
    }
}
