package main;

import Function.features;
import Inputes_of_game.keyboard_input;
import Inputes_of_game.mouse_input;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import static Function.features.player_features.*;
import static Function.features.PlayerDirectons.*;
public class game_panel extends JPanel {
    //Declarations
    //*********************************************
    private mouse_input mouseInput;
    game game1;

    //********************************************

    //***************************************************
    // Constructor and paintcomponent
    public  game_panel(game game1){   // Constructor
        mouseInput= new mouse_input(this);
        this.game1=game1;
        setFocusable(true);
        requestFocus();
       addKeyListener(new keyboard_input(this));
       addMouseListener(mouseInput);
       addMouseMotionListener(mouseInput);
       Set_sizeof_Panel();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        game1.RenderGraphicsGame(g);

    }
    //**********************************************************

    private void Set_sizeof_Panel() {
        Dimension dimension= new Dimension(game.GAME_WIDTH,game.GAME_HEIGHT);
        setPreferredSize(dimension);
    }

    public void updategame() {

    }

    public game getGame1() {
        return game1;
    }
}
