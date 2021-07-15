package UI;

import javax.swing.*;
import java.awt.*;

public class ScoreCellRenderer extends JLabel implements ListCellRenderer {
    private Color[] colors = {
            new Color(21, 49, 238),
            new Color(255, 3, 13),
            new Color(60, 255, 10),
            new Color(253, 114, 7),
            new Color(2, 253, 215, 171),
            new Color(182, 26, 174),
            new Color(173, 14, 14),
            new Color(14, 35, 123),
            new Color(66, 106, 34),
            new Color(6, 144, 116),
    };

    public ScoreCellRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        setForeground(Color.BLACK);
        setText(value.toString());
        if (index <= 9) {
            setForeground(colors[index]);
        }
        return this;
    }
}
