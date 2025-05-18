package heuristics;

import core.Board;
import core.Piece;

public class DistanceWithOrientationHeuristic implements Heuristic {
    @Override
    public int calculate(Board board) {
        Piece primaryPiece = board.getPrimaryPiece();
        int exitCol = board.getExitCol();
        int exitRow = board.getExitRow();

        // Hitung jarak Manhattan ke exit
        int distance = Math.abs(primaryPiece.getRow() - exitRow) +
                Math.abs(primaryPiece.getCol() - exitCol);

        // Tambah penalti untuk orientasi yang salah
        int orientationPenalty = 0;
        if (primaryPiece.isHorizontal() && primaryPiece.getRow() != exitRow) {
            orientationPenalty = 3; // Penalti lebih tinggi untuk orientasi yang salah
        } else if (!primaryPiece.isHorizontal() && primaryPiece.getCol() != exitCol) {
            orientationPenalty = 3;
        }

        // Tambah penalti untuk jarak dari baris/kolom exit
        int alignmentPenalty = 0;
        if (primaryPiece.isHorizontal() && primaryPiece.getRow() != exitRow) {
            alignmentPenalty = Math.abs(primaryPiece.getRow() - exitRow) * 2;
        } else if (!primaryPiece.isHorizontal() && primaryPiece.getCol() != exitCol) {
            alignmentPenalty = Math.abs(primaryPiece.getCol() - exitCol) * 2;
        }

        return distance + orientationPenalty + alignmentPenalty;
    }

    @Override
    public String getName() {
        return "Distance with Orientation";
    }
}