package pl.kaq.day14;

import pl.kaq.Solution;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Day14 extends Solution {

    @Override
    public String firstStar(String input) {
        final var board = board(input);
        north(board);
        return Long.toString(load(board));
    }

    private long load(char[][] board) {
        long sum = 0;
        long rowLoad = board.length;

        for (char[] row : board) {
            for (char c : row) {
                if (c == 'O') {
                    sum += rowLoad;
                }
            }
            rowLoad--;
        }

        return sum;
    }

    private void north(char[][] board) {
        for (int col = 0; col < board[0].length; col++) {
            north(board, col);
        }
    }

    private void north(char[][] board, int col) {
        for (int row = 0; row < board.length; row++) {
            if (board[row][col] == 'O') {
                int prev = row - 1;
                while (prev >= 0 && board[prev][col] == '.') {
                    board[prev + 1][col] = '.';
                    prev--;
                }
                board[prev + 1][col] = 'O';
            }
        }
    }

    private void south(char[][] board) {
        for (int col = 0; col < board[0].length; col++) {
            south(board, col);
        }
    }

    private void south(char[][] board, int col) {
        for (int row = board.length - 1; row >= 0; row--) {
            if (board[row][col] == 'O') {
                int prev = row + 1;
                while (prev < board.length && board[prev][col] == '.') {
                    board[prev - 1][col] = '.';
                    prev++;
                }
                board[prev - 1][col] = 'O';
            }
        }
    }

    private void east(char[][] board) {
        for (int row = 0; row < board.length; row++) {
            east(board, row);
        }
    }

    private void east(char[][] board, int row) {
        for (int col = board[0].length - 1; col >= 0; col--) {
            if (board[row][col] == 'O') {
                int prev = col + 1;
                while (prev < board.length && board[row][prev] == '.') {
                    board[row][prev - 1] = '.';
                    prev++;
                }
                board[row][prev - 1] = 'O';
            }
        }
    }

    private void west(char[][] board) {
        for (int row = 0; row < board.length; row++) {
            west(board, row);
        }
    }

    private void west(char[][] board, int row) {
        for (int col = 0; col < board[0].length; col++) {
            if (board[row][col] == 'O') {
                int prev = col - 1;
                while (prev >= 0 && board[row][prev] == '.') {
                    board[row][prev + 1] = '.';
                    prev--;
                }
                board[row][prev + 1] = 'O';
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

            var strRep = toStr(board);
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

    private String toStr(char[][] board) {
        return Arrays.stream(board)
                .map(String::new)
                .collect(Collectors.joining());
    }


    public static void main(String[] args) {
        new Day14().run("input.in");
    }

}
