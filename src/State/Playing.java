package State;

import Entities.Player;
import GameLevels.LevelManager;
import UI.Pause;
import main.game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Playing extends State implements Methods{
    private Player player;
    private LevelManager levelManager;
    private boolean paused=false;
    private Pause pause;
    public Playing(game game1){
        super(game1);
        initClasses();
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
        }
        else{
            pause.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        levelManager.draw(g);
        player.RenderPlayer(g);
        if (paused) pause.draw(g);
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

