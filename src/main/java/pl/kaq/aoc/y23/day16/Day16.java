package pl.kaq.aoc.y23.day16;

import pl.kaq.Solution;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;


record Board(char[][] board) {
    boolean onBoard(Position position) {
        return position.row() >= 0 && position.row() < board.length
                && position.col() >= 0 && position.col() < board[0].length;
    }

    char at(Position position) {
        assert onBoard(position);
        return board[position.row()][position.col()];
    }
}


record Position(int row, int col) {

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
}

record Beam(Position position, char direction) {

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

public class Day16 extends Solution {
    @Override
    public String firstStar(String input) {
        var board = new Board(board(input));
        return Integer.toString(calculateEnergy(board, new Beam(new Position(0, 0), 'R')));
    }

    private static int calculateEnergy(Board board, Beam startingBeam) {
        final var seenPositions = new HashSet<Position>();
        seenPositions.add(startingBeam.position());
        var seenBeams = new HashSet<Beam>();
        seenBeams.add(startingBeam);

        var beams = List.of(startingBeam);
        while (!beams.isEmpty()) {
            final var newBeams = getBeams(beams, board);

            newBeams
                    .stream()
                    .filter(beam -> board.onBoard(beam.position()))
                    .forEach(beam -> seenPositions.add(beam.position()));

            beams = newBeams.stream()
                    .filter(beam -> board.onBoard(beam.position()))
                    .filter(beam -> !seenBeams.contains(beam))
                    .toList();

            seenBeams.addAll(beams);
        }

        return seenPositions.size();
    }

    private static LinkedList<Beam> getBeams(List<Beam> beams, Board board) {
        var newBeams = new LinkedList<Beam>();

        // move all beams
        for (Beam beam : beams) {
            var cell = board.at(beam.position());
            var situation = new String(new char[]{beam.direction(), cell});
            switch (situation) {
                case "R.", "R-", "U/", "D\\":
                    newBeams.add(beam.moveRight());
                    break;
                case "R/", "L\\", "U.", "U|":
                    newBeams.add(beam.moveUp());
                    break;
                case "R\\", "L/", "D.", "D|":
                    newBeams.add(beam.moveDown());
                    break;
                case "R|", "L|": {
                    final var newBeam = beam.fork();
                    newBeams.add(beam.moveUp());
                    newBeams.add(newBeam.moveDown());
                    break;
                }
                case "L.", "L-", "U\\", "D/":
                    newBeams.add(beam.moveLeft());
                    break;
                case "U-", "D-": {
                    final var newBeam = beam.fork();
                    newBeams.add(beam.moveLeft());
                    newBeams.add(newBeam.moveRight());
                    break;
                }
            }

        }
        return newBeams;
    }

    @Override
    public String secondStar(String input) {
        var board = new Board(board(input));

        return Integer.toString(Stream.concat(
                        Stream.concat(
                                IntStream.range(0, board.board().length - 1).mapToObj(i -> new Beam(new Position(i, 0), 'R')),
                                IntStream.range(0, board.board().length - 1).mapToObj(i -> new Beam(new Position(i, board.board()[0].length - 1), 'L'))
                        ),
                        Stream.concat(
                                IntStream.range(0, board.board()[0].length - 1).mapToObj(i -> new Beam(new Position(0, i), 'D')),
                                IntStream.range(0, board.board()[0].length - 1).mapToObj(i -> new Beam(new Position(board.board().length - 1, i), 'U'))
                        ))
                .mapToInt(beam -> calculateEnergy(board, beam))
                .max()
                .orElseThrow());
    }

    public static void main(String[] args) {
        new Day16().run("input.in");
    }
}
