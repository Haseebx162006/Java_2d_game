package Function;


import main.game;


public  class features {

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
            public static final int SPRITE(int enemy_type,int enemy_state){
                switch (enemy_type){
                    case ENEMY_1 :
                        switch (enemy_state){
                            case IDLE:
                                return 9;
                            case RUNNING:
                                return 6;
                            case ATTACK:
                                return 7;
                            case HIT:
                                return 4;
                            case DEAD:
                                return 5;
                        }
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
