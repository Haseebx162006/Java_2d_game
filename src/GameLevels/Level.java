package GameLevels;

import Entities.Enemy1;
import main.game;

import javax.script.ScriptEngine;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static Function.StaticMethodsforMovement.GetLevelData;
import static Function.StaticMethodsforMovement.getEnemyCrab;
public class Level {
    private int[][] levelData;
    private BufferedImage img;
    private ArrayList<Enemy1> enemy1;
    private int lvlTiles_Width;
    private int maxTilesoff;
    private int maxLevelOffsetX;
    private Point spawn;
    Level(BufferedImage img){
        this.img=img;
        CreateLevel();
        CreateEnemies();
        CalculateOffsets();
    }

    private void CalculateOffsets() {
        lvlTiles_Width=img.getWidth();
        maxLevelOffsetX=lvlTiles_Width- game.TILE_WIDTH;
        maxLevelOffsetX=game.TILE_SIZE*maxLevelOffsetX;
    }

    private void CreateEnemies() {
        enemy1=getEnemyCrab(img);
    }

    private void CreateLevel() {
        // Generate level data from the image and keep a single authoritative array
        levelData = GetLevelData(img);
    }
    public int getleveloffset(){
        return maxLevelOffsetX;
    }
    public ArrayList<Enemy1> getEnemy1(){
        return enemy1;
    }
    public int getSpriteIndex(int x, int y){
        int value = levelData[y][x];

        if (value < 0 || value >= game.TILE_SIZE) {
            return 0;
        }

        return value;
    }
    public int[][] getLvldata(){
        return levelData;
    }
    public Point getPlayerSpawn() {
        return spawn;
    }
   // private void calcPlayerSpawn() {
      //  spawn = GetPlayerSpawn(img);
   // }
}
