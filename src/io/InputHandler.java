package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import core.Board;
import core.Piece;

public class InputHandler {
    public Board readInput(String filePath) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String[] dimensions = reader.readLine().trim().split(" ");
        int rows = Integer.parseInt(dimensions[0]);
        int cols = Integer.parseInt(dimensions[1]);
        int numPieces = Integer.parseInt(reader.readLine().trim());

        char[][] grid = new char[rows][cols];
        List<Piece> pieces = new ArrayList<>();
        int exitRow = -1, exitCol = -1;

        // Read grid
        for (int i = 0; i < rows; i++) {
            String line = reader.readLine().trim();
            // Check if K is at the end of the line (outside grid)
            if (line.length() > cols && line.charAt(cols) == 'K') {
                exitRow = i;
                exitCol = cols; // This will be the position right after the grid
                line = line.substring(0, cols); // Trim the K from the line
            }

            // Now check the trimmed line length
            if (line.length() != cols) {
                throw new Exception("Row " + (i + 1) + " has " + line.length() + " characters, expected " + cols);
            }

            for (int j = 0; j < cols; j++) {
                grid[i][j] = line.charAt(j);
                if (grid[i][j] == 'K') {
                    exitRow = i;
                    exitCol = j;
                }
            }
        }
        reader.close();

        // Validate the puzzle configuration
        try {
            ErrorHandler.validatePuzzle(grid, numPieces);
        } catch (ErrorHandler.PuzzleValidationException e) {
            throw new Exception("Invalid puzzle configuration: " + e.getMessage());
        }

        // Identify pieces
        boolean[][] visited = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] != '.' && grid[i][j] != 'K' && !visited[i][j]) {
                    char id = grid[i][j];
                    boolean isPrimary = id == 'P';
                    int size = 1;
                    String orientation;

                    // Check horizontally
                    if (j + 1 < cols && grid[i][j + 1] == id) {
                        orientation = "horizontal";
                        while (j + size < cols && grid[i][j + size] == id) {
                            size++;
                        }
                    }
                    // Check vertically
                    else if (i + 1 < rows && grid[i + 1][j] == id) {
                        orientation = "vertical";
                        while (i + size < rows && grid[i + size][j] == id) {
                            size++;
                        }
                    } else {
                        orientation = "horizontal"; // Default for single cell (rare case)
                    }

                    pieces.add(new Piece(id, i, j, size, orientation, isPrimary));
                    // Mark cells as visited
                    for (int k = 0; k < size; k++) {
                        if (orientation.equals("horizontal")) {
                            visited[i][j + k] = true;
                        } else {
                            visited[i + k][j] = true;
                        }
                    }
                }
            }
        }

        if (exitRow == -1 || exitCol == -1) {
            throw new Exception("Exit position (K) not found in the grid");
        }

        return new Board(rows, cols, grid, pieces, exitRow, exitCol);
    }
}