package pl.kaq.aoc.y23.day16;

import pl.kaq.model.Position;

public record Beam(Position position, char direction) {

    Beam fork() {
        return new Beam(new Position(position.row(), position.col()), this.direction);
    }

    public Beam moveRight() {
        return new Beam(position.right(), 'R');
    }

    public Beam moveUp() {
        return new Beam(position.up(), 'U');
    }

    public Beam moveDown() {
        return new Beam(position.down(), 'D');
    }

    public Beam moveLeft() {
        return new Beam(position.left(), 'L');
    }
}
