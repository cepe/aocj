package pl.kaq.aoc.y24.day06;

import pl.kaq.model.Board;
import pl.kaq.model.DirectedPosition;
import pl.kaq.model.Direction;
import pl.kaq.model.Position;

import java.util.HashSet;
import java.util.Set;

class Simulation {

    private final Board board;
    private final DirectedPosition startingPosition;
    private final Set<Position> visitedPositions = new HashSet<>();
    private final Set<DirectedPosition> visitedDirectedPositions = new HashSet<>();

    public Simulation(Board board) {
        this.board = board;
        this.startingPosition = new DirectedPosition(board.find('^'), Direction.UP);
    }

    public Integer countVisited() {
        return visitedPositions.size();
    }

    public boolean run() {
        var position = startingPosition;
        visitedPositions.clear();
        visitedDirectedPositions.clear();
        visitedPositions.add(position.position());
        visitedDirectedPositions.add(position);
        while (board.onBoard(position.next().position())) {
            if (board.at(position.next().position()) == '#') {
                position = position.rotateRight();
            } else {
                position = position.next();
                visitedPositions.add(position.position());
                if (visitedDirectedPositions.contains(position)) {
                    return true;
                }
                visitedDirectedPositions.add(position);
            }
        }
        return false;
    }

    public int countPositionsWithLoop() {
        var positionCandidates = board.positions().stream()
                .filter(position -> board.at(position) == '.')
                .toList();

        var counter = 0;
        for (var candidate : positionCandidates) {
            board.setAt(candidate, '#');
            if (run()) {
                counter += 1;
            }
            board.setAt(candidate, '.');
        }

        return counter;
    }
}
