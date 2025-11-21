package GameLevels;

import Function.LoadSave;
import main.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelManager {
    private game Game;
    private BufferedImage[] LevelImg;
    private Level levelone;
    public LevelManager(game Game){
        this.Game=Game;
        importImgArray(); // this will load the image in an array
        levelone= new Level(LoadSave.GetLevelData());
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
        int[][] levelData = levelone.getLvldata();
        int levelWidth = levelData[0].length;
        int levelHeight = levelData.length;
        for (int i = 0; i < levelHeight; i++) {
            for (int j = 0; j < levelWidth; j++) {
                int index=levelone.getSpriteIndex(j,i);
                g.drawImage(LevelImg[index],j*game.TILE_SIZE-leveloff,i*game.TILE_SIZE,game.TILE_SIZE,game.TILE_SIZE,null);
            }
        }

    }
    public Level getLevel(){
        return levelone;
    }
}
