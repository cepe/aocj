package pl.kaq.model;

public record DirectedPosition(Position position, Direction direction) {

    public DirectedPosition next() {
        return new DirectedPosition(direction.move(position), direction);
    }

    public DirectedPosition rotateRight() {
        return switch (direction) {
            case UP -> new DirectedPosition(position, Direction.RIGHT);
            case LEFT -> new DirectedPosition(position, Direction.UP);
            case RIGHT -> new DirectedPosition(position, Direction.DOWN);
            case DOWN -> new DirectedPosition(position, Direction.LEFT);
        };
    }

}
