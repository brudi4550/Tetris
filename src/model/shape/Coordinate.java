package model.shape;

public class Coordinate {
    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isDirectlyOver(Coordinate other) {
        if (this.getX() == other.getX() && this.getY()+1 == other.getY()) {
            return true;
        }
        return false;
    }

    public boolean isDirectlyUnder(Coordinate other) {
        if (this.getX() == other.getX() && this.getY()-1 == other.getY()) {
            return true;
        }
        return false;
    }

    public boolean isDirectlyLeftOf(Coordinate other) {
        if (this.getY() == other.getY() && this.getX() == other.getX()-1) {
            return true;
        }
        return false;
    }

    public boolean isDirectlyRightOf(Coordinate other) {
        if (this.getY() == other.getY() && this.getX() == other.getX()+1) {
            return true;
        }
        return false;
    }

    @Override
    public Coordinate clone() {
        return new Coordinate(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(Coordinate.class)) {
            Coordinate otherCoordinate = (Coordinate) obj;
            if (this.getX() == otherCoordinate.getX() && this.getY() == otherCoordinate.getY()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "X: "+this.x+", Y: "+this.y;
    }
}
