package Function;

import Entities.Enemy1;
import main.game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class LoadSave {
    public static final String PlayerImgAddress="p2.png";
    public static final String OutsideImgLevelAddress="outside_sprites.png";
   // public static final String LevelOneImageData="level_one_data.png";
    public static final String LevelOneImageData= "lvls/1.png";
    public static final String ButtonImages="button_atlas.png";
    public static final String Button_Background="menu_background.png";
    public static final String PAUSE="pause_menu.png";
    public static final String SOUND_BUTTON="sound_button.png";
    public static final String URM_BUTTON="urm_buttons.png";
    public static final String VOLUME_BUTTON="volume_buttons.png";
    public static final String BACKGROUND_IMAGE="playing_bg_img.png";
    public static final String BIG_CLOUD="big_clouds.png";
    public static final String SMALL_CLOUD="small_clouds.png";
    public static final String PAUSE_BACKGROUND="menu_bg.jpg";
    public static final String ENEMY_1_PNG ="enemy_1.png";
    public static final String LOAD_COMPLETE_LEVEL="completed_sprite.png";
    public static final String Potion="potions_sprites.png";
    public static final String OBJECT="objects_sprites.png";
    public static final String TRAPS_1="traps.png";
    public static final String CANNON="cannon.png";
    public static final String BALL="ball.png";
    public static final String WATER_BOTTOM = "water.png";
    public static final String SHIP = "ship.png";
    public static final String RAIN_PARTICLE = "rain_particle.png";
    public static final String WATER_TOP = "water_atlas_animation.png";
    public static final String GRASS_ATLAS = "grass_atlas.png";
    public static final String TREE_ONE_ATLAS = "tree_one_atlas.png";
    public static final String TREE_TWO_ATLAS = "tree_two_atlas.png";
    public static final String SHARK_PNG = "shark_atlas.png";
    public static final String PINKENEMY_PNG = "pinkstar_atlas.png";
    public static final String OPTIONS_IMG = "options_background.png";
   public static BufferedImage GetAtlas(String fileName){
        BufferedImage img=null;
        InputStream is = LoadSave.class.getResourceAsStream("/"+fileName);
        if (is == null) {
            throw new RuntimeException("Resource not found: /" + fileName + ". Please ensure the file exists in the resources folder.");
        }
        try {
            img= ImageIO.read(is);
        } catch (Exception e) {
            throw new RuntimeException("Error reading image file: /" + fileName, e);
        }finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                throw new RuntimeException("Error closing input stream for: /" + fileName, e);
            }
        }
        return img;
    }
    public static  BufferedImage[] getAll(){
        URL url = LoadSave.class.getResource("/lvls");
        File file = null;

        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return new BufferedImage[0];
        }

        if (file == null || !file.exists() || !file.isDirectory()) {
            System.err.println("Level directory not found: /lvls");
            return new BufferedImage[0];
        }

        File[] files = file.listFiles();
        if (files == null) {
            return new BufferedImage[0];
        }

        // Find the maximum level number by checking all files
        int maxLevel = 0;
        for (File f : files) {
            if (f.isFile() && f.getName().endsWith(".png")) {
                try {
                    String name = f.getName().replace(".png", "");
                    int levelNum = Integer.parseInt(name);
                    if (levelNum > maxLevel) {
                        maxLevel = levelNum;
                    }
                } catch (NumberFormatException e) {
                    // Skip files that don't match the pattern
                }
            }
        }

        if (maxLevel == 0) {
            System.err.println("No valid level files found in /lvls");
            return new BufferedImage[0];
        }

        // Create a map to quickly find files by name
        HashMap<String, File> fileMap = new HashMap<>();
        for (File f : files) {
            if (f.isFile() && f.getName().endsWith(".png")) {
                fileMap.put(f.getName(), f);
            }
        }

        // Load all levels from 1 to maxLevel in order
        java.util.ArrayList<BufferedImage> levelList = new java.util.ArrayList<>();
        
        for (int i = 1; i <= maxLevel; i++) {
            String fileName = i + ".png";
            File levelFile = fileMap.get(fileName);
            
            if (levelFile != null && levelFile.exists()) {
                try {
                    BufferedImage img = ImageIO.read(levelFile);
                    if (img != null) {
                        levelList.add(img);
                        System.out.println("Successfully loaded level: " + fileName);
                    } else {
                        System.err.println("Failed to read level file (returned null): " + fileName);
                    }
                } catch (IOException e) {
                    System.err.println("Error reading level file: " + fileName);
                    e.printStackTrace();
                }
            } else {
                System.err.println("Level file not found: " + fileName);
            }
        }

        // Convert ArrayList to array
        BufferedImage[] imgs = new BufferedImage[levelList.size()];
        levelList.toArray(imgs);

        System.out.println("Total levels loaded: " + imgs.length + " out of " + maxLevel + " expected");
        return imgs;
    }

    // Load enemy positions from all level images
    public static ArrayList<Enemy1> getEnemyCrab() {
        ArrayList<Enemy1> enemies = new ArrayList<>();
        BufferedImage[] levelImages = getAll();
        for (BufferedImage img : levelImages) {
            enemies.addAll(getEnemyCrab(img));
        }
        return enemies;
    }

    // Load enemy positions from a single level image
    public static ArrayList<Enemy1> getEnemyCrab(BufferedImage img) {
        ArrayList<Enemy1> enemies = new ArrayList<>();
        int width = img.getWidth();
        int height = img.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int value = img.getRGB(x, y);
                int red = (value >> 16) & 0xFF;
                // Use a specific red value (e.g., 1) to mark enemy spawn tiles
                if (red == 1) {
                    float worldX = x * game.TILE_SIZE;
                    float worldY = y * game.TILE_SIZE;
                    enemies.add(new Enemy1(worldX, worldY));
                }
            }
        }
        return enemies;
    }

}
