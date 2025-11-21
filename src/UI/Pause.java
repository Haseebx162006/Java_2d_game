package UI;

import Function.LoadSave;
import Function.features;
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
    private Sound_Button musicButton, SFX;
    private BufferedImage bufferedImage;
    private URMButtons menuButton,replayButton,unpauseButton;
    private VloumeButton volumeButton;
    int x, y, width, height;
    private Playing playing;
    public Pause(Playing playing){
        LoadImage();
        LoadSoundButtons();
        loadURMButtons();
        loadVolumeButton();
        this.playing=playing;
    }

    private void loadVolumeButton() {
        int VolumeX= (int)(309*game.SCALE);
        int VolumeY= (int)(278*game.SCALE);
        volumeButton= new VloumeButton(VolumeX,VolumeY,SLIDER_WIDTH,VOLUME_HEIGHT);
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

    private void LoadSoundButtons() {
        int soundX= (int)(450*game.SCALE);
        int musicY= (int)(140*game.SCALE);
        int sfxY= (int)(186*game.SCALE);
        musicButton= new Sound_Button(soundX,musicY,SOUND_BUTTON,SOUND_BUTTON);
        SFX= new Sound_Button(soundX,sfxY,SOUND_BUTTON,SOUND_BUTTON);
    }

    private void LoadImage() {
        bufferedImage= LoadSave.GetAtlas(LoadSave.PAUSE);
        width=(int)(bufferedImage.getWidth()* game.SCALE);
        height=(int)(bufferedImage.getHeight()*game.SCALE);
        x=game.GAME_WIDTH/2-width/2;
        y=(int)(25*game.SCALE);
    }

    public void update(){
        musicButton.update();
        SFX.update();
        menuButton.update();
        replayButton.update();
        unpauseButton.update();
        volumeButton.update();

    }
    public void draw(Graphics g){
        g.drawImage(bufferedImage,x,y,width,height,null);
        musicButton.draw(g);
        SFX.draw(g);
        menuButton.draw(g);
        replayButton.draw(g);
        unpauseButton.draw(g);
        volumeButton.draw(g);
    }

    public void mousePressed(MouseEvent e) {
        if (isPlayerPressing(e,musicButton)){
            musicButton.setMousePressed(true);
        }else if(isPlayerPressing(e,SFX)){
            SFX.setMousePressed(true);
        }
        else if(isPlayerPressing(e,menuButton)){
            menuButton.setMousePressed(true);
        }
        else if(isPlayerPressing(e,unpauseButton)){
            unpauseButton.setMousePressed(true);
        }
        else if(isPlayerPressing(e,replayButton)){
            replayButton.setMousePressed(true);
        }
        else if(isPlayerPressing(e,volumeButton)){
            volumeButton.setMousePressed(true);
        }

    }

    public void mouseReleased(MouseEvent e) {
        if (isPlayerPressing(e,musicButton)){
            if (musicButton.isMousePressed()){
                musicButton.setMuted(!musicButton.isMuted());
            }
        }
        else if (isPlayerPressing(e,SFX)){
            if (SFX.isMousePressed()){
                SFX.setMuted(!SFX.isMuted());
            }
        }
        else if (isPlayerPressing(e,menuButton)){
            if (menuButton.isMousePressed()){
                GameState.gameState=GameState.MENU;
            }
        }
        else if (isPlayerPressing(e,replayButton)){
            if (replayButton.isMousePressed()){
                System.out.println("Ah shit, Here wo Go againn!");
            }
        }
        else if (isPlayerPressing(e,unpauseButton)){
            if (unpauseButton.isMousePressed()){
                playing.unpaused();
            }
        }
        musicButton.reset();
        SFX.reset();
        menuButton.reset();
        replayButton.reset();
        unpauseButton.reset();
        volumeButton.reset();
    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        SFX.setMouseOver(false);
        menuButton.setMouseOver(false);
        unpauseButton.setMouseOver(false);
        replayButton.setMouseOver(false);
        volumeButton.setMouseOver(false);

        if (isPlayerPressing(e,musicButton)){
            musicButton.setMouseOver(true);
        }else if(isPlayerPressing(e,SFX)){
            SFX.setMouseOver(true);
        }else if(isPlayerPressing(e,menuButton)){
            menuButton.setMouseOver(true);
        }else if(isPlayerPressing(e,replayButton)){
            replayButton.setMouseOver(true);
        }
        else if(isPlayerPressing(e,unpauseButton)){
            unpauseButton.setMouseOver(true);
        }

    }
    public void mouseDragged(MouseEvent e){
        if (volumeButton.isMousePressed()){
            volumeButton.changeX(e.getX());
            volumeButton.setMousePressed(true);
        }

    }
    public boolean isPlayerPressing(MouseEvent e , PauseButton p){
        return p.getBox().contains(e.getX(),e.getY());
    }
}
