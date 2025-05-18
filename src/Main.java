import io.InputHandler;
import io.Step;
import core.Board;
import core.Piece;
import algorithms.UCS;
import algorithms.GreedyBestFirstSearch;
import java.util.List;
import java.util.Scanner;

import heuristics.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            // Read input file
            System.out.println("Reading input file (test1.txt):");
            InputHandler inputHandler = new InputHandler();
            Board board = inputHandler.readInput("test/input/test1.txt");

            // Pilih heuristik untuk Greedy atau A*
            System.out.println("\nPilih heuristik:");
            System.out.println("1. Distance to Exit");
            // System.out.println("2. Heuristics 2 "); (soon)
            System.out.print("Pilihan (1-2): ");
            int heuristicChoice = scanner.nextInt();
            
            // Inisialisasi heuristik yang dipilih
            Heuristic heuristic;
            switch (heuristicChoice) {
                case 1:
                    heuristic = new DistanceToExitHeuristic();
                    break;
                // case 2:
                //     heuristic = new BlockingVehiclesHeuristic();
                //     break;
                default:
                    System.out.println("Pilihan tidak valid! Menggunakan Distance to Exit sebagai default.");
                    heuristic = new DistanceToExitHeuristic();
            }

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
            // System.out.println("\nSolving puzzle using UCS...");
            // List<Step> solution = UCS.solve(board);

            //solve using greedy best first search
            System.out.println("\nSolving puzzle using Greedy best first Search...");
            List<Step> solution = GreedyBestFirstSearch.solve(board, heuristic);

            if (solution != null) {
                System.out.println("\nSolution found! Total steps: " + (solution.size() - 1));
                Step.printSteps(solution);
            } else {
                System.out.println("\nNo solution found!");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally{
            scanner.close();
        }
    }
}
