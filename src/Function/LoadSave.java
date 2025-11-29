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
   public static BufferedImage GetAtlas(String fileName){
        BufferedImage img=null;
        InputStream is = LoadSave.class.getResourceAsStream("/"+fileName);
        try {
            img= ImageIO.read(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            try {
                is.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
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
        }

        File[] files = file.listFiles();
        File[] filesSorted = new File[files.length];

        for (int i = 0; i < filesSorted.length; i++)
            for (int j = 0; j < files.length; j++) {
                if (files[j].getName().equals((i + 1) + ".png"))
                    filesSorted[i] = files[j];

            }

        BufferedImage[] imgs = new BufferedImage[filesSorted.length];

        for (int i = 0; i < imgs.length; i++)
            try {
                imgs[i] = ImageIO.read(filesSorted[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }

        return imgs;
    }

    // Load level data (tiles) from a level image
    public static int[][] GetLevelData(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        int[][] levelData = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int value = img.getRGB(x, y);
                int red = (value >> 16) & 0xFF;
                // Clamp to valid tile range if needed
                if (red >= game.TILE_WIDTH * game.TILE_HEIGHT) {
                    red = 0;
                }
                levelData[y][x] = red;
            }
        }
        return levelData;
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
