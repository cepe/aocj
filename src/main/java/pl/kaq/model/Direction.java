package pl.kaq.model;

import java.util.function.Function;

public enum Direction {
    UP(Position::up), LEFT(Position::left), RIGHT(Position::right), DOWN(Position::down);

    private final Function<Position, Position> movmentFunction;

    Direction(Function<Position, Position> movmentFunction) {
        this.movmentFunction = movmentFunction;
    }


    public Position move(Position position) {
        return movmentFunction.apply(position);
    }

    public Position moveBack(Position position) {
        return this.reverse().movmentFunction.apply(position);
    }

    private Direction reverse() {
        return switch (this) {
            case UP -> DOWN;
            case LEFT -> RIGHT;
            case DOWN -> UP;
            case RIGHT -> LEFT;
        };
    }

    public Direction onLeft() {
        return switch (this) {
            case UP -> LEFT;
            case LEFT -> DOWN;
            case DOWN -> RIGHT;
            case RIGHT -> UP;
        };
    }

    public Direction onRight() {
        return switch (this) {
            case UP -> RIGHT;
            case LEFT -> UP;
            case DOWN -> LEFT;
            case RIGHT -> DOWN;
        };
    }
}
