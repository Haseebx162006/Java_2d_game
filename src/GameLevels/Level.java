package GameLevels;

import Entities.Enemy1;
import Function.LoadSave;
import Function.StaticMethodsforMovement;
import Function.features;
import Rewards.*;
import Rewards.Container;
import main.game;

import javax.script.ScriptEngine;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static Function.StaticMethodsforMovement.GetLevelData;
import static Function.StaticMethodsforMovement.getEnemyCrab;
import static Function.features.Objects.*;
import static Function.features.UI.Enemies.ENEMY_1;

public class Level {
    private BufferedImage img;
    private int[][] lvlData;

    private ArrayList<Enemy1> crabs = new ArrayList<>();
  //  private ArrayList<Pinkstar> pinkstars = new ArrayList<>();
  //  private ArrayList<Shark> sharks = new ArrayList<>();
    private ArrayList<Potions> potions = new ArrayList<>();
    private ArrayList<Arrow> arrows = new ArrayList<>();
    private ArrayList<Container> containers = new ArrayList<>();
    private ArrayList<CannonGun> cannons = new ArrayList<>();
    private ArrayList<BackgroundTree> trees = new ArrayList<>();
    private ArrayList<Grass> grass = new ArrayList<>();

    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;
    private Point playerSpawn;

    public Level(BufferedImage img) {
        this.img = img;
        lvlData = new int[img.getHeight()][img.getWidth()];
        loadLevel();
        calcLvlOffsets();
    }

    private void loadLevel() {

        // Looping through the image colors just once. Instead of one per
        // object/enemy/etc..
        // Removed many methods in HelpMethods class.

        for (int y = 0; y < img.getHeight(); y++)
            for (int x = 0; x < img.getWidth(); x++) {
                Color c = new Color(img.getRGB(x, y));
                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();

                loadLevelData(red, x, y);
                loadEntities(green, x, y);
                loadObjects(blue, x, y);
            }
    }

    private void loadLevelData(int redValue, int x, int y) {
        if (redValue >= 50)
            lvlData[y][x] = 0;
        else
            lvlData[y][x] = redValue;
        switch (redValue) {
            case 0, 1, 2, 3, 30, 31, 33, 34, 35, 36, 37, 38, 39 ->
                    grass.add(new Grass((int) (x * game.TILE_SIZE), (int) (y * game.TILE_SIZE) - game.TILE_SIZE, getRndGrassType(x)));
        }
    }

    private int getRndGrassType(int xPos) {
        return xPos % 2;
    }

    private void loadEntities(int greenValue, int x, int y) {
        switch (greenValue) {
            case ENEMY_1 -> crabs.add(new Enemy1(x * game.TILE_SIZE, y * game.TILE_SIZE));
          //  case PINKSTAR -> pinkstars.add(new Pinkstar(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
         //   case SHARK -> sharks.add(new Shark(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
            case 100 -> playerSpawn = new Point(x * game.TILE_SIZE, y * game.TILE_SIZE);
        }
    }
    private void loadObjects(int blueValue, int x, int y) {
        switch (blueValue) {
            case RED_POTION, features.Objects.BLUE_POTION -> potions.add(new Potions(x * game.TILE_SIZE, y * game.TILE_SIZE, blueValue));
            case BOX, BARREL -> containers.add(new Container(x * game.TILE_SIZE, y * game.TILE_SIZE, blueValue));
            case SPIKE -> arrows.add(new Arrow(x * game.TILE_SIZE, y * game.TILE_SIZE, SPIKE));
            case CANNON_LEFT, CANNON_RIGHT -> cannons.add(new CannonGun(x * game.TILE_SIZE, y * game.TILE_SIZE, blueValue));
            case TREE_ONE, TREE_TWO, TREE_THREE -> trees.add(new BackgroundTree(x * game.TILE_SIZE, y * game.TILE_SIZE, blueValue));
        }
    }

    public BufferedImage getImg() {
        return img;
    }

    public int[][] getLvlData() {
        return lvlData;
    }

    public ArrayList<Enemy1> getCrabs() {
        return crabs;
    }

    public ArrayList<Potions> getPotions() {
        return potions;
    }

    public ArrayList<Arrow> getArrows() {
        return arrows;
    }

    public ArrayList<Container> getContainers() {
        return containers;
    }

    public ArrayList<CannonGun> getCannons() {
        return cannons;
    }

    public ArrayList<BackgroundTree> getTrees() {
        return trees;
    }

    public ArrayList<Grass> getGrass() {
        return grass;
    }

    public int getLvlTilesWide() {
        return lvlTilesWide;
    }

    public int getMaxTilesOffset() {
        return maxTilesOffset;
    }

    public int getMaxLvlOffsetX() {
        return maxLvlOffsetX;
    }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }

    private void calcLvlOffsets() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - game.TILE_WIDTH;
        maxLvlOffsetX = game.TILE_SIZE * maxTilesOffset;

    }

    public int getSpriteIndex(int x, int y) {
        return lvlData[y][x];
    }


}
