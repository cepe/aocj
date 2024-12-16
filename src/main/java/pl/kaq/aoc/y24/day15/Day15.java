package pl.kaq.aoc.y24.day15;

import pl.kaq.Solution;
import pl.kaq.model.Board;
import pl.kaq.model.Position;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Function;

record Puzzle(Board board, List<Movement> movements) {

}

enum Movement {
    LEFT(Position::left),
    RIGHT(Position::right),
    UP(Position::up),
    DOWN(Position::down);

    private final Function<Position, Position> movementFunc;

    Movement(Function<Position, Position> movementFunc) {
        this.movementFunc = movementFunc;
    }

    public Position apply(Position position) {
        return movementFunc.apply(position);
    }

    public boolean isHorizontal() {
        return EnumSet.of(LEFT, RIGHT).contains(this);
    }

    public static Movement from(int character) {
        return switch (character) {
            case '>' -> Movement.RIGHT;
            case '<' -> Movement.LEFT;
            case '^' -> Movement.UP;
            case 'v' -> Movement.DOWN;
            default -> throw new IllegalStateException();
        };
    }
}

public class Day15 extends Solution {

    @Override
    public String firstStar(String input) {
        var puzzle = parsePuzzle(input);
        moveAll(puzzle);
        return Integer.toString(sumOfCoordinates(puzzle.board()));
    }

    private int sumOfCoordinates(Board board) {
        return board.positions().stream()
                .filter(pos -> board.at(pos) == 'O' || board.at(pos) == '[')
                .mapToInt(pos -> pos.row() * 100 + pos.col())
                .sum();
    }

    private void moveAll(Puzzle puzzle) {
        var board = puzzle.board();
        var movements = puzzle.movements();
        var position = board.positions().stream()
                .filter(pos -> board.at(pos) == '@')
                .findFirst()
                .orElseThrow();
        for (var movement : movements) {
            if (canMove(board, position, movement)) {
                move(board, position, movement);
                position = movement.apply(position);
            }
        }
    }

    private void move(Board board, Position position, Movement movement) {
        var nextPosition = movement.apply(position);
        var at = board.at(position);
        var atNext = board.at(nextPosition);
        if (atNext == 'O') {
            move(board, nextPosition, movement);
        }
        if (movement.isHorizontal() && (atNext == '[' || atNext == ']')) {
            move(board, nextPosition, movement);
        }
        if (!movement.isHorizontal()) {
            if (atNext == '[') {
                move(board, nextPosition, movement);
                move(board, nextPosition.right(), movement);
                board.setAt(nextPosition.right(), '.');
                board.setAt(nextPosition, '.');
            }
            if (atNext == ']') {
                move(board, nextPosition, movement);
                move(board, nextPosition.left(), movement);
                board.setAt(nextPosition.left(), '.');
                board.setAt(nextPosition, '.');
            }
        }
        board.setAt(nextPosition, at);
        board.setAt(position, '.');
    }

    private boolean canMove(Board board, Position position, Movement movement) {
        var nextPosition = movement.apply(position);
        if (!board.onBoard(nextPosition)) {
            return false;
        }
        return switch (board.at(nextPosition)) {
            case '#' -> false;
            case '.' -> true;
            case 'O' -> canMove(board, nextPosition, movement);
            case '[' -> movement.isHorizontal() ?
                    canMove(board, nextPosition, movement) :
                    canMove(board, nextPosition, movement) && canMove(board, nextPosition.right(), movement);
            case ']' -> movement.isHorizontal() ?
                    canMove(board, nextPosition, movement) :
                    canMove(board, nextPosition, movement) && canMove(board, nextPosition.left(), movement);
            default -> throw new IllegalStateException();
        };
    }

    private Puzzle parsePuzzle(String input) {
        var split = input.split("\n\n");
        var movementsStr = String.join("", split[1].split("\n"));
        var movements = movementsStr.chars().boxed().map(Movement::from).toList();
        return new Puzzle(board(split[0]), movements);
    }

    @Override
    public String secondStar(String input) {
        var puzzle = upscalePuzzle(parsePuzzle(input));
        moveAll(puzzle);
        return Integer.toString(sumOfCoordinates(puzzle.board()));
    }

    private Puzzle upscalePuzzle(Puzzle puzzle) {
        var newBoard = puzzle.board().upscaleHorizontally(2);
        for (var position : newBoard.positions()) {
            if (newBoard.at(position) == 'O' && position.col() % 2 == 0) {
                newBoard.setAt(position, '[');
            }
            if (newBoard.at(position) == 'O' && position.col() % 2 == 1) {
                newBoard.setAt(position, ']');
            }
            if (newBoard.at(position) == '@' && position.col() % 2 == 1) {
                newBoard.setAt(position, '.');
            }
        }
        return new Puzzle(newBoard, puzzle.movements());
    }


    public static void main(String[] args) {
        new Day15().run("input.in");
    }
}