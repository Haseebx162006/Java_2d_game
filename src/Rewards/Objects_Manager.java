package Rewards;

import Function.LoadSave;
import Function.features;
import GameLevels.Level;
import State.Playing;

import javax.sound.sampled.Port;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Function.features.Objects.*;

public class Objects_Manager {
    private BufferedImage[][] potionImgs,containerImgs;
    private Playing playing;
    private ArrayList<Potions> potions;
    private ArrayList<Container> containers;
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
    public void loadObjects(Level newLevel) {
        potions = newLevel.getPotions();
        containers = newLevel.getContainers();
    }
    public void update(){
        for (Potions p : potions)
            if (p.isActive())
                p.update();

        for (Container gc : containers)
            if (gc.isActive())
                gc.update();
    }
    public void draw(Graphics g, int xLvlOffset) {
        drawPotions(g,xLvlOffset);
        drawContainers(g, xLvlOffset);
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
        potions=newLevel.getPotions();
        containers=newLevel.getContainers();
    }

    public void resetAllObjects() {
        for(Potions p: potions){
            p.reset();
        }
        for (Container c: containers){
            c.reset();
        }
    }
}
