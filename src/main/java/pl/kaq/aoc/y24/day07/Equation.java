package pl.kaq.aoc.y24.day07;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

import static java.util.stream.Collectors.toSet;

record Equation(Long sum, List<Long> nums) {
    static Equation parse(String equationStr) {
        var split = equationStr.split(": ");
        var sum = Long.parseLong(split[0]);
        var nums = Arrays.stream(split[1].split(" "))
                .map(Long::parseLong)
                .toList();
        return new Equation(sum, nums);
    }

    public boolean fixable() {
        return fixable(List.of(Long::sum, LongUtils::mul));
    }

    private boolean fixable(List<BiFunction<Long, Long, Long>> operations) {
        var possibilities = Set.of(nums.getFirst());
        for (var i = 1; i < nums.size(); i++) {
            var num = nums.get(i);
            possibilities = possibilities.stream()
                    .flatMap(possibility -> operations.stream().map(op -> op.apply(possibility, num)))
                    .collect(toSet());
        }
        return possibilities.contains(sum);
    }

    public boolean fixableWithConcat() {
        return fixable(List.of(Long::sum, LongUtils::mul, LongUtils::concat));
    }

}
