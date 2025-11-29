package Rewards;

import Entities.Player;
import Function.LoadSave;
import Function.features;
import GameLevels.Level;
import State.Playing;
import main.game;

import javax.sound.sampled.Port;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Spliterator;

import static Function.StaticMethodsforMovement.CanCannonSeePlayer;
import static Function.StaticMethodsforMovement.IsProjectileHittingLevel;
import static Function.features.Objects.*;
import static Function.features.Projectiles.CANNON_BALL_HEIGHT;
import static Function.features.Projectiles.CANNON_BALL_WIDTH;

public class Objects_Manager {
    private BufferedImage[][] potionImgs,containerImgs;
    private BufferedImage[] Cannonimage;
    private BufferedImage arrowImage,cannonBallimg;
    private Playing playing;
    private ArrayList<Potions> potions;
    private ArrayList<Container> containers;
    private ArrayList<CannonGun> cannonGuns;
    private ArrayList<Arrow> arrows;
    private ArrayList<Ball> balls= new ArrayList<>();
    public Objects_Manager(Playing playing){
        this.playing=playing;
        LoadImgs();
        potions= new ArrayList<>();
        containers= new ArrayList<>();
    }

    private void LoadImgs() {
        BufferedImage potionSprite = LoadSave.GetAtlas(LoadSave.Potion);
        potionImgs                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            = new BufferedImage[2][7];

        for (int j = 0; j < potionImgs.length; j++)
            for (int i = 0; i < potionImgs[j].length; i++)
                potionImgs[j][i] = potionSprite.getSubimage(12 * i, 16 * j, 12, 16);

        BufferedImage containerSprite = LoadSave.GetAtlas(LoadSave.OBJECT);
        containerImgs = new BufferedImage[2][8];

        for (int j = 0; j < containerImgs.length; j++)
            for (int i = 0; i < containerImgs[j].length; i++)
                containerImgs[j][i] = containerSprite.getSubimage(40 * i, 30 * j, 40, 30);


        arrowImage= LoadSave.GetAtlas(LoadSave.TRAPS_1);
        Cannonimage= new BufferedImage[7];
        BufferedImage temp= LoadSave.GetAtlas(LoadSave.CANNON);
        for (int i = 0; i < Cannonimage.length; i++) {
            Cannonimage[i]=temp.getSubimage(i*40,0,40,26);
        }
        cannonBallimg=LoadSave.GetAtlas(LoadSave.BALL);

    }

    public void checkTouch(Rectangle2D.Float box){
        for (Potions p: potions)
            if (p.isActive()){
                if (box.intersects(p.getBox())){
                    p.setActive(false);
                    ApplyEffects(p);
                }
            }
    }
    public void checkTrap(Player s){
        for (Arrow a : arrows){
            if (a.getBox().intersects(s.getBox())){
              s.KillPlayer();
            }

        }

    }
    private void ApplyEffects(Potions p) {
        if (p.getObjType() == RED_POTION)
            playing.getPlayer().changeHealth(RED_POTION_VALUE);
        else
            playing.getPlayer().changePower(BLUE_POTION_VALUE);
    }
    public void checkObjectHit(Rectangle2D.Float attackbox) {
        for (Container gc : containers)
            if (gc.isActive()) {
                if (gc.getBox().intersects(attackbox)) {
                    gc.setAnimation(true);
                    int type = 0;
                    if (gc.getObjType() == BARREL)
                        type = 1;
                    potions.add(new Potions((int) (gc.getBox().x + gc.getBox().width / 2), (int) (gc.getBox().y - gc.getBox().height / 2), type));
                    return;
                }
            }
    }
    public void checkTrapHit(Player p){
        for (Arrow a: arrows){
            if (a.getBox().intersects(p.getBox())){
                p.KillPlayer();
            }
        }

    }
    public void update(int[][] levelData,Player player){
        for (Potions p : potions)
            if (p.isActive())
                p.update();

        for (Container gc : containers)
            if (gc.isActive())
                gc.update();

        updateCannon(levelData,player);
        updateProjectile(levelData,player);
    }

    private void updateProjectile(int[][] levelData, Player player) {
        for (Ball b: balls)
            if (b.isActive()) {
                b.updatePos();
                if (b.getBox().intersects(player.getBox())) {
                    player.changeHealth(-25);
                    b.setActive(false);
                } else if (IsProjectileHittingLevel(b, levelData))
                    b.setActive(false);
            }
    }

    private void updateCannon(int[][] leveldata,Player player) {
        for (CannonGun c : cannonGuns) {
            if (!c.Animation)
                if (c.getTileYaxis() == player.getTileY())
                    if (isPlayerInRange(c, player))
                        if (isPlayerInfrontOfCannon(c, player))
                            if (CanCannonSeePlayer(leveldata, player.getBox(), c.getBox(), c.getTileYaxis()))
                                c.setAnimation(true);

            c.update();
            if (c.getAnimationIndex() == 4 && c.getAnimationTick() == 0)
                shootCannon(c);
        }

    }
    private boolean isPlayerInRange(CannonGun c, Player player) {
        int absValue = (int) Math.abs(player.getBox().x - c.getBox().x);
        return absValue <= game.TILE_SIZE * 5;
    }
    private void shootCannon(CannonGun c) {
        c.setAnimation(true);
        int direction=1;
        if (c.getObjType()==CANNON_LEFT){
            direction=-1;
        }

        Ball newBall = new Ball((int)(c.getBox().x),(int)(c.getBox().y),direction);
        balls.add(newBall);


    }

    private boolean isPlayerInfrontOfCannon(CannonGun c, Player player) {
        if (c.getObjType() == CANNON_LEFT) {
            if (c.getBox().x > player.getBox().x)
                return true;

        } else if (c.getBox().x < player.getBox().x)
            return true;
        return false;
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawPotions(g,xLvlOffset);
        drawContainers(g, xLvlOffset);

        drawTrap1(g,xLvlOffset);
        drawCannons(g,xLvlOffset);
        drawBalls(g,xLvlOffset);
    }

    private void drawBalls(Graphics g, int xLvlOffset) {
        for (Ball b : balls){
            if (b.isActive()){
                g.drawImage(cannonBallimg,(int)(b.getBox().x-xLvlOffset),(int)(b.getBox().y),CANNON_BALL_WIDTH,CANNON_BALL_HEIGHT,null);
            }
        }
    }

    private void drawCannons(Graphics g, int xlvlOffset) {
        for (CannonGun c: cannonGuns){
            int x= (int)(c.getBox().x-xlvlOffset);
            int width=CANNON_WIDTH;
            if (c.getObjType()==CANNON_RIGHT){
                x+=width;
                width*=-1;
            }
            g.drawImage(Cannonimage[c.getAnimationIndex()], x,(int)(c.getBox().y),width,CANNON_HEIGHT,null);
        }
    }

    private void drawTrap1(Graphics g, int xLvlOffset) {
        for (Arrow a : arrows) {
            g.drawImage(arrowImage,(int)(a.getBox().x-xLvlOffset),(int)(a.getBox().y-a.getyOffset()),SPIKE_WIDTH,SPIKE_WIDTH,null);
        }
    }

    private void drawPotions(Graphics g, int xLvlOffset) {
        for (Potions p : potions)
            if (p.isActive()) {
                int type = 0;
                if (p.getObjType() == RED_POTION)
                    type = 1;
                g.drawImage(potionImgs[type][p.getAnimationIndex()], (int) (p.getBox().x - p.getXOffset() - xLvlOffset), (int) (p.getBox().y - p.getyOffset()), POTION_WIDTH, POTION_HEIGHT,
                        null);
            }
    }
    private void drawContainers(Graphics g, int xLvlOffset) {
        for (Container gc : containers)
            if (gc.isActive()) {
                int type = 0;
                if (gc.getObjType() == BARREL)
                    type = 1;
                g.drawImage(containerImgs[type][gc.getAnimationIndex()], (int) (gc.getBox().x - gc.getXOffset() - xLvlOffset), (int) (gc.getBox().y - gc.getyOffset()), CONTAINER_WIDTH,
                        CONTAINER_HEIGHT, null);
            }
    }


    public void loadObject(Level newLevel) {
        potions=new ArrayList<>(newLevel.getPotions());
        containers=new ArrayList<>(newLevel.getContainers());
        arrows= newLevel.getArrows();
        cannonGuns=newLevel.getCannonGuns();
        balls.clear();
    }

    public void resetAllObjects() {
        loadObject(playing.getLevelManager().getLevel());
        for(Potions p: potions){
            p.reset();
        }
        for (Container c: containers){
            c.reset();
        }
        for(CannonGun c: cannonGuns){
            c.reset();
        }
    }
}
