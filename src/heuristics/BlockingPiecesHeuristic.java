package heuristics;

import core.Board;
import core.Piece;

public class BlockingPiecesHeuristic implements Heuristic {
    @Override
    public int calculate(Board board) {
        int blockingPieces = 0;
        Piece primaryPiece = board.getPrimaryPiece();
        int exitCol = board.getExitCol();
        int exitRow = board.getExitRow();

        // Ambil baris dan kolom dari piece utama
        int primaryRow = primaryPiece.getRow();
        int primaryCol = primaryPiece.getCol();
        int primaryLength = primaryPiece.getLength();

        // Cek blok horizontal (piece di baris yang sama)
        for (int col = primaryCol + primaryLength; col < board.getCols(); col++) {
            if (board.getGrid()[primaryRow][col] != '.') {
                blockingPieces += 2; // Bobot lebih tinggi untuk blok horizontal
            }
        }

        // Cek blok vertikal (piece yang mungkin perlu dipindahkan)
        for (int row = 0; row < board.getRows(); row++) {
            if (row != primaryRow) { // Lewati baris piece utama
                for (int col = primaryCol; col < primaryCol + primaryLength; col++) {
                    if (board.getGrid()[row][col] != '.') {
                        blockingPieces++; // Bobot lebih rendah untuk blok vertikal
                    }
                }
            }
        }

        return blockingPieces;
    }

    @Override
    public String getName() {
        return "Blocking Pieces";
    }
}