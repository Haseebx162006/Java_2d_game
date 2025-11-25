# HFS Game - Quick Reference Guide

## Quick Start

### Running the Game
```bash
java -cp out main.Main
```

### Essential Controls
- **A/D**: Move left/right
- **Space**: Jump
- **Left Click**: Attack
- **ESC**: Pause

---

## Class Quick Reference

### Player
```java
Player player = new Player(x, y, width, height);
player.UpdatePlayer();              // Update each frame
player.takeDamage(5);               // Apply damage
player.getAttackHitbox();           // Get attack area
player.isAlive();                   // Check if alive
player.reset();                     // Reset for replay
```

### Enemy
```java
Enemy1 enemy = new Enemy1(x, y);
enemy.setPlayer(player);            // Set player for AI
enemy.update();                     // Update AI and position
enemy.takeDamage(10);              // Apply damage
enemy.isDead();                    // Check if dead
```

### Combat
```java
// In Playing.java
checkCombat();                      // Check all combat collisions
enemyMangerclass.checkPlayerAttack(attackBox);  // Player attacks
enemyMangerclass.checkEnemyAttacks(player);     // Enemy attacks
```

---

## Constants Reference

### Player States
- `STANDING = 0`
- `RUNNING = 1`
- `JUMPING = 2`
- `FALLING = 3`
- `GROUND = 4` (Death)
- `HIT = 5`
- `ATTACK = 6`

### Enemy States
- `IDLE = 0`
- `RUNNING = 1`
- `ATTACK = 2`
- `HIT = 3`
- `DEAD = 4`

### Game States
- `MENU`
- `PLAYING`
- `OPTIONS`
- `QUIT`

---

## Health Values

| Entity | Max Health | Attack Damage |
|--------|-----------|---------------|
| Player | 100 | 10 |
| Enemy | 50 | 5 |

---

## Ranges & Distances

| Feature | Value |
|---------|-------|
| Enemy Vision Range | 200 pixels |
| Enemy Attack Range | 30 pixels |
| Player Attack Range | 40 pixels |
| Hit Cooldown (Player) | 60 frames |
| Hit Cooldown (Enemy) | 30 frames |
| Attack Cooldown (Enemy) | 90 frames |
| Death Animation | 120 frames |
| Game Over Display | 180 frames |

---

## File Locations

### Key Classes
- Entry Point: `src/main/Main.java`
- Game Loop: `src/main/game.java`
- Player: `src/Entities/Player.java`
- Enemy: `src/Entities/Enemy1.java`
- Gameplay: `src/State/Playing.java`
- Menu: `src/State/MENU.java`

### Resources
- All images: `Resource/` folder
- Level data: `Resource/level_one_data_long.png`
- Player sprite: `Resource/p2.png`
- Enemy sprite: `Resource/enemy_1.png`

---

## Common Code Patterns

### Adding New Enemy Type
```java
// 1. Create new class extending Enemy
public class Enemy2 extends Enemy {
    public Enemy2(float x, float y) {
        super(x, y, ENEMY2_WIDTH, ENEMY2_HEIGHT, ENEMY_2);
    }
    // Override update() for custom AI
}

// 2. Add to EnemyManager
private ArrayList<Enemy2> enemies2 = new ArrayList<>();
```

### Adding New Player State
```java
// 1. Add constant in features.java
public static final int NEW_STATE = 9;

// 2. Add sprite count
case NEW_STATE:
    return 4; // number of frames

// 3. Use in Player.setAnimation()
if (condition) {
    Playermove = NEW_STATE;
}
```

### Adding New Level
```java
// 1. Create level data image
// 2. Add to LoadSave.java
public static final String LevelTwoData = "level_two_data.png";

// 3. Load in LevelManager
leveltwo = new Level(LoadSave.GetLevelData(LevelTwoData));
```

---

## Debug Tips

### Enable Hitbox Visualization
```java
// In Player.java or Enemy.java
private static final boolean DEBUG_HITBOX = true;
```

### Check Collision
```java
// Use StaticMethodsforMovement
boolean canMove = CanMove(x, y, width, height, levelData);
boolean onFloor = OnFloor(box, levelData);
```

### Print Debug Info
```java
System.out.println("Player Health: " + player.getCurrentHealth());
System.out.println("Enemy Count: " + enemies.size());
```

---

## Performance Tips

1. **Limit Enemy Updates**: Only update enemies in view
2. **Cache Level Data**: Don't reload level data each frame
3. **Optimize Collision**: Use tile-based checks first
4. **Reduce Redraws**: Only repaint changed areas (if possible)

---

## Troubleshooting Checklist

- [ ] All resource files in `Resource/` folder?
- [ ] Java version 8+?
- [ ] Game panel has focus?
- [ ] Level data image correct format?
- [ ] Enemy spawn values match constants?
- [ ] Collision data loaded correctly?

---

**For detailed documentation, see README.md**

