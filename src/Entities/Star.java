package Entities;
import main.game;

import java.awt.geom.Rectangle2D;

import static Function.StaticMethodsforMovement.*;
import static Function.StaticMethodsforMovement.CanMove;
import static Function.features.PlayerDirectons.LEFT;
import static Function.features.PlayerDirectons.RIGHT;
import static Function.features.UI.Enemies.*;
public class Star extends Enemy{
    public Star(float x , float y){
        super(x,y,PINKSTAR_WIDTH,PINKSTAR_HEIGHT,PINKSTAR);
    }
    public void update(){
        if (isDead){
            // Only update animation when dead, don't update position or combat
            updateAnimation();
            return;
        }
        
        if (levelData!=null){
            updatePosition();
        }
        updateAnimation();
        updateCombat();
    }
    public void updateCombat(){
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
                float distanceToPlayer = Math.abs(player.getBox().x - box.x);
                boolean isFacingPlayer = (player.getBox().x < box.x && walkDir == LEFT) ||
                        (player.getBox().x > box.x && walkDir == RIGHT);

                if (canAttackPlayer() && distanceToPlayer < 50 * game.SCALE && isFacingPlayer) {
                    if (attackCooldown <= 0) {
                        State_of_enemy = ATTACK;
                        attackCooldown = ATTACK_COOLDOWN_TIME;
                    }
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

                switch (State_of_enemy) {
                    case IDLE:
                        State_of_enemy = RUNNING;
                        break;
                    case RUNNING:
                        if (State_of_enemy != HIT) {
                            float Speed = 0;
                            if (walkDir == LEFT) {
                                Speed = -Walk;
                            } else {
                                Speed = Walk;
                            }

                            float newX = box.x + Speed;
                            boolean canMoveHorizontally = CanMove(newX, box.y, box.width, box.height, levelData);
                            boolean floorAhead = isFloorAhead(Speed, levelData);

                            if (canMoveHorizontally && floorAhead) {
                                // Check if moving would cause overlap with player
                                Rectangle2D.Float tempBox = new Rectangle2D.Float(
                                        newX,
                                        box.y,
                                        box.width,
                                        box.height
                                );

                                if (!tempBox.intersects(player.getBox())) {
                                    box.x = newX;
                                } else {
                                    // If we would overlap, stop moving and attack if possible
                                    if (canAttackPlayer() && attackCooldown <= 0) {
                                        State_of_enemy = ATTACK;
                                        attackCooldown = ATTACK_COOLDOWN_TIME;
                                    }
                                }
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
