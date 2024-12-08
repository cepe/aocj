package pl.kaq.model;

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
}
