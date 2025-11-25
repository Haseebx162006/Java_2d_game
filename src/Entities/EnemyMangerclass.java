package Entities;

import Function.LoadSave;
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
        addEnemies();
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
    public void addEnemies(){
        crabbies=LoadSave.getEnemyCrab();
    }
    
    public void resetEnemies() {
        // Reset all enemies for replay
        crabbies.clear();
        addEnemies();
    }
    private void LoadEnemyImg() {
        Enemy_images= new BufferedImage[5][9];
        BufferedImage load= LoadSave.GetAtlas(LoadSave.ENEMY_1_PNG);
        for (int i = 0; i < Enemy_images.length; i++) {
            for (int j = 0; j < Enemy_images[i].length; j++) {
                Enemy_images[i][j]=load.getSubimage(j*ENEMY1_WIDTH_DEFAULT,i*ENEMY1_HEIGHT_DEFAULT,ENEMY1_WIDTH_DEFAULT,ENEMY1_HEIGHT_DEFAULT);
            }
        }
    }
    public void update(){
        for (Enemy1 c: crabbies){
            c.update();
        }
    }
    
    // Check if player's attack hits any enemy
    public void checkPlayerAttack(Rectangle2D.Float attackBox) {
        for (Enemy1 enemy : crabbies) {
            if (!enemy.isDead() && attackBox.intersects(enemy.getBox())) {
                enemy.takeDamage(20); // Player deals 10 damage
            }
        }
    }
    
    // Check if any enemy's attack hits player
    public void checkEnemyAttacks(Player player) {
        for (Enemy1 enemy : crabbies) {
            if (!enemy.isDead()) {
                Rectangle2D.Float enemyAttackBox = enemy.getAttackHitbox();
                if (enemyAttackBox != null && enemyAttackBox.intersects(player.getBox())) {
                    player.takeDamage(5); // Enemy deals 5 damage
                }
            }
        }
    }
}
