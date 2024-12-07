package pl.kaq.aoc.y24.day07;

import pl.kaq.Solution;

public class Day07 extends Solution {
    @Override
    public String firstStar(String input) {
        return Long.toString(input.lines()
                .map(Equation::parse)
                .filter(Equation::fixable)
                .mapToLong(Equation::sum)
                .sum());
    }

    @Override
    public String secondStar(String input) {
        return Long.toString(input.lines()
                .map(Equation::parse)
                .filter(Equation::fixableWithConcat)
                .mapToLong(Equation::sum)
                .sum());
    }

    public static void main(String[] args) {
        new Day07().run("input.in");
    }
}