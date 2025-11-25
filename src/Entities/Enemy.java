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
        CreateBox(x,y,width,height);
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
    
    // Health and combat system
    protected int maxHealth = 50;
    protected int currentHealth = maxHealth;
    protected boolean isDead = false;
    protected boolean isHit = false;
    protected int hitCooldown = 0;
    protected static final int HIT_COOLDOWN_TIME = 30;
    
    // Player detection and attack
    protected Player player = null;
    protected float visionRange = 200f * game.SCALE; // How far enemy can see player
    protected float attackRange = 30f * game.SCALE; // How close enemy needs to be to attack
    protected int attackCooldown = 0;
    protected static final int ATTACK_COOLDOWN_TIME = 90; // Frames between attacks

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
        // Position enemy on top of the floor
        adjustPositionOnFloor(levelData);
        
        // Check if enemy is on floor
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
    
    // Health methods
    public void takeDamage(int damage) {
        if (isDead || hitCooldown > 0) {
            return;
        }
        currentHealth -= damage;
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
    
    public int getCurrentHealth() {
        return currentHealth;
    }
    
    // Check if player is in vision range
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
    
    // Check if player is in attack range
    protected boolean canAttackPlayer() {
        if (player == null || isDead || attackCooldown > 0) {
            return false;
        }
        float distance = Math.abs(player.getBox().x - box.x);
        return distance <= attackRange;
    }
    
    // Get attack hitbox (area in front of enemy when attacking)
    public Rectangle2D.Float getAttackHitbox() {
        if (State_of_enemy != ATTACK || isDead) {
            return null;
        }
        float attackWidth = 30 * game.SCALE;
        float attackHeight = box.height;
        float attackX;
        
        if (walkDir == LEFT) {
            attackX = box.x - attackWidth;
        } else {
            attackX = box.x + box.width;
        }
        
        return new Rectangle2D.Float(attackX, box.y, attackWidth, attackHeight);
    }
    
    private void adjustPositionOnFloor(int[][] levelData) {
        // Find the floor tile directly below the enemy's spawn position
        // Check both left and right edges of the enemy to find the floor
        int leftTileX = (int)(box.x / game.TILE_SIZE);
        int rightTileX = (int)((box.x + box.width) / game.TILE_SIZE);
        int startTileY = (int)(box.y / game.TILE_SIZE);
        
        // Search downward for the first solid tile (floor) below the enemy
        int floorTileY = -1;
        for (int y = startTileY; y < levelData.length; y++) {
            boolean leftHasFloor = false;
            boolean rightHasFloor = false;
            
            // Check left edge
            if (leftTileX >= 0 && leftTileX < levelData[0].length && y >= 0 && y < levelData.length) {
                int value = levelData[y][leftTileX];
                if (value != 11) { // Solid tile (floor)
                    leftHasFloor = true;
                }
            }
            
            // Check right edge
            if (rightTileX >= 0 && rightTileX < levelData[0].length && y >= 0 && y < levelData.length) {
                int value = levelData[y][rightTileX];
                if (value != 11) { // Solid tile (floor)
                    rightHasFloor = true;
                }
            }
            
            // If we found floor at this Y position, use it
            if (leftHasFloor || rightHasFloor) {
                floorTileY = y;
                break;
            }
        }
        
        // If we found a floor, position the enemy on top of it
        if (floorTileY >= 0) {
            float floorTopY = floorTileY * game.TILE_SIZE;
            // Position enemy so its bottom edge is exactly at the top of the floor tile
            // Use a small offset to ensure it's properly on top
            box.y = floorTopY - box.height - 0.5f;
        }
    }

    protected int animationSpeed=25;

    protected boolean isFloorAhead(float Speed, int[][] levelData) {
        // Check if there's floor under both corners of the box after moving
        float newX = box.x + Speed;
        // Check floor slightly ahead (one pixel below the box)
        float checkY = box.y + box.height + 1;
        // Check both left and right corners
        boolean leftCorner = IsSolid(newX, checkY, levelData);
        boolean rightCorner = IsSolid(newX + box.width, checkY, levelData);
        // Both corners should have floor
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
            // For dead enemies, play death animation once then keep last frame
            animationTick++;
            if (animationTick>=animationSpeed){
                animationTick=0;
                animationIndex++;
                int maxSprites = SPRITE(Type_of_enemy,State_of_enemy);
                if (animationIndex>=maxSprites){
                    // Keep last frame of death animation
                    animationIndex = maxSprites - 1;
                }
            }
        } else {
            // Normal animation loop
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

