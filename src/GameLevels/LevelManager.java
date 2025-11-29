package GameLevels;

import Function.LoadSave;
import State.GameState;
import main.game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LevelManager {
    private game Game;
    private BufferedImage[] LevelImg;
    private ArrayList<Level> levels;
    private int Level_Index;
    public LevelManager(game Game){
        this.Game=Game;
        importImgArray();
        levels=new ArrayList<>();
        buildAllLevels();
    }

    private void buildAllLevels() {
        BufferedImage[] allLevels=LoadSave.getAll();
        for (BufferedImage img : allLevels){
            levels.add(new Level(img));
        }
    }

    private void importImgArray() {
        BufferedImage img= LoadSave.GetAtlas(LoadSave.OutsideImgLevelAddress);
        LevelImg= new BufferedImage[48];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 12; j++) {
                int index=i*12 +j;
                LevelImg[index]=img.getSubimage(j*32,i*32,32,32);
            }
        }

    }

    public void update(){

    }
    public void draw(Graphics g, int leveloff){
        for (int i = 0; i < game.TILE_HEIGHT; i++) {
            for (int j = 0; j < levels.get(Level_Index).getLvldata()[0].length; j++) {
                int index=levels.get(Level_Index).getSpriteIndex(j,i);
                if (i >= 12 && j < 15) {  // Check bottom 2 rows, first 15 columns
                    System.out.println("Bottom tile at (" + j + "," + i + ") = " + index);
                }
                // Skip drawing tile 11 (air/empty)
                if (index == 11) {
                    continue;
                }
                int displayIndex = (index == 13) ? 1 : index;
                g.drawImage(LevelImg[index],game.TILE_SIZE*j-leveloff,i*game.TILE_SIZE,game.TILE_SIZE,game.TILE_SIZE,null);
            }
        }

    }
    public Level getLevel(){
        return levels.get(Level_Index);
    }
    public int getTotalLevelsnumber(){
        return levels.size();
    }

    public void loadnextLevel() {
        Level_Index++;
        if (Level_Index>=levels.size()){
            Level_Index=0;
            System.out.println("No more Levels Game Completed");
            GameState.gameState=GameState.MENU;
        }
        Level newLevel=levels.get(Level_Index);
        Game.getPlaying().getEnemyMangerclass().addEnemies(newLevel);
        Game.getPlaying().getPlayer().LoadlevelData(newLevel.getLvldata());
        Game.getPlaying().setLevelOffset(newLevel.getleveloffset());
        Game.getPlaying().getObjectsManager().loadObject(newLevel);

    }
}
