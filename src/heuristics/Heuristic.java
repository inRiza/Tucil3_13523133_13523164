package heuristics;

import core.Board;

/**
 * Interface untuk semua heuristik yang digunakan dalam algoritma pencarian.
 * Setiap heuristik harus mengimplementasikan metode calculate untuk menghitung
 * nilai heuristik dari suatu state papan.
 */
public interface Heuristic {
    /**
     * Calculate the heuristic value for a given board state
     * 
     * @param board The current board state
     * @return The heuristic value (lower is better)
     */
    int calculate(Board board);

    /**
     * Get the name of the heuristic
     * 
     * @return The name of the heuristic
     */
    String getName();
}
