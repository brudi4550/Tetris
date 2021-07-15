package model.shape;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tetrimino extends Shape {

    public Tetrimino(Coordinate c1, Coordinate c2, Coordinate c3, Coordinate c4) {
        this(c1, c2, c3, c4, null, Color.BLACK);
    }

    public Tetrimino(Coordinate c1, Coordinate c2, Coordinate c3, Coordinate c4, Coordinate central) {
        this(c1, c2, c3, c4, central, Color.BLACK);
    }

    public Tetrimino(Coordinate c1, Coordinate c2, Coordinate c3, Coordinate c4, Coordinate central, Color color) {
        this(new ArrayList<>(Arrays.asList(c1, c2, c3, c4)), central, color);
    }

    private Tetrimino(List<Coordinate> coords, Coordinate central, Color color) {
        super(coords, central, color);
    }

    @Override
    public Tetrimino[] getSplitUpShapes() {
        List<Tetrimino> disconnected = new ArrayList<>();
        List<Coordinate> alreadyAdded = new ArrayList<>();
        for (Coordinate c : super.getCoordinates()) {
            if (!alreadyAdded.contains(c)) {
                List<Coordinate> coords = new ArrayList<>();
                coords = getConnected(c);
                Tetrimino splitTetrimino = new Tetrimino(coords, null, getColor());
                disconnected.add(splitTetrimino);
                alreadyAdded.addAll(coords);
            }
        }
        return disconnected.toArray(new Tetrimino[0]);
    }

    @Override
    public Tetrimino clone() {
        super.clone();
        List<Coordinate> cloneList = new ArrayList<>();
        for (Coordinate c : super.getCoordinates()) {
            cloneList.add(new Coordinate(c.getX(), c.getY()));
        }
        Coordinate central = super.getCentralCoordinate() == null ? null : super.getCentralCoordinate().clone();
        return new Tetrimino(cloneList, central, super.getColor());
    }
}
