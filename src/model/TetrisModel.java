package model;

import model.event.EventType;
import model.event.TetrisEvent;
import model.event.TetrisListener;
import model.shape.*;

import java.util.*;

public class TetrisModel {
    private final TetrisJSON json = new TetrisJSON();
    private final TetrisInput input;
    private final List<TetrisListener> listeners = new ArrayList<>();
    private GameMode gameMode = GameMode.NORMAL;
    private List<Tetrimino> tetriminos;
    private Tetrimino next;
    private Tetrimino current;
    private Timer gameLoopTimer;
    private String playerName;
    private int score;
    private boolean gameOver;
    private boolean gameInProgress;
    private double nextLoopTime;
    private double currentGameLoopSpeed;

    public TetrisModel(TetrisInput input) {
        this.input = input;
    }

    public void startGame() {
        if (gameInProgress) return;
        resetFields();
        next = TetriminoFactory.getRandomTetrimino(gameMode);
        gameLoopTimer = new Timer();
        gameLoopTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                gameLoop();
            }
        }, 0, 10);
    }

    private void resetFields() {
        gameOver = false;
        gameInProgress = true;
        next = null;
        current = null;
        score = 0;
        tetriminos = new ArrayList<>();
        currentGameLoopSpeed = Tetris.STARTING_GAME_LOOP_SPEED;
        nextLoopTime = 0;
    }

    private void gameLoop() {
        evaluateGameOver();
        if (!gameOver) {
            evaluateInput();
            evaluateGravity();
        }
    }

    private void evaluateGameOver() {
        if (current == null) return;
        if (current.isOutOfBound(Tetris.NR_OF_COLUMNS, 0, Tetris.NR_OF_ROWS, 0) &&
                !tetriminoCanMove(current, Direction.DOWN, 1)) {
            gameOver = true;
            stopGame();
            if (json.isHighscore(gameMode.name().toLowerCase() + "Highscores", score)) {
                json.addHighscore(gameMode.name().toLowerCase() + "Highscores", playerName, score);
            }
            fireEvent(EventType.GAME);
        }
    }

    private void evaluateInput() {
        if (input.moveDownInstant()) {
            while(tetriminoCanMove(current, Direction.DOWN, 1)) {
                current.moveY(-1);
            }
            fireEvent(EventType.GAME);
            input.setMoveDownInstant(false);
        } else if (input.moveLeft() && tetriminoCanMove(current, Direction.LEFT, 1)) {
            moveTetrimino(current, Direction.LEFT, 1);
            fireEvent(EventType.GAME);
            input.setMoveLeft(false);
        } else if (input.moveRight() && tetriminoCanMove(current, Direction.RIGHT, 1)) {
            moveTetrimino(current, Direction.RIGHT, 1);
            fireEvent(EventType.GAME);
            input.setMoveRight(false);
        } else if (input.moveDown() && tetriminoCanMove(current, Direction.DOWN, 1)) {
            moveTetrimino(current, Direction.DOWN, 1);
            fireEvent(EventType.GAME);
            input.setMoveDown(false);
        } else if (input.rotateLeft() && tetriminoCanRotate(current, Direction.LEFT)) {
            current.rotate(Direction.LEFT);
            fireEvent(EventType.GAME);
            input.setRotateLeft(false);
        } else if (input.rotateRight() && tetriminoCanRotate(current, Direction.RIGHT)) {
            current.rotate(Direction.RIGHT);
            fireEvent(EventType.GAME);
            input.setRotateRight(false);
        }
    }

    private void evaluateGravity() {
        double currentTime = System.nanoTime() / 1000000000.0;
        if (currentTime > nextLoopTime) {
            if (!tetriminoCanMove(current, Direction.DOWN, 1)) {
                current = next;
                tetriminos.add(current);
                next = TetriminoFactory.getRandomTetrimino(gameMode);
                boolean linesRemoved;
                do {
                    linesRemoved = removeFullLines();
                    if (linesRemoved) updateGroundTetriminos();
                } while(linesRemoved);
                if (currentGameLoopSpeed > Tetris.MAX_GAME_LOOP_SPEED) {
                    currentGameLoopSpeed *= ((100 - Tetris.SPEED_INCREASE_IN_PERCENT) / 100);
                }
            }
            moveTetrimino(current, Direction.DOWN, 1);
            nextLoopTime = currentTime + currentGameLoopSpeed;
            fireEvent(EventType.GAME);
        }
    }

    public Tetrimino getCurrentProjection() {
        int count = 0;
        while(tetriminoCanMove(current, Direction.DOWN, 1)) {
            count++;
            current.moveY(-1);
        }
        Tetrimino projection = current.clone();
        current.moveY(count);
        return projection;
    }

    private void moveTetrimino(Tetrimino t, Direction d, int steps) {
        if (d == Direction.LEFT || d == Direction.DOWN) steps = -steps;
        switch (d) {
            case LEFT, RIGHT -> t.moveX(steps);
            case DOWN, UP -> t.moveY(steps);
        }
        fireEvent(EventType.GAME);
    }

    private boolean tetriminoCanMove(Tetrimino current, Direction d, int steps) {
        if (current == null || d == null) return false;
        if (d == Direction.LEFT || d == Direction.DOWN) steps = -steps;
        Tetrimino copy = current.clone();
        switch (d) {
            case LEFT, RIGHT -> copy.moveX(steps);
            case DOWN, UP -> copy.moveY(steps);
        }
        if (copy.isOutOfBound(Tetris.NR_OF_COLUMNS, 0, Tetris.NR_OF_ROWS+10, 0)) return false;
        for (Tetrimino t : tetriminos) {
            if (!t.equals(current) && copy.overlapsWith(t)) {
                return false;
            }
        }
        return true;
    }

    private boolean tetriminoCanRotate(Tetrimino t, Direction direction) {
        if (t == null) return false;
        if (direction == Direction.DOWN || direction == Direction.UP) return false;
        Tetrimino copy = t.clone();
        copy.rotate(direction);
        for (Tetrimino current : tetriminos) {
            if (copy.isOutOfBound(Tetris.NR_OF_COLUMNS, 0, Tetris.NR_OF_ROWS+10, 0)) return false;
            if (!t.equals(current) && copy.overlapsWith(current)) return false;
        }
        return true;
    }

    private void updateGroundTetriminos() {
        if (tetriminos.isEmpty()) return;
        boolean changeOccurred;
        do {
            changeOccurred = false;
            for (Tetrimino t : tetriminos) {
                if (!t.equals(current) && tetriminoCanMove(t, Direction.DOWN, 1)) {
                    moveTetrimino(t, Direction.DOWN, 1);
                    changeOccurred = true;
                }
            }
        } while (changeOccurred);
    }

    private boolean removeFullLines() {
        boolean[][] gameState = get2DGameState();
        List<Integer> rowNrsToBeRemoved = getListOfFullLines(gameState);
        score += rowNrsToBeRemoved.size() * Tetris.POINTS_PER_FINISHED_ROW;
        removeLines(rowNrsToBeRemoved);
        replaceDisconnectedTetriminos();
        return rowNrsToBeRemoved.size() > 0;
    }

    private void replaceDisconnectedTetriminos() {
        List<Tetrimino> newList = new ArrayList<>();
        newList.add(current);
        for (Tetrimino t : tetriminos) {
            if (!t.equals(current)) {
                Tetrimino[] s = t.getSplitUpShapes();
                Collections.addAll(newList, s);
            }
        }
        tetriminos.clear();
        tetriminos.addAll(newList);
    }

    private boolean[][] get2DGameState() {
        boolean[][] state = new boolean[Tetris.NR_OF_ROWS+1][Tetris.NR_OF_COLUMNS+1];
        for (Tetrimino t : tetriminos) {
            for (Coordinate c : t.getCoordinates()) {
                if (c.getY() >= 0 && c.getY() <= Tetris.NR_OF_ROWS) {
                    state[c.getY()][c.getX()] = true;
                }
            }
        }
        return state;
    }

    private List<Integer> getListOfFullLines(boolean[][] gameState) {
        List<Integer> rowNrsToBeRemoved = new ArrayList<>();
        for (int i = 0; i < gameState.length; i++) {
            boolean rowIsFull = true;
            for (int j = 0; j < gameState[i].length; j++) {
                if (!gameState[i][j]) {
                    rowIsFull = false;
                    break;
                }
            }
            if (rowIsFull) {
                rowNrsToBeRemoved.add(i);
            }
        }
        return rowNrsToBeRemoved;
    }

    private void removeLines(List<Integer> rowNrsToBeRemoved) {
        List<Tetrimino> tetriminosToBeRemoved = new ArrayList<>();
        for (Integer i : rowNrsToBeRemoved) {
            for (Tetrimino t : tetriminos) {
                List<Coordinate> coordsToBeRemoved = new ArrayList<>();
                for (Coordinate c : t.getCoordinates()) {
                    if (c.getY() == i) {
                        coordsToBeRemoved.add(c);
                    }
                }
                t.getCoordinates().removeAll(coordsToBeRemoved);
                if (t.getCoordinates().isEmpty()) {
                    tetriminosToBeRemoved.add(t);
                }
            }
        }
        tetriminos.removeAll(tetriminosToBeRemoved);
    }

    private void fireEvent(EventType type) {
        TetrisEvent event = new TetrisEvent(this, type);
        for (TetrisListener l : listeners) {
            l.changeOccurred(event);
        }
    }

    public int getScore() {
        return score;
    }

    public Tetrimino getNext() {
        return next;
    }

    public Tetrimino getCurrent() {
        return current;
    }

    public TetrisJSON getTetrisJSON() {
        return json;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameInProgress() {
        return gameInProgress;
    }

    public List<Tetrimino> getTetriminos() {
        return tetriminos;
    }

    public void addListener(TetrisListener l) {
        listeners.add(l);
    }

    public void removeListener(TetrisListener l) {
        listeners.remove(l);
    }

    public void setName(String name) {
        playerName = (name == null || name.equals("")) ? "Unnamed" : name;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
        fireEvent(EventType.SETTINGS);
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void clearHighscores() {
        json.clear();
        fireEvent(EventType.HIGHSCORE);
    }

    public void stopGame() {
        if (gameInProgress) {
            gameInProgress = false;
            gameLoopTimer.cancel();
            fireEvent(EventType.GAME);
        }
    }
}
