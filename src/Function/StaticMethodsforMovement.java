package Function;
import main.game;

import java.awt.geom.Rectangle2D;

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
        
        // Bounds checking to prevent array index out of bounds
        int tileX = (int) xi;
        int tileY = (int) yi;
        if(tileX < 0 || tileX >= levelData[0].length || tileY < 0 || tileY >= levelData.length){
            return true;
        }

        int value= levelData[tileY][tileX];
        // Tile value 11 is air/empty, anything else is solid
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

}
