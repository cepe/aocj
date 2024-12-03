package pl.kaq.aoc.y24.day01;

import pl.kaq.Solution;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

record Pair<T>(T first, T second) {
}

public class Day01 extends Solution {
    @Override
    public String firstStar(String input) {
        var pairs = input.lines().map(this::toLongPair).toList();
        var firstColumn = pairs.stream().map(Pair::first).sorted().toList();
        var secondColumn = pairs.stream().map(Pair::second).sorted().toList();
        var result = 0L;
        for (int i = 0; i < firstColumn.size(); i++) {
            result += Math.abs(firstColumn.get(i) - secondColumn.get(i));
        }
        return Long.toString(result);
    }

    @Override
    public String secondStar(String input) {
        var pairs = input.lines().map(this::toLongPair).toList();
        var counts = pairs.stream().map(Pair::second).collect(groupingBy(identity(), counting()));
        return Long.toString(pairs.stream().map(Pair::first).mapToLong(i -> i * counts.getOrDefault(i, 0L)).sum());
    }

    private Pair<Long> toLongPair(String s) {
        var numbers = s.split("\\s+");
        return new Pair<>(Long.parseLong(numbers[0]), Long.parseLong(numbers[1]));
    }

    public static void main(String[] args) {
        new Day01().run("input.in");
    }
}
