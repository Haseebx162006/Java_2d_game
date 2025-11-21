package UI;

import Function.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import static Function.features.UI.URM.*;
public class URMButtons extends PauseButton{
    private int row_index, index=0;
    private BufferedImage[] bufferedImage;
    private boolean mouseOver, mousePressed;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getRow_index() {
        return row_index;
    }

    public void setRow_index(int row_index) {
        this.row_index = row_index;
    }

    public BufferedImage[] getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage[] bufferedImage) {
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

    public URMButtons(int x, int y, int width, int height, int row_index) {
        super(x, y, width, height);
        this.row_index=row_index;
        LoadImg();
    }

    private void LoadImg() {
        BufferedImage temp = LoadSave.GetAtlas(LoadSave.URM_BUTTON);
        bufferedImage= new BufferedImage[3];
        for (int i = 0; i < bufferedImage.length; i++) {
            bufferedImage[i]= temp.getSubimage(i*URM_BUTTON_DEFAULT,row_index*URM_BUTTON_DEFAULT,URM_BUTTON_DEFAULT,URM_BUTTON_DEFAULT);
        }
    }

    public void update(){
        if (mouseOver){
            index=1;
        }
        if (mousePressed){
            index=2;
        }
    }
    public void draw(Graphics g){
        g.drawImage(bufferedImage[index],x,y,URM_BUTTON,URM_BUTTON,null);
    }
    public void reset(){
        mousePressed=false;
        mouseOver=false;
    }
}
