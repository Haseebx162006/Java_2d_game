package UI;

import Function.LoadSave;
import State.GameState;

import java.awt.*;
import java.awt.image.BufferedImage;

import static Function.features.UI.BUTTON.*;

public class MenuButton {
    int xPosition;
    int yPosition;
    int rowIndex;
    int xOff=BUTTON_WIDTH/2;
    int index;
    GameState state;
    BufferedImage[] bufferedImage;
    private boolean mouseOver;
    private boolean mousePressed;
    private Rectangle buttonBox;

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public MenuButton(int xPosition, int yPosition, int index, GameState state) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.rowIndex = index;
        this.state = state;
        Load_image();
        InitButtonBox();
    }

    private void InitButtonBox() {
        buttonBox= new Rectangle(xPosition-xOff,yPosition,BUTTON_WIDTH,BUTTON_HEIGHT);
    }

    public void Load_image() {
        bufferedImage = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetAtlas(LoadSave.ButtonImages);
        for (int i = 0; i < bufferedImage.length; i++) {
            bufferedImage[i]=temp.getSubimage(i * BUTTON_WIDTH_DEFAULT, rowIndex *BUTTON_HEIGHT_DEFAULT, BUTTON_WIDTH_DEFAULT, BUTTON_HEIGHT_DEFAULT);

        }
    }
    public void draw(Graphics g){
        g.drawImage(bufferedImage[index], xPosition - xOff, yPosition,
                BUTTON_WIDTH, BUTTON_HEIGHT, null);

    }
    public void update(){
        index=0;
        if (mouseOver){
            index=1;
        }
        if (mousePressed){
            index =2;
        }
    }
    public void Apply_GameState(){
        GameState.gameState=state;
    }
    public void Reset(){
        mouseOver=false;
        mousePressed=false;
    }

    public Rectangle getButtonBox() {
        return buttonBox;
    }
    public void reset(){
        mousePressed=false;
        mouseOver=false;
    }
}