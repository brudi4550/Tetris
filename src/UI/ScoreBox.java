package UI;

import model.*;
import javax.swing.*;
import java.awt.*;

public class ScoreBox extends JComponent {
    private TetrisModel model;
    private final int WIDTH = 100;
    private final int HEIGHT = 50;

    public ScoreBox(TetrisModel model) {
        this.model = model;
    }

    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        String score = String.valueOf(model.getScore());
        g.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        g.drawString("Current Score: "+score, 0, 25);
    }
}
