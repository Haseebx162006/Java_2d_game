package main;

import Entities.Player;
import GameLevels.LevelManager;

import java.awt.*;

// This is the main Game class where i handle all the game loop . i develop diff logics and section to handle fps for
// fps for screen rendering and ups for updating the game logic
public class game implements Runnable{
    private final int FPS=60;
    private Thread gameThread;  // My game loop will run in a separate thread making it free from other parts of game
    private  window_of_game game_window;
    private game_panel Game_panel;
    private final int UPS=90;
    private Player player;
    private LevelManager levelManager;

    public static final int TILE_DEFAULT_SIZE=32;
    public static final float SCALE=1.0f;
    public static final int TILE_HEIGHT=14;
    public static final int TILE_WIDTH=26;
    public static final int TILE_SIZE= (int)(TILE_DEFAULT_SIZE*SCALE);
    public static final int GAME_WIDTH= TILE_SIZE*TILE_WIDTH;
    public static final int GAME_HEIGHT= TILE_SIZE* TILE_HEIGHT;
    //*********************************
    // Game constructor
    public game(){
        initClasses();
        Game_panel=new game_panel(this);
        game_window= new window_of_game(Game_panel);  // passing the gamePanel object to window so it will be drawn
        Game_panel.requestFocus(); // i use it for receiving the inputs from game window

        StartgameLoop();// game logic started in a separate thread

    }
    //*********************************
    private void initClasses(){
        levelManager= new LevelManager(this);
        player= new Player(200,200, (int) (64*SCALE),(int)(40*SCALE));
        player.LoadlevelData(levelManager.getLevel().getLvldata());

    }
    private void StartgameLoop(){
        gameThread= new Thread(this);
        gameThread.start();
    }
    @Override
    public void run() {
        // Calculating the time for updating the FPS and UPS.
        double Timeperframe=1000000000.0/FPS;
        double Timeperupdate=1000000000.0/UPS;
        long previous_time=System.nanoTime();
        double deltaUpdate=0;
        double deltaFrame=0;
        
        while(true){
        long current_time=System.nanoTime();
        deltaUpdate+=(current_time-previous_time)/Timeperupdate;
        deltaFrame+=(current_time-previous_time)/Timeperframe;
        previous_time=current_time;
        if (deltaUpdate>1){
            UpdateGame();
            deltaUpdate--;
        }
        if (deltaFrame>1){
            Game_panel.repaint();
            deltaFrame--;
        }

        }
    }

    public void UpdateGame() {
        player.UpdatePlayer();
        levelManager.update();
    }
    public void RenderGraphicsGame(Graphics g){
        player.RenderPlayer(g);
        levelManager.draw(g);
    }
    public Player getPlayer(){
        return player;
    }

    public void windowFocusLost() {
        player.ResetDirection();
    }
}
