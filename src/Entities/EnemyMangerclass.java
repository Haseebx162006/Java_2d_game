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
    private ArrayList<Enemy1> crabbies= new ArrayList<>();
    public EnemyMangerclass(Playing playing){
        this.playing=playing;
        LoadEnemyImg();
    }

    public void loadLevelData(int[][] levelData) {
        for (Enemy1 c : crabbies) {
            c.loadLevelData(levelData);
        }
    }

    public void setPlayer(Player player) {
        for (Enemy1 c : crabbies) {
            c.setPlayer(player);
        }
    }
    public void draw(Graphics g,int levelxoffset){
        drawCrbbies(g,levelxoffset);
    }

    private void drawCrbbies(Graphics g, int levelxoffset) {
        for (Enemy1 c: crabbies){
            g.drawImage(Enemy_images[c.getState_of_enemy()][c.getAnimationIndex()],(int)c.getBox().x-levelxoffset,(int)c.getBox().y,ENEMY1_WIDTH,ENEMY1_HEIGHT,null);
        }
    }
    public void addEnemies(Level level){
        // Create a NEW ArrayList with copies of the enemies, don't use the same reference!
        crabbies = new ArrayList<>(level.getCrabs());


        // Ensure every enemy knows about the level data for collision & movement
        loadLevelData(level.getLvlData());

        // Also make sure each enemy has a reference to the current player
        if (playing != null && playing.getPlayer() != null) {
            setPlayer(playing.getPlayer());
        }
    }


    public void resetEnemies() {
        // Reset all enemies for replay
        crabbies.clear();
    }
    public void LoadEnemyImg() {
        Enemy_images= new BufferedImage[5][9];
        BufferedImage load= LoadSave.GetAtlas(LoadSave.ENEMY_1_PNG);
        for (int i = 0; i < Enemy_images.length; i++) {
            for (int j = 0; j < Enemy_images[i].length; j++) {
                Enemy_images[i][j]=load.getSubimage(j*ENEMY1_WIDTH_DEFAULT,i*ENEMY1_HEIGHT_DEFAULT,ENEMY1_WIDTH_DEFAULT,ENEMY1_HEIGHT_DEFAULT);
            }
        }
    }
    public void update(int[][] lvldata, Player player){
        boolean anyEnemyAlive = false;

        for (Enemy1 c: crabbies){
            if (!c.isDead()) {
                c.update();
                anyEnemyAlive = true;
            }
        }

        // Only complete level if there were enemies AND they're all dead
        if (crabbies.size() > 0 && !anyEnemyAlive){
            playing.setLevelCompleted(true);
        }
    }

    // Check if player's attack hits any enemy
    public void checkPlayerAttack(Rectangle2D.Float attackBox) {
        if (attackBox == null) {
            return;
        }
        
        // Check all enemies and hit the first valid one (prevents multiple hits per frame)
        for (Enemy1 enemy : crabbies) {
            if (!enemy.isDead() && !enemy.isHit()) {
                // Use more precise hit detection - check if attack box intersects enemy hitbox
                if (attackBox.intersects(enemy.getBox())) {
                    enemy.takeDamage(25);
                    // Only hit one enemy per attack frame for better feel
                    return;
                }
            }
        }
    }
    
    // Check if any enemy's attack hits player
    public void checkEnemyAttacks(Player player) {
        for (Enemy1 enemy : crabbies) {
            if (!enemy.isDead()) {
                Rectangle2D.Float enemyAttackBox = enemy.getAttackHitbox();
                if (enemyAttackBox != null && enemyAttackBox.intersects(player.getBox())) {
                    // Pass enemy position for proper knockback direction
                    player.takeDamage(5, enemy.getBox().x);
                    // Only allow one hit per enemy attack animation
                    return; // Prevent multiple enemies hitting in same frame
                }
            }
        }
    }
}
