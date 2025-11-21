package Function;

import main.game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class LoadSave {
    public static final String PlayerImgAddress="p2.png";
    public static final String OutsideImgLevelAddress="outside_sprites.png";
   // public static final String LevelOneImageData="level_one_data.png";
    public static final String LevelOneImageData="level_one_data_long.png";
    public static final String ButtonImages="button_atlas.png";
    public static final String Button_Background="menu_background.png";
    public static final String PAUSE="pause_menu.png";
    public static final String SOUND_BUTTON="sound_button.png";
    public static final String URM_BUTTON="urm_buttons.png";
    public static final String VOLUME_BUTTON="volume_buttons.png";
    public static final String BACKGROUND_IMAGE="playing_bg_img.png";
    public static final String BIG_CLOUD="big_clouds.png";
    public static final String SMALL_CLOUD="small_clouds.png";
    public static final  String PAUSE_BACKGROUND="menu_bg.jpg";
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
    public static int[][] GetLevelData(){
       BufferedImage img=GetAtlas(LevelOneImageData);
       int[][] lvldata= new int[img.getHeight()][img.getWidth()];
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color color= new Color(img.getRGB(j,i));
                int value=color.getRed();
                if (value>=48){
                    value=0;
                }
                lvldata[i][j]=value;
            }
        }
        return lvldata;
    }
}
