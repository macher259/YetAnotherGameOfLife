package life;

import java.util.Arrays;
import java.util.Random;

/**
 * Class representing cell matrix in the Game of Life
 */
public class Board {
    public enum CellState {
        ALIVE('O', true),
        DEAD(' ', false);

        private final char character;
        private final boolean value;

        CellState(char character, boolean value) {
            this.character = character;
            this.value = value;
        }

        char getCharacter() {
            return character;
        }

        boolean getValue() {
            return value;
        }
    }
    private final CellState[][] board;
    private final Random random;
    private int generation;

    /**
     * Constructor used to get specific starting board.
     * @param size      - number of rows/columns of our board
     * @param seed      - seed to specify starting board
     */
    public Board(int size, int seed) {
        board = new CellState[size][size];
        random = new Random(seed);
        restart();
    }

    /**
     * Normal constructor.
     * @param size      - number of rows/columns of our board
     */
    public Board(int size) {
        board = new CellState[size][size];
        random = new Random();
        restart();
    }

    /**
     * Counts how many cells at the board are alive.
     * @return          - number of alive cells
     */
    public int getAliveCount() {
        int count = 0;
        for (int row = 0; row < getSize(); ++row) {
            for (int col = 0; col < getSize(); ++col) {
                if (board[row][col] == CellState.ALIVE) {
                    ++count;
                }
            }
        }
        return count;
    }

    /**
     * Getter for generation counter.
     * @return          - number of current generation
     */
    public int getGeneration() {
        return generation;
    }

    /**
     * Getter for number of rows/columns.
     * @return          - number of rows/columns of the board
     */
    public int getSize() {
        return board.length;
    }

    /**
     * Restarts board state by creating new initial state of board cells
     * and setting generation counter to 1.
     */
    public void restart() {
        generation = 1;
        for (int row = 0; row < getSize(); ++row) {
            for (int col = 0; col < getSize(); ++col) {
                if (random.nextBoolean()) {
                    board[row][col] = CellState.ALIVE;
                }
                else {
                    board[row][col] = CellState.DEAD;
                }
            }
        }
    }

    /**
     * Counts number of alive neighbours of cell specified by given position in the board
     * int given generation. Each cell has 8 adjacent neighbour cells. Our board is periodic -
     * cell at position (row, col) = (size - 1, size - 1) is neighbours with cell (0,0) in 0-based
     * indexing.
     * @param generation    - board state we check neighbours of our cell in
     * @param row           - row of our cell
     * @param col           - columns of our cell
     * @return              - number of alive neighbours of cell specified by given position in given generation.
     */
    private int countNeighbours(CellState[][] generation, int row, int col) {
        // Directions array, should be the same length
        int[] dirX = new int[]{-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dirY = new int[]{-1, 0, 1, 1, -1, -1, 0, 1};
        int count = 0;
        for (int i = 0; i < dirY.length; ++i) {
            int newRow = (row + dirY[i] + getSize()) % getSize();
            int newCol = (col + dirX[i] + getSize()) % getSize();

            if (generation[newRow][newCol] == CellState.ALIVE) {
                ++count;
            }
        }
        return count;
    }

    /**
     * If cell is alive and has either 2 or 3 alive neighbours, it survives current generation.
     * If cell is dead and has 3 alive neighbours, it becomes alive.
     * If any of those conditions is not satisfied, cell will be dead in the next generation.
     * @param state             - state of the cell we decide fate
     * @param neighbours        - number of alive neighbours of our cell
     * @return                  - CellState.ALIVE if cell will be alive or CellState.DEAD if cell will be dead
     */
    private CellState decideFate(CellState state, int neighbours) {
        if (state == CellState.ALIVE) {
            if (neighbours == 2 || neighbours == 3) {
                return CellState.ALIVE;
            }
            else {
                return CellState.DEAD;
            }
        }
        if (neighbours == 3) {
            return CellState.ALIVE;
        }
        return CellState.DEAD;
    }

    /**
     * Changes state of the board according to set rules.
     * Increases generation count by 1.
     */
    public void nextGeneration() {
        ++generation;
        CellState[][] oldGeneration = Arrays.stream(board)
                .map(row -> Arrays.copyOf(row, row.length))
                .toArray(CellState[][]::new);

        for (int row = 0; row < getSize(); ++row) {
            for (int col = 0; col < getSize(); ++col) {
                int neighbours = countNeighbours(oldGeneration, row, col);
                board[row][col] = decideFate(oldGeneration[row][col], neighbours);
            }
        }
    }

    /**
     * Array where true means a cell is alive and false means its dead.
     * @return          - 2D boolean state array
     */
    public boolean[][] getState() {
        boolean[][] stateArray = new boolean[getSize()][getSize()];
        for (int i = 0; i < getSize(); ++i) {
            for (int j = 0; j < getSize(); ++j) {
                stateArray[i][j] = board[i][j].getValue();
            }
        }
        return stateArray;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(getSize() * getSize());
        for (int row = 0; row < getSize(); ++row) {
            for (int col = 0; col < getSize(); ++col) {
                stringBuilder.append(board[row][col].getCharacter());
            }
            stringBuilder.append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }
}
