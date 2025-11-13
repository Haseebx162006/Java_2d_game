package GameLevels;

public class Level {
    private int[][] lvldata;
    Level(int[][] lvldata){
        this.lvldata=lvldata;
    }
    public int getSpriteIndex(int x, int y){
        return lvldata[y][x];
    }
}
