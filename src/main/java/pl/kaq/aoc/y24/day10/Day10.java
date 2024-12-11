package pl.kaq.aoc.y24.day10;

import pl.kaq.Solution;
import pl.kaq.model.Board;
import pl.kaq.model.Position;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;


public class Day10 extends Solution {
    @Override
    public String firstStar(String input) {
        var board = board(input);
        return Long.toString(board.positions().stream()
                .filter(position -> board.at(position) == '0')
                .mapToLong(position -> countTops(board, position))
                .sum());
    }

    private long countTops(Board board, Position startPosition) {
        return reachablePositions(board, startPosition).keySet().stream()
                .filter(position -> board.at(position) == '9')
                .count();
    }

    @Override
    public String secondStar(String input) {
        var board = board(input);
        return Long.toString(board.positions().stream()
                .filter(position -> board.at(position) == '0')
                .mapToLong(position -> countRating(board, position))
                .sum());
    }

    private long countRating(Board board, Position startPosition) {
        return reachablePositions(board, startPosition).entrySet().stream()
                .filter(entry -> board.at(entry.getKey()) == '9')
                .mapToInt(Map.Entry::getValue)
                .sum();
    }

    private static Map<Position, Integer> reachablePositions(Board board, Position startPosition) {
        var reachable = new HashMap<Position, Integer>();
        var toVisit = new LinkedHashSet<Position>();
        toVisit.add(startPosition);
        reachable.put(startPosition, 1);

        while (!toVisit.isEmpty()) {
            var current = toVisit.removeFirst();
            var next = current.around().stream()
                    .filter(board::onBoard)
                    .filter(position -> board.at(position) == board.at(current) + 1)
                    .toList();
            next.forEach(pos -> reachable.put(pos, reachable.getOrDefault(pos, 0) + reachable.get(current)));
            toVisit.addAll(next);
        }
        return reachable;
    }

    public static void main(String[] args) {
        new Day10().run("input.in");
    }
}