package model;

public class TetrisInput {
    private boolean moveLeft;
    private boolean moveRight;
    private boolean moveUp;
    private boolean moveDown;
    private boolean moveDownInstant;
    private boolean rotateLeft;
    private boolean rotateRight;

    public boolean moveLeft() {
        return moveLeft;
    }

    public void setMoveLeft(boolean moveLeft) {
        this.moveLeft = moveLeft;
    }

    public boolean moveRight() {
        return moveRight;
    }

    public void setMoveRight(boolean moveRight) {
        this.moveRight = moveRight;
    }

    public boolean moveUp() {
        return moveUp;
    }

    public void setMoveUp(boolean moveUp) {
        this.moveUp = moveUp;
    }

    public boolean moveDown() {
        return moveDown;
    }

    public void setMoveDown(boolean moveDown) {
        this.moveDown = moveDown;
    }

    public boolean moveDownInstant() {
        return moveDownInstant;
    }

    public void setMoveDownInstant(boolean moveDownInstant) {
        this.moveDownInstant = moveDownInstant;
    }

    public boolean rotateLeft() {
        return rotateLeft;
    }

    public void setRotateLeft(boolean rotateLeft) {
        this.rotateLeft = rotateLeft;
    }

    public boolean rotateRight() {
        return rotateRight;
    }

    public void setRotateRight(boolean rotateRight) {
        this.rotateRight = rotateRight;
    }

    public void clearInputs() {
        moveDown = false;
        moveUp = false;
        moveLeft = false;
        moveRight = false;
        rotateRight = false;
        rotateLeft = false;
    }
}
