package pl.kaq.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Board {

    private final char[][] board;

    public Board(char[][] board) {
        this.board = board;
    }

    public boolean onBoard(Position position) {
        return position.row() >= 0 && position.row() < board.length
               && position.col() >= 0 && position.col() < board[0].length;
    }

    public char at(int row, int col) {
        final var position = new Position(row, col);
        return at(position);
    }

    public char at(Position position) {
        assert onBoard(position);
        return board[position.row()][position.col()];
    }

    public int noRows() {
        return board.length;
    }

    public int noCols() {
        return board[0].length;
    }


    public void print() {
        var rowLength = board[0].length;
        for (char[] row : board) {
            for (int col = 0; col < rowLength; col++) {
                System.out.printf("%c", row[col]);
            }
            System.out.println();
        }
    }

    public char[][] rows() {
        return board;
    }

    public void setAt(int row, int col, char c) {
        board[row][col] = c;
    }

    public void setAt(Position position, char c) {
        setAt(position.row(), position.col(), c);
    }

    public String asStringLine() {
        return Arrays.stream(board)
                .map(String::new)
                .collect(Collectors.joining());
    }

    public List<Position> positions() {
        var positions = new ArrayList<Position>();
        for (int row = 0; row < noRows(); row++) {
            for (int col = 0; col < noCols(); col++) {
                positions.add(new Position(row, col));
            }
        }
        return positions;
    }

    public BoardView startingAt(Position position) {
        return new BoardView(this, position);
    }

    public Position find(char character) {
        return positions().stream()
                .filter(position -> at(position) == character)
                .findFirst()
                .orElseThrow();
    }
}
