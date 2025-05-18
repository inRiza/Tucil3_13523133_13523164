package io;

import java.util.*;

public class ErrorHandler {
    private static final int MIN_PIECE_SIZE = 2;
    private static final char PRIMARY_PIECE = 'P';
    private static final char EMPTY_CELL = '.';

    public static class PuzzleValidationException extends Exception {
        public PuzzleValidationException(String message) {
            super(message);
        }
    }

    public static void validatePuzzle(char[][] grid, int expectedPieceCount) throws PuzzleValidationException {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            throw new PuzzleValidationException("Invalid grid: Grid cannot be null or empty");
        }

        Map<Character, List<int[]>> piecePositions = new HashMap<>();
        int rows = grid.length;
        int cols = grid[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char cell = grid[i][j];
                if (cell != EMPTY_CELL) {
                    piecePositions.computeIfAbsent(cell, k -> new ArrayList<>()).add(new int[] { i, j });
                }
            }
        }

        if (!piecePositions.containsKey(PRIMARY_PIECE)) {
            throw new PuzzleValidationException("Invalid configuration: Primary piece 'P' is missing");
        }

        // Check for single-cell pieces
        for (Map.Entry<Character, List<int[]>> entry : piecePositions.entrySet()) {
            if (entry.getValue().size() < MIN_PIECE_SIZE) {
                throw new PuzzleValidationException(
                        String.format("Invalid piece '%c': Each piece must be at least %d cells",
                                entry.getKey(), MIN_PIECE_SIZE));
            }
        }

        // Count actual pieces (excluding primary piece 'K')
        int actualPieceCount = (int) piecePositions.entrySet().stream()
                .filter(entry -> entry.getKey() != PRIMARY_PIECE)
                .count();

        if (actualPieceCount != expectedPieceCount) {
            throw new PuzzleValidationException(
                    String.format("Invalid piece count: Expected %d pieces (excluding primary piece), but found %d",
                            expectedPieceCount, actualPieceCount));
        }

        for (Map.Entry<Character, List<int[]>> entry : piecePositions.entrySet()) {
            if (!isPieceConnected(entry.getValue())) {
                throw new PuzzleValidationException(
                        String.format("Invalid piece '%c': All cells must be connected", entry.getKey()));
            }
        }
        for (Map.Entry<Character, List<int[]>> entry : piecePositions.entrySet()) {
            if (!isPieceWithinBounds(entry.getValue(), rows, cols)) {
                throw new PuzzleValidationException(
                        String.format("Invalid piece '%c': Piece is outside grid boundaries", entry.getKey()));
            }
        }

        if (hasOverlappingPieces(piecePositions)) {
            throw new PuzzleValidationException("Invalid configuration: Pieces cannot overlap");
        }
    }

    private static boolean isPieceConnected(List<int[]> positions) {
        if (positions.size() < 2)
            return true;

        Set<String> visited = new HashSet<>();
        Queue<int[]> queue = new LinkedList<>();
        queue.add(positions.get(0));
        visited.add(positions.get(0)[0] + "," + positions.get(0)[1]);

        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

        while (!queue.isEmpty()) {
            int[] current = queue.poll();

            for (int[] dir : directions) {
                int newRow = current[0] + dir[0];
                int newCol = current[1] + dir[1];
                String key = newRow + "," + newCol;

                if (!visited.contains(key)) {
                    for (int[] pos : positions) {
                        if (pos[0] == newRow && pos[1] == newCol) {
                            queue.add(pos);
                            visited.add(key);
                            break;
                        }
                    }
                }
            }
        }

        return visited.size() == positions.size();
    }

    private static boolean isPieceWithinBounds(List<int[]> positions, int rows, int cols) {
        for (int[] pos : positions) {
            if (pos[0] < 0 || pos[0] >= rows || pos[1] < 0 || pos[1] >= cols) {
                return false;
            }
        }
        return true;
    }

    private static boolean hasOverlappingPieces(Map<Character, List<int[]>> piecePositions) {
        Set<String> allPositions = new HashSet<>();
        for (List<int[]> positions : piecePositions.values()) {
            for (int[] pos : positions) {
                String key = pos[0] + "," + pos[1];
                if (!allPositions.add(key)) {
                    return true;
                }
            }
        }
        return false;
    }
}