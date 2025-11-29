package GameLevels;

import Entities.Enemy1;
import Function.LoadSave;
import Function.StaticMethodsforMovement;
import Rewards.Arrow;
import Rewards.CannonGun;
import Rewards.Container;
import Rewards.Potions;
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
    private ArrayList<Potions> potions;
    private ArrayList<Container> containers;
    private ArrayList<Arrow> arrows;
    private ArrayList<CannonGun> cannonGuns;

    private int lvlTiles_Width;
    private int maxTilesoff;
    private int maxLevelOffsetX;
    private Point spawn;
    Level(BufferedImage img){
        this.img=img;
        CreateLevel();
        CreateEnemies();
        createPotions();
        createArrows();
        createContainers();
        createCannons();
        CalculateOffsets();
    }

    private void createCannons() {
        cannonGuns=StaticMethodsforMovement.getCannon(img);
    }

    private void createArrows() {
        arrows= StaticMethodsforMovement.getSpikes(img);
    }

    private void createContainers() {
        containers= StaticMethodsforMovement.getContainers(img);
    }

    public ArrayList<Container> getContainers() {
        return containers;
    }

    public ArrayList<Potions> getPotions() {
        return potions;
    }

    private void createPotions() {
        potions=StaticMethodsforMovement.getPotions(img);
    }

    private void CalculateOffsets() {
        lvlTiles_Width=img.getWidth();
        maxLevelOffsetX=lvlTiles_Width- game.TILE_WIDTH;
        maxLevelOffsetX=game.TILE_SIZE*maxLevelOffsetX;
    }

    public ArrayList<Arrow> getArrows() {
        return arrows;
    }

    public void setArrows(ArrayList<Arrow> arrows) {
        this.arrows = arrows;
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

        // Check if value is a valid tile index (0-47 for 48 tiles)
        if (value < 0 || value >= 48) {
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

    public ArrayList<CannonGun> getCannonGuns() {
        return cannonGuns;
    }
}
