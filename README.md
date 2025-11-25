# HFS Game - Complete Documentation

## Table of Contents
1. [Overview](#overview)
2. [Features](#features)
3. [Game Mechanics](#game-mechanics)
4. [Controls](#controls)
5. [Installation & Running](#installation--running)
6. [Project Structure](#project-structure)
7. [Architecture](#architecture)
8. [Class Documentation](#class-documentation)
9. [Game States](#game-states)
10. [Combat System](#combat-system)
11. [Enemy AI](#enemy-ai)
12. [Level System](#level-system)
13. [Development Notes](#development-notes)

---

## Overview

HFS (Horizontal Fighting System) is a 2D side-scrolling platformer game built with Java Swing. The game features a player character that can move, jump, and attack enemies in a tile-based level. Enemies patrol the level and will chase and attack the player when detected.

### Game Genre
- **Type**: 2D Side-Scrolling Platformer
- **Style**: Action/Combat Platformer
- **Engine**: Java Swing with custom game loop

---

## Features

### Core Features
- ✅ **Player Movement**: Smooth horizontal movement with jumping mechanics
- ✅ **Combat System**: Player and enemy attack mechanics with hitboxes
- ✅ **Health System**: Player and enemy health with visual health bar
- ✅ **Enemy AI**: Intelligent enemies that patrol, detect, chase, and attack the player
- ✅ **Level System**: Tile-based level rendering with collision detection
- ✅ **Death System**: Player death animation and Game Over screen
- ✅ **Replay System**: Reset and replay functionality
- ✅ **Menu System**: Main menu with play, options, and quit
- ✅ **Pause System**: In-game pause menu with replay option
- ✅ **Animation System**: Sprite-based animations for player and enemies

### Visual Features
- Animated player sprites (9 states: Standing, Running, Jumping, Falling, Ground, Hit, Attack, etc.)
- Animated enemy sprites (5 states: Idle, Running, Attack, Hit, Dead)
- Health bar UI with current/max health display
- Game Over screen with fade effect
- Background clouds with parallax scrolling
- Tile-based level rendering

---

## Game Mechanics

### Player Mechanics

#### Movement
- **Horizontal Movement**: Left (A) and Right (D) keys
- **Jumping**: Space bar or Right Mouse Button
- **Gravity**: Applied when player is in air
- **Collision Detection**: Wall and floor collision using tile-based system

#### Combat
- **Attack**: Left Mouse Button or Left Click
- **Attack Range**: 40 pixels in front of player
- **Attack Damage**: 10 damage per hit
- **Attack Animation**: 3-frame attack animation

#### Health
- **Max Health**: 100 HP
- **Damage**: 5 damage per enemy attack
- **Invincibility**: 60 frames (1 second) after taking damage
- **Death**: When health reaches 0, plays death animation (GROUND state)

### Enemy Mechanics

#### Behavior States
1. **Patrolling**: Default state - moves back and forth
2. **Chasing**: When player is within vision range (200 pixels)
3. **Attacking**: When player is within attack range (30 pixels)
4. **Hit**: Brief stun state after taking damage
5. **Dead**: Death animation plays, enemy stops moving

#### Enemy Stats
- **Max Health**: 50 HP
- **Attack Damage**: 5 damage per attack
- **Vision Range**: 200 pixels
- **Attack Range**: 30 pixels
- **Attack Cooldown**: 90 frames (1.5 seconds)
- **Movement Speed**: 1.0 * SCALE

### Level Mechanics

#### Tile System
- **Tile Size**: 32x32 pixels (scaled)
- **Level Data**: Loaded from image file (`level_one_data_long.png`)
- **Collision**: Tile value 11 = air, all other values = solid
- **Enemy Spawning**: Enemies placed based on green channel values in level image

#### Camera System
- **Camera Follow**: Follows player with left/right borders
- **Level Offset**: Scrolls level based on player position
- **Boundaries**: Prevents scrolling beyond level edges

---

## Controls

### Keyboard Controls

| Key | Action |
|-----|--------|
| **A** | Move Left |
| **D** | Move Right |
| **Space** | Jump |
| **ESC** | Pause/Unpause Game |
| **Enter** | Start Game (from menu) |

### Mouse Controls

| Button | Action |
|--------|--------|
| **Left Click** | Attack |
| **Right Click** | Jump (alternative) |
| **Mouse Movement** | Navigate menus, adjust volume slider |

### Menu Navigation
- **Mouse Over**: Highlights menu buttons
- **Left Click**: Selects menu option
- **Enter Key**: Starts game from main menu

---

## Installation & Running

### Prerequisites
- Java JDK 8 or higher
- IDE (IntelliJ IDEA, Eclipse, or VS Code) recommended

### Setup Instructions

1. **Clone or Download Project**
   ```bash
   # If using git
   git clone <repository-url>
   cd HFS
   ```

2. **Project Structure**
   ```
   HFS/
   ├── src/
   │   ├── main/           # Main game classes
   │   ├── Entities/       # Player and Enemy classes
   │   ├── State/          # Game states (Menu, Playing)
   │   ├── UI/             # UI components
   │   ├── Function/       # Utility classes
   │   ├── GameLevels/     # Level management
   │   └── Inputes_of_game/ # Input handlers
   ├── Resource/           # Game assets (images)
   └── README.md
   ```

3. **Resource Files Required**
   Ensure all images are in the `Resource/` folder:
   - `p2.png` - Player sprite sheet
   - `enemy_1.png` - Enemy sprite sheet
   - `level_one_data_long.png` - Level data image
   - `outside_sprites.png` - Level tiles
   - `menu_background.png` - Menu panel
   - `menu_bg.jpg` - Menu background
   - `pause_menu.png` - Pause menu
   - `button_atlas.png` - Button sprites
   - `sound_button.png` - Sound button
   - `urm_buttons.png` - URM buttons
   - `volume_buttons.png` - Volume slider
   - `playing_bg_img.png` - Game background
   - `big_clouds.png` - Large clouds
   - `small_clouds.png` - Small clouds

4. **Running the Game**
   ```bash
   # Compile (if needed)
   javac -d out src/**/*.java
   
   # Run
   java -cp out main.Main
   ```
   
   Or run directly from your IDE:
   - Open project in IDE
   - Run `src/main/Main.java`

### Build Configuration
- **Game Scale**: 1.0 (can be adjusted in `game.java`)
- **Target FPS**: 120
- **Target UPS**: 120
- **Game Resolution**: 832x448 pixels (26x14 tiles)

---

## Project Structure

### Package Organization

```
src/
├── main/
│   ├── Main.java              # Entry point
│   ├── game.java              # Main game loop and state management
│   ├── game_panel.java        # Swing panel for rendering
│   └── window_of_game.java    # JFrame window setup
│
├── Entities/
│   ├── Entity.java            # Base entity class
│   ├── Player.java            # Player character
│   ├── Enemy.java             # Base enemy class
│   ├── Enemy1.java            # Crab enemy implementation
│   └── EnemyMangerclass.java  # Enemy manager
│
├── State/
│   ├── State.java             # Base state class
│   ├── Methods.java           # State interface
│   ├── GameState.java         # Game state enum
│   ├── MENU.java              # Main menu state
│   └── Playing.java           # Gameplay state
│
├── UI/
│   ├── MenuButton.java        # Menu button component
│   ├── Pause.java             # Pause menu
│   ├── PauseButton.java       # Base pause button
│   ├── Sound_Button.java      # Sound toggle button
│   ├── URMButtons.java        # URM (Unpause/Replay/Menu) buttons
│   └── VloumeButton.java      # Volume slider
│
├── Function/
│   ├── LoadSave.java          # Asset loading utilities
│   ├── StaticMethodsforMovement.java  # Collision and movement helpers
│   └── features.java          # Game constants and configurations
│
├── GameLevels/
│   ├── Level.java             # Level data structure
│   └── LevelManager.java      # Level rendering and management
│
└── Inputes_of_game/
    ├── keyboard_input.java    # Keyboard input handler
    └── mouse_input.java       # Mouse input handler
```

---

## Architecture

### Game Loop

The game uses a fixed timestep game loop running in a separate thread:

```java
// Update loop: 120 UPS (Updates Per Second)
if (deltaUpdate >= 1) {
    UpdateGame();
    deltaUpdate--;
}

// Render loop: 120 FPS (Frames Per Second)
if (deltaFrame >= 1) {
    Game_panel.repaint();
    deltaFrame--;
}
```

### State Management

The game uses a state pattern for managing different game screens:

- **MENU**: Main menu screen
- **PLAYING**: Active gameplay
- **OPTIONS**: Options menu (placeholder)
- **QUIT**: Exit game

States implement the `Methods` interface which defines:
- `update()` - Update game logic
- `draw(Graphics)` - Render graphics
- Input handlers (mouse/keyboard)

### Entity System

All game entities inherit from `Entity` base class:
- **Entity**: Base class with position, hitbox, and rendering
- **Player**: Extends Entity, adds movement, combat, health
- **Enemy**: Extends Entity, adds AI, combat, health
- **Enemy1**: Specific enemy implementation (Crab)

---

## Class Documentation

### Core Classes

#### `game.java`
Main game class managing the game loop and state switching.

**Key Methods:**
- `run()` - Main game loop thread
- `UpdateGame()` - Updates current game state
- `RenderGraphicsGame(Graphics)` - Renders current state

#### `Player.java`
Player character with movement, combat, and health systems.

**Key Methods:**
- `UpdatePlayer()` - Updates player state
- `takeDamage(int)` - Applies damage to player
- `getAttackHitbox()` - Returns attack collision box
- `reset()` - Resets player for replay
- `isAlive()` - Checks if player is alive
- `isDead()` - Checks if player is dead

**Key Properties:**
- `currentHealth` - Current health (0-100)
- `maxHealth` - Maximum health (100)
- `isDead` - Death state flag
- `attacking` - Attack state flag

#### `Enemy.java` (Abstract)
Base enemy class with AI, combat, and health systems.

**Key Methods:**
- `loadLevelData(int[][])` - Loads level collision data
- `setPlayer(Player)` - Sets player reference for AI
- `canSeePlayer()` - Checks if player is in vision range
- `canAttackPlayer()` - Checks if player is in attack range
- `takeDamage(int)` - Applies damage to enemy
- `getAttackHitbox()` - Returns attack collision box

**Key Properties:**
- `visionRange` - Detection range (200 pixels)
- `attackRange` - Attack range (30 pixels)
- `currentHealth` - Current health (0-50)
- `isDead` - Death state flag

#### `Enemy1.java`
Crab enemy implementation with patrolling and chasing AI.

**Key Methods:**
- `update()` - Updates enemy state and AI
- `updatePosition()` - Handles movement and AI decisions
- `updateCombat()` - Updates combat cooldowns

#### `EnemyMangerclass.java`
Manages all enemies in the game.

**Key Methods:**
- `update()` - Updates all enemies
- `draw(Graphics, int)` - Renders all enemies
- `checkPlayerAttack(Rectangle2D.Float)` - Checks player attacks
- `checkEnemyAttacks(Player)` - Checks enemy attacks on player
- `resetEnemies()` - Resets all enemies for replay

#### `Playing.java`
Main gameplay state managing player, enemies, and level.

**Key Methods:**
- `update()` - Updates game state
- `draw(Graphics)` - Renders game
- `checkCombat()` - Handles combat collisions
- `resetGame()` - Resets game for replay
- `drawHealthBar(Graphics)` - Renders health bar
- `drawGameOver(Graphics)` - Renders game over screen

#### `LoadSave.java`
Utility class for loading game assets.

**Key Methods:**
- `GetAtlas(String)` - Loads image from resources
- `GetLevelData()` - Loads level collision data
- `getEnemyCrab()` - Creates enemies from level data

#### `StaticMethodsforMovement.java`
Static utility methods for collision detection.

**Key Methods:**
- `CanMove(float, float, float, float, int[][])` - Checks if entity can move
- `IsSolid(float, float, int[][])` - Checks if position is solid
- `OnFloor(Rectangle2D.Float, int[][])` - Checks if entity is on floor
- `PositionNextToWall(Rectangle2D.Float, float)` - Calculates wall collision position
- `CheckUnderEnvironmentorAbove(Rectangle2D.Float, float)` - Calculates floor/ceiling collision

---

## Game States

### MENU State
Main menu screen with three buttons:
- **Play** - Starts new game (resets game state)
- **Options** - Opens options menu (placeholder)
- **Quit** - Exits game

### PLAYING State
Active gameplay state with:
- Player movement and combat
- Enemy AI and combat
- Level rendering
- Health bar
- Pause menu (ESC key)
- Game Over screen (when player dies)

### State Transitions
```
MENU → PLAYING (Play button)
PLAYING → MENU (Game Over or Menu button in pause)
PLAYING → PLAYING (Pause/Unpause with ESC)
```

---

## Combat System

### Player Attacks

**Attack Trigger:**
- Left Mouse Button pressed

**Attack Hitbox:**
- Width: 40 pixels
- Height: Player height
- Position: Extends in front of player (left or right based on facing direction)

**Attack Damage:**
- 10 damage per hit

**Attack Animation:**
- 3 frames
- Plays once per attack

### Enemy Attacks

**Attack Trigger:**
- Player within 30 pixels
- Attack cooldown expired (90 frames)

**Attack Hitbox:**
- Width: 30 pixels
- Height: Enemy height
- Position: Extends in front of enemy (based on facing direction)

**Attack Damage:**
- 5 damage per hit

**Attack Animation:**
- 7 frames
- Plays during attack state

### Combat Detection

Combat is checked every frame in `Playing.checkCombat()`:
1. Check player attack hitbox against all enemies
2. Check all enemy attack hitboxes against player
3. Apply damage if hitboxes intersect
4. Respect invincibility cooldowns

---

## Enemy AI

### AI States

#### 1. Patrolling (Default)
- Moves back and forth
- Changes direction on wall collision or cliff edge
- Checks floor ahead before moving

#### 2. Chasing (Player Detected)
- Player within 200 pixels (vision range)
- Faces player direction
- Moves toward player
- Still respects floor and wall collisions

#### 3. Attacking (Player in Range)
- Player within 30 pixels (attack range)
- Enters ATTACK state
- Attack cooldown prevents spam
- Returns to chasing after attack

#### 4. Hit (Taking Damage)
- Brief stun state
- Plays HIT animation (4 frames)
- Returns to previous state after cooldown

#### 5. Dead
- Health reaches 0
- Plays DEAD animation (5 frames)
- Stops at last frame
- No movement or attacks

### AI Decision Flow

```
Update Loop:
1. Check if dead → Stop all actions
2. Check if can see player → Enter chase mode
3. Check if can attack player → Enter attack mode
4. Otherwise → Continue patrolling
```

---

## Level System

### Level Data Format

Levels are defined using image files where:
- **Red Channel**: Tile type (0-47 = tile sprite index, 11 = air)
- **Green Channel**: Enemy spawn points (value = ENEMY_1 constant)
- **Blue Channel**: (Unused)

### Level Loading

1. Load level image (`level_one_data_long.png`)
2. Extract red channel for tile data
3. Extract green channel for enemy positions
4. Create collision map from tile data
5. Spawn enemies at marked positions

### Tile Rendering

- Tiles are rendered from sprite sheet (`outside_sprites.png`)
- 48 different tile sprites (4 rows × 12 columns)
- Each tile is 32×32 pixels
- Level scrolls based on camera position

### Collision System

- Tile value 11 = Air (passable)
- All other values = Solid (collision)
- Collision checked at 4 corners of entity hitbox
- Floor detection checks 1 pixel below entity

---

## Development Notes

### Code Style
- Package naming: lowercase (e.g., `Entities`, `State`)
- Class naming: PascalCase (e.g., `Player`, `Enemy1`)
- Method naming: camelCase (e.g., `UpdatePlayer()`, `takeDamage()`)
- Constants: UPPER_SNAKE_CASE (e.g., `MAX_HEALTH`, `TILE_SIZE`)

### Performance Considerations
- Game loop runs at 120 FPS/UPS
- Double buffering for smooth rendering
- Efficient collision detection using tile-based system
- Enemy updates only when in active area

### Known Limitations
- Fixed game resolution (832×448)
- Single level (no level progression)
- No save system
- Options menu not implemented
- Sound system placeholder (buttons exist but no audio)

### Future Improvements
- Multiple levels
- Save/Load system
- Sound effects and music
- More enemy types
- Power-ups and collectibles
- Score system
- Difficulty levels

---

## Troubleshooting

### Common Issues

**Game won't start:**
- Check that all resource files are in `Resource/` folder
- Verify Java version (JDK 8+)
- Check console for error messages

**Player falls through floor:**
- Verify level data image is loaded correctly
- Check collision detection in `StaticMethodsforMovement`

**Enemies not spawning:**
- Check green channel values in level image
- Verify `ENEMY_1` constant matches level data

**Controls not working:**
- Ensure game panel has focus (click on game window)
- Check input handlers are properly attached

---

## Credits

- **Game Engine**: Custom Java Swing implementation
- **Sprite Assets**: Custom/Provided sprite sheets
- **Architecture**: State pattern, Entity-Component system

---

## License

[Specify your license here]

---

## Contact

[Add contact information if needed]

---

**Last Updated**: 2024
**Version**: 1.0
