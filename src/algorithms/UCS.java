package algorithms;

import java.util.*;
import core.Board;
import core.Piece;
import io.Step;

public class UCS {
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

    public static List<Step> solve(Board initialBoard) {
        PriorityQueue<Node> queue = new PriorityQueue<>();
        Set<String> visited = new HashSet<>();
        List<Step> initialPath = new ArrayList<>();
        initialPath.add(new Step(null, null, 0, initialBoard));
        queue.add(new Node(initialBoard, initialPath, 0));

        int nodesExplored = 0;
        int maxQueueSize = 0;

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            String state = current.board.getState();
            nodesExplored++;
            maxQueueSize = Math.max(maxQueueSize, queue.size());

            if (visited.contains(state)) {
                continue;
            }
            visited.add(state);

            // Print current state
            System.out.println("\nExploring state " + nodesExplored + ":");
            System.out.println("Current cost: " + current.cost);
            Step.printBoard(current.board);

            if (current.board.isGoalState()) {
                System.out.println("\nSolution found!");
                System.out.println("Total nodes explored: " + nodesExplored);
                System.out.println("Maximum queue size: " + maxQueueSize);
                return current.path;
            }

            List<Object[]> possibleMoves = current.board.getPossibleMoves();
            System.out.println("Possible moves: " + possibleMoves.size());
            for (Object[] move : possibleMoves) {
                Piece piece = (Piece) move[0];
                String direction = (String) move[1];
                int steps = (int) move[2];
                System.out.println("  " + piece.getId() + " " + direction + " " + steps);

                Board newBoard = current.board.movePiece(piece, direction, steps);
                if (!visited.contains(newBoard.getState())) {
                    List<Step> newPath = new ArrayList<>(current.path);
                    newPath.add(new Step(piece, direction, steps, newBoard));
                    queue.add(new Node(newBoard, newPath, current.cost + steps));
                }
            }
        }

        System.out.println("\nNo solution found!");
        System.out.println("Total nodes explored: " + nodesExplored);
        System.out.println("Maximum queue size: " + maxQueueSize);
        return null;
    }
}
