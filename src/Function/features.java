package Function;


public  class features {
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
