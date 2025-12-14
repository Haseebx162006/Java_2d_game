
package Function;
import Entities.Enemy1;
import Rewards.*;
import Rewards.Container;
import main.game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Function.features.Objects.*;

public class StaticMethodsforMovement {

    public static boolean CanMove(float x, float y, float width,float height, int[][] levelData){
        if (!IsSolid(x,y,levelData)) // top left ko check karne ke leye
            if(!IsSolid(x+width,y+height,levelData)) //bottom right
                if (!IsSolid(x+width,y,levelData)) // Top Right
                    if (!IsSolid(x,y+height,levelData)) //Bottom left
                        return true;
        return false;
    }
    public static boolean IsSolid(float x, float y, int[][] levelData){
        // Ye method check kare ga ke given surface solid ha ya nae
        // iska matlab ke jab sprite ka pixel is se touch hoga to yr true value return kare ga
        int maxWidth=levelData[0].length*game.TILE_SIZE;
        int maxHeight=levelData.length*game.TILE_SIZE;
        if(x<0 || x>= maxWidth){
            return true;
        }
        if (y< 0|| y>=maxHeight){
            return  true;
        }
        float xi= x/game.TILE_SIZE;
        float yi= y/game.TILE_SIZE;

        int tileX = (int) xi;
        int tileY = (int) yi;
        if(tileX < 0 || tileX >= levelData[0].length || tileY < 0 || tileY >= levelData.length){
            return true;
        }

        int value= levelData[tileY][tileX];

        if (value != 11) {
            return true;
        }
        return false;
    }
    public static float PositionNextToWall(Rectangle2D.Float box, float Xspeed){
        int tile= (int) box.x/game.TILE_SIZE;
        if (Xspeed>0){
            // Player will move to right
            int tilePostiton= tile*game.TILE_SIZE;
            int xOff= (int)(game.TILE_SIZE-box.width);
            return tilePostiton+xOff-1;
        }else{
            return tile*game.TILE_SIZE;
        }
    }
    public static float CheckUnderEnvironmentorAbove(Rectangle2D.Float box, float airSpeed){
        int tile= (int) box.y/game.TILE_SIZE;
        if (airSpeed>0) {
            // in this method i will calculathe falling mechanism
            int tilePosition= tile*game.TILE_SIZE;
            int Yoff= (int)(game.TILE_SIZE-box.height);
            return tilePosition+Yoff-1;
        }
        else{
            // ise bad me karon ga jumping wale ko Date 19-11-2025
            return tile*game.TILE_SIZE;
        }
    }
    public static boolean isFloor(Rectangle2D.Float box,float Speed,int[][] levelData){
        return IsSolid(box.x+Speed,box.y+box.height+1,levelData);
    }
    public static boolean OnFloor(Rectangle2D.Float box, int[][] levelData){
        if (!IsSolid(box.x,box.y+box.height+1,levelData))
            if (!IsSolid(box.x+box.width,box.y+box.height+1,levelData))
                return false;
        return true;
    }
    public static int[][] GetLevelData(BufferedImage img){
        int[][] lvldata= new int[img.getHeight()][img.getWidth()];
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color color= new Color(img.getRGB(j,i));
                int value=color.getRed();
                if (value>=48){
                    value=0;
                }
                lvldata[i][j]=value;
            }
        }
        return lvldata;
    }

    public static boolean IsEntityInWater(Rectangle2D.Float hitbox, int[][] lvlData) {
        // Water starts at a specific Y coordinate (adjust based on your level)
        // Typically 3-4 tiles from the bottom
        int lvlHeight = lvlData.length;
        float waterY = (lvlHeight - 3) * game.TILE_SIZE; // Adjust the "3" if needed

        // Player dies if their feet touch the water
        return (hitbox.y + hitbox.height) >= waterY;
    }

    private static int GetTileValue(float xPos, float yPos, int[][] lvlData) {
        int xCord = (int) (xPos / game.TILE_SIZE);
        int yCord = (int) (yPos / game.TILE_SIZE);
        return lvlData[yCord][xCord];
    }
    public static ArrayList<Enemy1> getEnemyCrab(BufferedImage img) {
        ArrayList<Enemy1> list = new ArrayList<>();
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(j, i));
                int value = color.getGreen();
                if (value == features.UI.Enemies.ENEMY_1) {
                    list.add(new Enemy1(j * game.TILE_SIZE, i * game.TILE_SIZE));
                }
            }
        }
        return list;
    }
    public static ArrayList<Potions> getPotions(BufferedImage img) {
        ArrayList<Potions> list = new ArrayList<>();
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(j, i));
                int red = color.getRed();
                int blue = color.getBlue();

                // Skip if it's a tile/brick (red channel is used for tiles)
                if (red < 48 && red != 0) {
                    continue;
                }
                if (blue == RED_POTION) {
                    list.add(new Potions(j*game.TILE_SIZE,i*game.TILE_SIZE,RED_POTION));
                }
                else if (blue == BLUE_POTION){
                    list.add(new Potions(j*game.TILE_SIZE,i*game.TILE_SIZE,BLUE_POTION));
                }
            }
        }
        return list;
    }
    public static ArrayList<Container> getContainers(BufferedImage img) {
        ArrayList<Container> list = new ArrayList<>();
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(j, i));
                int value = color.getBlue();
                if (value == BOX || value==BARREL) {
                    list.add(new Container(j*game.TILE_SIZE,i*game.TILE_SIZE,value));
                }
            }
        }
        return list;
    }

    public static ArrayList<Arrow> getSpikes(BufferedImage img) {
        ArrayList<Arrow> list = new ArrayList<>();
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(j, i));
                int value = color.getBlue();
                if (value == SPIKE) {
                    list.add(new Arrow(j*game.TILE_SIZE,i*game.TILE_SIZE,SPIKE));
                }
            }
        }
        return list;
    }
    public static ArrayList<CannonGun> getCannon(BufferedImage img) {
        ArrayList<CannonGun> list = new ArrayList<>();
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(j, i));
                int value = color.getBlue();
                if (value == CANNON_LEFT|| value==CANNON_RIGHT) {
                    list.add(new CannonGun(j*game.TILE_SIZE,i*game.TILE_SIZE,value));
                }
            }
        }
        return list;
    }
    public static boolean IsProjectileHittingLevel(Ball b, int[][] lvlData) {
        return IsSolid( b.getBox().x+ b.getBox().width / 2, b.getBox().y + b.getBox().height / 2, lvlData);

    }

    public static boolean CanCannonSeePlayer(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
        // Use center X coordinates for more accurate line-of-sight
        float firstCenterX = firstHitbox.x + firstHitbox.width / 2;
        float secondCenterX = secondHitbox.x + secondHitbox.width / 2;
        
        int firstXTile = (int) (firstCenterX / game.TILE_SIZE);
        int secondXTile = (int) (secondCenterX / game.TILE_SIZE);

        if (firstXTile > secondXTile)
            return IsAllTilesClear(secondXTile, firstXTile, yTile, lvlData);
        else
            return IsAllTilesClear(firstXTile, secondXTile, yTile, lvlData);
    }
    public static boolean IsAllTilesClear(int xStart, int xEnd, int yTile, int[][] lvlData) {

        for (int yOffset = -1; yOffset <= 1; yOffset++) {
            int checkYTile = yTile + yOffset;
            // Make sure we don't go out of bounds
            if (checkYTile < 0 || checkYTile >= lvlData.length) {
                continue;
            }
            
            for (int x = xStart; x <= xEnd; x++) {
                // Make sure we don't go out of bounds
                if (x < 0 || x >= lvlData[0].length) {
                    continue;
                }
                

                int value = lvlData[checkYTile][x];
                if (value != 11) { // 11 is air/empty
                    if (yOffset == 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
