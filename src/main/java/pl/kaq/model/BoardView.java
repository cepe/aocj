package pl.kaq.model;

public class BoardView {
    private final Board board;
    private final Position position;

    public BoardView(Board board, Position position) {
        this.board = board;
        this.position = position;
    }

    public int noCols() {
        return board.noCols() - position.col();
    }

    public int noRows() {
        return board.noRows() - position.row();
    }

    public char at(int row, int col) {
        return board.at(row + position.row(), col + position.col());
    }
}
