package UI;

import model.*;
import controller.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class TetrisView {
    private final TetrisController controller;
    private final TetrisModel model;
    private final JFrame view;
    private final Container mainPane;

    public TetrisView(TetrisController controller, TetrisModel model) {
        this.view = new JFrame("Tetris");
        this.mainPane = view.getContentPane();
        this.controller = controller;
        this.model = model;
    }

    public void standardInit() {
        view.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainPane.setLayout(new FlowLayout());
        view.setJMenuBar(new TetrisMenuBar(controller, model));
        mainPane.add(new InfoPanel(model, controller));
        GamePanel gamePanel = new GamePanel(model, controller);
        mainPane.add(gamePanel);
        mapKeyInputsToActions(gamePanel);
        mainPane.add(new HighscoresPanel(model));
        view.pack();
        view.setVisible(true);
    }

    private void mapKeyInputsToActions(JPanel gamePanel) {
        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveLeft");
        gamePanel.getActionMap().put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.moveLeft();
            }
        });
        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moveRight");
        gamePanel.getActionMap().put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.moveRight();
            }
        });
        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "moveDown");
        gamePanel.getActionMap().put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.moveDown();
            }
        });
        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_X, 0), "moveDownInstant");
        gamePanel.getActionMap().put("moveDownInstant", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.moveDownInstant();
            }
        });
        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "rotateTetriminoLeft");
        gamePanel.getActionMap().put("rotateTetriminoLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.rotateLeft();
            }
        });
        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "rotateTetriminoRight");
        gamePanel.getActionMap().put("rotateTetriminoRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.rotateRight();
            }
        });
        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "stopGame");
        gamePanel.getActionMap().put("stopGame", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePanel.requestFocusInWindow();
                controller.stopGame();
            }
        });
        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "startGame");
        gamePanel.getActionMap().put("startGame", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePanel.requestFocusInWindow();
                controller.startGame();
            }
        });
    }
}
