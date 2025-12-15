package UI;

import main.game;

import java.awt.*;
import java.awt.event.MouseEvent;

import static Function.features.UI.PauseButtons.SOUND_BUTTON;
import static Function.features.UI.VOLUME.SLIDER_WIDTH;
import static Function.features.UI.VOLUME.VOLUME_HEIGHT;

public class Audio {
    private VloumeButton volumeButton;
    private Sound_Button musicButton, SFX;
    private game game;
    public Audio(){
        loadVolumeButton();
        LoadSoundButtons();
    }
    
    public void setGame(game game) {
        this.game = game;
    }
    private void LoadSoundButtons() {
        int soundX= (int)(450* game.SCALE);
        int musicY= (int)(140*game.SCALE);
        int sfxY= (int)(186*game.SCALE);
        musicButton= new Sound_Button(soundX,musicY,SOUND_BUTTON,SOUND_BUTTON);
        SFX= new Sound_Button(soundX,sfxY,SOUND_BUTTON,SOUND_BUTTON);
    }
    private void loadVolumeButton() {
        int VolumeX= (int)(309*game.SCALE);
        int VolumeY= (int)(278*game.SCALE);
        volumeButton= new VloumeButton(VolumeX,VolumeY,SLIDER_WIDTH,VOLUME_HEIGHT);
    }
    public void update(){
        musicButton.update();
        SFX.update();
        volumeButton.update();
        
        // Update volume based on slider position
        if (game != null && game.getAudioPlayer() != null && volumeButton != null) {
            float volumeValue = (float)(volumeButton.getButtonX() - volumeButton.getMin()) / 
                               (float)(volumeButton.getMax() - volumeButton.getMin());
            game.getAudioPlayer().setVolume(volumeValue);
        }
    }
    public void draw(Graphics g){
        musicButton.draw(g);
        SFX.draw(g);
        volumeButton.draw(g);
    }
    public void mousePressed(MouseEvent e) {
        if (isPlayerPressing(e,musicButton)){
            musicButton.setMousePressed(true);
        }else if(isPlayerPressing(e,SFX)){
            SFX.setMousePressed(true);
        }
        else if(isPlayerPressing(e,volumeButton)){
            volumeButton.setMousePressed(true);
        }

    }

    public void mouseReleased(MouseEvent e) {
        if (isPlayerPressing(e,musicButton)){
            if (musicButton.isMousePressed()){
                musicButton.setMuted(!musicButton.isMuted());
                // Toggle music mute in AudioPlayer
                if (game != null && game.getAudioPlayer() != null) {
                    game.getAudioPlayer().toggleSongMute();
                }
            }
        }
        else if (isPlayerPressing(e,SFX)){
            if (SFX.isMousePressed()){
                SFX.setMuted(!SFX.isMuted());
                // Toggle SFX mute in AudioPlayer
                if (game != null && game.getAudioPlayer() != null) {
                    game.getAudioPlayer().toggleEffectMute();
                }
            }
        }
        musicButton.reset();
        SFX.reset();
        volumeButton.reset();
    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        SFX.setMouseOver(false);
        volumeButton.setMouseOver(false);

        if (isPlayerPressing(e,musicButton)){
            musicButton.setMouseOver(true);
        }else if(isPlayerPressing(e,SFX)){
            SFX.setMouseOver(true);
        }

    }
    public void mouseDragged(MouseEvent e){
        if (volumeButton.isMousePressed()){
            volumeButton.changeX(e.getX());
            volumeButton.setMousePressed(true);

            if (game != null && game.getAudioPlayer() != null && volumeButton != null) {
                float volumeValue = (float)(volumeButton.getButtonX() - volumeButton.getMin()) / 
                                   (float)(volumeButton.getMax() - volumeButton.getMin());
                game.getAudioPlayer().setVolume(volumeValue);
            }
        }

    }
    public boolean isPlayerPressing(MouseEvent e , PauseButton p){
        return p.getBox().contains(e.getX(),e.getY());
    }

}
