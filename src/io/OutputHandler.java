package io;

import java.io.*;
import java.util.List;
import core.Board;
import core.Piece;

public class OutputHandler {
    private static final String OUTPUT_DIR = "test/output";

    public static void saveSolution(String filename, List<Step> solution, Board initialBoard, long explorationTime,
            int nodesExplored, int maxQueueSize) {
        // Create output directory if it doesn't exist
        File outputDir = new File(OUTPUT_DIR);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        // Create the output file
        File outputFile = new File(outputDir, filename);
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
            // Write initial board information
            writer.println("Initial Board Configuration:");
            writer.println("Board dimensions: " + initialBoard.getRows() + "x" + initialBoard.getCols());
            writer.println(
                    "Exit position (K): Row " + initialBoard.getExitRow() + ", Col " + initialBoard.getExitCol());
            writer.println("\nPieces found:");
            for (Piece piece : initialBoard.getPieces()) {
                writer.printf("Piece ID: %c, Position: (%d,%d), Size: %d, Orientation: %s, Is Primary: %b%n",
                        piece.getId(), piece.getRow(), piece.getCol(), piece.getSize(),
                        piece.getOrientation(), piece.isPrimary());
            }

            // Write solution steps
            writer.println("\nSolution Steps:");
            writer.println("Total steps: " + solution.size());
            writer.println("\nStep-by-step solution:");

            for (int i = 0; i < solution.size(); i++) {
                Step step = solution.get(i);
                writer.printf("\nStep %d:%n", i + 1);
                if (step.getPiece() != null) {
                    writer.printf("Move: %c %s %d steps%n",
                            step.getPiece().getId(), step.getDirection(), step.getSteps());
                }
                writer.println("Board state:");
                printBoardState(writer, step.getBoard());
            }

            // Write exploration statistics
            writer.println("\nExploration Statistics:");
            writer.printf("Exploration time: %.3f seconds%n", explorationTime / 1000.0);
            writer.println("Nodes explored: " + nodesExplored);
            writer.println("Maximum queue size: " + maxQueueSize);

            // Write summary
            writer.println("\nSolution Summary:");
            writer.println("Total moves: " + solution.size());
            int totalSteps = solution.stream()
                    .mapToInt(Step::getSteps)
                    .sum();
            writer.println("Total steps taken: " + totalSteps);
        } catch (IOException e) {
            throw new RuntimeException("Error saving solution: " + e.getMessage());
        }
    }

    private static void printBoardState(PrintWriter writer, Board board) {
        char[][] grid = board.getGrid();
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                writer.print(grid[i][j] + " ");
            }
            writer.println();
        }
        writer.println();
    }
}