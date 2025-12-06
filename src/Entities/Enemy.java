package Entities;
import static Function.features.UI.Enemies.*;
import static Function.StaticMethodsforMovement.*;
import static Function.features.PlayerDirectons.*;
import main.game;
import java.awt.geom.Rectangle2D;

public abstract class Enemy extends Entity {
    public Enemy(float x, float y, int width, int height,int Type_of_enemy) {
        super(x, y, width, height);
        this.Type_of_enemy=Type_of_enemy;
        CreateBox(x,y,width*0.5f,height);
        this.State_of_enemy = RUNNING;
    }
    protected float Walk=1.0f*game.SCALE;
    protected int animationIndex;
    protected int State_of_enemy;
    protected int Type_of_enemy;
    protected int animationTick;
    protected int[][] levelData;
    protected float airSpeed = 0;
    protected boolean inAir = false;
    protected int walkDir=LEFT;
    protected float gravity = 0.04f * game.SCALE;
    protected float fallSpeed = 0.5f * game.SCALE;

    protected int maxHealth = 50;
    protected int currentHealth = maxHealth;
    protected boolean isDead = false;
    protected boolean isHit = false;
    protected int hitCooldown = 0;
    protected static final int HIT_COOLDOWN_TIME = 30;

    protected Player player = null;
    protected float visionRange = 200f * game.SCALE;
    protected float attackRange = 40f * game.SCALE;
    protected int attackCooldown = 0;
    protected static final int ATTACK_COOLDOWN_TIME = 90;

    public int getAnimationSpeed() {
        return animationSpeed;
    }

    public void setAnimationSpeed(int animationSpeed) {
        this.animationSpeed = animationSpeed;
    }

    public int getAnimationTick() {
        return animationTick;
    }

    public void setAnimationTick(int animationTick) {
        this.animationTick = animationTick;
    }

    public int getType_of_enemy() {
        return Type_of_enemy;
    }

    public void setType_of_enemy(int type_of_enemy) {
        Type_of_enemy = type_of_enemy;
    }

    public int getState_of_enemy() {
        return State_of_enemy;
    }

    public void setState_of_enemy(int state_of_enemy) {
        State_of_enemy = state_of_enemy;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }

    public void setAnimationIndex(int animationIndex) {
        this.animationIndex = animationIndex;
    }

    public void loadLevelData(int[][] levelData) {
        this.levelData = levelData;
        adjustPositionOnFloor(levelData);

        if (OnFloor(box, levelData)) {
            inAir = false;
            airSpeed = 0;
        } else {
            inAir = true;
            airSpeed = 0;
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void takeDamage(int damage) {
        if (isDead || hitCooldown > 0) {
            return;
        }
        
        currentHealth -= damage;
        
        // Apply stronger knockback if not dead for better feedback
        if (currentHealth > 0 && player != null) {
            // Push enemy away from player
            float knockbackDirection = player.getBox().x < box.x ? 1 : -1;
            float knockback = 8.0f * game.SCALE; // Increased knockback for better feel
            
            // Update position with knockback
            float newX = box.x + knockback * knockbackDirection;
            if (CanMove(newX, box.y, box.width, box.height, levelData)) {
                box.x = newX;
            }
        }
        
        if (currentHealth <= 0) {
            currentHealth = 0;
            isDead = true;
            State_of_enemy = DEAD;
        } else {
            isHit = true;
            State_of_enemy = HIT;
            hitCooldown = HIT_COOLDOWN_TIME;
        }
    }

    public boolean isDead() {
        return isDead;
    }
    
    public boolean isHit() {
        return isHit;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    protected boolean canSeePlayer() {
        if (player == null || isDead) {
            return false;
        }
        float distance = (float) Math.sqrt(
                Math.pow(player.getBox().x - box.x, 2) +
                        Math.pow(player.getBox().y - box.y, 2)
        );
        return distance <= visionRange;
    }

    protected boolean canAttackPlayer() {
        if (player == null || isDead || attackCooldown > 0 || player.isDead()) {
            return false;
        }
        float distance = Math.abs(player.getBox().x - box.x);
        float yDistance = Math.abs(player.getBox().y - box.y);
        // Check if player is within attack range and roughly at the same height
        return distance <= attackRange && yDistance < box.height * 0.8f;
    }

    public Rectangle2D.Float getAttackHitbox() {
        if (State_of_enemy != ATTACK || isDead) {
            return null;
        }

        // Only active during specific attack frames (middle of 7-frame attack animation)
        int attackActiveFrameStart = 3;
        int attackActiveFrameEnd = 5;

        if (animationIndex < attackActiveFrameStart || animationIndex > attackActiveFrameEnd) {
            return null;
        }

        // Improved hitbox - larger and better positioned for accurate hits
        float attackWidth = 45 * game.SCALE;
        float attackHeight = box.height * 0.85f;
        float attackY = box.y + (box.height - attackHeight) / 2;
        float attackX;

        if (walkDir == LEFT) {
            attackX = box.x - attackWidth;
        } else {
            attackX = box.x + box.width;
        }

        return new Rectangle2D.Float(attackX, attackY, attackWidth, attackHeight);
    }

    private void adjustPositionOnFloor(int[][] levelData) {
        int leftTileX = (int)(box.x / game.TILE_SIZE);
        int rightTileX = (int)((box.x + box.width) / game.TILE_SIZE);
        int startTileY = (int)(box.y / game.TILE_SIZE);

        int floorTileY = -1;
        for (int y = startTileY; y < levelData.length; y++) {
            boolean leftHasFloor = false;
            boolean rightHasFloor = false;

            if (leftTileX >= 0 && leftTileX < levelData[0].length && y >= 0 && y < levelData.length) {
                int value = levelData[y][leftTileX];
                if (value != 11) {
                    leftHasFloor = true;
                }
            }

            if (rightTileX >= 0 && rightTileX < levelData[0].length && y >= 0 && y < levelData.length) {
                int value = levelData[y][rightTileX];
                if (value != 11) {
                    rightHasFloor = true;
                }
            }

            if (leftHasFloor || rightHasFloor) {
                floorTileY = y;
                break;
            }
        }

        if (floorTileY >= 0) {
            float floorTopY = floorTileY * game.TILE_SIZE;
            box.y = floorTopY - box.height - 0.5f;
        }
    }

    protected int animationSpeed=25;

    protected boolean isFloorAhead(float Speed, int[][] levelData) {
        float newX = box.x + Speed;
        float checkY = box.y + box.height + 1;
        boolean leftCorner = IsSolid(newX, checkY, levelData);
        boolean rightCorner = IsSolid(newX + box.width, checkY, levelData);
        return leftCorner && rightCorner;
    }

    protected void ChangeDirection(){
        if (walkDir==LEFT){
            walkDir=RIGHT;
        }
        else
            walkDir=LEFT;
    }

    protected void updateAnimation(){
        if (isDead && State_of_enemy == DEAD) {
            animationTick++;
            if (animationTick>=animationSpeed){
                animationTick=0;
                animationIndex++;
                int maxSprites = SPRITE(Type_of_enemy,State_of_enemy);
                if (animationIndex>=maxSprites){
                    animationIndex = maxSprites - 1;
                }
            }
        } else {
            animationTick++;
            if (animationTick>=animationSpeed){
                animationTick=0;
                animationIndex++;
                if (animationIndex>=SPRITE(Type_of_enemy,State_of_enemy)){
                    animationIndex=0;
                }
            }
        }
    }
    public int CurrentIndex(){
        return animationIndex;
    }
    public int CurrentEnemyState(){
        return State_of_enemy;
    }

}