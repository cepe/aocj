package pl.kaq.aoc.y16.day02;

import pl.kaq.model.Board;
import pl.kaq.model.Position;

import java.util.function.Function;

public class Panel {

    private final Board board;

    private Position position;

    public Panel(Board board, Position startingPosition) {
        this.board = board;
        position = startingPosition;
    }

    public void up() {
        moveOnBoard(Position::up);
    }

    public void down() {
        moveOnBoard(Position::down);
    }

    public void left() {
        moveOnBoard(Position::left);
    }

    public void right() {
        moveOnBoard(Position::right);
    }

    private void moveOnBoard(Function<Position, Position> moveFunc) {
        final var newPos = moveFunc.apply(position);
        if (board.onBoard(newPos) && board.at(newPos) != ' ') {
            position = newPos;
        }
    }

    public String button() {
        return String.valueOf(board.at(position));
    }
}
