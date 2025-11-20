package main;

import Inputes_of_game.keyboard_input;  // Sahi import
import Inputes_of_game.mouse_input;
import javax.swing.*;
import java.awt.*;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;


public class game_panel extends JPanel {
    private final game Game;
    private BufferedImage offscreenImage;

    public game_panel(game Game) {
        this.Game = Game;
        SetPanelSize();
        setDoubleBuffered(true);
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new keyboard_input(this));
        mouse_input mouseInput = new mouse_input(this);
        addMouseListener(mouseInput);
        addMouseMotionListener(mouseInput);
    }

    private void SetPanelSize() {
        Dimension size = new Dimension(game.GAME_WIDTH, game.GAME_HEIGHT);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (offscreenImage == null ||
                offscreenImage.getWidth() != game.GAME_WIDTH ||
                offscreenImage.getHeight() != game.GAME_HEIGHT) {
            offscreenImage = new BufferedImage(game.GAME_WIDTH, game.GAME_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        }

        Graphics2D offscreenGraphics = offscreenImage.createGraphics();
        offscreenGraphics.setComposite(AlphaComposite.Src);
        offscreenGraphics.setColor(Color.BLACK);
        offscreenGraphics.fillRect(0, 0, game.GAME_WIDTH, game.GAME_HEIGHT);
        offscreenGraphics.setComposite(AlphaComposite.SrcOver);
        Game.RenderGraphicsGame(offscreenGraphics);
        offscreenGraphics.dispose();

        ((Graphics2D) g).drawImage(offscreenImage, 0, 0, null);
        Toolkit.getDefaultToolkit().sync();
    }
    public game getGame1() {  // Method name ko keyboard_input ke according rakha
        return Game;
    }
}