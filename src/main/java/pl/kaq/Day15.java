package pl.kaq;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

sealed interface Step permits MinusStep, EqualStep {
}

record MinusStep(String label) implements Step {
}

record EqualStep(String label, Integer focalLength) implements Step {
}

class Box {
    private final LinkedHashMap<String, Integer> lenses = new LinkedHashMap<>();
    private final int boxNumber;

    public Box(int boxNumber) {
        this.boxNumber = boxNumber;
    }

    public void minus(String label) {
        lenses.remove(label);
    }

    public void addOrUpdate(String label, Integer focalLength) {
        lenses.put(label, focalLength);
    }

    public int focalPower() {
        var sum = new AtomicInteger();
        var position = new AtomicInteger(1);

        lenses.forEach((key, value) -> {
            final var delta = (boxNumber + 1) * position.get() * value;
            sum.addAndGet(delta);
            position.getAndIncrement();
        });

        return sum.get();
    }

    @Override
    public String toString() {
        return lenses.toString();
    }
}

public class Day15 extends Solution {
    @Override
    String firstStar(String input) {
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
    String secondStar(String input) {
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
