package UI;

import controller.TetrisController;
import model.TetrisModel;
import model.event.EventType;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private TetrisModel model;
    private TetrisController controller;

    public GamePanel(TetrisModel model, TetrisController controller) {
        this.model = model;
        this.controller = controller;
        initPanel();
    }

    private void initPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel welcomeMessage = new JLabel("Tetris");
        welcomeMessage.setFont(new Font("TimesNewRoman", Font.PLAIN, 30));
        this.add(welcomeMessage);
        GameArea gameArea = new GameArea(model);
        ScoreBox scoreBox = new ScoreBox(model);
        this.add(gameArea);
        this.add(scoreBox);
        model.addListener(e -> {
            if (e.getType() == EventType.GAME)
                SwingUtilities.invokeLater(gameArea::repaint);
        });
        model.addListener(e -> {
            if (e.getType() == EventType.GAME)
                SwingUtilities.invokeLater(scoreBox::repaint);
        });
    }
}
