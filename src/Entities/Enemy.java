package Entities;
import static Function.features.UI.Enemies.*;
import static Function.StaticMethodsforMovement.*;
import static Function.features.PlayerDirectons.*;
import main.game;

public abstract class Enemy extends Entity {
    public Enemy(float x, float y, int width, int height,int Type_of_enemy) {
        super(x, y, width, height);
        this.Type_of_enemy=Type_of_enemy;
        CreateBox(x,y,width,height);
        this.State_of_enemy = RUNNING;
    }
    private float Walk=1.0f*game.SCALE;
    private int animationIndex;
    private int State_of_enemy;
    private int Type_of_enemy;
    private int animationTick;
    private int[][] levelData;
    private float airSpeed = 0;
    private boolean inAir = false;
    private int walkDir=LEFT;
    private float gravity = 0.04f * game.SCALE;
    private float fallSpeed = 0.5f * game.SCALE;

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
            airSpeed = 0; // Initialize airSpeed when starting in air
        }
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

    private int animationSpeed=25;

    public void update(){
        if (levelData != null) {
            updatePosition();
        }
        updateAnimation();
    }

    private void updatePosition() {
        // Check if enemy is on floor
        if (!inAir) {
            if (!OnFloor(box, levelData)) {
                inAir = true;
                airSpeed = 0;
            }
        }

        if (inAir) {
            // Apply gravity when in air
            float newY = box.y + airSpeed;
            if (CanMove(box.x, newY, box.width, box.height, levelData)) {
                box.y = newY;
                airSpeed += gravity;
            } else {
                // Hit something (floor or ceiling)
                float correctedY = CheckUnderEnvironmentorAbove(box, airSpeed);
                if (correctedY >= 0 && correctedY < game.GAME_HEIGHT) {
                    box.y = correctedY;
                }
                if (airSpeed > 0) {
                    // Hit floor
                    airSpeed = 0;
                    inAir = false;
                } else {
                    // Hit ceiling
                    airSpeed = fallSpeed;
                }
            }
        } else {
            // Enemy is on ground, handle patrolling
            switch (State_of_enemy){
                case IDLE:
                    State_of_enemy=RUNNING;
                    break;
                case RUNNING:
                    float Speed=0;
                    if (walkDir==LEFT){
                        Speed=-Walk;
                    }
                    else{
                        Speed=Walk;
                    }
                    
                    // Check if can move horizontally and if there's floor ahead
                    float newX = box.x + Speed;
                    boolean canMoveHorizontally = CanMove(newX, box.y, box.width, box.height, levelData);
                    boolean floorAhead = isFloorAhead(Speed, levelData);
                    
                    if (canMoveHorizontally && floorAhead) {
                        box.x = newX;
                    } else {
                        // Can't move (wall collision) or no floor ahead (cliff) - change direction
                        ChangeDirection();
                    }
                    break;
            }
        }
    }
    
    private boolean isFloorAhead(float Speed, int[][] levelData) {
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

    private void updateAnimation(){
        animationTick++;
        if (animationTick>=animationSpeed){
            animationTick=0;
            animationIndex++;
            if (animationIndex>=SPRITE(Type_of_enemy,State_of_enemy)){
                animationIndex=0;
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

