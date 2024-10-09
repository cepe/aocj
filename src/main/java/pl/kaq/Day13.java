package pl.kaq;

import java.util.Arrays;

public class Day13 extends Solution {

    @Override
    public String firstStar(String input) {
        final var patterns = input.split("\n\n");

        final var reflections = Arrays.stream(patterns)
                .map(this::pattern)
                .map(this::reflection)
                .toList();

        final var answer = reflections.stream()
                .map(reflection -> switch (reflection) {
                    case Vertical v -> v.pos;
                    case Horizontal h -> h.pos * 100;
                }).reduce(0, Integer::sum);

        return Integer.toString(answer);
    }

    private char[][] pattern(String pattern) {
        final var stringList = Arrays.stream(pattern.split("\n")).toList();
        if (stringList.isEmpty()) {
            return new char[0][0];
        }

        int rows = stringList.size();
        int cols = stringList.stream().mapToInt(String::length).max().orElse(0);

        char[][] charArray = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            String str = stringList.get(i);
            for (int j = 0; j < str.length(); j++) {
                charArray[i][j] = str.charAt(j);
            }

            for (int j = str.length(); j < cols; j++) {
                charArray[i][j] = ' ';
            }
        }

        return charArray;
    }

    private Reflection reflection(char[][] pattern) {
        // search for vertical line
        var length = pattern[0].length;
        for (int pos = 0; pos <= length - 2; pos++) {
            int range = Math.min(pos, length - pos - 2);
            boolean mirrored = true;
            for (char[] chars : pattern) {
                for (int r = 0; r <= range; r++) {
                    if (chars[pos + 1 + r] != chars[pos - r]) {
                        mirrored = false;
                        break;
                    }
                }
            }
            if (mirrored) {
                return new Vertical(pos + 1);
            }
        }

        // search for horizontal line
        length = pattern.length;
        for (int pos = 0; pos <= length - 2; pos++) {
            int range = Math.min(pos, length - pos - 2);
            boolean mirrored = true;
            for (int r = 0; r <= range; r++) {
                for (int col = 0; col < pattern[0].length; col ++) {
                    if (pattern[pos + 1 + r][col] != pattern[pos - r][col]) {
                        mirrored = false;
                        break;
                    }
                }
            }
            if (mirrored) {
                return new Horizontal(pos + 1);
            }
        }

        throw new IllegalStateException("Mirror not found! Pattern: %s");
    }

    @Override
    public String secondStar(String input) {
        return String.valueOf(3);
    }

    public static void main(String[] args) {
        new Day13().run("ex.in");
    }

    private sealed interface Reflection permits Horizontal, Vertical {
    }

    private record Horizontal(int pos) implements Reflection {
    }

    private record Vertical(int pos) implements Reflection {
    }
}
