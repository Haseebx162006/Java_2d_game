package UI;

import Function.LoadSave;
import State.GameState;
import State.Playing;
import main.game;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import static Function.features.UI.PauseButtons.*;
import static Function.features.UI.URM.*;
import static Function.features.UI.VOLUME.*;
public class Pause {
    private BufferedImage bufferedImage;
    private URMButtons menuButton,replayButton,unpauseButton;
    private  Audio audiobuttons;
    int x, y, width, height;
    private Playing playing;
    public Pause(Playing playing){
        this.playing=playing;
        audiobuttons=playing.Getgame().getAudiobuttons();

        LoadImage();
        loadURMButtons();
    }



    private void loadURMButtons() {
        int menuX=(int)(313*game.SCALE);
        int replayX=(int)(387*game.SCALE);
        int unPauseX=(int)(462*game.SCALE);
        int bY=(int)(325*game.SCALE);
        replayButton= new URMButtons(replayX,bY,URM_BUTTON,URM_BUTTON,1);
        menuButton= new URMButtons(menuX,bY, URM_BUTTON,URM_BUTTON,2);
        unpauseButton= new URMButtons(unPauseX,bY,URM_BUTTON,URM_BUTTON,0);
    }


    private void LoadImage() {
        bufferedImage= LoadSave.GetAtlas(LoadSave.PAUSE);
        width=(int)(bufferedImage.getWidth()* game.SCALE);
        height=(int)(bufferedImage.getHeight()*game.SCALE);
        x=game.GAME_WIDTH/2-width/2;
        y=(int)(25*game.SCALE);
    }

    public void update(){
        menuButton.update();
        replayButton.update();
        unpauseButton.update();
        audiobuttons.update();

    }
    public void draw(Graphics g){
        g.drawImage(bufferedImage,x,y,width,height,null);
        menuButton.draw(g);
        replayButton.draw(g);
        unpauseButton.draw(g);
        audiobuttons.draw(g);
    }
    public void mouseDragged(MouseEvent e) {
        audiobuttons.mouseDragged(e);
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuButton))
            menuButton.setMousePressed(true);
        else if (isIn(e, replayButton))
            replayButton.setMousePressed(true);
        else if (isIn(e, unpauseButton))
            unpauseButton.setMousePressed(true);
        else
            audiobuttons.mousePressed(e);
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e, menuButton)) {
            if (menuButton.isMousePressed()) {
                GameState.gameState = GameState.MENU;
                playing.unpaused();
            }
        } else if (isIn(e, replayButton)) {
            if (replayButton.isMousePressed()) {
                playing.resetGame();
                playing.unpaused();
            }
        } else if (isIn(e, unpauseButton)) {
            if (unpauseButton.isMousePressed())
                playing.unpaused();
        } else
            audiobuttons.mouseReleased(e);

        menuButton.reset();
        replayButton.reset();
        unpauseButton.reset();

    }

    public void mouseMoved(MouseEvent e) {
        menuButton.setMouseOver(false);
        replayButton.setMouseOver(false);
        unpauseButton.setMouseOver(false);

        if (isIn(e, menuButton))
            menuButton.setMouseOver(true);
        else if (isIn(e, replayButton))
            replayButton.setMouseOver(true);
        else if (isIn(e, unpauseButton))
            unpauseButton.setMouseOver(true);
        else
            audiobuttons.mouseMoved(e);
    }

    private boolean isIn(MouseEvent e, PauseButton b) {
        return b.getBox().contains(e.getX(), e.getY());
    }

}
