package algorithms;

import java.util.*;
import core.Board;
import core.Piece;
import io.Step;
import heuristics.Heuristic;

public class GreedyBestFirstSearch {
    private static int nodesExplored = 0;
    private static int maxQueueSize = 0;
    private static long startTime;
    private static Heuristic heuristic;

    private static class Node implements Comparable<Node> {
        Board board;
        List<Step> path;
        int heuristicValue;

        Node(Board board, List<Step> path, int heuristicValue) {
            this.board = board;
            this.path = path;
            this.heuristicValue = heuristicValue;
        }

        public int compareTo(Node other) {
            return Integer.compare(this.heuristicValue, other.heuristicValue);
        }
    }

    public static List<Step> solve(Board initialBoard, Heuristic heuristicFunc) {
        // Reset statistics
        nodesExplored = 0;
        maxQueueSize = 0;
        startTime = System.currentTimeMillis();
        heuristic = heuristicFunc;

        // PriorityQueue untuk menyimpan node yang akan dieksplorasi
        // Node dengan nilai heuristik terendah akan dieksplorasi terlebih dahulu
        PriorityQueue<Node> queue = new PriorityQueue<>();

        // Set untuk menyimpan state yang sudah dikunjungi
        Set<String> visited = new HashSet<>();

        // Inisialisasi path awal dengan state awal
        List<Step> initialPath = new ArrayList<>();
        initialPath.add(new Step(null, null, 0, initialBoard));
        int initialHeuristic = heuristic.calculate(initialBoard);
        queue.add(new Node(initialBoard, initialPath, initialHeuristic));

        // Loop utama algoritma Greedy Best First Search
        while (!queue.isEmpty()) {
            // Ambil node dengan nilai heuristik terendah dari queue
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
            System.out.println("Current heuristic value: " + current.heuristicValue);
            Step.printBoard(current.board);

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
                    int newHeuristic = heuristic.calculate(newBoard);
                    queue.add(new Node(newBoard, newPath, newHeuristic));
                }
            }
        }
        
        long endTime = System.currentTimeMillis();
        double timeInSeconds = (endTime - startTime) / 1000.0;
        System.out.println("\nNo solution found!");
        System.out.printf("Exploration time: %.3f seconds%n", timeInSeconds);
        System.out.println("Total nodes explored: " + nodesExplored);
        System.out.println("Maximum queue size: " + maxQueueSize);
        return null;
    }

    public static int getNodesExplored() {
        return nodesExplored;
    }

    public static int getMaxQueueSize() {
        return maxQueueSize;
    }
}