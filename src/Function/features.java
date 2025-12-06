package Function;


import main.game;


public  class features {
    public static final float GRAVITY = 0.04f * game.SCALE;
    public static final int ANI_SPEED = 25;
    public static class background{
        public static final int SMALL_CLOUD_WIDTH_DEFAULT=74;
        public static final int SMALL_CLOUD_HEIGHT_DEFAULT=24;
        public static final int BIG_CLOUD_WIDTH_DEFAULT=448;
        public static final int BIG_CLOUD_HEIGHT_DEFAULT=101;
        public static final int BIG_CLOUD_HEIGHT=(int)(BIG_CLOUD_HEIGHT_DEFAULT*game.SCALE);
        public static final int BIG_CLOUD_WIDTH=(int)(BIG_CLOUD_WIDTH_DEFAULT*game.SCALE);
        public static final int SMALL_CLOUD_HEIGHT=(int)(SMALL_CLOUD_HEIGHT_DEFAULT*game.SCALE);
        public static final int SMALL_CLOUD_WIDTH=(int)(SMALL_CLOUD_WIDTH_DEFAULT*game.SCALE);

    }

    public static class Objects{
        public static final int RED_POTION = 0;
        public static final int BLUE_POTION = 1;
        public static final int BARREL = 2;
        public static final int BOX = 3;
        public static final int SPIKE = 4;
        public static final int CANNON_LEFT = 5;
        public static final int CANNON_RIGHT = 6;
        public static final int TREE_ONE = 7;
        public static final int TREE_TWO = 8;
        public static final int TREE_THREE = 9;



        public static final int RED_POTION_VALUE = 15;
        public static final int BLUE_POTION_VALUE = 10;

        public static final int CONTAINER_WIDTH_DEFAULT = 40;
        public static final int CONTAINER_HEIGHT_DEFAULT = 30;
        public static final int CONTAINER_WIDTH = (int) (game.SCALE * CONTAINER_WIDTH_DEFAULT);
        public static final int CONTAINER_HEIGHT = (int) (game.SCALE * CONTAINER_HEIGHT_DEFAULT);

        public static final int POTION_WIDTH_DEFAULT = 12;
        public static final int POTION_HEIGHT_DEFAULT = 16;
        public static final int POTION_WIDTH = (int) (game.SCALE * POTION_WIDTH_DEFAULT);
        public static final int POTION_HEIGHT = (int) (game.SCALE * POTION_HEIGHT_DEFAULT);

        public static final int CANNON_WIDTH_DEFAULT = 40;
        public static final int CANNON_HEIGHT_DEFAULT = 26;
        public static final int CANNON_WIDTH = (int) (CANNON_WIDTH_DEFAULT * game.SCALE);
        public static final int CANNON_HEIGHT = (int) (CANNON_HEIGHT_DEFAULT * game.SCALE);

        public static final int SPIKE_WIDTH_DEFAULT = 32;
        public static final int SPIKE_HEIGHT_DEFAULT = 32;
        public static final int SPIKE_WIDTH = (int) (game.SCALE * SPIKE_WIDTH_DEFAULT);
        public static final int SPIKE_HEIGHT = (int) (game.SCALE * SPIKE_HEIGHT_DEFAULT);

        public static int GetSpriteAmount(int object_type) {
            switch (object_type) {
                case RED_POTION, BLUE_POTION:
                    return 7;
                case BARREL, BOX:
                    return 8;
                case CANNON_LEFT, CANNON_RIGHT:
                    return 7;
            }
            return 1;
        }
    }
    public static class Projectiles{
        public static final int CANNON_BALL_DEFAULT_WIDTH = 15;
        public static final int CANNON_BALL_DEFAULT_HEIGHT = 15;

        public static final int CANNON_BALL_WIDTH = (int)(game.SCALE * CANNON_BALL_DEFAULT_WIDTH);
        public static final int CANNON_BALL_HEIGHT = (int)(game.SCALE * CANNON_BALL_DEFAULT_HEIGHT);
        public static final float SPEED = 0.75f * game.SCALE;
    }
    public static class UI{
        public static class BUTTON{
            public static final int BUTTON_WIDTH_DEFAULT=140;
            public static final int BUTTON_HEIGHT_DEFAULT=56;
            public static final int BUTTON_WIDTH= (int)(BUTTON_WIDTH_DEFAULT* game.SCALE);
            public static final int BUTTON_HEIGHT= (int)(BUTTON_HEIGHT_DEFAULT* game.SCALE);
        }
        public static class PauseButtons{
            public static final int SOUND_BUTTON_DEFAULT=42;
            public static final int SOUND_BUTTON=(int)(SOUND_BUTTON_DEFAULT*game.SCALE);
        }
        public static class URM{
            public static final int URM_BUTTON_DEFAULT=56;
            public static final int URM_BUTTON=(int)(URM_BUTTON_DEFAULT*game.SCALE);
        }
        public static class VOLUME{
            public static final int VOLUME_WIDTH_DEFAULT=28;
            public static final int VOLUME_HEIGHT_DEFAULT=44;
            public static final int SLIDER_WIDTH_DEFAULT=215;
            public static final int VOLUME_HEIGHT=(int)(VOLUME_HEIGHT_DEFAULT*game.SCALE);
            public static final int VOLUME_WIDTH=(int)(VOLUME_WIDTH_DEFAULT*game.SCALE);
            public static final int SLIDER_WIDTH=(int)(SLIDER_WIDTH_DEFAULT*game.SCALE);
        }
        public  static class Enemies{
            public static final int ENEMY_1=0;
            public static final int PINKSTAR=1;
            public static final int SHARK = 2;
            public static  final int IDLE=0;
            public static  final int RUNNING=1;
            public static  final int ATTACK=2;
            public static  final int HIT=3;
            public static  final int DEAD=4;

            public static final int ENEMY1_WIDTH_DEFAULT=72;
            public static final int ENEMY1_HEIGHT_DEFAULT=30;

            public static final int ENEMY1_WIDTH=(int)(ENEMY1_WIDTH_DEFAULT*game.SCALE);
            public static final int ENEMY1_HEIGHT=(int)(ENEMY1_HEIGHT_DEFAULT*game.SCALE);
            public static final int ENEMY1_X=(int)(26*game.SCALE);
            public static final int ENEMY1_Y=(int)(9*game.SCALE);
            public static final int SHARK_WIDTH_DEFAULT = 34;
            public static final int SHARK_HEIGHT_DEFAULT = 30;
            public static final int SHARK_WIDTH = (int) (SHARK_WIDTH_DEFAULT * game.SCALE);
            public static final int SHARK_HEIGHT = (int) (SHARK_HEIGHT_DEFAULT * game.SCALE);
            public static final int SHARK_DRAWOFFSET_X = (int) (8 * game.SCALE);
            public static final int SHARK_DRAWOFFSET_Y = (int) (6 * game.SCALE);

            public static int SPRITE(int enemy_type, int enemy_state) {
                switch (enemy_state) {
                    case IDLE:
                        if (enemy_type == ENEMY_1)
                            return 9;
                        else if (enemy_type == PINKSTAR || enemy_type == SHARK)
                            return 8;
                        return 0;
                    case RUNNING:
                        return 6;
                    case ATTACK:
                        if (enemy_type == SHARK)
                            return 8;
                        return 7;
                    case HIT:
                        return 4;
                    case DEAD:
                        return 5;
                }

                return 0;
            }

        }
    }
    public static class player_features{
        public static final int STANDING=0;
        public static final int RUNNING=1;
        public static final int JUMPING=2;
        public static final int FALLING=3;
        public static final int GROUND=4;
        public static final int HIT=5;
        public static final int ATTACK=6;
        public static final int ATTACK_JUMP1=7;
        public static final int ATTACK_JUMP2=8;

        public static int GetPlayerMove(int PlayerMove){
            switch (PlayerMove){
                case STANDING:
                    return 5;
                case RUNNING:
                    return 6;
                case HIT:
                    return 4;
                case ATTACK:
                case ATTACK_JUMP1:
                case ATTACK_JUMP2:
                    return 3;
                case GROUND:
                    return 2;
                case FALLING:
                default:
                    return 1;
            }
        }

    }
    public static class PlayerDirectons{
        public static final int LEFT=0;
        public static final int RIGHT=2;
        public static final int UP=1;
        public static final int DOWN=3;
    }
}
