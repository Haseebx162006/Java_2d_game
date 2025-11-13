package Function;
import main.game;
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
        if(x<0 || x>= game.GAME_WIDTH){
            return true;
        }
        if (y< 0|| y>=game.GAME_HEIGHT){
            return  true;
        }
        float xi= x/game.TILE_SIZE;
        float yi= y/game.TILE_SIZE;
        
        int value= levelData[(int) yi][(int) xi];
        if (value>=48 || value<0 || value!=11) {
            return true;
        }
        return false;
    }
}
