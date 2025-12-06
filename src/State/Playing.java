package State;

import Entities.EnemyMangerclass;
import Entities.Player;
import Function.LoadSave;
import GameLevels.LevelManager;
import Rewards.Objects_Manager;
import UI.CompleteLevelBanner;
import UI.Pause;
import main.game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import static Function.features.background.*;

public class Playing extends State implements Methods{
    private boolean drawShip = true;
    private int shipAni, shipTick, shipDir = 1;
    private float shipHeightDelta, shipHeightChange = 0.05f * game.SCALE;
    private BufferedImage background,bigCloud,smallCloud;
    int[] cloudPositons;
    private Objects_Manager objectsManager;
    private boolean level_Complete;
    private Player player;
    private LevelManager levelManager;
    private boolean paused=false;
    private Pause pause;
    private Random random= new Random();
    private EnemyMangerclass enemyMangerclass;
    private int levelxOffset;
    private int left_border=(int)(0.25*game.GAME_WIDTH);
    private int right_border=(int)(0.75*game.GAME_WIDTH);
    private int maxLevelOffset;
    public CompleteLevelBanner completeLevelBanner;
    // Game Over state
    private boolean showGameOver = false;
    private int gameOverTimer = 0;
    private static final int GAME_OVER_DISPLAY_TIME = 180; // frames before returning to menu
    public Playing(game game1){
        super(game1);
        initClasses();
        background=LoadSave.GetAtlas(LoadSave.BACKGROUND_IMAGE);
        bigCloud=LoadSave.GetAtlas(LoadSave.BIG_CLOUD);
        smallCloud=LoadSave.GetAtlas(LoadSave.SMALL_CLOUD);
        cloudPositons= new int[9];
        for (int i = 0; i < cloudPositons.length; i++) {
            cloudPositons[i]= (int)(90*game.SCALE)+random.nextInt((int)(150*game.SCALE));
        }
        calculateLevelOffset();
        loadFirstlevel();
    }
    public void LoadNextLevel(){
        levelManager.setLevel_Index(levelManager.getLevel_Index() + 1);
        levelManager.loadnextLevel();
        //player.(levelManager.getCurrentLevel().getPlayerSpawn());
        resetGame();
        drawShip = false;
    }
    private void loadFirstlevel() {
        enemyMangerclass.addEnemies(levelManager.getLevel());
        objectsManager.loadObject(levelManager.getLevel());
    }

    private void calculateLevelOffset() {
        maxLevelOffset=levelManager.getLevel().getMaxLvlOffsetX();
    }

    private void initClasses(){
        levelManager= new LevelManager(game1);
        enemyMangerclass= new EnemyMangerclass(this);
        objectsManager= new Objects_Manager(this);
        player= new Player(200,200, (int) (64* game.SCALE),(int)(40*game.SCALE),this);
        player.LoadlevelData(levelManager.getLevel().getLvlData());
        enemyMangerclass.loadLevelData(levelManager.getLevel().getLvlData());
        //enemyMangerclass.setPlayer(player); // Pass player reference to enemies
        pause= new Pause(this);
        completeLevelBanner= new CompleteLevelBanner(this);
    }

    public void resetGame() {
        // Reset game state for replay
        showGameOver = false;
        gameOverTimer = 0;
        levelxOffset = 0;
        paused = false;

        level_Complete=false;
        player.reset();
        player.LoadlevelData(levelManager.getLevel().getLvlData());

        // Reset enemies - MUST RE-ADD THEM!
        enemyMangerclass.resetEnemies(); // Clears the list
        enemyMangerclass.addEnemies(levelManager.getLevel());
        objectsManager.resetAllObjects();
    }
    public Player getPlayer(){
        return player;
    }

    public void windowFocusLost() {
        player.ResetDirection();
    }
    public EnemyMangerclass getEnemyMangerclass(){
        return enemyMangerclass;
    }
    @Override
    public void update() {
        if (showGameOver) {
            gameOverTimer++;
            if (gameOverTimer >= GAME_OVER_DISPLAY_TIME) {
                // Return to menu after showing game over
                GameState.gameState = GameState.MENU;
                showGameOver = false;
                gameOverTimer = 0;
            }
            return;
        }

        if (player.isDead()) {
            if (player.isDeathAnimationComplete() && !showGameOver) {
                showGameOver = true;
                gameOverTimer = 0;
            }
        }
        if (paused){
            pause.update();
        } else if (level_Complete) {
            completeLevelBanner.update();
        }
        else if (!paused && !player.isDead() && !level_Complete){
            levelManager.update();
            objectsManager.update(levelManager.getLevel().getLvlData(),player);
            player.UpdatePlayer();
            checkBorder();
            enemyMangerclass.update(levelManager.getLevel().getLvlData(),player);
            checkCombat(); // Check for attacks and collisions
        }
        else if (paused){
            pause.update();
        } else if (player.isDead()) {
            // Only update player death animation
            player.UpdatePlayer();
        }

    }




    private void checkCombat() {
        if (!player.isAlive()) {
            return; // Player is dead, no combat
        }

        // Check player attacks on enemies
        if (player.isAttacking()) {
            Rectangle2D.Float playerAttackBox = player.getAttackHitbox();
            if (playerAttackBox != null) {
                enemyMangerclass.checkPlayerAttack(playerAttackBox);
            }
        }

        // Check enemy attacks on player
        enemyMangerclass.checkEnemyAttacks(player);
    }
    

    private void checkBorder() {
        int playerX= (int) player.getBox().x;
        int difference=playerX-levelxOffset;
        if (difference>right_border){
            levelxOffset+=difference-right_border;
        }
        else if (difference<left_border){
            levelxOffset+=difference-left_border;
        }
        if (levelxOffset>maxLevelOffset){
            levelxOffset=maxLevelOffset;
        } else if (levelxOffset<0) {
            levelxOffset=0;
        }
    }
    public void checkPotionTouched(Rectangle2D.Float box){
        objectsManager.checkTouch(box);

    }
    @Override
    public void draw(Graphics g) {
        g.drawImage(background,0,0,game.GAME_WIDTH,game.GAME_HEIGHT,null);
        drawCloud(g);
        levelManager.draw(g,levelxOffset);
        player.RenderPlayer(g,levelxOffset);
        enemyMangerclass.draw(g,levelxOffset);
        objectsManager.draw(g,levelxOffset);
        g.setColor(Color.RED);
        drawHealthBar(g); // Draw player health bar
        if (showGameOver) {
            drawGameOver(g);
        }
        if (paused) pause.draw(g);
        else if (level_Complete) {
            completeLevelBanner.draw(g);
        }
    }

    private void drawGameOver(Graphics g) {
        // Semi-transparent overlay
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, game.GAME_WIDTH, game.GAME_HEIGHT);

        // Game Over text
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        FontMetrics fm = g.getFontMetrics();
        String gameOverText = "GAME OVER";
        int textX = (game.GAME_WIDTH - fm.stringWidth(gameOverText)) / 2;
        int textY = game.GAME_HEIGHT / 2 - 50;
        g.drawString(gameOverText, textX, textY);

        // Return to menu message
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        String menuText = "Returning to menu...";
        fm = g.getFontMetrics();
        int menuX = (game.GAME_WIDTH - fm.stringWidth(menuText)) / 2;
        int menuY = game.GAME_HEIGHT / 2 + 20;
        g.drawString(menuText, menuX, menuY);
    }

    private void drawHealthBar(Graphics g) {
        int barWidth = 200;
        int barHeight = 20;
        int x = 10;
        int y = 10;

        // Background (red)
        g.setColor(Color.RED);
        g.fillRect(x, y, barWidth, barHeight);

        // Health (green)
        int healthWidth = (int)((player.getCurrentHealth() / (float)player.getMaxHealth()) * barWidth);
        g.setColor(Color.GREEN);
        g.fillRect(x, y, healthWidth, barHeight);

        // Border
        g.setColor(Color.WHITE);
        g.drawRect(x, y, barWidth, barHeight);

        // Health text
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        String healthText = player.getCurrentHealth() + " / " + player.getMaxHealth();
        g.drawString(healthText, x + 5, y + 15);
    }

    private void drawCloud(Graphics g) {
        for (int i = 0; i < 3; i++) {
            g.drawImage(bigCloud,(i*BIG_CLOUD_WIDTH)-(int)(levelxOffset*0.3),(int)(204*game.SCALE),BIG_CLOUD_WIDTH,BIG_CLOUD_HEIGHT,null);
        }
        // chote clouds
        for (int i = 0; i < cloudPositons.length; i++) {
            g.drawImage(smallCloud,SMALL_CLOUD_WIDTH*4*i-(int)(levelxOffset*0.9),cloudPositons[i],SMALL_CLOUD_WIDTH,SMALL_CLOUD_HEIGHT,null);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
    public void unpaused(){
        paused=false;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (paused){
            pause.mousePressed(e);
        } else if (level_Complete) {
            completeLevelBanner.mousePressed(e);
        }
        if (e.getButton()==MouseEvent.BUTTON1){
            player.setAttack(true);
        }
        if (e.getButton()==MouseEvent.BUTTON3){
            player.setJump(true);
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (paused){
            pause.mouseReleased(e);
        }
        else if (level_Complete) {
            completeLevelBanner.mouseReleased(e);
        }
        if (e.getButton() == MouseEvent.BUTTON1) {
            player.setAttack(false);
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            player.setJump(false);

        }

    }
    public void mouseDragged(MouseEvent e){
        if (paused){
            pause.mouseDragged(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (paused){
            pause.mouseMoved(e);
        }
        else if (level_Complete) {
            completeLevelBanner.mouseMoved(e);
        }
    }
    public void setLevelOffset(int levelOffset){
        this.maxLevelOffset=levelOffset;
    }
    @Override
    public void KeyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_A:
                player.setLeft(true);
                break;
            case KeyEvent.VK_D:
                player.setRight(true);

                break;
            case KeyEvent.VK_SPACE:
                player.setJump(true);
                break;
            case KeyEvent.VK_ESCAPE:
                paused=!paused;
        }
    }

    public Objects_Manager getObjectsManager() {
        return objectsManager;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_A:
                player.setLeft(false);
                break;
            case KeyEvent.VK_D:
                player.setRight(false);
                break;
            case KeyEvent.VK_SPACE:
                player.setJump(false);
                break;
        }
    }

    public void setLevelCompleted(boolean levelComppleted) {
        this.level_Complete=levelComppleted;

    }

    public void checkObjectHit(Rectangle2D.Float box) {
        objectsManager.checkObjectHit(box);
    }

    public void checkTrapHit(Player p
    ) {
        objectsManager.checkTrapHit(p);
    }
}

