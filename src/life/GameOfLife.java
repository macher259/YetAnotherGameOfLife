package life;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Hashtable;

/**
 * Class representing the game with gui and api to control.
 */
public class GameOfLife extends JFrame {
    private JLabel aliveLabel;
    private JLabel generationLabel;
    private BoardView board;
    private int speed = 5;
    private final int size;
    private boolean isPaused = false;
    private boolean isToRestart = false;

    public GameOfLife(int size) {
        this.size = size;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setExtendedState(MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        initializeComponents();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeComponents() {
        board = new BoardView(size);
        board.setMaximumSize(getMaximumSize());

        add(board, BorderLayout.CENTER);
        initializeButtonPanel();
        initializeInformationPanel();
    }

    private void initializeInformationPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        aliveLabel = new JLabel("ALIVE: 0");
        generationLabel = new JLabel("GENERATION: 0");

        leftPanel.add(aliveLabel);
        leftPanel.add(generationLabel);
        add(leftPanel, BorderLayout.NORTH);
    }

    private void initializeButtonPanel() {
        JButton resetButton = new JButton("Reset");
        resetButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                isToRestart = true;
            }
        });

        JToggleButton playToggleButton = new JToggleButton("Pause");
        playToggleButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                isPaused = !isPaused;
            }
        });

        int maxSpeed = 10;
        int minSpeed = 1;
        JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, minSpeed, maxSpeed, speed);
        speedSlider.setMajorTickSpacing(10);
        speedSlider.setMinorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        Hashtable<Integer, JLabel> labelTable= new Hashtable<>();
        labelTable.put(maxSpeed, new JLabel("fast"));
        labelTable.put(minSpeed, new JLabel("slow"));
        speedSlider.setLabelTable(labelTable);
        speedSlider.addChangeListener(changeEvent -> {
            JSlider source = (JSlider) changeEvent.getSource();
            if (!source.getValueIsAdjusting()) {
                speed = source.getValue();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3));
        buttonPanel.setAlignmentX(SwingConstants.RIGHT);
        buttonPanel.add(speedSlider);
        buttonPanel.add(resetButton);
        buttonPanel.add(playToggleButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Updates alive count label to a given integer
     * @param newAliveCount         - new number of alive cells on board
     */
    public void updateAliveCount(int newAliveCount) {
        aliveLabel.setText("Alive: " + newAliveCount);
    }

    /**
     * Updates generation count label to a given integer
     * @param newGenerationCount    - new number of generation
     */
    public void updateGenerationCount(int newGenerationCount) {
        generationLabel.setText("Generation: " + newGenerationCount);
    }

    /**
     * Updates graphic representation of cell matrix with given cell state matrix.
     * true means cell is alive and false means cell is dead.
     *
     * Currently alive cell is represented by a color black and dead by a white.
     * @param cellState             - square matrix with state of each cell
     * @throws IllegalArgumentException - if given state 2d array is null, has null rows or is not a square matrix
     */
    public void updateBoard(boolean[][] cellState) throws IllegalArgumentException {
        if (cellState == null || cellState[0] == null || cellState.length != cellState[0].length) {
            throw new IllegalArgumentException();
        }
        Color[][] colorArray = new Color[cellState.length][cellState[0].length];
        for (int i = 0; i < cellState.length; ++i) {
            for (int j = 0; j < cellState[0].length; ++j) {
                colorArray[i][j] = cellState[i][j] ? Color.BLACK : Color.WHITE;
            }
        }
        board.paint(colorArray);
    }

    /**
     * @return speed of the game, the bigger the number is, the slower the game will be.
     */
    public long getSpeed() {
        return speed;
    }

    /**
     * @return boolean flag representing if the game is paused
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * @return boolean flag representing if the user has requested to restart the game
     */
    public boolean shouldRestart() {
        return isToRestart;
    }

    /**
     * Restarts cell matrix with new initial state and updates gui.
     * @param board     - cell matrix
     */
    public void restartGame(Board board) {
        board.restart();
        updateAliveCount(board.getAliveCount());
        updateGenerationCount(board.getGeneration());
        updateBoard(board.getState());
        isToRestart = false;
    }
}
