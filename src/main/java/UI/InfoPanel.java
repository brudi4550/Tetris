package UI;

import controller.TetrisController;
import model.TetrisModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class InfoPanel extends JPanel {
    private TetrisModel model;
    private TetrisController controller;

    public InfoPanel(TetrisModel model, TetrisController controller) {
        this.model = model;
        this.controller = controller;
        initPanel();
    }

    private void initPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel currentMode = new JLabel("Currently selected mode: " + model.getGameMode().name());
        model.addListener(e -> SwingUtilities.invokeLater(()
                -> currentMode.setText("Currently selected mode: " + model.getGameMode().name())));
        JPanel namePanel = new JPanel(new FlowLayout());
        JLabel nameLabel = new JLabel("Name: ");
        JTextField nameField = new JTextField(10);
        nameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                controller.setName(nameField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                controller.setName(nameField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                controller.setName(nameField.getText());
            }
        });
        namePanel.add(nameLabel);
        namePanel.add(nameField);

        JButton resetGame = new JButton("Stop Game");
        resetGame.addActionListener(al -> controller.stopGame());
        JButton startGame = new JButton("Start Game");
        startGame.addActionListener(al -> controller.startGame());

        this.add(currentMode);
        this.add(namePanel);
        ShapePreview preview = new ShapePreview(model);
        this.add(preview);
        model.addListener(e -> SwingUtilities.invokeLater(preview::repaint));
        this.add(Box.createRigidArea(new Dimension(10, 10)));
        this.add(startGame);
        this.add(Box.createRigidArea(new Dimension(10, 10)));
        this.add(resetGame);
    }
}
