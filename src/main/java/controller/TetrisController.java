package controller;

import model.*;
import UI.*;
import javax.swing.SwingUtilities;

public class TetrisController {
    private final TetrisModel model;
    private final TetrisInput input;
    private final TetrisView view;

    public TetrisController() {
        input = new TetrisInput();
        model = new TetrisModel(input);
        view = new TetrisView(this, model);
    }

    public void initView() {
        SwingUtilities.invokeLater(view::standardInit);
    }

    public void startGame() {
        model.startGame();
    }

    public void stopGame() {
        model.stopGame();
    }

    public void setName(String name) {
        model.setName(name);
    }

    public void setGameMode(GameMode gameMode) {
        model.setGameMode(gameMode);
    }

    public void moveLeft() {
        input.setMoveLeft(true);
    }

    public void moveRight() {
        input.setMoveRight(true);
    }

    public void moveDown() {
        input.setMoveDown(true);
    }

    public void moveDownInstant() {
        input.setMoveDownInstant(true);
    }

    public void rotateLeft() {
        input.setRotateLeft(true);
    }

    public void rotateRight() {
        input.setRotateRight(true);
    }

    public void clearHighscores() {
        model.clearHighscores();
    }

}
