package GameLevels;

import Function.LoadSave;
import State.GameState;
import main.game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LevelManager {
    private game Game;
    private BufferedImage[] LevelImg,waterSprite;
    private ArrayList<Level> levels;

    public int getLevel_Index() {
        return Level_Index;
    }

    public void setLevel_Index(int level_Index) {
        Level_Index = level_Index;
    }

    public game getGame() {
        return Game;
    }

    public void setGame(game game) {
        Game = game;
    }

    public BufferedImage[] getLevelImg() {
        return LevelImg;
    }

    public void setLevelImg(BufferedImage[] levelImg) {
        LevelImg = levelImg;
    }

    public BufferedImage[] getWaterSprite() {
        return waterSprite;
    }

    public void setWaterSprite(BufferedImage[] waterSprite) {
        this.waterSprite = waterSprite;
    }

    public ArrayList<Level> getLevels() {
        return levels;
    }

    public void setLevels(ArrayList<Level> levels) {
        this.levels = levels;
    }

    public int getAniTick() {
        return aniTick;
    }

    public void setAniTick(int aniTick) {
        this.aniTick = aniTick;
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public void setAniIndex(int aniIndex) {
        this.aniIndex = aniIndex;
    }

    private int Level_Index=0,aniTick, aniIndex;
    public LevelManager(game Game){
        this.Game=Game;
        importImgArray();
        levels=new ArrayList<>();
        createWater();
        buildAllLevels();
    }
    private void createWater() {
        waterSprite = new BufferedImage[5];
        BufferedImage img = LoadSave.GetAtlas(LoadSave.WATER_TOP);
        for (int i = 0; i < 4; i++)
            waterSprite[i] = img.getSubimage(i * 32, 0, 32, 32);
        waterSprite[4] = LoadSave.GetAtlas(LoadSave.WATER_BOTTOM);
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
        for (int j = 0; j < Game.TILE_HEIGHT; j++)
            for (int i = 0; i < levels.get(Level_Index).getLvlData()[0].length; i++) {
                int index = levels.get(Level_Index).getSpriteIndex(i, j);
                int x = game.TILE_SIZE * i - leveloff;
                int y = game.TILE_SIZE * j;
                if (index == 48)
                    g.drawImage(waterSprite[aniIndex], x, y, game.TILE_SIZE, game.TILE_SIZE, null);
                else if (index == 49)
                    g.drawImage(waterSprite[4], x, y, game.TILE_SIZE, game.TILE_SIZE, null);
                else
                    g.drawImage(LevelImg[index], x, y, game.TILE_SIZE,game.TILE_SIZE, null);
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
        Game.getPlaying().getPlayer().LoadlevelData(newLevel.getLvlData());
        Game.getPlaying().setLevelOffset(newLevel.getMaxLvlOffsetX());
        Game.getPlaying().getObjectsManager().loadObject(newLevel);

    }
}
