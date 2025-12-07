package State;

import Function.LoadSave;
import UI.Audio;
import UI.Pause;
import UI.PauseButton;
import UI.URMButtons;
import main.game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static Function.features.UI.URM.*;

public class OPTIONS extends State implements Methods {
    private Audio audiobuttons;
    private URMButtons urmButtons,menuButton;
    private BufferedImage backgroundImg, optionsBackgroundImg;
    private int bgX, bgY, bgW, bgH;
    public  OPTIONS(game game1){
        super(game1);
        LoadImg();
        Buttons();
        LoadButtons();
        audiobuttons= game1.getAudiobuttons();

    }

    private void LoadButtons() {
        int menuX = (int) (387 * game.SCALE);
        int menuY = (int) (325 * game.SCALE);

        menuButton= new URMButtons(menuX, menuY, URM_BUTTON, URM_BUTTON, 2);
    }

    private void Buttons() {
    }

    private void LoadImg() {
        backgroundImg = LoadSave.GetAtlas(LoadSave.PAUSE_BACKGROUND);
        optionsBackgroundImg = LoadSave.GetAtlas(LoadSave.OPTIONS_IMG);

        bgW = (int) (optionsBackgroundImg.getWidth() * game.SCALE);
        bgH = (int) (optionsBackgroundImg.getHeight() * game.SCALE);
        bgX = game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (33 * game.SCALE);
    }

    @Override
    public void update() {
        menuButton.update();
        audiobuttons.update();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, game.GAME_WIDTH, game.GAME_HEIGHT, null);
        g.drawImage(optionsBackgroundImg, bgX, bgY, bgW, bgH, null);

        menuButton.draw(g);
        audiobuttons.draw(g);

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isPlayerPressing(e, menuButton)) {
            menuButton.setMousePressed(true);
        } else
            audiobuttons.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isPlayerPressing(e, menuButton)) {
            if (menuButton.isMousePressed())
                GameState.gameState = GameState.MENU;
        } else
            audiobuttons.mouseReleased(e);

        menuButton.reset();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        menuButton.setMouseOver(false);
        if (isPlayerPressing(e, menuButton))
            menuButton.setMouseOver(true);
        else
            audiobuttons.mouseMoved(e);
    }

    @Override
    public void KeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            GameState.gameState = GameState.MENU;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    public  void mouseDrag(MouseEvent e){
        audiobuttons.mouseDragged(e);
    }
    private boolean isPlayerPressing(MouseEvent e, PauseButton b) {
        return b.getBox().contains(e.getX(), e.getY());
    }
}
