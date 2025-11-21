package State;

import Entities.Player;
import Function.LoadSave;
import GameLevels.LevelManager;
import UI.Pause;
import main.game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import static Function.features.background.*;

public class Playing extends State implements Methods{
    private BufferedImage background,bigCloud,smallCloud;
    int[] cloudPositons;
    private Player player;
    private LevelManager levelManager;
    private boolean paused=false;
    private Pause pause;
    private Random random= new Random();
    private int levelxOffset;
    private int left_border=(int)(0.2*game.GAME_WIDTH);
    private int right_border=(int)(0.8*game.GAME_WIDTH);
    private int levelTilesw= LoadSave.GetLevelData()[0].length;
    private int maxTiles=levelTilesw-game.TILE_WIDTH;
    private int maxLevelOffset=maxTiles*game.TILE_SIZE;
    public Playing(game game1){
        super(game1);
        initClasses();
        background=LoadSave.GetAtlas(LoadSave.BACKGROUND_IMAGE);
        bigCloud=LoadSave.GetAtlas(LoadSave.BIG_CLOUD);
        smallCloud=LoadSave.GetAtlas(LoadSave.SMALL_CLOUD);
        cloudPositons= new int[9];
        for (int i = 0; i < cloudPositons.length; i++) {
            cloudPositons[i]= (int)(90*game.SCALE)+random.nextInt((int)(150*game.SCALE));
        }
    }
    private void initClasses(){
        levelManager= new LevelManager(game1);
        player= new Player(200,200, (int) (64* game.SCALE),(int)(40*game.SCALE));
        player.LoadlevelData(levelManager.getLevel().getLvldata());
        pause= new Pause(this);
    }
    public Player getPlayer(){
        return player;
    }

    public void windowFocusLost() {
        player.ResetDirection();
    }

    @Override
    public void update() {
        if (!paused){
            levelManager.update();
            player.UpdatePlayer();
            checkBorder();
        }
        else{
            pause.update();
        }
    }

    private void checkBorder() {
        int playerX= (int) player.getBox().x;
        int difference=playerX-levelxOffset;
        if (difference>right_border){
            levelxOffset+=difference-right_border;
        }
        else if (difference<left_border){
            levelxOffset+=difference-left_border;
        }
        if (levelxOffset>maxLevelOffset){
            levelxOffset=maxLevelOffset;
        } else if (levelxOffset<0) {
            levelxOffset=0;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(background,0,0,game.GAME_WIDTH,game.GAME_HEIGHT,null);
        drawCloud(g);
        levelManager.draw(g,levelxOffset);
        player.RenderPlayer(g,levelxOffset);
        if (paused) pause.draw(g);
    }

    private void drawCloud(Graphics g) {
        for (int i = 0; i < 3; i++) {
            g.drawImage(bigCloud,(i*BIG_CLOUD_WIDTH)-(int)(levelxOffset*0.3),(int)(204*game.SCALE),BIG_CLOUD_WIDTH,BIG_CLOUD_HEIGHT,null);
        }
        // chote clouds
        for (int i = 0; i < cloudPositons.length; i++) {
            g.drawImage(smallCloud,SMALL_CLOUD_WIDTH*4*i-(int)(levelxOffset*0.9),cloudPositons[i],SMALL_CLOUD_WIDTH,SMALL_CLOUD_HEIGHT,null);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
    public void unpaused(){
        paused=false;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (paused){
            pause.mousePressed(e);
        }
        if (e.getButton()==MouseEvent.BUTTON1){
            player.setAttack(true);
        }
        if (e.getButton()==MouseEvent.BUTTON3){
            player.setJump(true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (paused){
            pause.mouseReleased(e);
        }
        if (e.getButton() == MouseEvent.BUTTON1) {
            player.setAttack(false);
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            player.setJump(false);

        }

    }
    public void mouseDragged(MouseEvent e){
        if (paused){
            pause.mouseDragged(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (paused){
            pause.mouseMoved(e);
        }
    }

    @Override
    public void KeyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_A:
                player.setLeft(true);
                break;
            case KeyEvent.VK_D:
                player.setRight(true);

                break;
            case KeyEvent.VK_SPACE:
                player.setJump(true);
                break;
            case KeyEvent.VK_ESCAPE:
                paused=!paused;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_A:
                player.setLeft(false);
                break;
            case KeyEvent.VK_D:
                player.setRight(false);
                break;
            case KeyEvent.VK_SPACE:
                player.setJump(false);
                break;
        }
    }
    }

