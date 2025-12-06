# How to Add Enemies to the Game

This guide explains how to add a new enemy type to the game, using the Shark enemy as an example.

## Step-by-Step Guide

### Step 1: Create the Enemy Class

Create a new class that extends the `Enemy` base class in `src/Entities/YourEnemyName.java`:

```java
package Entities;
import main.game;

import java.awt.geom.Rectangle2D;

import static Function.StaticMethodsforMovement.*;
import static Function.features.PlayerDirectons.*;
import static Function.features.UI.Enemies.*;

public class YourEnemyName extends Enemy {
    public YourEnemyName(float x, float y) {
        super(x, y, YOUR_ENEMY_WIDTH, YOUR_ENEMY_HEIGHT, YOUR_ENEMY_TYPE);
    }
    
    public void update() {
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
    
    // Add your custom AI logic here
    private void updatePosition() {
        // Implement enemy movement and AI behavior
    }
    
    private void updateCombat() {
        // Handle attack cooldowns and hit cooldowns
    }
}
```

### Step 2: Add Enemy Constants to features.java

In `src/Function/features.java`, add constants for your enemy in the `Enemies` class:

```java
public static class Enemies {
    // ... existing constants ...
    public static final int YOUR_ENEMY_TYPE = 3; // Use a unique number
    
    // Add dimensions
    public static final int YOUR_ENEMY_WIDTH_DEFAULT = 34;
    public static final int YOUR_ENEMY_HEIGHT_DEFAULT = 30;
    public static final int YOUR_ENEMY_WIDTH = (int) (YOUR_ENEMY_WIDTH_DEFAULT * game.SCALE);
    public static final int YOUR_ENEMY_HEIGHT = (int) (YOUR_ENEMY_HEIGHT_DEFAULT * game.SCALE);
    
    // Add sprite counts for each state in the SPRITE method
    public static final int SPRITE(int enemy_type, int enemy_state) {
        switch (enemy_type) {
            // ... existing cases ...
            case YOUR_ENEMY_TYPE:
                switch (enemy_state) {
                    case IDLE:
                        return 9;  // Number of frames for IDLE animation
                    case RUNNING:
                        return 6;  // Number of frames for RUNNING animation
                    case ATTACK:
                        return 7;  // Number of frames for ATTACK animation
                    case HIT:
                        return 4;  // Number of frames for HIT animation
                    case DEAD:
                        return 5;  // Number of frames for DEAD animation
                }
                break;
        }
        return 0;
    }
}
```

### Step 3: Add Sprite Image to LoadSave.java

In `src/Function/LoadSave.java`, add a constant for your enemy's sprite file:

```java
public static final String YOUR_ENEMY_PNG = "your_enemy_atlas.png";
```

**Important:** Place your sprite atlas image in the `Resource/` folder. The sprite atlas should be organized as:
- Rows: Each row represents a different state (IDLE, RUNNING, ATTACK, HIT, DEAD)
- Columns: Each column represents a frame in the animation
- The image should match the dimensions you specified (width x height per frame)

### Step 4: Update EnemyManager

In `src/Entities/EnemyMangerclass.java`:

1. **Add a list for your enemy:**
```java
private ArrayList<YourEnemyName> yourEnemies = new ArrayList<>();
```

2. **Add sprite loading in `LoadEnemyImg()`:**
```java
// Load YourEnemy images
YourEnemy_images = new BufferedImage[5][9]; // [states][max frames]
BufferedImage yourEnemyLoad = LoadSave.GetAtlas(LoadSave.YOUR_ENEMY_PNG);
for (int i = 0; i < YourEnemy_images.length; i++) {
    for (int j = 0; j < YourEnemy_images[i].length; j++) {
        YourEnemy_images[i][j] = yourEnemyLoad.getSubimage(
            j * YOUR_ENEMY_WIDTH_DEFAULT, 
            i * YOUR_ENEMY_HEIGHT_DEFAULT, 
            YOUR_ENEMY_WIDTH_DEFAULT, 
            YOUR_ENEMY_HEIGHT_DEFAULT
        );
    }
}
```

3. **Update `loadLevelData()` to include your enemy:**
```java
for (YourEnemyName e : yourEnemies) {
    e.loadLevelData(levelData);
}
```

4. **Update `setPlayer()` to include your enemy:**
```java
for (YourEnemyName e : yourEnemies) {
    e.setPlayer(player);
}
```

5. **Add drawing method:**
```java
private void drawYourEnemies(Graphics g, int levelxoffset) {
    for (YourEnemyName e : yourEnemies) {
        g.drawImage(
            YourEnemy_images[e.getState_of_enemy()][e.getAnimationIndex()],
            (int)e.getBox().x - levelxoffset,
            (int)e.getBox().y,
            YOUR_ENEMY_WIDTH,
            YOUR_ENEMY_HEIGHT,
            null
        );
    }
}
```

6. **Call the draw method in `draw()`:**
```java
public void draw(Graphics g, int levelxoffset) {
    drawCrbbies(g, levelxoffset);
    drawSharks(g, levelxoffset);
    drawYourEnemies(g, levelxoffset); // Add this
}
```

7. **Update `addEnemies()` to load your enemies:**
```java
yourEnemies = new ArrayList<>(level.getYourEnemies());
```

8. **Update `update()` to update your enemies:**
```java
for (YourEnemyName e : yourEnemies) {
    if (!e.isDead()) {
        e.update();
        anyEnemyAlive = true;
    }
}
```

9. **Update `checkPlayerAttack()` to handle your enemy:**
```java
for (YourEnemyName enemy : yourEnemies) {
    if (!enemy.isDead() && !enemy.isHit()) {
        if (attackBox.intersects(enemy.getBox())) {
            enemy.takeDamage(25);
            return;
        }
    }
}
```

10. **Update `checkEnemyAttacks()` to handle your enemy:**
```java
for (YourEnemyName enemy : yourEnemies) {
    if (!enemy.isDead()) {
        Rectangle2D.Float enemyAttackBox = enemy.getAttackHitbox();
        if (enemyAttackBox != null && enemyAttackBox.intersects(player.getBox())) {
            player.takeDamage(5, enemy.getBox().x);
            return;
        }
    }
}
```

11. **Update `resetEnemies()`:**
```java
yourEnemies.clear();
```

### Step 5: Update Level.java

In `src/GameLevels/Level.java`:

1. **Import your enemy class:**
```java
import Entities.YourEnemyName;
```

2. **Add a list for your enemy:**
```java
private ArrayList<YourEnemyName> yourEnemies = new ArrayList<>();
```

3. **Load your enemy in `loadEntities()`:**
```java
private void loadEntities(int greenValue, int x, int y) {
    switch (greenValue) {
        case ENEMY_1 -> crabs.add(new Enemy1(x * game.TILE_SIZE, y * game.TILE_SIZE));
        case SHARK -> sharks.add(new Shark(x * game.TILE_SIZE, y * game.TILE_SIZE));
        case YOUR_ENEMY_TYPE -> yourEnemies.add(new YourEnemyName(x * game.TILE_SIZE, y * game.TILE_SIZE));
        case 100 -> playerSpawn = new Point(x * game.TILE_SIZE, y * game.TILE_SIZE);
    }
}
```

4. **Add a getter method:**
```java
public ArrayList<YourEnemyName> getYourEnemies() {
    return yourEnemies;
}
```

### Step 6: Add Enemy to Level Data

To place your enemy in a level:

1. Open your level image file (e.g., `Resource/lvls/1.png`)
2. Use an image editor to set the **green channel** value to your enemy type constant (e.g., `3` for YOUR_ENEMY_TYPE)
3. The red channel controls level collision data
4. The blue channel controls objects
5. The green channel controls entities (enemies, player spawn)

**Example:**
- Green value `0` = Enemy1 (Crab)
- Green value `2` = Shark
- Green value `3` = YourEnemyName
- Green value `100` = Player spawn

## Summary Checklist

- [ ] Created enemy class extending `Enemy`
- [ ] Added enemy constants to `features.java`
- [ ] Added sprite file constant to `LoadSave.java`
- [ ] Added sprite atlas image to `Resource/` folder
- [ ] Updated `EnemyMangerclass.java`:
  - [ ] Added enemy list
  - [ ] Added sprite loading
  - [ ] Added drawing method
  - [ ] Updated all management methods
- [ ] Updated `Level.java`:
  - [ ] Added enemy list
  - [ ] Added loading in `loadEntities()`
  - [ ] Added getter method
- [ ] Added enemy to level data image using green channel

## Notes

- Enemy states: IDLE (0), RUNNING (1), ATTACK (2), HIT (3), DEAD (4)
- Enemy sprite atlas should have 5 rows (one per state) and enough columns for the maximum number of frames
- The `Enemy` base class provides: `canSeePlayer()`, `canAttackPlayer()`, `takeDamage()`, `getAttackHitbox()`, etc.
- Use `box` (not `hitbox`) for collision detection
- Use `State_of_enemy` (not `state`) for state management
- Use `Type_of_enemy` (not `enemyType`) for enemy type
- Use `animationIndex` (not `aniIndex`) for animation frame

## Example: Shark Enemy

The Shark enemy has been fully implemented and can be used as a reference. Check:
- `src/Entities/Shark.java` - Enemy implementation
- `src/Function/features.java` - SHARK constants
- `src/Function/LoadSave.java` - SHARK_PNG constant
- `src/Entities/EnemyMangerclass.java` - Shark management
- `src/GameLevels/Level.java` - Shark loading

