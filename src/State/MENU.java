package State;

import Function.LoadSave;
import UI.MenuButton;
import main.game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class MENU extends State implements  Methods{
    private MenuButton[] menuButtons= new MenuButton[3];
    public BufferedImage bufferedImage; // menu_background.png - the panel behind buttons
    private BufferedImage consoleBackground; // menu_bg.jpg - the full-screen background
    int menuX,menuY,menuWidth,menuHeight;
    public MENU(game game1) {
        super(game1);
        LoadButtons();
        LoadBackground();
    }

    private void LoadBackground() {
        // Load the full-screen console background
        consoleBackground = LoadSave.GetAtlas(LoadSave.PAUSE_BACKGROUND);
        // Load the button panel background
        bufferedImage= LoadSave.GetAtlas(LoadSave.Button_Background);
        menuWidth= (int)(bufferedImage.getWidth()* game.SCALE);
        menuHeight=(int)(bufferedImage.getHeight()*game.SCALE);
        menuX= game.GAME_WIDTH/2 - menuWidth/2;
        menuY= (int)(45*game.SCALE);
    }

    private void LoadButtons() {
        menuButtons[0]= new MenuButton(game.GAME_WIDTH/2,(int)(150*game.SCALE),0,GameState.PLAYING);
        menuButtons[1]= new MenuButton(game.GAME_WIDTH/2,(int)(220*game.SCALE),1,GameState.OPTIONS);
        menuButtons[2]= new MenuButton(game.GAME_WIDTH/2,(int)(290*game.SCALE),2,GameState.QUIT);
    }

    @Override
    public void update() {
        for (MenuButton m: menuButtons){
            m.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        // Draw the full-screen console background first
        g.drawImage(consoleBackground, 0, 0, game.GAME_WIDTH, game.GAME_HEIGHT, null);
        // Draw the button panel background
        g.drawImage(bufferedImage, menuX, menuY, menuWidth, menuHeight, null);
        // Draw the buttons on top
        for (MenuButton m: menuButtons){
            m.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton m: menuButtons){
            if (isPlayerPressing(e,m)){
                m.setMousePressed(true);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton m: menuButtons){
            if (isPlayerPressing(e,m)){
                if (m.isMousePressed()){
                    m.Apply_GameState();
                }
                break;
            }

        }
        ResetButtons();
    }

    private void ResetButtons() {
        for (MenuButton m: menuButtons){
            m.Reset();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for(MenuButton m : menuButtons){
            m.setMouseOver(false);
        }
        for (MenuButton m : menuButtons){
            if (isPlayerPressing(e,m)){
                m.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void KeyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_ENTER){
            GameState.gameState=GameState.PLAYING;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
