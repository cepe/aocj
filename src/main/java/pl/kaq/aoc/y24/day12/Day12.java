package pl.kaq.aoc.y24.day12;

import pl.kaq.Solution;
import pl.kaq.model.Board;
import pl.kaq.model.Position;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

record Prices(long fencingPrice, long discountPrice) {
}

public class Day12 extends Solution {
    @Override
    public String firstStar(String input) {
        return Long.toString(calculatePrices(board(input)).fencingPrice());
    }

    private Prices calculatePrices(Board orginalBoard) {
        var board = orginalBoard.upscale(3);
        var seen = new HashSet<Position>();
        var visited = new HashSet<Position>();



        var fencingPrice = 0L;
        var discountedPrice = 0L;
        for (var position : board.positions()) {
            if (!visited.contains(position)) {
                var toVisit = new LinkedHashSet<Position>();
                var horizontal = new HashSet<Position>();
                var vertical = new HashSet<Position>();
                seen.add(position);
                toVisit.add(position);
                var area = 0L;
                var perimeter = 0L;
                while (!toVisit.isEmpty()) {
                    var current = toVisit.removeFirst();
                    visited.add(current);
                    area += 1;
                    perimeter += current.around().stream().filter(notSameAs(board, current)).toList().size();

                    vertical.addAll(current.aroundHorizontal().stream().filter(notSameAs(board, current)).toList());
                    horizontal.addAll(current.aroundVertical().stream().filter(notSameAs(board, current)).toList());

                    var next = current.around().stream()
                            .filter(board::onBoard)
                            .filter(pos -> board.at(pos) == board.at(current))
                            .filter(Predicate.not(seen::contains))
                            .toList();
                    seen.addAll(next);
                    toVisit.addAll(next);
                }
                fencingPrice += (area / 9) * (perimeter / 3);
                discountedPrice += (area / 9) * (countVerticalSides(vertical) + countHorizontalSides(horizontal));
            }
        }
        return new Prices(fencingPrice, discountedPrice);
    }

    private long countHorizontalSides(Set<Position> horizontal) {
        return horizontal.stream()
                .collect(Collectors.groupingBy(
                        Position::row,
                        Collectors.mapping(Position::col, Collectors.toList()))
                ).values().stream()
                .mapToLong(this::countSides)
                .sum();
    }

    private long countVerticalSides(Set<Position> vertical) {
        return vertical.stream()
                .collect(Collectors.groupingBy(
                        Position::col,
                        Collectors.mapping(Position::row, Collectors.toList()))
                ).values().stream()
                .mapToLong(this::countSides)
                .sum();
    }

    private long countSides(List<Integer> cords) {
        Collections.sort(cords);
        var count = 0L;
        var prev = Integer.MIN_VALUE;
        for (var cord : cords) {
            if (prev + 1 != cord) {
                count += 1;
            }
            prev = cord;
        }
        return count;
    }

    private static Predicate<Position> notSameAs(Board board, Position current) {
        return pos1 -> !board.onBoard(pos1) || (board.onBoard(pos1) && board.at(pos1) != board.at(current));
    }

    @Override
    public String secondStar(String input) {
        return Long.toString(calculatePrices(board(input)).discountPrice());
    }

    public static void main(String[] args) {

        new Day12().run("input.in");
    }
}