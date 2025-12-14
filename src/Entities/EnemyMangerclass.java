package Entities;

import Function.LoadSave;
import GameLevels.Level;
import State.Playing;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Function.features.UI.Enemies.*;

public class EnemyMangerclass {
    private Playing playing;
    private BufferedImage[][] Enemy_images;
    private BufferedImage[][] Shark_images;
    private BufferedImage[][] Star_images;
    private ArrayList<Enemy1> crabbies= new ArrayList<>();
    private ArrayList<Shark> sharks = new ArrayList<>();
    private ArrayList<Star> stars= new ArrayList<>();
    public EnemyMangerclass(Playing playing){
        this.playing=playing;
        LoadEnemyImg();
    }

    public void loadLevelData(int[][] levelData) {
        for (Enemy1 c : crabbies) {
            c.loadLevelData(levelData);
        }
        for (Shark s : sharks) {
            s.loadLevelData(levelData);
        }
        for (Star s: stars){
            s.loadLevelData(levelData);
        }
    }

    public void setPlayer(Player player) {
        for (Enemy1 c : crabbies) {
            c.setPlayer(player);
        }
        for (Shark s : sharks) {
            s.setPlayer(player);
        }
        for (Star s: stars){
            s.setPlayer(player);
        }
    }
    public void draw(Graphics g,int levelxoffset){
        drawCrbbies(g,levelxoffset);
        drawSharks(g,levelxoffset);
        drawStars(g,levelxoffset);
    }

    private void drawCrbbies(Graphics g, int levelxoffset) {
        for (Enemy1 c: crabbies){
            int state = c.getState_of_enemy();
            int animIndex = c.getAnimationIndex();
            // Safety check to prevent array index out of bounds
            if (state >= 0 && state < Enemy_images.length && 
                animIndex >= 0 && animIndex < Enemy_images[state].length &&
                Enemy_images[state][animIndex] != null) {
                g.drawImage(Enemy_images[state][animIndex],(int)c.getBox().x-levelxoffset,(int)c.getBox().y,ENEMY1_WIDTH,ENEMY1_HEIGHT,null);
            }
        }
    }
    
    private void drawSharks(Graphics g, int levelxoffset) {
        for (Shark s: sharks){
            int state = s.getState_of_enemy();
            int animIndex = s.getAnimationIndex();
            if (state >= 0 && state < Shark_images.length && 
                animIndex >= 0 && animIndex < Shark_images[state].length &&
                Shark_images[state][animIndex] != null) {
                g.drawImage(Shark_images[state][animIndex],(int)s.getBox().x-levelxoffset,(int)s.getBox().y,SHARK_WIDTH,SHARK_HEIGHT,null);
            }
        }
    }
    private void drawStars(Graphics g , int levelxoffset){
        for (Star s : stars){
            int state = s.getState_of_enemy();
            int animIndex = s.getAnimationIndex();
            // Safety check to prevent array index out of bounds
            if (state >= 0 && state < Star_images.length && 
                animIndex >= 0 && animIndex < Star_images[state].length &&
                Star_images[state][animIndex] != null) {
                g.drawImage(Star_images[state][animIndex],(int)s.getBox().x-levelxoffset,(int)s.getBox().y,PINKSTAR_WIDTH,PINKSTAR_HEIGHT,null);
            }
        }
    }
    
    public void addEnemies(Level level){
        // Create a NEW ArrayList with copies of the enemies, don't use the same reference!
        crabbies = new ArrayList<>(level.getCrabs());
        sharks = new ArrayList<>(level.getSharks());
        stars= new ArrayList<>(level.getStars());

        // Reset all enemies to alive state
        for (Enemy1 crab : crabbies) {
            crab.reset();
        }
        for (Shark shark : sharks) {
            shark.reset();
        }
        for (Star star : stars) {
            star.reset();
        }

        loadLevelData(level.getLvlData());

        if (playing != null && playing.getPlayer() != null) {
            setPlayer(playing.getPlayer());
        }
    }


    public void resetEnemies() {
        crabbies.clear();
        sharks.clear();
        stars.clear();
    }
    private BufferedImage[][] getImgArr(BufferedImage atlas, int xSize, int ySize, int spriteW, int spriteH) {
        BufferedImage[][] tempArr = new BufferedImage[ySize][xSize];
        for (int j = 0; j < tempArr.length; j++)
            for (int i = 0; i < tempArr[j].length; i++)
                tempArr[j][i] = atlas.getSubimage(i * spriteW, j * spriteH, spriteW, spriteH);
        return tempArr;
    }
    public void LoadEnemyImg() {

        Star_images= getImgArr(LoadSave.GetAtlas(LoadSave.PINKENEMY_PNG),8,5,PINKSTAR_WIDTH_DEFAULT,PINKSTAR_HEIGHT_DEFAULT);

        Enemy_images= new BufferedImage[5][9];
        BufferedImage load= LoadSave.GetAtlas(LoadSave.ENEMY_1_PNG);
        for (int i = 0; i < Enemy_images.length; i++) {
            for (int j = 0; j < Enemy_images[i].length; j++) {
                Enemy_images[i][j]=load.getSubimage(j*ENEMY1_WIDTH_DEFAULT,i*ENEMY1_HEIGHT_DEFAULT,ENEMY1_WIDTH_DEFAULT,ENEMY1_HEIGHT_DEFAULT);
            }
        }
        // Load Shark images
        BufferedImage sharkLoad = LoadSave.GetAtlas(LoadSave.SHARK_PNG);
        int sharkAtlasWidth = sharkLoad.getWidth();
        int sharkAtlasHeight = sharkLoad.getHeight();
        
        // Calculate maximum frames per row based on atlas width
        int maxFramesPerRow = sharkAtlasWidth / SHARK_WIDTH_DEFAULT;

        Shark_images = new BufferedImage[5][9];
        
        for (int i = 0; i < Shark_images.length; i++) {
            // Get the number of frames for this state from SPRITE method
            int framesForState = SPRITE(SHARK, i);
            
            // Only extract frames that exist in the atlas
            int framesToExtract = Math.min(framesForState, maxFramesPerRow);
            
            // Make sure we don't go beyond the image height
            if (i * SHARK_HEIGHT_DEFAULT + SHARK_HEIGHT_DEFAULT <= sharkAtlasHeight) {
                for (int j = 0; j < framesToExtract; j++) {
                    // Make sure we don't go beyond the image width
                    if (j * SHARK_WIDTH_DEFAULT + SHARK_WIDTH_DEFAULT <= sharkAtlasWidth) {
                        Shark_images[i][j] = sharkLoad.getSubimage(
                            j * SHARK_WIDTH_DEFAULT, 
                            i * SHARK_HEIGHT_DEFAULT, 
                            SHARK_WIDTH_DEFAULT, 
                            SHARK_HEIGHT_DEFAULT
                        );
                    }
                }
            }
        }
    }
    public void update(int[][] lvldata, Player player){
        boolean anyEnemyAlive = false;

        for (Enemy1 c: crabbies){
            if (c.isDead()) {

                c.update();
            } else {
                c.update();
                anyEnemyAlive = true;
            }
        }
        
        for (Shark s: sharks){
            if (s.isDead()) {
                
                s.update();
            } else {
                s.update();
                anyEnemyAlive = true;
            }
        }
        for (Star s: stars){
            if (s.isDead){
           
                s.update();
            } else {
                s.update();
                anyEnemyAlive=true;
            }
        }

        
        if ((crabbies.size() > 0 || sharks.size() > 0 || stars.size()>0) && !anyEnemyAlive){
            // Only set completed if not already completed and game is not in completed state
            if (!playing.isLevelCompleted() && !playing.isGameCompleted()) {
                playing.setLevelCompleted(true);
            }
        }
    }

    // Check if player's attack hits any enemy
    public void checkPlayerAttack(Rectangle2D.Float attackBox) {
        if (attackBox == null) {
            return;
        }
        
        

        // Check Enemy1 (Crabs)
        for (Enemy1 enemy : crabbies) {
            if (!enemy.isDead() && !enemy.isHit()) {
                if (isAccurateHit(attackBox, enemy.getBox())) {
                    enemy.takeDamage(35);
                    return; // Only hit one enemy per attack
                }
            }
        }
        
        // Check Sharks
        for (Shark enemy : sharks) {
            if (!enemy.isDead() && !enemy.isHit()) {
                if (isAccurateHit(attackBox, enemy.getBox())) {
                    enemy.takeDamage(30); // Balanced damage
                    return;
                }
            }
        }
        
        // Check Stars
        for (Star enemy : stars) {
            if (!enemy.isDead() && !enemy.isHit()) {
                if (isAccurateHit(attackBox, enemy.getBox())) {
                    enemy.takeDamage(30); // Balanced damage
                    return;
                }
            }
        }
    }
    

    private boolean isAccurateHit(Rectangle2D.Float attackBox, Rectangle2D.Float enemyBox) {
        if (!attackBox.intersects(enemyBox)) {
            return false;
        }

        float verticalOverlap = Math.min(attackBox.y + attackBox.height, enemyBox.y + enemyBox.height) - 
                               Math.max(attackBox.y, enemyBox.y);
        float minRequiredOverlap = Math.min(attackBox.height, enemyBox.height) * 0.5f;
        
        if (verticalOverlap < minRequiredOverlap) {
            return false;
        }
        

        float horizontalDistance = Math.abs((attackBox.x + attackBox.width / 2) - 
                                           (enemyBox.x + enemyBox.width / 2));
        float maxDistance = Math.max(attackBox.width, enemyBox.width) * 1.5f;
        
        return horizontalDistance <= maxDistance;
    }
    

    public void checkEnemyAttacks(Player player) {
        if (player == null || player.isDead() || player.isInvulnerable()) {
            return; // Player can't be hit if dead or invulnerable
        }

        for (Enemy1 enemy : crabbies) {
            if (!enemy.isDead()) {
                Rectangle2D.Float enemyAttackBox = enemy.getAttackHitbox();
                if (enemyAttackBox != null && isAccurateEnemyHit(enemyAttackBox, player.getBox())) {
                    player.takeDamage(5, enemy.getBox().x);
                    return;
                }
            }
        }
        
        // Check Sharks
        for (Shark enemy : sharks) {
            if (!enemy.isDead()) {
                Rectangle2D.Float enemyAttackBox = enemy.getAttackHitbox();
                if (enemyAttackBox != null && isAccurateEnemyHit(enemyAttackBox, player.getBox())) {
                    player.takeDamage(6, enemy.getBox().x);
                    return;
                }
            }
        }
        
        // Check Stars
        for (Star enemy : stars) {
            if (!enemy.isDead()) {
                Rectangle2D.Float enemyAttackBox = enemy.getAttackHitbox();
                if (enemyAttackBox != null && isAccurateEnemyHit(enemyAttackBox, player.getBox())) {
                    player.takeDamage(6, enemy.getBox().x); // Slightly more damage
                    return;
                }
            }
        }
    }
    

    private boolean isAccurateEnemyHit(Rectangle2D.Float enemyAttackBox, Rectangle2D.Float playerBox) {
        if (!enemyAttackBox.intersects(playerBox)) {
            return false;
        }
        
        // Check vertical alignment for more accurate hits
        float verticalOverlap = Math.min(enemyAttackBox.y + enemyAttackBox.height, playerBox.y + playerBox.height) - 
                               Math.max(enemyAttackBox.y, playerBox.y);
        float minRequiredOverlap = Math.min(enemyAttackBox.height, playerBox.height) * 0.6f;
        
        return verticalOverlap >= minRequiredOverlap;
    }

    public Playing getPlaying() {
        return playing;
    }

    public void setPlaying(Playing playing) {
        this.playing = playing;
    }

    public BufferedImage[][] getEnemy_images() {
        return Enemy_images;
    }

    public void setEnemy_images(BufferedImage[][] enemy_images) {
        Enemy_images = enemy_images;
    }

    public BufferedImage[][] getShark_images() {
        return Shark_images;
    }

    public void setShark_images(BufferedImage[][] shark_images) {
        Shark_images = shark_images;
    }

    public ArrayList<Enemy1> getCrabbies() {
        return crabbies;
    }

    public void setCrabbies(ArrayList<Enemy1> crabbies) {
        this.crabbies = crabbies;
    }

    public ArrayList<Shark> getSharks() {
        return sharks;
    }

    public void setSharks(ArrayList<Shark> sharks) {
        this.sharks = sharks;
    }

    public ArrayList<Star> getStars() {
        return stars;
    }

    public void setStars(ArrayList<Star> stars) {
        this.stars = stars;
    }
}
