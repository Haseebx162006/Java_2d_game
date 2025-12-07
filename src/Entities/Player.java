package Entities;

import Function.LoadSave;
import Sounds.AudioPlayer;
import State.Playing;
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
    private float XOffset;// these offset variables are for the collision box
    private float YOffset;
    private static final float FOOT_ADJUST = 12f * game.SCALE;
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
    private int tileY = 0;
    private int flipX = 0;
    private int flipW = 1;
    public int getTileY() {
        return tileY;
    }

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
    private static final int ATTACK_ACTIVE_FRAME_START = 0; // Attack hitbox is active from frame 0
    private static final int ATTACK_ACTIVE_FRAME_END = 2; // Attack hitbox is active until frame 2 (of 3 total)
    private boolean invulnerable = false;
    private int invulnerabilityTimer = 0;
    private static final int ATTACK_COOLDOWN = 15; // Cooldown between attacks (reduced for better responsiveness)
    private int attackCooldown = 0;
    private Playing playing;
    private float knockbackX = 0; // Knockback velocity
    private static final float KNOCKBACK_SPEED = 4.0f * game.SCALE; // Increased for better feedback
    private static final int KNOCKBACK_DURATION = 12; // frames (slightly longer)
    private int knockbackTimer = 0;
    private int lastAttackFrame = -1; // Track which frame the attack was processed on

    public void setDead(boolean dead) {
        isDead = dead;
    }
    public void setSpawn(Point spawn) {
        this.x = spawn.x;
        this.y = spawn.y;
        box.x = x;
        box.y = y;
        // Force player to be in air when spawning to avoid getting stuck
        DuringAir = true;
        airSpeed = 0;
    }
    private static final int INVULNERABILITY_DURATION = 60; // 1 second at 60 FPS
    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y,width,height);
        this.playing=playing;
        load_Animations();
        CreateBox(x,y,game.SCALE*20,game.SCALE*29);
        XOffset = (width - box.width) / 2f;
        YOffset = Math.max(0, height - box.height - FOOT_ADJUST);
    }
    public void changeHealth(int value) {
        currentHealth += value;
        if (currentHealth <= 0){
            currentHealth=0;
            KillPlayer();
        }
        else if (currentHealth >= maxHealth)
            currentHealth = maxHealth;
    }
    public void UpdatePlayer(){
        if (isDead) {
            updateDeathAnimation();
            // Still update animation so death animation plays
            setAnimation();
            updateAnimation();
            return;
        }

        UpdatePosition();
        
        // Check for water collision after position update to catch movement into water
        if (IsEntityInWater(box, playing.getLevelManager().getLevel().getLvlData())) {
            KillPlayer();
            return;
        }
        
        playing.checkObjectHit(box);
        
        // Always check for spikes/arrows regardless of movement (player can fall onto them)
        checkArrowChecked();
        
        // Always check for potions when moving horizontally
        if (Player_is_moving) {
            checkPotiontouched();
            tileY=(int)(box.y/game.TILE_SIZE);
        }
        updateCombat();
        updateKnockback();
        updateAnimation();
        setAnimation();
        updateHitCooldown();
        updateInvulnerability();
    }

    private void checkArrowChecked() {
        playing.checkTrapHit(this);
    }

    private void checkPotiontouched() {
        playing.checkPotionTouched(box);
    }

    public void changePower(int value) {
        System.out.println("Added power!");
    }
    public void KillPlayer(){
        this.currentHealth=0;
        isDead=true;
        // Reset death animation state
        Playermove = GROUND;
        Animation_tick = 0;
        Animation_index = 0;
        deathAnimationTimer = 0;
        // Play die sound
        if (playing != null && playing.Getgame() != null && playing.Getgame().getAudioPlayer() != null) {
            playing.Getgame().getAudioPlayer().playEffect(AudioPlayer.DIE);
        }
    }

    private void checkInsideWater() {
        System.out.println("=== WATER CHECK ===");
        System.out.println("Player box.y: " + box.y + ", box.height: " + box.height);
        System.out.println("Player bottom Y: " + (box.y + box.height));

        int[][] lvlData = playing.getLevelManager().getLevel().getLvlData();
        System.out.println("Level data dimensions: " + lvlData.length + " x " + lvlData[0].length);

        boolean inWater = IsEntityInWater(this.box, lvlData);
        System.out.println("IsEntityInWater returned: " + inWater);

        if (inWater){
            System.out.println("!!! PLAYER DIED FROM WATER !!!");
            KillPlayer();
            setDead(true);
        }
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
        // Handle attack cooldown
        if (attackCooldown > 0) {
            attackCooldown--;
        }

        // Update attack direction continuously based on movement input for better responsiveness
        if (left) {
            attackDirection = LEFT;
        } else if (right) {
            attackDirection = RIGHT;
        }

        // Reset attack hit tracking when attack animation starts (first frame)
        if (attacking && Playermove == ATTACK && Animation_index == 0 && Animation_tick == 0) {
            resetAttackTracking();
        }

        // Reset attack hit tracking when attack ends
        if (!attacking && attackHitProcessed) {
            attackHitProcessed = false;
            lastAttackFrame = -1;
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
        // Overload without enemy position - use default knockback
        takeDamage(damage, box.x);
    }
    
    public void takeDamage(int damage, float enemyX) {
        if (invulnerable || isDead()) {
            return; // Can't take damage while invulnerable or dead
        }

        currentHealth -= damage;
        if (currentHealth <= 0) {
            currentHealth = 0;
            KillPlayer();
        }

        // Activate invulnerability
        invulnerable = true;
        invulnerabilityTimer = INVULNERABILITY_DURATION;
        
        // Apply knockback when hit - push player away from enemy
        applyKnockback(enemyX);
    }
    
    private void applyKnockback(float enemyX) {
        // Push player away from the enemy that hit them
        if (enemyX < box.x) {
            // Enemy is to the left, push player right
            knockbackX = KNOCKBACK_SPEED;
        } else {
            // Enemy is to the right, push player left
            knockbackX = -KNOCKBACK_SPEED;
        }
        knockbackTimer = KNOCKBACK_DURATION;
    }
    
    private void updateKnockback() {
        // Knockback is now handled in UpdatePosition() method
        // This method just decrements the timer and applies damping
        if (knockbackTimer > 0) {
            knockbackTimer--;
            // Gradually reduce knockback with smooth damping
            knockbackX *= 0.88f; // Slightly slower damping for smoother feel
            if (knockbackTimer <= 0 || Math.abs(knockbackX) < 0.1f) {
                knockbackX = 0;
                knockbackTimer = 0;
            }
        }
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
        // Reset position to spawn point from level, or default
        Point spawnPoint = playing.getLevelManager().getLevel().getPlayerSpawn();
        if (spawnPoint != null) {
            box.x = spawnPoint.x;
            box.y = spawnPoint.y;
            x = spawnPoint.x;
            y = spawnPoint.y;
        } else {
            // Default spawn if no spawn point set
            box.x = 200;
            box.y = 200;
            x = 200;
            y = 200;
        }
        // Always start in air when resetting to spawn point so player falls safely
        DuringAir = true;
        airSpeed = 0;
        knockbackX = 0;
        knockbackTimer = 0;
        invulnerable = false;
        invulnerabilityTimer = 0;
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
        
        // Attack hitbox is active during frames 0-2 for better hit detection
        if (Animation_index < ATTACK_ACTIVE_FRAME_START || Animation_index > ATTACK_ACTIVE_FRAME_END) {
            return null;
        }
        
        // Enhanced melee attack hitbox - better range and positioning
        float attackWidth = 40 * game.SCALE; // Increased width for better reach
        float attackHeight = box.height * 0.9f; // Taller hitbox for better vertical coverage
        float attackY = box.y + (box.height - attackHeight) / 2; // Center vertically
        
        // Determine attack direction based on player movement
        if (left) {
            attackDirection = LEFT;
        } else if (right) {
            attackDirection = RIGHT;
        }
        // If not moving, use last direction
        
        float attackX;
        
        // Attack hitbox extends in the direction player is facing
        if (attackDirection == LEFT) {
            attackX = box.x - attackWidth; // Extend to the left
        } else {
            attackX = box.x + box.width; // Extend to the right
        }
        
        return new Rectangle2D.Float(attackX, attackY, attackWidth, attackHeight);
    }
    
    // Check if this attack frame has already been processed
    public boolean isAttackFrameProcessed(int frame) {
        return lastAttackFrame == frame;
    }
    
    // Mark attack frame as processed
    public void markAttackFrameProcessed(int frame) {
        lastAttackFrame = frame;
    }
    
    // Reset attack tracking when starting new attack
    public void resetAttackTracking() {
        attackHitProcessed = false;
        lastAttackFrame = -1;
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
    public void setAttack(boolean attacking) {
        // Only allow starting attack if not already attacking and cooldown is complete
        if (attacking && !this.attacking && attackCooldown <= 0) {
            this.attacking = true;
            resetAttackTracking(); // Reset tracking for new attack
            attackCooldown = ATTACK_COOLDOWN;
            // Set attack animation
            Playermove = ATTACK;
            Animation_tick = 0;
            Animation_index = 0;
        } else if (!attacking) {
            // Allow stopping attack
            this.attacking = false;
        }
    }
    private void UpdatePosition() {
        Player_is_moving=false;
        
        // Apply knockback first if active (takes priority)
        if (knockbackTimer > 0) {
            float newX = box.x + knockbackX;
            if (CanMove(newX, box.y, box.width, box.height, DataOfLevel)) {
                box.x = newX;
            }
            // Still allow gravity during knockback
            if (DuringAir) {
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
            return; // Don't allow other movement during knockback
        }
        
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
        // Play jump sound
        if (playing != null && playing.Getgame() != null && playing.Getgame().getAudioPlayer() != null) {
            playing.Getgame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
        }
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
            if (Animation_index>=Function.features.player_features.GetPlayerMove(Playermove)){
                // For death animation, stay on last frame instead of looping
                if (isDead && Playermove == GROUND) {
                    Animation_index = Function.features.player_features.GetPlayerMove(Playermove) - 1;
                } else {
                    Animation_index=0;
                    attacking=false;
                }
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
        // Check if player is stuck in tiles and move them up if needed
        if (DataOfLevel != null && CanMove(box.x, box.y, box.width, box.height, DataOfLevel)) {
            // Position is clear, check if on floor
            if (!OnFloor(box, DataOfLevel)){
                DuringAir=true;
            }
        } else {
            // Player is stuck in tiles, move them up until clear
            DuringAir = true;
            airSpeed = 0;
            // Try moving up tile by tile until we find a clear position
            for (int i = 1; i <= 10; i++) {
                float testY = box.y - (i * game.TILE_SIZE);
                if (testY >= 0 && CanMove(box.x, testY, box.width, box.height, DataOfLevel)) {
                    box.y = testY;
                    y = testY;
                    break;
                }
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

    public BufferedImage[][] getAnimation() {
        return Animation;
    }

    public void setAnimation(BufferedImage[][] animation) {
        Animation = animation;
    }

    public float getXOffset() {
        return XOffset;
    }

    public void setXOffset(float XOffset) {
        this.XOffset = XOffset;
    }

    public float getYOffset() {
        return YOffset;
    }

    public void setYOffset(float YOffset) {
        this.YOffset = YOffset;
    }

    public float getGravity() {
        return Gravity;
    }

    public void setGravity(float gravity) {
        Gravity = gravity;
    }

    public int getAnimation_tick() {
        return Animation_tick;
    }

    public void setAnimation_tick(int animation_tick) {
        Animation_tick = animation_tick;
    }

    public int getAnimation_index() {
        return Animation_index;
    }

    public void setAnimation_index(int animation_index) {
        Animation_index = animation_index;
    }

    public int getAnimation_Speed() {
        return Animation_Speed;
    }

    public void setAnimation_Speed(int animation_Speed) {
        Animation_Speed = animation_Speed;
    }

    public int getPlayermove() {
        return Playermove;
    }

    public void setPlayermove(int playermove) {
        Playermove = playermove;
    }

    public boolean isPlayer_is_moving() {
        return Player_is_moving;
    }

    public void setPlayer_is_moving(boolean player_is_moving) {
        Player_is_moving = player_is_moving;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public boolean isUp() {
        return up;
    }

    public boolean isDown() {
        return down;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isJump() {
        return jump;
    }

    public float getPlayerSpeed() {
        return playerSpeed;
    }

    public void setPlayerSpeed(float playerSpeed) {
        this.playerSpeed = playerSpeed;
    }

    public boolean isCanJump() {
        return canJump;
    }

    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }

    public int[][] getDataOfLevel() {
        return DataOfLevel;
    }

    public void setDataOfLevel(int[][] dataOfLevel) {
        DataOfLevel = dataOfLevel;
    }

    public float getAirSpeed() {
        return airSpeed;
    }

    public void setAirSpeed(float airSpeed) {
        this.airSpeed = airSpeed;
    }

    public float getJumpSpeed() {
        return jumpSpeed;
    }

    public void setJumpSpeed(float jumpSpeed) {
        this.jumpSpeed = jumpSpeed;
    }

    public float getFallSpeed() {
        return fallSpeed;
    }

    public void setFallSpeed(float fallSpeed) {
        this.fallSpeed = fallSpeed;
    }

    public boolean isDuringAir() {
        return DuringAir;
    }

    public void setDuringAir(boolean duringAir) {
        DuringAir = duringAir;
    }

    public void setTileY(int tileY) {
        this.tileY = tileY;
    }

    public int getFlipX() {
        return flipX;
    }

    public void setFlipX(int flipX) {
        this.flipX = flipX;
    }

    public int getFlipW() {
        return flipW;
    }

    public void setFlipW(int flipW) {
        this.flipW = flipW;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean hit) {
        isHit = hit;
    }

    public int getHitCooldown() {
        return hitCooldown;
    }

    public void setHitCooldown(int hitCooldown) {
        this.hitCooldown = hitCooldown;
    }

    public int getDeathAnimationTimer() {
        return deathAnimationTimer;
    }

    public void setDeathAnimationTimer(int deathAnimationTimer) {
        this.deathAnimationTimer = deathAnimationTimer;
    }

    public boolean isAttackHitProcessed() {
        return attackHitProcessed;
    }

    public int getAttackDirection() {
        return attackDirection;
    }

    public void setAttackDirection(int attackDirection) {
        this.attackDirection = attackDirection;
    }

    public boolean isInvulnerable() {
        return invulnerable;
    }

    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

    public int getInvulnerabilityTimer() {
        return invulnerabilityTimer;
    }

    public void setInvulnerabilityTimer(int invulnerabilityTimer) {
        this.invulnerabilityTimer = invulnerabilityTimer;
    }

    public Playing getPlaying() {
        return playing;
    }

    public void setPlaying(Playing playing) {
        this.playing = playing;
    }
}
