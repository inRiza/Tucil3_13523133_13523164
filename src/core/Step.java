package core;

public class Step {
    private Piece piece;
    private String direction;
    private int steps;
    private Board board;

    public Step(Piece piece, String direction, int steps, Board board) {
        this.piece = piece;
        this.direction = direction;
        this.steps = steps;
        this.board = board;
    }

    public static void printBoard(Board board) {
        char[][] grid = board.getGrid();
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void printSteps(java.util.List<Step> steps) {
        System.out.println("Papan Awal");
        printBoard(steps.get(0).getBoard());

        for (int i = 1; i < steps.size(); i++) {
            Step step = steps.get(i);
            System.out.println("Gerakan " + i + ": " +
                    step.getPiece().getId() + "-" +
                    step.getDirection() + " " +
                    step.getSteps() + " langkah");
            printBoard(step.getBoard());
        }
    }

    public Piece getPiece() {
        return piece;
    }

    public String getDirection() {
        return direction;
    }

    public int getSteps() {
        return steps;
    }

    public Board getBoard() {
        return board;
    }
}