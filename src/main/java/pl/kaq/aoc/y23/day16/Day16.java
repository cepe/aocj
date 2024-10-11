package pl.kaq.aoc.y23.day16;

import pl.kaq.Solution;
import pl.kaq.model.Board;
import pl.kaq.model.Position;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class Day16 extends Solution {
    @Override
    public String firstStar(String input) {
        var board = board(input);
        return Integer.toString(calculateEnergy(board, new Beam(new pl.kaq.model.Position(0, 0), 'R')));
    }

    private static int calculateEnergy(Board board, Beam startingBeam) {
        final var seenPositions = new HashSet<pl.kaq.model.Position>();
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
        var board = board(input);

        return Integer.toString(Stream.concat(
                        Stream.concat(
                                IntStream.range(0, board.noRows() - 1).mapToObj(i -> new Beam(new pl.kaq.model.Position(i, 0), 'R')),
                                IntStream.range(0, board.noRows() - 1).mapToObj(i -> new Beam(new pl.kaq.model.Position(i, board.noCols() - 1), 'L'))
                        ),
                        Stream.concat(
                                IntStream.range(0, board.noCols() - 1).mapToObj(i -> new Beam(new pl.kaq.model.Position(0, i), 'D')),
                                IntStream.range(0, board.noCols() - 1).mapToObj(i -> new Beam(new Position(board.noRows() - 1, i), 'U'))
                        ))
                .mapToInt(beam -> calculateEnergy(board, beam))
                .max()
                .orElseThrow());
    }

    public static void main(String[] args) {
        new Day16().run("input.in");
    }
}
