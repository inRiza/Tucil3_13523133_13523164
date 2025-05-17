package core;

public class Piece {
    private char id;
    private int row;
    private int col;
    private int size;
    private String orientation;
    private boolean isPrimary;

    public Piece(char id, int row, int col, int size, String orientation, boolean isPrimary) {
        this.id = id;
        this.row = row;
        this.col = col;
        this.size = size;
        this.orientation = orientation;
        this.isPrimary = isPrimary;
    }

    public char getId() {
        return id;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getSize() {
        return size;
    }

    public String getOrientation() {
        return orientation;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    // Get occupied cells
    public int[][] getOccupiedCells() {
        int[][] cells = new int[size][2];
        for (int i = 0; i < size; i++) {
            if (orientation.equals("horizontal")) {
                cells[i][0] = row;
                cells[i][1] = col + i;
            } else {
                cells[i][0] = row + i;
                cells[i][1] = col;
            }
        }
        return cells;
    }
}