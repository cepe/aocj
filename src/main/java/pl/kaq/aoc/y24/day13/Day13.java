package pl.kaq.aoc.y24.day13;

import pl.kaq.Solution;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

record Cord(long x, long y) {
}

record Machine(Cord a, Cord b, Cord prize) {
}

public class Day13 extends Solution {
    @Override
    public String firstStar(String input) {
        var machines = parseMachines(input);
        return Long.toString(machines.stream()
                .map(this::minimumToWin)
                .filter(Optional::isPresent)
                .mapToLong(Optional::get)
                .sum());
    }

    private Optional<Long> minimumToWin(Machine machine) {
        var a = machine.a();
        var b = machine.b();
        var c = machine.prize();

        var det = det(a.x(), b.x(), a.y(), b.y());
        if (det == 0) {
            return Optional.empty();
        } else {
            var detX = det(c.x(), b.x(), c.y(), b.y());
            var detY = det(a.x(), c.x(), a.y(), c.y());
            if (detX % det == 0 && detY % det == 0) {
                return Optional.of(detX / det * 3 + detY / det);
            } else {
                return Optional.empty();
            }
        }
    }

    private long det(long a1, long b1, long a2, long b2) {
        return a1 * b2 - a2 * b1;
    }

    private List<Machine> parseMachines(String input) {
        return Arrays.stream(input.split("\n\n")).map(this::parseMachine).toList();
    }

    private Machine parseMachine(String machineStr) {
        var split = machineStr.split("\n");
        var a = Pattern.compile(".*X\\+(\\d+), Y\\+(\\d+)").matcher(split[0]);
        var b = Pattern.compile(".*X\\+(\\d+), Y\\+(\\d+)").matcher(split[1]);
        var prize = Pattern.compile(".*X=(\\d+), Y=(\\d+)").matcher(split[2]);
        if (a.matches() && b.matches() && prize.matches()) {
            return new Machine(
                    new Cord(parseLong(a.group(1)), parseInt(a.group(2))),
                    new Cord(parseLong(b.group(1)), parseInt(b.group(2))),
                    new Cord(parseLong(prize.group(1)), parseInt(prize.group(2)))
            );
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public String secondStar(String input) {
        var machines = parseMachines(input);
        return Long.toString(machines.stream()
                .map(this::fixMachine)
                .map(this::minimumToWin)
                .filter(Optional::isPresent)
                .mapToLong(Optional::get)
                .sum());
    }

    private Machine fixMachine(Machine machine) {
        return new Machine(
                machine.a(),
                machine.b(),
                new Cord(
                        machine.prize().x() + 10000000000000L,
                        machine.prize().y() + 10000000000000L
                )
        );
    }

    public static void main(String[] args) {
        new Day13().run("input.in");
    }
}