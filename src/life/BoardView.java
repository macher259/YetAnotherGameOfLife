package life;

import javax.swing.*;
import java.awt.*;

/**
 * Class representing N x N board with N x N squares equal in size.
 * Not efficient, so it has problems with initializing with N > ~150.
 */
public class BoardView extends JPanel {
    private final int size;
    private final JPanel[][] cells;

    public BoardView(int size) {
        super();
        setLayout(new GridLayout(size, size, 1, 1));
        this.size = size;
        cells = new JPanel[size][size];
        for (int row = 0; row < size; ++row) {
            for (int col = 0; col < size; ++col) {
                cells[row][col] = new JPanel();
                add(cells[row][col]);
            }
        }
    }

    /**
     * Paints board squares according to given color array
     * @param colors                        - color to paint the board matrix
     * @throws IllegalArgumentException     - if a given color array is invalid
     */
    public void paint(Color[][] colors) throws IllegalArgumentException {
        if (colors == null || colors.length != size || colors[0].length != size) {
            throw new IllegalArgumentException();
        }
        for (int row = 0; row < size; ++row) {
            for (int col = 0; col < size; ++col) {
                cells[row][col].setForeground(colors[row][col]);
                cells[row][col].setBackground(colors[row][col]);
                repaint();
            }
        }
    }
}
