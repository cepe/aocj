package pl.kaq.aoc.y24.day06;

import pl.kaq.Solution;

public class Day06 extends Solution {
    @Override
    public String firstStar(String input) {
        var simulation = new Simulation(board(input));
        simulation.run();
        return Integer.toString(simulation.countVisited());
    }

    @Override
    public String secondStar(String input) {
        var simulation = new Simulation(board(input));
        return Integer.toString(simulation.countPositionsWithLoop());
    }

    public static void main(String[] args) {
        new Day06().run("input.in");
    }
}