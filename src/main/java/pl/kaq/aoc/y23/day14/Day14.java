package pl.kaq.aoc.y23.day14;

import pl.kaq.Solution;
import pl.kaq.model.Board;

import java.util.HashMap;

public class Day14 extends Solution {

    @Override
    public String firstStar(String input) {
        final var board = board(input);
        north(board);
        return Long.toString(load(board));
    }

    private long load(Board board) {
        long sum = 0;
        long rowLoad = board.noRows();

        for (char[] row : board.rows()) {
            for (char c : row) {
                if (c == 'O') {
                    sum += rowLoad;
                }
            }
            rowLoad--;
        }

        return sum;
    }

    private void north(Board board) {
        for (int col = 0; col < board.noCols(); col++) {
            north(board, col);
        }
    }

    private void north(Board board, int col) {
        for (int row = 0; row < board.noRows(); row++) {
            if (board.at(row, col) == 'O') {
                int prev = row - 1;
                while (prev >= 0 && board.at(prev, col) == '.') {
                    board.setAt(prev + 1, col, '.');
                    prev--;
                }
                board.setAt(prev + 1, col, 'O');
            }
        }
    }

    private void south(Board board) {
        for (int col = 0; col < board.noCols(); col++) {
            south(board, col);
        }
    }

    private void south(Board board, int col) {
        for (int row = board.noRows() - 1; row >= 0; row--) {
            if (board.at(row, col) == 'O') {
                int prev = row + 1;
                while (prev < board.noRows() && board.at(prev, col) == '.') {
                    board.setAt(prev - 1, col, '.');
                    prev++;
                }
                board.setAt(prev - 1, col, 'O');
            }
        }
    }

    private void east(Board board) {
        for (int row = 0; row < board.noRows(); row++) {
            east(board, row);
        }
    }

    private void east(Board board, int row) {
        for (int col = board.noCols() - 1; col >= 0; col--) {
            if (board.at(row, col) == 'O') {
                int prev = col + 1;
                while (prev < board.noRows() && board.at(row, prev) == '.') {
                    board.setAt(row, prev - 1, '.');
                    prev++;
                }
                board.setAt(row, prev - 1, 'O');
            }
        }
    }

    private void west(Board board) {
        for (int row = 0; row < board.noRows(); row++) {
            west(board, row);
        }
    }

    private void west(Board board, int row) {
        for (int col = 0; col < board.noCols(); col++) {
            if (board.at(row, col) == 'O') {
                int prev = col - 1;
                while (prev >= 0 && board.at(row, prev) == '.') {
                    board.setAt(row, prev + 1, '.');
                    prev--;
                }
                board.setAt(row, prev + 1, 'O');
            }
        }
    }

    @Override
    public String secondStar(String input) {
        var board = board(input);

        var seen = new HashMap<String, Integer>();

        int cycles = 1000000000;

        var x = 0;
        for (int cycle = 1; cycle <= cycles; cycle++) {
            north(board);
            west(board);
            south(board);
            east(board);

            var strRep = board.asStringLine();
            if (seen.containsKey(strRep)) {
                x = (cycles - seen.get(strRep)) % (cycle - seen.get(strRep)) + seen.get(strRep);
                break;
            } else {
                seen.put(strRep, cycle);
            }

        }
        board = board(input);

        for (int cycle = 1; cycle <= x; cycle++) {
            north(board);
            west(board);
            south(board);
            east(board);
        }

        return Long.toString(load(board));
    }


    public static void main(String[] args) {
        new Day14().run("input.in");
    }

}
