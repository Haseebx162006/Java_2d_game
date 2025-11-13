## HFS Game – Code Flow Documentation

### Overview
This project is a simple Java Swing game skeleton with a fixed-step update loop and a Swing-rendered panel. A sprite sheet (`p2.png`) is used to animate a player character that can move with WASD keys.

### Project Structure
- `src/main/Main.java`: Application entry point.
- `src/main/game.java`: Game bootstrap and main loop (updates and frames per second).
- `src/main/window_of_game.java`: Creates the main `JFrame`, attaches the game panel.
- `src/main/game_panel.java`: Core game surface; handles state, updates, rendering, input hookup, and animations.
- `src/Inputes_of_game/keyboard_input.java`: Keyboard controls (W/A/S/D), sets direction and movement state.
- `src/Inputes_of_game/mouse_input.java`: Mouse listener stubs (currently logs clicks).
- `src/Function/features.java`: Constants and helpers for player states and directions.
- `Resource/p2.png`: Sprite sheet loaded from the classpath at `/p2.png`.

### High-Level Flow
1. `Main.main(...)` instantiates `game`.
2. `game` constructs a `game_panel`, then a `window_of_game` with that panel, requests focus, and starts the game loop thread.
3. `window_of_game` creates a `JFrame`, packs it, and adds a new `game_panel` instance to it (note: this creates a second panel; see Notes below).
4. `game_panel` initializes input listeners, loads the sprite sheet, slices animations, sets preferred size, and starts a small Swing `Timer` that triggers `repaint()`.
5. The game loop in `game.run()` ticks:
   - Updates are targeted at 200 UPS via `UpdateGame()` → `game_panel.updategame()`.
   - Renders are targeted at 120 FPS via `Game_panel.repaint()`.
6. `game_panel.paintComponent(...)` clears, calls `updategame()` again, and draws the current animation frame at `(xaxis, yaxis)`.

### Update Loop Details
- `game.run()` maintains two accumulators:
  - `deltaUpdate`: when ≥ 1, calls `UpdateGame()` and decrements.
  - `deltaFrame`: when ≥ 1, calls `repaint()` and decrements.
- `UpdateGame()` delegates to `game_panel.updategame()`.

### `game_panel` Responsibilities
- Input hookup: adds `keyboard_input` and `mouse_input` listeners.
- Asset loading: `importImage()` loads `/p2.png` from the classpath.
- Animation prep: `load_Animations()` slices the sprite sheet into a 2D array `[move][frame]` using 64×40 subimages.
- Sizing: `Set_sizeof_Panel()` sets preferred size to 1280×800.
- Update pipeline in `updategame()` (order):
  1. `updateAnimation()` increments frame index at `Animation_Speed` ticks, using `features.player_features.GetPlayerMove(...)` for each move’s frame count.
  2. `setAnimation()` sets `Playermove` to `RUNNING` if moving, else `STANDING`.
  3. `UpdatePosition()` applies direction to `(xaxis, yaxis)` when moving.
- Rendering: `paintComponent(...)` draws `Animation[Playermove][Animation_index]` scaled to 128×96 at the current position.

### Input Handling
- Keyboard (`keyboard_input`):
  - `W` → `setdirection(UP)`
  - `A` → `setdirection(LEFT)`
  - `S` → `setdirection(DOWN)`
  - `D` → `setdirection(RIGHT)`
  - On key release of any movement key → `setMoving(false)` to stop movement.
- Mouse (`mouse_input`):
  - Emits a console log on click; other handlers are placeholders.

### Features and Directions
- `features.player_features` defines move/state constants (`STANDING`, `RUNNING`, etc.) and `GetPlayerMove(int)` which returns the number of frames for the current state.
- `features.PlayerDirectons` defines `LEFT`, `RIGHT`, `UP`, `DOWN` used by input and movement.

### Assets and Classpath
- The image is fetched via `getResourceAsStream("/p2.png")`. Ensure `p2.png` is on the runtime classpath root (e.g., put it under `Resource/` and configure your build to place it at the classpath root or copy it to `src/main/resources` if you restructure to Maven/Gradle).

### Notes and Potential Improvements
- Two `game_panel` instances: `game` creates one and passes it to `window_of_game`, but `window_of_game` currently constructs a new panel internally. Prefer adding the provided panel to the frame to keep a single source of truth for input focus and state.
- Double updates per frame: `paintComponent(...)` calls `updategame()` while the game loop also updates at 200 UPS. Consider removing the update call from `paintComponent` and relying solely on the game loop for updates.
- Fixed timestep/rendering: The loop uses nanos and accumulators; if you need deterministic physics, keep updates constant and render interpolated positions.
- Focus handling: `game_panel` calls `setFocusable(true)` and requests focus; ensure the instance attached to the frame receives focus.

### How to Run
1. Build the project (ensure `p2.png` is on the classpath at `/p2.png`).
2. Run `Main.main`. A window should appear with the animated sprite that moves with WASD.

### Quick Reference – Control Flow
- Entry: `Main` → `new game()`
- Boot: `game` → `new game_panel()` and `new window_of_game(panel)` → starts game thread
- Loop: `game.run()` → `UpdateGame()` and `repaint()` at target rates
- Update: `game_panel.updategame()` → animation, state, position
- Render: `game_panel.paintComponent()` → draw current frame
- Input: `keyboard_input`/`mouse_input` → `game_panel.setdirection(...)` and `setMoving(false)`


