package model;

public final class Tetris {
    //NR_OF_ROWS, NR_OF_COLUMNS and NR_OF_TETRIMINOS are all indexed beginning from 0
    public static final int NR_OF_ROWS = 14;
    public static final int NR_OF_COLUMNS = 9;
    public static final int NR_OF_TETRIMINOS = 7;
    public static int POINTS_PER_FINISHED_ROW = 100;
    public static double STARTING_GAME_LOOP_SPEED = 0.8d;
    public static double MAX_GAME_LOOP_SPEED = 0.2d;
    public static double SPEED_INCREASE_IN_PERCENT = 1.5d;
}
