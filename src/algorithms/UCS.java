package algorithms;

import java.util.*;
import core.Board;
import core.Piece;
import io.Step;

/**
 * Implementasi algoritma Uniform Cost Search (UCS) untuk menyelesaikan Rush
 * Hour puzzle.
 * UCS adalah algoritma pencarian yang memilih node dengan cost terendah untuk
 * dieksplorasi.
 * Dalam kasus ini, cost adalah jumlah total langkah yang diperlukan untuk
 * mencapai state tersebut.
 */
public class UCS {
    /**
     * Kelas Node untuk merepresentasikan state dalam pencarian.
     * Setiap node menyimpan:
     * - board: konfigurasi papan saat ini
     * - path: urutan langkah untuk mencapai state ini
     * - cost: total cost (jumlah langkah) untuk mencapai state ini
     */
    private static class Node implements Comparable<Node> {
        Board board;
        List<Step> path;
        int cost;

        Node(Board board, List<Step> path, int cost) {
            this.board = board;
            this.path = path;
            this.cost = cost;
        }

        /**
         * Membandingkan node berdasarkan cost untuk PriorityQueue
         * Node dengan cost lebih rendah akan dieksplorasi terlebih dahulu
         */
        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.cost, other.cost);
        }
    }


     /** 
     * param initialBoard Konfigurasi awal papan
     * return List of Step yang merepresentasikan solusi (ini biar loop stepnya bisa dijalankan), atau null jika tidak ada
     */  
    public static List<Step> solve(Board initialBoard) {
        // PriorityQueue untuk menyimpan node yang akan dieksplorasi
        // Node dengan cost terendah akan dieksplorasi terlebih dahulu
        PriorityQueue<Node> queue = new PriorityQueue<>();

        // Set untuk menyimpan state yang sudah dikunjungi
        // Mencegah eksplorasi state yang sama berulang kali
        Set<String> visited = new HashSet<>();

        // Inisialisasi path awal dengan state awal
        List<Step> initialPath = new ArrayList<>();
        initialPath.add(new Step(null, null, 0, initialBoard));
        queue.add(new Node(initialBoard, initialPath, 0));

        // Variabel untuk statistik pencarian
        int nodesExplored = 0;
        int maxQueueSize = 0;

        // Loop utama algoritma UCS
        while (!queue.isEmpty()) {
            // Ambil node dengan cost terendah dari queue
            Node current = queue.poll();
            String state = current.board.getState();
            nodesExplored++;
            maxQueueSize = Math.max(maxQueueSize, queue.size());

            // Skip jika state sudah pernah dikunjungi
            if (visited.contains(state)) {
                continue;
            }
            visited.add(state);

            // Tampilkan informasi state yang sedang dieksplorasi
            System.out.println("\nExploring state " + nodesExplored + ":");
            System.out.println("Current cost: " + current.cost);
            Step.printBoard(current.board);

            // Cek apakah state saat ini adalah goal state
            if (current.board.isGoalState()) {
                System.out.println("\nSolution found!");
                System.out.println("Total nodes explored: " + nodesExplored);
                System.out.println("Maximum queue size: " + maxQueueSize);
                return current.path;
            }

            // Dapatkan semua kemungkinan gerakan dari state saat ini
            List<Object[]> possibleMoves = current.board.getPossibleMoves();
            System.out.println("Possible moves: " + possibleMoves.size());

            // Eksplorasi setiap kemungkinan gerakan
            for (Object[] move : possibleMoves) {
                Piece piece = (Piece) move[0];
                String direction = (String) move[1];
                int steps = (int) move[2];
                System.out.println("  " + piece.getId() + " " + direction + " " + steps);

                // Buat state baru dengan melakukan gerakan
                Board newBoard = current.board.movePiece(piece, direction, steps);

                // Jika state baru belum pernah dikunjungi, tambahkan ke queue
                if (!visited.contains(newBoard.getState())) {
                    List<Step> newPath = new ArrayList<>(current.path);
                    newPath.add(new Step(piece, direction, steps, newBoard));
                    queue.add(new Node(newBoard, newPath, current.cost + steps));
                }
            }
        }

        // Jika queue kosong dan tidak ada solusi yang ditemukan
        System.out.println("\nNo solution found!");
        System.out.println("Total nodes explored: " + nodesExplored);
        System.out.println("Maximum queue size: " + maxQueueSize);
        return null;
    }
}
