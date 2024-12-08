package pl.kaq.aoc.y24.day08;

import pl.kaq.Solution;
import pl.kaq.model.Board;
import pl.kaq.model.Position;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day08 extends Solution {
    @Override
    public String firstStar(String input) {
        var board = board(input);
        var groups = board.positions().stream()
                .filter(position -> board.at(position) != '.')
                .collect(Collectors.groupingBy(board::at));
        return Integer.toString(count(board, groups));
    }

    private int count(Board board, Map<Character, List<Position>> groups) {
        var positions = new HashSet<>();
        for (var cords : groups.values()) {
            for (var i = 0; i < cords.size(); i++) {
                for (var j = i + 1; j < cords.size(); j++) {
                    var a = cords.get(i);
                    var b = cords.get(j);
                    var vector = b.minus(a);
                    if (board.onBoard(a.minus(vector))) {
                        positions.add(a.minus(vector));
                    }
                    if (board.onBoard(b.plus(vector))) {
                        positions.add(b.plus(vector));
                    }
                }
            }
        }
        return positions.size();
    }

    private int countMany(Board board, Map<Character, List<Position>> groups) {
        var positions = new HashSet<>();
        for (var cords : groups.values()) {
            for (var i = 0; i < cords.size(); i++) {
                for (var j = i + 1; j < cords.size(); j++) {
                    var a = cords.get(i);
                    var b = cords.get(j);
                    var vector = b.minus(a);

                    var pos = a;
                    while (board.onBoard(pos)) {
                        positions.add(pos);
                        pos = pos.minus(vector);
                    }

                    pos = b;
                    while (board.onBoard(pos)) {
                        positions.add(pos);
                        pos = pos.plus(vector);
                    }
                }
            }
        }
        return positions.size();
    }

    @Override
    public String secondStar(String input) {
        var board = board(input);
        var groups = board.positions().stream()
                .filter(position -> board.at(position) != '.')
                .collect(Collectors.groupingBy(board::at));
        return Integer.toString(countMany(board, groups));
    }

    public static void main(String[] args) {
        new Day08().run("input.in");
    }
}