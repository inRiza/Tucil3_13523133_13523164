package core;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private int rows, cols;
    private char[][] grid;
    private List<Piece> pieces;
    private Piece primaryPiece;
    private int exitRow, exitCol;

    public Board(int rows, int cols, char[][] grid, List<Piece> pieces, int exitRow, int exitCol) {
        this.rows = rows;
        this.cols = cols;
        this.grid = grid;
        this.pieces = pieces;
        this.exitRow = exitRow;
        this.exitCol = exitCol;
        // Find primary piece
        for (Piece p : pieces) {
            if (p.isPrimary()) {
                this.primaryPiece = p;
                break;
            }
        }
        if (this.primaryPiece == null) {
            throw new IllegalStateException("No primary piece (P) found in the board");
        }
    }

    // Deep copy constructor
    public Board(Board other) {
        this.rows = other.rows;
        this.cols = other.cols;
        this.grid = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(other.grid[i], 0, this.grid[i], 0, cols);
        }
        this.pieces = new ArrayList<>();
        for (Piece p : other.pieces) {
            Piece newPiece = new Piece(p.getId(), p.getRow(), p.getCol(), p.getSize(), p.getOrientation(),
                    p.isPrimary());
            this.pieces.add(newPiece);
            if (p.isPrimary()) {
                this.primaryPiece = newPiece;
            }
        }
        this.exitRow = other.exitRow;
        this.exitCol = other.exitCol;
    }

    // Check if a move is valid
    public boolean isValidMove(Piece piece, String direction, int steps) {
        int newRow = piece.getRow();
        int newCol = piece.getCol();

        // Calculate new position
        if (piece.getOrientation().equals("horizontal")) {
            if (direction.equals("left")) {
                newCol -= steps;
            } else if (direction.equals("right")) {
                newCol += steps;
            } else {
                return false;
            }
        } else {
            if (direction.equals("up")) {
                newRow -= steps;
            } else if (direction.equals("down")) {
                newRow += steps;
            } else {
                return false;
            }
        }

        // Check boundaries
        if (newRow < 0 || newCol < 0) {
            return false;
        }

        // Check if piece would go out of bounds
        if (piece.getOrientation().equals("horizontal")) {
            if (newCol + piece.getSize() > cols) {
                return false;
            }
        } else {
            if (newRow + piece.getSize() > rows) {
                return false;
            }
        }

        // Create temporary grid without the moving piece
        char[][] tempGrid = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                tempGrid[i][j] = grid[i][j] == 'K' ? 'K' : '.';
            }
        }

        // Place all other pieces
        for (Piece p : pieces) {
            if (p != piece) {
                for (int[] cell : p.getOccupiedCells()) {
                    tempGrid[cell[0]][cell[1]] = p.getId();
                }
            }
        }

        // Check if new position is free
        for (int i = 0; i < piece.getSize(); i++) {
            int r = piece.getOrientation().equals("horizontal") ? newRow : newRow + i;
            int c = piece.getOrientation().equals("horizontal") ? newCol + i : newCol;
            if (tempGrid[r][c] != '.' && tempGrid[r][c] != 'K') {
                return false;
            }
        }

        return true;
    }

    // Move a piece and return new board
    public Board movePiece(Piece piece, String direction, int steps) {
        Board newBoard = new Board(this);
        int pieceIndex = pieces.indexOf(piece);
        if (pieceIndex == -1) {
            throw new IllegalArgumentException("Piece not found in the board");
        }
        Piece movedPiece = newBoard.pieces.get(pieceIndex);
        int newRow = movedPiece.getRow();
        int newCol = movedPiece.getCol();
        if (direction.equals("left"))
            newCol -= steps;
        else if (direction.equals("right"))
            newCol += steps;
        else if (direction.equals("up"))
            newRow -= steps;
        else if (direction.equals("down"))
            newRow += steps;

        // Update grid
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                newBoard.grid[i][j] = newBoard.grid[i][j] == 'K' ? 'K' : '.';
            }
        }
        movedPiece = new Piece(movedPiece.getId(), newRow, newCol, movedPiece.getSize(), movedPiece.getOrientation(),
                movedPiece.isPrimary());
        newBoard.pieces.set(pieceIndex, movedPiece);
        if (movedPiece.isPrimary()) {
            newBoard.primaryPiece = movedPiece;
        }
        for (Piece p : newBoard.pieces) {
            for (int[] cell : p.getOccupiedCells()) {
                newBoard.grid[cell[0]][cell[1]] = p.getId();
            }
        }
        return newBoard;
    }

    // Get all possible moves
    public List<Object[]> getPossibleMoves() {
        List<Object[]> moves = new ArrayList<>();
        for (Piece piece : pieces) {
            String[] directions = piece.getOrientation().equals("horizontal") ? new String[] { "left", "right" }
                    : new String[] { "up", "down" };
            for (String dir : directions) {
                int maxSteps = piece.getOrientation().equals("horizontal")
                        ? (dir.equals("left") ? piece.getCol() : cols - piece.getCol() - piece.getSize())
                        : (dir.equals("up") ? piece.getRow() : rows - piece.getRow() - piece.getSize());
                for (int steps = 1; steps <= maxSteps; steps++) {
                    if (isValidMove(piece, dir, steps)) {
                        moves.add(new Object[] { piece, dir, steps });
                    } else {
                        break;
                    }
                }
            }
        }
        return moves;
    }

    // Check if goal state is reached
    public boolean isGoalState() {
        // For horizontal primary piece, check if its right end reaches the last column
        if (primaryPiece.getOrientation().equals("horizontal")) {
            int rightEnd = primaryPiece.getCol() + primaryPiece.getSize() - 1;
            return primaryPiece.getRow() == exitRow && rightEnd == cols - 1;
        }
        // For vertical primary piece, check if its bottom end reaches the last row
        else {
            int bottomEnd = primaryPiece.getRow() + primaryPiece.getSize() - 1;
            return primaryPiece.getCol() == exitCol && bottomEnd == rows - 1;
        }
    }

    // Get board state as string for hashing
    public String getState() {
        StringBuilder sb = new StringBuilder();
        for (Piece p : pieces) {
            sb.append(p.getId()).append(p.getRow()).append(p.getCol());
        }
        return sb.toString();
    }

    // Getters
    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public char[][] getGrid() {
        return grid;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public int getExitRow() {
        return exitRow;
    }

    public int getExitCol() {
        return exitCol;
    }

    public Piece getPrimaryPiece() {
        return primaryPiece;
    }
}