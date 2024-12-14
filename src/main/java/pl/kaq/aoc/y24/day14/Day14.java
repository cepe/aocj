package pl.kaq.aoc.y24.day14;

import pl.kaq.Solution;
import pl.kaq.model.Position;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

record Robot(Position pos, Position velocity) {
    public Position simulate(int steps, int rows, int cols) {
        var movedRobot = this;
        for (var i = 0; i < steps; i++) {
            movedRobot = movedRobot.move(rows, cols);
        }
        return movedRobot.pos();
    }

    public Robot move(int rows, int cols) {
        return new Robot(
                new Position(
                        wrap(pos.row() + velocity.row(), rows),
                        wrap(pos.col() + velocity.col(), cols)
                ),
                velocity
        );
    }

    private int wrap(int num, int bound) {
        if (num > 0) {
            return num % bound;
        }
        return (bound + (num % bound)) % bound;
    }

}


public class Day14 extends Solution {
    @Override
    public String firstStar(String input) {
        var rows = 103;
        var cols = 101;
        var robots = parseRobots(input);
        var positions = robots.stream().map(robot -> robot.simulate(100, rows, cols)).toList();
        var q1 = positions.stream().filter(pos -> pos.row() < rows / 2 && pos.col() < cols / 2).count();
        var q2 = positions.stream().filter(pos -> pos.row() > rows / 2 && pos.col() < cols / 2).count();
        var q3 = positions.stream().filter(pos -> pos.row() < rows / 2 && pos.col() > cols / 2).count();
        var q4 = positions.stream().filter(pos -> pos.row() > rows / 2 && pos.col() > cols / 2).count();
        return Long.toString(q1 * q2 * q3 * q4);
    }

    private List<Robot> parseRobots(String input) {
        return Arrays.stream(input.split("\n")).map(this::parseRobot).toList();
    }

    private Robot parseRobot(String robotStr) {
        var split = robotStr.split(" ");
        var positionCords = split[0].split("=")[1].split(",");
        var velocityCords = split[1].split("=")[1].split(",");
        return new Robot(
                new Position(parseInt(positionCords[1]), parseInt(positionCords[0])),
                new Position(parseInt(velocityCords[1]), parseInt(velocityCords[0]))
        );
    }

    @Override
    public String secondStar(String input) {
        var rows = 103;
        var cols = 101;
        var robots = parseRobots(input);

        var seconds = 0;
        while (!treeDetected(robots)) {
            robots = robots.stream().map(robot -> robot.move(rows, cols)).toList();
            seconds += 1;
        }
        return Integer.toString(seconds);
    }

    private boolean treeDetected(List<Robot> robots) {
        return robots.stream().map(Robot::pos).collect(Collectors.groupingBy(Position::row)).values().stream()
                       .mapToLong(this::longestRow)
                       .max().orElseThrow() > 7;
    }

    private long longestRow(List<Position> positions) {
        var cols = positions.stream().map(Position::col).sorted().toList();
        var prev = Integer.MIN_VALUE;
        var longest = 0L;
        var current = 0L;
        for (var col : cols) {
            if (col == prev + 1) {
                current += 1;
            } else {
                current = 0L;
            }
            longest = Math.max(longest, current);
            prev = col;
        }
        return longest;
    }

    public static void main(String[] args) {
        new Day14().run("input.in");
    }
}