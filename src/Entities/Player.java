package Entities;

import Function.LoadSave;
import main.game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import static Function.features.player_features.*;
import static Function.StaticMethodsforMovement.*;
import static Function.features.PlayerDirectons.*;

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
    
    // Health system
    private int maxHealth = 100;
    private int currentHealth = maxHealth;
    private boolean isHit = false;
    private int hitCooldown = 0;
    private static final int HIT_COOLDOWN_TIME = 60; // frames of invincibility after being hit
    private boolean isDead = false;
    private int deathAnimationTimer = 0;
    private static final int DEATH_ANIMATION_DURATION = 120; // frames for death animation
    
    // Combat system
    private boolean attackHitProcessed = false; // Track if this attack has already dealt damage
    private int attackDirection = RIGHT; // Track which direction player is facing for attack
    private static final int ATTACK_ACTIVE_FRAME_START = 1; // Attack hitbox is active from frame 1
    private static final int ATTACK_ACTIVE_FRAME_END = 2; // Attack hitbox is active until frame 2 (of 3 total)
    private boolean invulnerable = false;
    private int invulnerabilityTimer = 0;
    private static final int INVULNERABILITY_DURATION = 60; // 1 second at 60 FPS
    public Player(float x, float y,int width,int height) {
        super(x, y,width,height);
        load_Animations();
        CreateBox(x,y,game.SCALE*28,game.SCALE*28);
        XOffset = (width - box.width) / 2f;
        YOffset = Math.max(0, height - box.height - FOOT_ADJUST);
    }
    public void UpdatePlayer(){
        if (isDead) {
            updateDeathAnimation();
            return;
        }
        updateCombat();
        UpdatePosition();
        updateAnimation();
        setAnimation();
        updateHitCooldown();
        updateInvulnerability();
    }
    private void updateInvulnerability() {
        if (invulnerable) {
            invulnerabilityTimer--;
            if (invulnerabilityTimer <= 0) {
                invulnerable = false;
            }
        }
    }
    private void updateCombat() {
        // Reset attack hit tracking when attack animation starts (first frame)
        if (attacking && Playermove == ATTACK && Animation_index == 0 && Animation_tick == 0) {
            attackHitProcessed = false;
        }

        // Store attack direction when attack is first triggered (before animation starts)
        // This is handled in setAttack() method

        // Reset attack hit tracking when attack ends
        if (!attacking && Playermove != ATTACK) {
            attackHitProcessed = false;
        }
    }
    
    private void updateDeathAnimation() {
        deathAnimationTimer++;
    }
    
    public boolean isDeathAnimationComplete() {
        return isDead && deathAnimationTimer >= DEATH_ANIMATION_DURATION;
    }
    
    private void updateHitCooldown() {
        if (hitCooldown > 0) {
            hitCooldown--;
            if (hitCooldown == 0) {
                isHit = false;
            }
        }
    }
    
    // Health methods
    public void takeDamage(int damage) {
        if (invulnerable || isDead()) {
            return; // Can't take damage while invulnerable or dead
        }

        currentHealth -= damage;
        System.out.println("Player took " + damage + " damage. Health: " + currentHealth);

        if (currentHealth <= 0) {
            currentHealth = 0;
        }

        // Activate invulnerability
        invulnerable = true;
        invulnerabilityTimer = INVULNERABILITY_DURATION;
    }
    
    public int getCurrentHealth() {
        return currentHealth;
    }
    
    public int getMaxHealth() {
        return maxHealth;
    }
    
    public boolean isAlive() {
        return !isDead && currentHealth > 0;
    }
    
    public boolean isDead() {
        return isDead;
    }
    
    public void reset() {
        // Reset player for replay
        currentHealth = maxHealth;
        isDead = false;
        isHit = false;
        hitCooldown = 0;
        deathAnimationTimer = 0;
        attacking = false;
        attackHitProcessed = false;
        attackDirection = RIGHT;
        Playermove = STANDING;
        ResetAnimation();
        // Reset position
        box.x = 200;
        box.y = 200;
        DuringAir = false;
        airSpeed = 0;
    }
    
    public boolean isAttacking() {
        return attacking && Playermove == ATTACK;
    }
    
    // Get attack hitbox (area in front of player when attacking)
    // Only active during specific frames of the attack animation
    public Rectangle2D.Float getAttackHitbox() {
        if (!attacking || Playermove != ATTACK) {
            return null;
        }
        
        // Attack hitbox is only active during frames 1-2 (middle frames of 3-frame attack)
        if (Animation_index < ATTACK_ACTIVE_FRAME_START || Animation_index > ATTACK_ACTIVE_FRAME_END) {
            return null;
        }
        
        float attackWidth = 40 * game.SCALE;
        float attackHeight = box.height;
        float attackX;
        
        // Attack hitbox extends in the direction player was facing when attack started
        if (attackDirection == LEFT) {
            attackX = box.x - attackWidth;
        } else {
            attackX = box.x + box.width;
        }
        
        return new Rectangle2D.Float(attackX, box.y, attackWidth, attackHeight);
    }
    
    public boolean hasAttackHitProcessed() {
        return attackHitProcessed;
    }
    
    public void setAttackHitProcessed(boolean processed) {
        attackHitProcessed = processed;
    }
    public void RenderPlayer(Graphics g, int levelOff) {

        int drawX = Math.round(box.x - XOffset)-levelOff;
        int drawY = Math.round(box.y - YOffset);
        g.drawImage(Animation[Playermove][Animation_index], drawX, drawY, width, height, null);
        if (DEBUG_HITBOX) {
            drawBox(g, 8);
        }
    }
    public void setAttack(boolean attacking){
        // Only allow starting attack if not already attacking
        if (attacking && !this.attacking) {
            this.attacking = true;
            attackHitProcessed = false;
            // Store attack direction when attack starts
            if (left) {
                attackDirection = LEFT;
            } else if (right) {
                attackDirection = RIGHT;
            }
            // If no direction, keep previous direction (defaults to RIGHT)
        } else if (!attacking) {
            // Allow stopping attack
            this.attacking = false;
        }
    }
    private void UpdatePosition() {
        Player_is_moving=false;
        
        // Prevent jump during attack animation
        if (jump && !DuringAir && !attacking){
            PlayerJumping();
        }
        
        // Lock movement during attack animation
        if (attacking && Playermove == ATTACK) {
            // Only allow vertical movement (gravity/jumping) during attack, not horizontal
            if (DuringAir) {
                // Apply gravity during attack if in air
                float newY = box.y + airSpeed;
                if (CanMove(box.x, newY, box.width, box.height, DataOfLevel)) {
                    box.y = newY;
                    airSpeed += Gravity;
                } else {
                    float correctedY = CheckUnderEnvironmentorAbove(box, airSpeed);
                    if (correctedY >= 0 && correctedY < game.GAME_HEIGHT) {
                        box.y = correctedY;
                    }
                    if (airSpeed > 0) {
                        resetAirState();
                    } else {
                        airSpeed = fallSpeed;
                    }
                }
            }
            return; // Don't allow horizontal movement during attack
        }
        
        if (!left && !right && !DuringAir){
            return;
        }
        // i create this method for updating the position
        // whenever the player press a key

        // Temporary variables to hold values only for canMove function and i then pass to original x and y
        float Xspeed=0;
        boolean horizontalMove = false;
        if (left){
            Xspeed-=playerSpeed;
            Player_is_moving=true;
            horizontalMove = true;
        }
        if (right) {
            Xspeed+=playerSpeed;
            Player_is_moving=true;
            horizontalMove = true;
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
                    resetAirState();
                }
                else{
                    airSpeed=fallSpeed;  // jab gravity ose neeche khenche ge to Player ke girne ke speed falling speed ke equal hojaye ge jese pehle he mene uper set keya howa
                }
                UpdatePostionX(Xspeed);
            }
        }
        else
            UpdatePostionX(Xspeed);

        Player_is_moving=horizontalMove;

    }

    private void PlayerJumping() {
        if (DuringAir || !jump || !canJump)
            return;
        DuringAir=true;
        airSpeed=jumpSpeed;
        canJump=false;
        jump=false;
    }

    private void resetAirState() {
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
        if (isDead) {
            Playermove = GROUND; // Keep death animation
            return;
        }
        
        int previousAnimation = Playermove;

        if (attacking) {
            Playermove = ATTACK;
        } else if (DuringAir) {
            Playermove = (airSpeed < 0) ? JUMPING : FALLING;
        } else if (Player_is_moving) {
            Playermove = RUNNING;
        } else {
            Playermove = STANDING;
        }

        if (previousAnimation != Playermove) {
            ResetAnimation();
        }
    }

    private void ResetAnimation() {
        Animation_tick=0;
        Animation_index=0;
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
        // Completely separate from attack - never allow jump during attack
        if (jump && !DuringAir && !attacking){
            this.jump=true;
        } else if (!jump) {
            this.jump=false;
        }
    }
    public void ResetDirection() {
        up=false;
        down=false;
        right=false;
        left=false;
    }

}
