package Entities;
import main.game;

import static Function.StaticMethodsforMovement.*;
import static Function.StaticMethodsforMovement.CanMove;
import static Function.features.PlayerDirectons.*;
import static Function.features.UI.Enemies.*;
public class Enemy1 extends Enemy{
    public Enemy1(float x, float y) {
        super(x, y,ENEMY1_WIDTH,ENEMY1_HEIGHT,ENEMY_1);
    }
    public void update(){
        if (isDead) {
            updateAnimation();
            return;
        }
        
        if (levelData != null) {
            updatePosition();
        }
        updateAnimation();
        updateCombat();
    }
    
    private void updateCombat() {

        if (attackCooldown > 0) {
            attackCooldown--;
        }
        
        // Update hit cooldown
        if (hitCooldown > 0) {
            hitCooldown--;
            if (hitCooldown == 0) {
                isHit = false;
                if (!isDead && State_of_enemy == HIT) {
                    State_of_enemy = RUNNING;
                }
            }
        }
    }
    
    private void updatePosition() {
        if (!inAir) {
            if (!OnFloor(box, levelData)) {
                inAir = true;
                airSpeed = 0;
            }
        }

        if (inAir) {
            float newY = box.y + airSpeed;
            if (CanMove(box.x, newY, box.width, box.height, levelData)) {
                box.y = newY;
                airSpeed += gravity;
            } else {
                float correctedY = CheckUnderEnvironmentorAbove(box, airSpeed);
                if (correctedY >= 0 && correctedY < game.GAME_HEIGHT) {
                    box.y = correctedY;
                }
                if (airSpeed > 0) {
                    // Hit floor
                    airSpeed = 0;
                    inAir = false;
                } else {
                    airSpeed = fallSpeed;
                }
            }
        } else {
            if (canSeePlayer() && !isDead) {

                float playerX = player.getBox().x;
                float enemyX = box.x;

                if (playerX < enemyX) {
                    walkDir = LEFT;
                } else {
                    walkDir = RIGHT;
                }
                if (canAttackPlayer()) {
                    State_of_enemy = ATTACK;
                    attackCooldown = ATTACK_COOLDOWN_TIME;
                } else if (State_of_enemy != ATTACK && State_of_enemy != HIT) {
                    // Chase player
                    State_of_enemy = RUNNING;
                    float Speed = (walkDir == LEFT) ? -Walk : Walk;
                    float newX = box.x + Speed;
                    
                    if (CanMove(newX, box.y, box.width, box.height, levelData) && isFloorAhead(Speed, levelData)) {
                        box.x = newX;
                    }
                }
            } else {

                if (State_of_enemy == ATTACK && attackCooldown == 0) {
                    State_of_enemy = RUNNING;
                }
                
                switch (State_of_enemy){
                    case IDLE:
                        State_of_enemy=RUNNING;
                        break;
                    case RUNNING:
                        if (State_of_enemy != HIT) {
                            float Speed=0;
                            if (walkDir==LEFT){
                                Speed=-Walk;
                            }
                            else{
                                Speed=Walk;
                            }

                            float newX = box.x + Speed;
                            boolean canMoveHorizontally = CanMove(newX, box.y, box.width, box.height, levelData);
                            boolean floorAhead = isFloorAhead(Speed, levelData);

                            if (canMoveHorizontally && floorAhead) {
                                box.x = newX;
                            } else {

                                ChangeDirection();
                            }
                        }
                        break;
                }
            }
        }
    }

}
