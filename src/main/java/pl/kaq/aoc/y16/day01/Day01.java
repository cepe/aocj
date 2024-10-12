package pl.kaq.aoc.y16.day01;

import pl.kaq.Solution;
import pl.kaq.model.Position;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.lang.Integer.parseInt;

class Santa {

    private char orientation;
    private Position position;
    private final Set<Position> seen = new HashSet<>();
    private Optional<Position> firstPosition = Optional.empty();

    public Santa() {
        this.orientation = 'U';
        this.position = new Position(0, 0);
        this.seen.add(this.position);
    }

    void follow(Instruction instruction) {
        switch (instruction) {
            case Left(int steps) -> moveLeft(steps);
            case Right(int steps) -> moveRight(steps);
        }
    }

    private void moveRight(int steps) {
        switch (orientation) {
            case 'U' -> right(steps);
            case 'R' -> down(steps);
            case 'D' -> left(steps);
            case 'L' -> up(steps);
            default -> throw new IllegalStateException();
        }
    }

    private void right(int steps) {
        this.orientation = 'R';
        for (int i = 0; i < steps; i++) {
            this.position = this.position.right();
            if (seen.contains(this.position)) this.register(this.position);
            seen.add(this.position);
        }
    }

    private void down(int steps) {
        this.orientation = 'D';
        for (int i = 0; i < steps; i++) {
            this.position = this.position.down();
            if (seen.contains(this.position)) this.register(this.position);

            seen.add(this.position);
        }
    }

    private void up(int steps) {
        this.orientation = 'U';
        for (int i = 0; i < steps; i++) {
            this.position = this.position.up();
            if (seen.contains(this.position)) this.register(this.position);

            seen.add(this.position);
        }
    }

    private void left(int steps) {
        this.orientation = 'L';
        for (int i = 0; i < steps; i++) {
            this.position = this.position.left();
            if (seen.contains(this.position)) this.register(this.position);

            seen.add(this.position);
        }
    }

    private void register(Position position) {
        if (this.firstPosition.isEmpty()) {
            this.firstPosition = Optional.of(position);
        }
    }

    private void moveLeft(int steps) {
        switch (orientation) {
            case 'U' -> left(steps);
            case 'R' -> up(steps);
            case 'D' -> right(steps);
            case 'L' -> down(steps);
            default -> throw new IllegalStateException();
        }

    }

    public int distance() {
        return Math.abs(position.col()) + Math.abs(position.row());
    }

    public int distanceToFirstPosition() {
        final var pos = this.firstPosition.orElseThrow();
        return Math.abs(pos.col()) + Math.abs(pos.row());
    }
}

public class Day01 extends Solution {
    @Override
    public String firstStar(String input) {
        final var instructions = Arrays.stream(input.split(", "))
                .map(this::parseInstruction)
                .toList();

        var santa = new Santa();
        instructions.forEach(santa::follow);

        return Integer.toString(santa.distance());
    }

    private Instruction parseInstruction(String str) {
        return switch (str.charAt(0)) {
            case 'L' -> new Left(parseInt(str.substring(1)));
            case 'R' -> new Right(parseInt(str.substring(1)));
            default -> throw new IllegalStateException();
        };
    }

    @Override
    public String secondStar(String input) {
        final var instructions = Arrays.stream(input.split(", "))
                .map(this::parseInstruction)
                .toList();

        var santa = new Santa();
        instructions.forEach(santa::follow);

        return Integer.toString(santa.distanceToFirstPosition());

    }

    public static void main(String[] args) {
        new Day01().run("input.in");
    }
}
