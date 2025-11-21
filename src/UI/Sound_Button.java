package UI;

import Function.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;


import static Function.features.UI.PauseButtons.SOUND_BUTTON;
import static Function.features.UI.PauseButtons.SOUND_BUTTON_DEFAULT;

public class Sound_Button extends PauseButton{
    private BufferedImage[][] bufferedImage;
    private boolean mouseOver,mousePressed;
    private boolean muted;
    private int row_index,column_index;

    public BufferedImage[][] getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage[][] bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public Sound_Button(int x, int y, int width, int height) {
        super(x, y, width, height);
        row_index = 0;
        column_index = 0;
        LoadSoundImage();
    }

    private void LoadSoundImage() {
        BufferedImage img= LoadSave.GetAtlas(LoadSave.SOUND_BUTTON);
        bufferedImage= new BufferedImage[2][3];
        for (int j=0; j<bufferedImage.length; j++){
            for (int i = 0; i < bufferedImage[j].length; i++) {
                bufferedImage[j][i]=img.getSubimage(i*SOUND_BUTTON_DEFAULT,j*SOUND_BUTTON_DEFAULT,SOUND_BUTTON_DEFAULT,SOUND_BUTTON_DEFAULT);
            }
        }
    }
    public void draw(Graphics g){
        g.drawImage(bufferedImage[row_index][column_index], x, y, width, height, null);
    }
    public void update(){
        if (muted){
            row_index=1;
        }
        else{
            row_index=0;
        }
        if (mousePressed){
            column_index=2;
        }
        else if (mouseOver){
            column_index=1;
        }
        else {
            column_index=0;
        }
    }
    public void reset(){
        mousePressed=false;
        mouseOver=false;
    }
}
