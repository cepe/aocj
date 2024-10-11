package pl.kaq.aoc.y23.day15;

import pl.kaq.Solution;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Day15 extends Solution {
    @Override
    public String firstStar(String input) {
        var sum = Arrays.stream(input.split(","))
                .map(String::trim)
                .mapToInt(this::hash)
                .sum();
        return Integer.toString(sum);
    }

    private int hash(String string) {
        var hash = 0;
        final var chars = string.toCharArray();
        for (char c : chars) {
            hash += c;
            hash *= 17;
            hash %= 256;
        }

        return hash;
    }

    @Override
    public String secondStar(String input) {
        var boxes = IntStream.range(0, 256)
                .mapToObj(i -> new Box(i))
                .toArray(Box[]::new);

        Arrays.stream(input.split(","))
                .map(this::parseStep)
                .forEach(step -> {
                    switch (step) {
                        case MinusStep ms -> boxes[hash(ms.label())].minus(ms.label());
                        case EqualStep ms -> boxes[hash(ms.label())].addOrUpdate(ms.label(), ms.focalLength());
                    }
                });

        System.out.println(Arrays.toString(boxes));

        return String.valueOf(Arrays.stream(boxes)
                .mapToInt(Box::focalPower)
                .sum());

    }

    private Step parseStep(String stepStr) {
        if (stepStr.endsWith("-")) {
            var label = stepStr.substring(0, stepStr.length() - 1);
            return new MinusStep(label);
        } else {
            final var split = stepStr.split("=");
            return new EqualStep(split[0], Integer.parseInt(split[1]));
        }
    }

    public static void main(String[] args) {
        new Day15().run("ex.in");
    }
}
