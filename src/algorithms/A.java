package algorithms;

import java.util.*;
import core.Board;
import core.Piece;
import core.Step;
import heuristics.Heuristic;

public class A {
    private static int nodesExplored = 0;
    private static int maxQueueSize = 0;
    private static long startTime;
    private static Heuristic heuristic;
    
    private static class Node implements Comparable<Node>{
        Board board;
        List<Step> path;
        int cost;
        int heuristicValue;
        int total;

        Node(Board board, List<Step> path, int heuristicValue, int cost) {
            this.board = board;
            this.path = path;
            this.heuristicValue = heuristicValue;
            this.cost = cost;
            this.total = heuristicValue + cost;
        }

        public int compareTo(Node other) {
            return Integer.compare(this.total, other.total);
        }   
    }

    public static List<Step> solve(Board initialBoard,  Heuristic heuristicFunc){
        nodesExplored = 0;
        maxQueueSize = 0;
        startTime = System.currentTimeMillis();
        heuristic = heuristicFunc;

        PriorityQueue<Node> queue = new PriorityQueue<>();

        Map<String, Integer> Score = new HashMap<>();
        Set<String> visited = new HashSet<>();

        List<Step> initialPath = new ArrayList<>();
        initialPath.add(new Step(null, null, 0, initialBoard));
        int initialH = heuristic.calculate(initialBoard);
        queue.add(new Node(initialBoard, initialPath, 0, initialH));
        Score.put(initialBoard.getState(), 0);

        while (!queue.isEmpty()) {
            // Ambil node dengan nilai f terendah dari openSet
            Node current = queue.poll();
            String state = current.board.getState();
            nodesExplored++;
            maxQueueSize = Math.max(maxQueueSize, queue.size());

            // Jika state sudah diproses sepenuhnya, skip
            if (visited.contains(state)) {
                continue;
            }
            
            // Tampilkan informasi state yang sedang dieksplorasi
            System.out.println("\nExploring state " + nodesExplored + ":");
            System.out.println("g: " + current.cost + ", h: " + current.heuristicValue + ", f: " + current.total);
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

            // Tandai state sebagai sudah diproses sepenuhnya
            visited.add(state);

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
                String newState = newBoard.getState();
                
                // Jika state baru sudah diproses sepenuhnya, skip
                if (visited.contains(newState)) {
                    continue;
                }
                

                int newCost = current.cost + steps;
                
                // Jika state baru belum pernah ditemui atau memiliki g yang lebih baik
                if (!Score.containsKey(newState) || newCost < Score.get(newState)) {
                    // Update gScore
                    Score.put(newState, newCost);
                    
                    // Buat path baru
                    List<Step> newPath = new ArrayList<>(current.path);
                    newPath.add(new Step(piece, direction, steps, newBoard));
                    
                    // Hitung nilai heuristik
                    int newH = heuristic.calculate(newBoard);
                    
                    queue.add(new Node(newBoard, newPath, newCost, newH));
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
