package pl.kaq.aoc.y16.day02;

import pl.kaq.Solution;
import pl.kaq.model.Board;
import pl.kaq.model.Position;


public class Day02 extends Solution {
    @Override
    public String firstStar(String input) {
        final var panelBoard = board("123\n456\n789");
        final var startingPosition = new Position(1, 1);
        final var board = board(input);
        var panel = new Panel(panelBoard, startingPosition);
        return solve(board, panel);
    }

    @Override
    public String secondStar(String input) {
        final var input1 = "  1  \n 234 \n56789\n ABC \n  D  ";
        final var panelBoard = board(input1);
        final var startingPosition = new Position(2, 0);
        final var board = board(input);
        var panel = new Panel(panelBoard, startingPosition);
        return solve(board, panel);

    }

    private static String solve(Board board, Panel panel) {
        var code = new StringBuilder();
        for (var line : board.rows()) {
            for (var c : line) {
                switch (c) {
                    case 'U' -> panel.up();
                    case 'D' -> panel.down();
                    case 'L' -> panel.left();
                    case 'R' -> panel.right();
                }
            }
            code.append(panel.button());
        }
        return code.toString();
    }

    public static void main(String[] args) {
        new Day02().run("input.in");
    }
}
