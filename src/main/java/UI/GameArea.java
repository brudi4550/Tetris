package UI;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import model.*;
import model.shape.*;

public class GameArea extends JComponent {
    private TetrisModel model;
    private final int WIDTH = 500;
    private final int HEIGHT = 750;
    private final int PIXELS_PER_BLOCK = 50;

    public GameArea(TetrisModel model) {
        this.model = model;
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (model.getTetriminos() == null) return;
        drawProjection(g);
        drawMainGame(g);
        drawGameOverScreen(g);
    }

    private void drawProjection(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Tetrimino projection = model.getCurrentProjection();
        g2d.setColor(projection.getColor());
        int thickness = 2;
        Stroke previousStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(thickness));
        for (Coordinate c : projection.getCoordinates()) {
            int x = c.getX() * PIXELS_PER_BLOCK;
            int y = -(c.getY()-Tetris.NR_OF_ROWS) * PIXELS_PER_BLOCK;
            g2d.drawRect(x, y, PIXELS_PER_BLOCK, PIXELS_PER_BLOCK);
            g2d.drawLine(x, y, x + PIXELS_PER_BLOCK, y + PIXELS_PER_BLOCK);
            g2d.drawLine(x + PIXELS_PER_BLOCK, y, x, y + PIXELS_PER_BLOCK);
        }
        g2d.setStroke(previousStroke);
    }

    private void drawMainGame(Graphics g) {
        List<Tetrimino> gameState = model.getTetriminos();
        for (Tetrimino t : gameState) {
            if (t != null) {
                List<Coordinate> coords = t.getCoordinates();
                for (Coordinate c : coords) {
                    int x = c.getX() * PIXELS_PER_BLOCK;
                    //Flipping the y Coordinate because positive y in Swing is downwards
                    int y = -(c.getY()-Tetris.NR_OF_ROWS) * PIXELS_PER_BLOCK;
                    g.setColor(t.getColor());
                    g.fillRect(x, y, PIXELS_PER_BLOCK, PIXELS_PER_BLOCK);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, PIXELS_PER_BLOCK, PIXELS_PER_BLOCK);
                }
            }
        }
    }

    private void drawGameOverScreen(Graphics g) {
        if (model.isGameOver()) {
            g.setColor(Color.BLACK);
            g.drawRect(19, 299, 401, 201);
            g.setColor(new Color(108, 108, 108, 255));
            g.fillRect(20, 300, 400, 200);
            g.setColor(Color.BLACK);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
            g.drawString("GAME OVER", 30, 350);
            g.drawString("YOUR SCORE: "+model.getScore(), 30, 400);
            g.drawString("BETTER LUCK NEXT TIME", 30, 450);
        }
    }
}
