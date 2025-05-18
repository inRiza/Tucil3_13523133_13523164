package algorithms;

import java.util.*;
import core.Board;
import core.Piece;
import core.Step;

/**
 * Implementasi algoritma Dijkstra untuk menyelesaikan Rush Hour puzzle.
 * Dijkstra adalah algoritma pencarian yang memilih node dengan cost terendah
 * untuk
 * dieksplorasi, mirip dengan UCS namun dengan beberapa optimisasi tambahan.
 */
public class Dijkstra {
    private static int nodesExplored = 0;
    private static int maxQueueSize = 0;
    private static long startTime;

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

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.cost, other.cost);
        }
    }

    /**
     * @param initialBoard Konfigurasi awal papan
     * @return List of Step yang merepresentasikan solusi, atau null jika tidak ada
     */
    public static List<Step> solve(Board initialBoard) {
        // Reset statistics
        nodesExplored = 0;
        maxQueueSize = 0;
        startTime = System.currentTimeMillis();

        // PriorityQueue untuk menyimpan node yang akan dieksplorasi
        PriorityQueue<Node> queue = new PriorityQueue<>();

        // Set untuk menyimpan state yang sudah dikunjungi
        Set<String> visited = new HashSet<>();

        // Map untuk menyimpan cost terendah untuk setiap state
        Map<String, Integer> distance = new HashMap<>();

        // Inisialisasi path awal
        List<Step> initialPath = new ArrayList<>();
        initialPath.add(new Step(null, null, 0, initialBoard));
        queue.add(new Node(initialBoard, initialPath, 0));
        distance.put(initialBoard.getState(), 0);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            String state = current.board.getState();
            nodesExplored++;
            maxQueueSize = Math.max(maxQueueSize, queue.size());

            // Skip jika state sudah pernah dikunjungi dengan cost lebih rendah
            if (visited.contains(state) && distance.get(state) < current.cost) {
                continue;
            }
            visited.add(state);

            // Cek apakah state saat ini adalah goal state
            if (current.board.isGoalState()) {
                long endTime = System.currentTimeMillis();
                double timeInSeconds = (endTime - startTime) / 1000.0;
                System.out.println("\nSolution found!");
                System.out.printf("Exploration time: %.3f seconds%n", timeInSeconds);
                System.out.println("Total nodes explored: " + nodesExplored);
                System.out.println("Maximum queue size: " + maxQueueSize);
                return current.path;
            }

            // Dapatkan semua kemungkinan gerakan
            List<Object[]> possibleMoves = current.board.getPossibleMoves();

            // Eksplorasi setiap kemungkinan gerakan
            for (Object[] move : possibleMoves) {
                Piece piece = (Piece) move[0];
                String direction = (String) move[1];
                int steps = (int) move[2];

                // Buat state baru dengan melakukan gerakan
                Board newBoard = current.board.movePiece(piece, direction, steps);
                String newState = newBoard.getState();
                int newCost = current.cost + steps;

                // Jika state baru belum pernah dikunjungi atau memiliki cost lebih rendah
                if (!distance.containsKey(newState) || newCost < distance.get(newState)) {
                    distance.put(newState, newCost);
                    List<Step> newPath = new ArrayList<>(current.path);
                    newPath.add(new Step(piece, direction, steps, newBoard));
                    queue.add(new Node(newBoard, newPath, newCost));
                }
            }
        }

        // Jika queue kosong dan tidak ada solusi yang ditemukan
        long endTime = System.currentTimeMillis();
        double timeInSeconds = (endTime - startTime) / 1000.0;
        System.out.println("\nNo solution found!");
        System.out.printf("Exploration time: %.3f seconds%n", timeInSeconds);
        System.out.println("Total nodes explored: " + nodesExplored);
        System.out.println("Maximum queue size: " + maxQueueSize);
        return null;
    }

    /**
     * Mengembalikan jumlah node yang dieksplorasi
     * 
     * @return banyak node yang dieksplorasi
     */
    public static int getNodesExplored() {
        return nodesExplored;
    }

    /**
     * Mengembalikan ukuran maksimum queue
     * 
     * @return ukuran maksimum queue
     */
    public static int getMaxQueueSize() {
        return maxQueueSize;
    }
}