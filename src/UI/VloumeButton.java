package UI;

import Function.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static Function.features.UI.VOLUME.*;

public class VloumeButton extends PauseButton{
    private int index=0;
    private BufferedImage[] bufferedImage;
    private BufferedImage slide;
    private boolean mouseOver;
    private boolean mousePressed;
    public int buttonX;
    public int max;
    public int min;
    public VloumeButton(int x, int y, int width, int height) {
        super(x+width/2, y, VOLUME_WIDTH, height);
        buttonX=x+width/2;
        this.x=x;
        this.width=width;
        min=x;
        max=x+width-VOLUME_WIDTH;
        LoadImg();
    }
    private void LoadImg() {
        BufferedImage temp = LoadSave.GetAtlas(LoadSave.VOLUME_BUTTON);
        bufferedImage= new BufferedImage[3];
        for (int i = 0; i < bufferedImage.length; i++) {
            bufferedImage[i]= temp.getSubimage(i*VOLUME_WIDTH_DEFAULT,0,VOLUME_WIDTH_DEFAULT,VOLUME_HEIGHT_DEFAULT);
            slide= temp.getSubimage(3*VOLUME_WIDTH_DEFAULT,0,SLIDER_WIDTH_DEFAULT,VOLUME_HEIGHT_DEFAULT);
        }
    }
    public void changeX(int x){
        int newx = x - VOLUME_WIDTH / 2;
        if (newx <min){
            buttonX=min;
        }
        else if (newx >max){
            buttonX=max;
        }
        else {
            buttonX=newx;
        }
        box.x=buttonX;
    }
    public void update(){
        index=0;
        if (mouseOver){
            index=1;
        }
        if (mousePressed){
            index=2;
        }
    }
    public void draw(Graphics g){
        g.drawImage(slide,x,y,width,height,null);
        g.drawImage(bufferedImage[index],buttonX,y,VOLUME_WIDTH,height,null);
    }
    public void reset(){
        mousePressed=false;
        mouseOver=false;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public BufferedImage[] getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage[] bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public BufferedImage getSlide() {
        return slide;
    }

    public void setSlide(BufferedImage slide) {
        this.slide = slide;
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

    public int getButtonX() {
        return buttonX;
    }

    public void setButtonX(int buttonX) {
        this.buttonX = buttonX;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }
}
