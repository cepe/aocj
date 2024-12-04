package pl.kaq.aoc.y24.day04;

import pl.kaq.Solution;
import pl.kaq.model.BoardView;

import java.util.ArrayList;
import java.util.List;

record Pattern(char[][] block) {

    public boolean matches(BoardView boardView) {
        var noCols = noCols();
        var noRows = noRows();

        if (boardView.noCols() < noCols || boardView.noRows() < noRows) {
            return false;
        }

        for (int row = 0; row < noRows; row++) {
            for (int col = 0; col < noCols; col++) {
                if (block[row][col] == ' ') {
                    continue;
                }
                if (block[row][col] != boardView.at(row, col)) {
                    return false;
                }
            }
        }

        return true;
    }

    private int noRows() {
        return block.length;
    }

    private int noCols() {
        return block[0].length;
    }
}

public class Day04 extends Solution {
    private final List<Pattern> patternsFirstStar = List.of(
            pattern("XMAS"),
            pattern("SAMX"),
            pattern("X", "M", "A", "S"),
            pattern("S", "A", "M", "X"),
            pattern("X   ", " M  ", "  A ", "   S"),
            pattern("S   ", " A  ", "  M ", "   X"),
            pattern("   X", "  M ", " A  ", "S    "),
            pattern("   S", "  A ", " M  ", "X    ")
    );

    private final List<Pattern> patternsSecondStar = List.of(
            pattern("M S", " A ", "M S"),
            pattern("S M", " A ", "S M"),
            pattern("S S", " A ", "M M"),
            pattern("M M", " A ", "S S")
    );


    private Pattern pattern(String... lines) {
        var block = new ArrayList<char[]>();
        for (var line : lines) {
            block.add(line.toCharArray());
        }
        return new Pattern(block.toArray(new char[][]{}));
    }

    @Override
    public String firstStar(String input) {
        return Integer.toString(countPatterns(input, patternsFirstStar));
    }

    private int countPatterns(String input, List<Pattern> patterns) {
        var board = board(input);
        var result = 0;
        for (var pattern : patterns) {
            for (var position : board.positions()) {
                if (pattern.matches(board.startingAt(position))) {
                    result += 1;
                }
            }
        }
        return result;
    }


    @Override
    public String secondStar(String input) {
        return Integer.toString(countPatterns(input, patternsSecondStar));
    }

    public static void main(String[] args) {
        new Day04().run("input.in");
    }
}
