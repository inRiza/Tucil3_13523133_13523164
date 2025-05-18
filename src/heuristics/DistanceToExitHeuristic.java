package heuristics;

import core.Board;
import core.Piece;

public class DistanceToExitHeuristic implements Heuristic {

    @Override
    public int calculate(Board board) {
        Piece primaryPiece = board.getPrimaryPiece();

        if (primaryPiece.getOrientation().equals("horizontal")) {
            // Untuk primary piece horizontal, hitung jarak dari ujung kanan primary piece
            // ke pintu keluar
            int rightEnd = primaryPiece.getCol() + primaryPiece.getSize() - 1;
            int distance = board.getCols() - 1 - rightEnd;
            return distance;
        } else {
            // Untuk primary piece vertikal, hitung jarak dari ujung bawah primary piece ke
            // pintu keluar
            int bottomEnd = primaryPiece.getRow() + primaryPiece.getSize() - 1;
            int distance = board.getRows() - 1 - bottomEnd;
            return distance;
        }
    }

    @Override
    public String getName() {
        return "Distance to Exit";
    }
}