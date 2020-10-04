package life;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        // sample size that is neither too small nor too big
        // may add component to change size on runtime
        int size = 100;

        Board board = new Board(size);
        GameOfLife game = new GameOfLife(size);

        while (true) {
            if (game.shouldRestart()) {
                game.restartGame(board);
            } else if (!game.isPaused()) {
                game.updateAliveCount(board.getAliveCount());
                game.updateGenerationCount(board.getGeneration());
                game.updateBoard(board.getState());
                board.nextGeneration();
                Thread.sleep(1000L / game.getSpeed());
            } else {
                Thread.sleep(500L);
            }
        }
    }
}
