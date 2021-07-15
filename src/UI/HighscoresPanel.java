package UI;

import model.Score;
import model.TetrisModel;
import model.event.EventType;

import javax.swing.*;
import java.awt.*;

public class HighscoresPanel extends JPanel {
    private TetrisModel model;
    private JLabel title;
    private JList<String> scores;
    private DefaultListModel<String> listModel;
    private JScrollPane scroller;

    public HighscoresPanel(TetrisModel model) {
        this.model = model;
        title = new JLabel("TOP 100 HIGHSCORES: ");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        scores = new JList();
        listModel = new DefaultListModel<>();
        scores.setCellRenderer(new ScoreCellRenderer());
        scores.setModel(listModel);
        scores.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        scores.setLayoutOrientation(JList.VERTICAL);
        scores.setVisibleRowCount(-1);
        scroller = new JScrollPane(scores);
        scroller.setPreferredSize(new Dimension(70, 500));
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(title);
        this.add(scroller);
        model.addListener(e -> {
            if (e.getType() == EventType.SETTINGS)
                SwingUtilities.invokeLater(this::repaint);
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        listModel.removeAllElements();
        int i = 0;
        for (Score s : model.getTetrisJSON().getList(model.getGameMode().name().toLowerCase()+"Highscores")) {
            listModel.addElement((i+1) + ". " + s.toString());
            i++;
        }
    }
}
