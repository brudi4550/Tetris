package UI;

import controller.TetrisController;
import model.GameMode;
import model.Tetris;
import model.TetrisModel;

import javax.swing.*;

public class TetrisMenuBar extends JMenuBar {
    private TetrisModel model;
    private TetrisController controller;

    public TetrisMenuBar(TetrisController controller, TetrisModel model) {
        this.controller = controller;
        this.model = model;
        initOptionsMenu();
    }

    private void initOptionsMenu() {
        JMenu options = new JMenu("Options");
        options.add(addResetHighscoreOption());
        options.add(addShowControlsOption());
        options.addSeparator();
        addGameModeSelection(options);
        this.add(options);
    }

    private JMenuItem addResetHighscoreOption() {
        JMenuItem resetHighscores = new JMenuItem("Reset highscores");
        resetHighscores.addActionListener(al -> {
            int answer = JOptionPane.showConfirmDialog(
                    this,
                    """
                            Are you sure you want to delete all highscores?
                            This action cannot be undone.""",
                    "ALERT",
                    JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) controller.clearHighscores();
        });
        return resetHighscores;
    }

    private JMenuItem addShowControlsOption() {
        JMenuItem controls = new JMenuItem("Show controls");
        controls.addActionListener(al -> JOptionPane.showMessageDialog(this,
                """
                        Arrow keys to move the tetriminos around.
                        A and D to rotate the tetriminos.
                        X to move the tetrimino instantly to it's projected end-position.
                        Enter to start the game, escape key to end a game prematurely.""",
                "Controls",
                JOptionPane.INFORMATION_MESSAGE));
        return controls;
    }

    private void addGameModeSelection(JMenu menu) {
        menu.add(new JLabel("Mode selection: "));
        ButtonGroup gamemodes = new ButtonGroup();
        JRadioButtonMenuItem easyMode = new JRadioButtonMenuItem("EASY");
        easyMode.addActionListener(al -> {
            controller.setGameMode(GameMode.EASY);
            Tetris.MAX_GAME_LOOP_SPEED = 0.4d;
            Tetris.SPEED_INCREASE_IN_PERCENT = 1d;
        });
        JRadioButtonMenuItem normalMode = new JRadioButtonMenuItem("NORMAL");
        normalMode.addActionListener(al -> {
            controller.setGameMode(GameMode.NORMAL);
            Tetris.MAX_GAME_LOOP_SPEED = 0.3d;
            Tetris.SPEED_INCREASE_IN_PERCENT = 1.5d;
        });
        JRadioButtonMenuItem hardMode = new JRadioButtonMenuItem("HARD");
        hardMode.addActionListener(al -> {
            controller.setGameMode(GameMode.HARD);
            Tetris.MAX_GAME_LOOP_SPEED = 0.2d;
            Tetris.SPEED_INCREASE_IN_PERCENT = 2d;
        });
        JRadioButtonMenuItem insaneMode = new JRadioButtonMenuItem("INSANE");
        insaneMode.addActionListener(al -> {
            controller.setGameMode(GameMode.INSANE);
            Tetris.MAX_GAME_LOOP_SPEED = 0.1d;
            Tetris.SPEED_INCREASE_IN_PERCENT = 3d;
        });
        normalMode.setSelected(true);
        model.addListener(e -> SwingUtilities.invokeLater(() -> {
            if (model.isGameInProgress()) {
                easyMode.setEnabled(false);
                normalMode.setEnabled(false);
                hardMode.setEnabled(false);
                insaneMode.setEnabled(false);
            } else {
                easyMode.setEnabled(true);
                normalMode.setEnabled(true);
                hardMode.setEnabled(true);
                insaneMode.setEnabled(true);
            }
        }));
        gamemodes.add(easyMode);
        gamemodes.add(normalMode);
        gamemodes.add(hardMode);
        gamemodes.add(insaneMode);

        menu.add(easyMode);
        menu.add(normalMode);
        menu.add(hardMode);
        menu.add(insaneMode);
    }
}
