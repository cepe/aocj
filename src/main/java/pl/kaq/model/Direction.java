package pl.kaq.model;

import java.util.function.Function;

public enum Direction {
    UP(Position::up), LEFT(Position::left), RIGHT(Position::right), DOWN(Position::down);

    private final Function<Position, Position> movmentFunction;

    Direction(Function<Position, Position> movmentFunction) {
        this.movmentFunction = movmentFunction;
    }

    Position move(Position position) {
        return movmentFunction.apply(position);
    }
}
