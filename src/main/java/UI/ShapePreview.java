package UI;

import model.TetrisModel;

import javax.swing.*;
import java.awt.*;

import model.shape.Shape;

public class ShapePreview extends JComponent {
    private TetrisModel model;
    private final int HEIGHT = 75;
    private final int WIDTH = 75;
    private final int PIXELS_PER_BLOCK = 25;

    public ShapePreview (TetrisModel model) {
        this.model = model;
        this.setPreferredSize(getPreferredSize());
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }

    @Override
    public void paintComponent(Graphics g) {
        int compHeight = this.getHeight();
        int compWidth = this.getWidth();
        super.paintComponent(g);
        Shape preview = model.getNext();
        if (preview == null) return;
        boolean[][] block = preview.getBooleanRepresentation();
        int startY = (compHeight - preview.getHeight() * PIXELS_PER_BLOCK) / 2;
        int startX = (compWidth - preview.getWidth() * PIXELS_PER_BLOCK) / 2;
        g.setColor(preview.getColor());
        for (int i = 0; i < block.length; i++) {
            for (int j = 0; j < block[i].length; j++) {
                if (block[i][j]) {
                    g.fillRect(j * PIXELS_PER_BLOCK + startX, i*PIXELS_PER_BLOCK + startY,
                            PIXELS_PER_BLOCK, PIXELS_PER_BLOCK);
                }
            }
        }
    }
}
