package Entities;
import static Function.features.UI.Enemies.*;
public class Enemy1 extends Enemy{
    public Enemy1(float x, float y) {
        super(x, y,ENEMY1_WIDTH,ENEMY1_HEIGHT,ENEMY_1);
        CreateBox(x,y,22,19);
    }
}
