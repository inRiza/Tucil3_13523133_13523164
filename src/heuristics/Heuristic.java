package heuristics;

import core.Board;

public interface Heuristic {
    int calculate(Board board);
}