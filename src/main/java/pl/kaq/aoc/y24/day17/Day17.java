package pl.kaq.aoc.y24.day17;

import pl.kaq.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Day17 extends Solution {

    @Override
    public String firstStar(String input) {
        return nativeCode(18427963).stream().map(i -> Long.toString(i)).collect(Collectors.joining(","));
    }

    public List<Long> nativeCode(long a) {
        long b;
        long c;
        var result = new ArrayList<Long>();
        while (a != 0) {
            b = a % 8;
            b = b ^ 1;
            c = a / (1L << b);
            b = b ^ c;
            b = b ^ 6;
            result.add(b % 8);
            a = a / 8;
        }
        return result;
    }

    public long reverse(List<Integer> digits) {
        var upTo8 = LongStream.range(0, 8).boxed().toList();
        var possible = LongStream.range(0, 8).boxed().collect(Collectors.toSet());
        for (var digit : digits) {
            possible = possible.stream()
                    .filter(a -> {
                        long b = a % 8;
                        b = b ^ 1;
                        long c = a / (1L << b);
                        b = b ^ c;
                        b = b ^ 6;
                        return (b % 8) == digit;
                    })
                    .flatMap(p -> upTo8.stream().map(u -> p * 8 + u))
                    .collect(Collectors.toSet());
        }

        return possible.stream().mapToLong(p -> p / 8).min().orElseThrow();
    }


    @Override
    public String secondStar(String input) {
        return Long.toString(reverse(List.of(2, 4, 1, 1, 7, 5, 0, 3, 4, 3, 1, 6, 5, 5, 3, 0).reversed()));
    }


    public static void main(String[] args) {
        new Day17().run("input.in");
    }
}
