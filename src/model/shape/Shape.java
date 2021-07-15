package model.shape;

import model.Direction;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a 2D shape in a regular coordinate system.
 * Shape doesn't have to be connected.
 * Takes in a list of coordinates the shape should consist of.
 * If you want to rotate the shape, specify a central coordinate,
 * around which it should rotate.
 * */
public class Shape implements Cloneable {
    private List<Coordinate> coordinates;
    private Coordinate centralCoordinate;
    private Color color;

    public Shape() {
        this(new ArrayList<>(), null, Color.BLACK);
    }

    public Shape(List<Coordinate> coordinates) {
        this(coordinates, null, Color.BLACK);
    }

    public Shape(List<Coordinate> coordinates, Color color) {
        this(coordinates, null, color);
    }

    public Shape(List<Coordinate> coordinates, Coordinate central, Color color) {
        this.color = color;
        this.coordinates = coordinates;
        this.centralCoordinate = central;
    }

    /**
     * Moves the shape on the X-axis
     * @param steps amount of steps the shape should make
     * */
    public void moveX(int steps) {
        coordinates.forEach(c -> c.setX(c.getX()+steps));
    }

    /**
     * Moves the shape on the Y-Axis
     * @param steps amount of steps the shape should make
     */
    public void moveY(int steps) {
        coordinates.forEach(c -> c.setY(c.getY()+steps));
    }

    /**
     * Rotates the shape. If no central coordinate is specified,
     * the shape will not rotate.
     * @param direction either LEFT or RIGHT
     * */
    public void rotate(Direction direction) {
        if (direction == Direction.DOWN || direction == null || centralCoordinate == null) return;
        int xOther, yOther, xDistance, yDistance, stepsX, stepsY;
        int xCentral = centralCoordinate.getX();
        int yCentral = centralCoordinate.getY();
        for (Coordinate c : coordinates) {
            if (!c.equals(centralCoordinate)) {
                xOther = c.getX();
                yOther = c.getY();
                xDistance = xOther - xCentral;
                yDistance = yOther - yCentral;
                if (direction == Direction.LEFT) {
                    stepsX = -yDistance;
                    stepsY = xDistance;
                } else {
                    stepsX = yDistance;
                    stepsY = -xDistance;
                }
                c.setX(xCentral + stepsX);
                c.setY(yCentral + stepsY);
            }
        }
    }

    /**
     * Checks if the shape is split up (i.e. some coordinates
     * don't have a direct neighbour any longer) and returns a list of shapes that
     * the whole shape object is made up of.
     * @return a list of Shapes*/
    public Shape[] getSplitUpShapes() {
        List<Shape> disconnected = new ArrayList<>();
        List<Coordinate> alreadyAdded = new ArrayList<>();
        for (Coordinate c : coordinates) {
            if (!alreadyAdded.contains(c)) {
                List<Coordinate> coords = new ArrayList<>();
                coords = getNeighbouringGroup(c, coords);
                Shape splitShape = new Shape(coords, null, getColor());
                disconnected.add(splitShape);
                alreadyAdded.addAll(coords);
            }
        }
        return disconnected.toArray(new Shape[0]);
    }

    /**
     * Uses getNeighbouringGroup to collect all coordinates ,that
     * have a direct connection to the coordinate provided, into a new list.
     * @param c the coordinate you want the direct neighbours of.
     * @return a list of connected coordinates*/
    public List<Coordinate> getConnected(Coordinate c) {
        return getNeighbouringGroup(c, new ArrayList<>());
    }

    /**
     * Recursively gets all coordinates that have a direct connection to the coordinate provided
     * within the Tetrimino.
     * @param current the coordinate you want the direct neighbours of.
     * @param list an empty list where all neighbours can be put in.
     * @return a list of coordinates*/
    public List<Coordinate> getNeighbouringGroup(Coordinate current, List<Coordinate> list) {
        list.add(current);
        for (Coordinate other : coordinates) {
            if (!list.contains(other) && !current.equals(other)) {
                if (other.isDirectlyOver(current) || other.isDirectlyUnder(current)
                        || other.isDirectlyLeftOf(current) || other.isDirectlyRightOf(current)) {
                    getNeighbouringGroup(other, list);
                }
            }
        }
        return list;
    }

    /**
     * Checks if the shape overlaps with shape provided at any coordinate.
     * @param other the shape you want to compare your shape object to
     * @return true if the shape overlaps at any coordinate with the shape object provided.
     * */
    public boolean overlapsWith(Shape other) {
        for (Coordinate thisC : this.coordinates) {
            for (Coordinate otherC : other.getCoordinates()) {
                if (thisC.getX() == otherC.getX() && thisC.getY() == otherC.getY()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if any of the coordinates the shape consists of are
     * out of the Bound provided.
     * @param xUpper the max X value any of the coordinates should have
     * @param xLower the min X value any of the coordinates should have
     * @param yUpper the max Y value any of the coordinates should have
     * @param yLower the min Y value any of the coordinates should have
     * */
    public boolean isOutOfBound(int xUpper, int xLower, int yUpper, int yLower) {
        for (Coordinate c : coordinates) {
            int cX = c.getX(), cY = c.getY();
            if (cX > xUpper || cX < xLower || cY > yUpper || cY < yLower) {
                return true;
            }
        }
        return false;
    }

    /** Creates a deep clone of the Shape object.
     * @return a deep clone of the Shape object.*/
    @Override
    public Shape clone() {
        try {
            super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        List<Coordinate> cloneList = new ArrayList<>();
        for (Coordinate c : coordinates) {
            cloneList.add(new Coordinate(c.getX(), c.getY()));
        }
        Coordinate central = centralCoordinate == null ? null : centralCoordinate.clone();
        return new Shape(cloneList, central, color);
    }

    public int getWidth() {
        return getMaxX() - getMinX() + 1;
    }

    public int getHeight() {
        return getMaxY() - getMinY() + 1;
    }

    public int getMaxX() {
        int maxX = Integer.MIN_VALUE;
        for (Coordinate c : coordinates) {
            if (c.getX() > maxX) maxX = c.getX();
        }
        return maxX;
    }

    public int getMinX() {
        int minX = Integer.MAX_VALUE;
        for (Coordinate c : coordinates) {
            if (c.getX() < minX) minX = c.getX();
        }
        return minX;
    }

    public int getMaxY() {
        int maxY = Integer.MIN_VALUE;
        for (Coordinate c : coordinates) {
            if (c.getY() > maxY) maxY = c.getY();
        }
        return maxY;
    }

    public int getMinY() {
        int minY = Integer.MAX_VALUE;
        for (Coordinate c : coordinates) {
            if (c.getY() < minY) minY = c.getY();
        }
        return minY;
    }

    /**
     * Returns a two dimensional boolean array that is the minimum size
     * needed to represent the shape
     * @return a two dimensional boolean array*/
    public boolean[][] getBooleanRepresentation() {
        int minX = getMinX();
        int maxY = getMaxY();
        boolean[][] array = new boolean[getHeight()][getWidth()];
        for (Coordinate c : coordinates) {
            array[maxY - c.getY()][c.getX() - minX] = true;
        }
        return array;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public Coordinate getCentralCoordinate() {
        return centralCoordinate;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        StringBuilder strb = new StringBuilder();
        for (int i = 0; i < coordinates.size(); i++) {
            if (i == coordinates.size() - 1) {
                Coordinate c = coordinates.get(i);
                strb.append("Coordinate "+i+" at: X = "+c.getX()+", Y = "+c.getY());
                break;
            }
            Coordinate c = coordinates.get(i);
            strb.append("Coordinate "+i+" at: X = "+c.getX()+", Y = "+c.getY()+"\n");
        }
        return strb.toString();
    }
}
