package UI;

import Function.LoadSave;
import main.game;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import static Function.features.UI.PauseButtons.*;
public class Pause {
    private Sound_Button musicButton, SFX;
    private BufferedImage bufferedImage;
    int x, y, width, height;
    public Pause(){
        LoadImage();
        LoadSoundButtons();
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

    }
    public void draw(Graphics g){
        g.drawImage(bufferedImage,x,y,width,height,null);
        musicButton.draw(g);
        SFX.draw(g);
    }

    public void mousePressed(MouseEvent e) {
        if (isPlayerPressing(e,musicButton)){
            musicButton.setMousePressed(true);
        }else if(isPlayerPressing(e,SFX)){
            SFX.setMousePressed(true);
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
        musicButton.setMousePressed(false);
        SFX.setMousePressed(false);
    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        SFX.setMouseOver(false);

        if (isPlayerPressing(e,musicButton)){
            musicButton.setMouseOver(true);
        }else if(isPlayerPressing(e,SFX)){
            SFX.setMouseOver(true);
        }
    }
    public void mouseDragged(MouseEvent e){

    }
    public boolean isPlayerPressing(MouseEvent e , PauseButton p){
        return p.getBox().contains(e.getX(),e.getY());
    }
}
