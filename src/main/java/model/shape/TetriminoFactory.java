package model.shape;

import model.GameMode;
import model.Tetris;

import java.awt.Color;
import java.util.Random;

public class TetriminoFactory {
    private final static Random random = new Random();
    private static final Color[] colorPalette = {Color.BLUE, Color.CYAN, Color.RED, Color.YELLOW, Color.GREEN, Color.MAGENTA};
    private static int randomColor;

    private TetriminoFactory(){}

    public static Tetrimino getRandomTetrimino(GameMode gameMode) {
        randomColor = random.nextInt(colorPalette.length);
        switch(gameMode) {
            case EASY -> {
                switch (random.nextInt(3)) {
                    case 0: return getStraightTetrimino();
                    case 1: return getSquareTetrimino();
                    case 2: return getLTetrimino();
                }
            }
            case NORMAL -> {
                switch(random.nextInt(Tetris.NR_OF_TETRIMINOS)) {
                    case 0: return getStraightTetrimino();
                    case 1: return getSquareTetrimino();
                    case 2: return getTTetrimino();
                    case 3: return getLTetrimino();
                    case 4: return getSkewTetrimino();
                    case 5: return getMirroredLTetrimino();
                    case 6: return getMirroredSkewTetrimino();
                }
            }
            case HARD -> {
                switch(random.nextInt(6)) {
                    case 0: return getSquareTetrimino();
                    case 1: return getTTetrimino();
                    case 2: return getLTetrimino();
                    case 3: return getSkewTetrimino();
                    case 4: return getMirroredLTetrimino();
                    case 5: return getMirroredSkewTetrimino();
                }
            }
            case INSANE -> {
                switch(random.nextInt(2)) {
                    case 0: return getSkewTetrimino();
                    case 1: return getLTetrimino();
                }
            }
        }
        return new Tetrimino(new Coordinate(-99, -99),
                            new Coordinate(-99, -99),
                            new Coordinate(-99, -99),
                            new Coordinate(-99, -99));
    }

    public static Tetrimino getStraightTetrimino() {
        Coordinate straight1 = new Coordinate(3, 15);
        Coordinate straight2 = new Coordinate(4, 15);
        Coordinate straight3 = new Coordinate(5, 15);
        Coordinate straight4 = new Coordinate(6, 15);
        return new Tetrimino(straight1, straight2, straight3, straight4, straight2, colorPalette[randomColor]);
    }

    public static Tetrimino getSquareTetrimino() {
        Coordinate square1 = new Coordinate(3, 15);
        Coordinate square2 = new Coordinate(4, 15);
        Coordinate square3 = new Coordinate(3, 16);
        Coordinate square4 = new Coordinate(4, 16);
        return new Tetrimino(square1, square2, square3, square4, null, colorPalette[randomColor]);
    }

    public static Tetrimino getTTetrimino() {
        Coordinate tTetrimino1 = new Coordinate(4, 16);
        Coordinate tTetrimino2 = new Coordinate(3, 16);
        Coordinate tTetrimino3 = new Coordinate(5, 16);
        Coordinate tTetrimino4 = new Coordinate(4, 15);
        return new Tetrimino(tTetrimino1, tTetrimino2, tTetrimino3, tTetrimino4, tTetrimino1, colorPalette[randomColor]);
    }

    public static Tetrimino getLTetrimino() {
        Coordinate LTetrimino1 = new Coordinate(3, 17);
        Coordinate LTetrimino2 = new Coordinate(3, 16);
        Coordinate LTetrimino3 = new Coordinate(3, 15);
        Coordinate LTetrimino4 = new Coordinate(4, 15);
        return new Tetrimino(LTetrimino1, LTetrimino2, LTetrimino3, LTetrimino4, LTetrimino2, colorPalette[randomColor]);
    }

    public static Tetrimino getSkewTetrimino() {
        Coordinate skew1 = new Coordinate(3, 15);
        Coordinate skew2 = new Coordinate(4, 15);
        Coordinate skew3 = new Coordinate(4, 16);
        Coordinate skew4 = new Coordinate(5, 16);
        return new Tetrimino(skew1, skew2, skew3, skew4, skew3, colorPalette[randomColor]);
    }

    public static Tetrimino getMirroredLTetrimino() {
        Coordinate LTetrimino1 = new Coordinate(4, 17);
        Coordinate LTetrimino2 = new Coordinate(4, 16);
        Coordinate LTetrimino3 = new Coordinate(4, 15);
        Coordinate LTetrimino4 = new Coordinate(3, 15);
        return new Tetrimino(LTetrimino1, LTetrimino2, LTetrimino3, LTetrimino4, LTetrimino2, colorPalette[randomColor]);
    }

    public static Tetrimino getMirroredSkewTetrimino() {
        Coordinate skew1 = new Coordinate(5, 15);
        Coordinate skew2 = new Coordinate(4, 15);
        Coordinate skew3 = new Coordinate(4, 16);
        Coordinate skew4 = new Coordinate(3, 16);
        return new Tetrimino(skew1, skew2, skew3, skew4, skew3, colorPalette[randomColor]);
    }
}
