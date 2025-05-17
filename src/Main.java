import io.InputHandler;
import io.Step;
import core.Board;
import core.Piece;
import algorithms.UCS;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Read input file
            System.out.println("Reading input file (test1.txt):");
            InputHandler inputHandler = new InputHandler();
            Board board = inputHandler.readInput("test/input/test1.txt");

            // Print initial board information
            System.out.println("\nBoard dimensions: " + board.getRows() + "x" + board.getCols());
            System.out.println("Exit position (K): Row " + board.getExitRow() + ", Col " + board.getExitCol());

            // Print all pieces
            System.out.println("\nPieces found:");
            for (Piece piece : board.getPieces()) {
                System.out.println("Piece ID: " + piece.getId() +
                        ", Position: (" + piece.getRow() + "," + piece.getCol() + ")" +
                        ", Size: " + piece.getSize() +
                        ", Orientation: " + piece.getOrientation() +
                        ", Is Primary: " + piece.isPrimary());
            }

            // Solve using UCS
            System.out.println("\nSolving puzzle using UCS...");
            List<Step> solution = UCS.solve(board);

            if (solution != null) {
                System.out.println("\nSolution found! Total steps: " + (solution.size() - 1));
                Step.printSteps(solution);
            } else {
                System.out.println("\nNo solution found!");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
