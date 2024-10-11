package pl.kaq.aoc.y23.day13;

import pl.kaq.Solution;
import pl.kaq.model.Board;

import java.util.Arrays;

public class Day13 extends Solution {

    @Override
    public String firstStar(String input) {
        final var patterns = input.split("\n\n");

        final var reflections = Arrays.stream(patterns)
                .map(this::board)
                .map(this::reflection)
                .toList();

        final var answer = reflections.stream()
                .map(reflection -> switch (reflection) {
                    case Vertical v -> v.pos;
                    case Horizontal h -> h.pos * 100;
                    default -> throw new IllegalStateException("Should not ever happen");
                }).reduce(0, Integer::sum);

        return Integer.toString(answer);
    }

    @Override
    public String secondStar(String input) {
        final var patterns = input.split("\n\n");

        final var reflections = Arrays.stream(patterns)
                .map(this::board)
                .map(this::reflection2)
                .toList();

        final var answer = reflections.stream()
                .map(reflection -> switch (reflection) {
                    case Vertical v -> v.pos;
                    case Horizontal h -> h.pos * 100;
                    case NoMirror i -> throw new IllegalStateException("Should not ever happen");
                }).reduce(0, Integer::sum);

        return Integer.toString(answer);
    }


    private Reflection reflection(Board board, Reflection toAvoid) {
        var length = board.noCols();
        for (int pos = 0; pos <= length - 2; pos++) {
            int range = Math.min(pos, length - pos - 2);
            boolean mirrored = true;
            for (char[] row : board.rows()) {
                for (int r = 0; r <= range; r++) {
                    if (row[pos + 1 + r] != row[pos - r]) {
                        mirrored = false;
                        break;
                    }
                }
            }
            if (mirrored) {
                final var reflection = new Vertical(pos + 1);
                if (!reflection.equals(toAvoid)) {
                    return reflection;
                }
            }
        }

        // search for horizontal line
        length = board.noRows();
        for (int pos = 0; pos <= length - 2; pos++) {
            int range = Math.min(pos, length - pos - 2);
            boolean mirrored = true;
            for (int r = 0; r <= range; r++) {
                for (int col = 0; col < board.noCols(); col++) {
                    if (board.at(pos + 1 + r, col) != board.at(pos - r, col)) {
                        mirrored = false;
                        break;
                    }
                }
            }
            if (mirrored) {
                final var reflection = new Horizontal(pos + 1);
                if (!reflection.equals(toAvoid)) {
                    return reflection;
                }
            }
        }

        return new NoMirror();
    }

    private Reflection reflection(Board board) {
        return reflection(board, new NoMirror());
    }

    private Reflection reflection2(Board board) {
        var orginalReflection = reflection(board);
        for (int row = 0; row < board.noRows(); row++) {
            for (int col = 0; col < board.noCols(); col++) {
                toggle(board, row, col);
                final var newReflection = reflection(board, orginalReflection);
                toggle(board, row, col);
                if (!newReflection.equals(new NoMirror())) {
                    return newReflection;
                }
            }
        }

        throw new IllegalStateException("New mirror not found!");
    }

    private void toggle(Board board, int row, int col) {
        if (board.at(row, col) == '.') {
            board.setAt(row, col, '#');
        } else if (board.at(row, col) == '#') {
            board.setAt(row, col, '.');
        } else throw new IllegalStateException();
    }


    public static void main(String[] args) {
        new Day13().run("input.in");
    }

    private sealed interface Reflection permits Horizontal, Vertical, NoMirror {
    }

    private record Horizontal(int pos) implements Reflection {
    }

    private record Vertical(int pos) implements Reflection {
    }

    private record NoMirror() implements Reflection {
    }
}
