package Entities;

import Function.LoadSave;
import main.game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Function;

import static Function.features.player_features.*;
import static Function.StaticMethodsforMovement.*;

public class Player extends  Entity{
    // I create a 2D array to store the sprites png and performing actions
    private BufferedImage[][] Animation;
    private float XOffset=21* game.SCALE;  // these offset variables are for the Colision box
    private float YOffset=4*game.SCALE;
    private int Animation_tick,Animation_index,Animation_Speed=10;
    private int Playermove=STANDING;
    private int PlayerDirection=-1;
    private boolean Player_is_moving=false, attacking=false;
    private boolean up,down,right,left;
    private float playerSpeed=2.0f;
    private int[][] DataOfLevel;

    public Player(float x, float y,int width,int height) {
        super(x, y,width,height);
        load_Animations();
        CreateBox(x,y,game.SCALE*28,game.SCALE*28);
    }
    public void UpdatePlayer(){
        UpdatePosition();
        updateAnimation();
        setAnimation();

    }
    public void RenderPlayer(Graphics g){
        g.drawImage(Animation[Playermove][Animation_index],(int)(box.x-XOffset),(int)(box.y-YOffset),width,height,null);
        drawBox(g);
    }
    public void setAttack(boolean attacking){
        this.attacking=attacking;
    }
    private void UpdatePosition() {
        if (!left && !right && !down && !up){
            return;
        }
        // i create this method for updating the position
        // whenever the player press a key

        // Temporary variables to hold values only for canMove function and i then pass to original x and y
        float Xspeed=0;
        float Yspeed=0;

        if (left && !right){
            Xspeed-=playerSpeed;
        } else if (right && !left) {
            Xspeed+=playerSpeed;
        }

        if (up && !down){
            Yspeed-=playerSpeed;

        } else if (down && !up) {
            Yspeed+=playerSpeed;
        }
        if (CanMove(box.x+Xspeed,box.y+Yspeed,box.width,box.height,DataOfLevel)) {
            box.x+=Xspeed;
            box.y+=Yspeed;
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
    public void LoadlevelData(int[][] DataOfLevel){
        this.DataOfLevel=DataOfLevel;
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
