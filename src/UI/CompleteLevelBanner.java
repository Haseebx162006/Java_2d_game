package UI;

import Function.LoadSave;
import State.GameState;
import State.Playing;
import main.game;
import static Function.features.UI.URM.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class CompleteLevelBanner {
    private Playing playing;
    private URMButtons menu, next;
    private BufferedImage image;
    private int backgroundX,backgroundY,backgroundW,backgroundH;
    public CompleteLevelBanner(Playing playing){
        this.playing=playing;
        LoadImg();
        LoadButtons();
    }
    public void update(){
        menu.update();
        next.update();

    }
    private void LoadButtons() {
        int menu_ButtonX=(int)(330*game.SCALE);
        int next_ButtonX=(int)(445*game.SCALE);
        int y= (int) (195*game.SCALE);
        next=new URMButtons(next_ButtonX,y,URM_BUTTON,URM_BUTTON,0);
        menu= new URMButtons(menu_ButtonX,y,URM_BUTTON,URM_BUTTON,2);
    }

    private void LoadImg() {
        image= LoadSave.GetAtlas(LoadSave.LOAD_COMPLETE_LEVEL);
        backgroundW=(int)(image.getWidth()*game.SCALE);
        backgroundH=(int)(image.getHeight()*game.SCALE);
        backgroundX=game.GAME_WIDTH/2 - backgroundW/2;
        backgroundY=(int)(75*game.SCALE);
    }
    public void draw(Graphics g){
        g.setColor(new Color(0,0,0,200));
        g.fillRect(0,0,game.GAME_WIDTH,game.GAME_HEIGHT);

        g.drawImage(image,backgroundX,backgroundY,backgroundW,backgroundH,null);
        next.draw(g);
        menu.draw(g);
    }
    public void mouseMoved(MouseEvent e){
        next.setMouseOver(false);
        menu.setMouseOver(false);
        if (isClicked(menu,e)){
            menu.setMouseOver(true);
        } else if (isClicked(next,e)) {
            menu.setMouseOver(true);
        }
    }
    public boolean isClicked(URMButtons button, MouseEvent e){
        return button.getBox().contains(e.getX(),e.getY());
    }
    public void mouseReleased(MouseEvent e){
        if (isClicked(menu,e)){
            if (menu.isMousePressed()){
                playing.resetGame();
                GameState.gameState=GameState.MENU;
            }
        } else if (isClicked(next,e)) {
           if (next.isMousePressed()){
              playing.LoadNextLevel();
           }
        }
        menu.reset();
        next.reset();
    }
    public void mousePressed(MouseEvent e){
        if (isClicked(menu,e)){
            menu.setMousePressed(true);
        }
        else if (isClicked(next,e)){
            next.setMousePressed(true);
        }
    }
}
