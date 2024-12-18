package pl.kaq.model;

import java.util.List;
import java.util.function.Function;

public record Position(int row, int col) {

    public Position left() {
        return new Position(row, col - 1);
    }

    public Position right() {
        return new Position(row, col + 1);
    }

    public Position up() {
        return new Position(row - 1, col);
    }

    public Position down() {
        return new Position(row + 1, col);
    }

    public Position minus(Position position) {
        return new Position(row - position.row(), col - position.col());
    }

    public Position plus(Position position) {
        return new Position(row + position.row(), col + position.col());
    }

    public List<Position> around() {
        List<Function<Position, Position>> aroundFunctions =
                List.of(Position::up, Position::down, Position::right, Position::left);
        return aroundFunctions.stream().map(func -> func.apply(this)).toList();
    }

    public List<Position> aroundHorizontal() {
        List<Function<Position, Position>> aroundFunctions = List.of(Position::right, Position::left);
        return aroundFunctions.stream().map(func -> func.apply(this)).toList();
    }

    public List<Position> aroundVertical() {
        List<Function<Position, Position>> aroundFunctions = List.of(Position::up, Position::down);
        return aroundFunctions.stream().map(func -> func.apply(this)).toList();
    }
}
