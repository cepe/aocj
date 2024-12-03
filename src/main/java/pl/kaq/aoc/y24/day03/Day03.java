package pl.kaq.aoc.y24.day03;

import pl.kaq.Solution;

import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class Day03 extends Solution {
    @Override
    public String firstStar(String input) {
        var oneLineInput = input.replaceAll("\n", "");
        return Integer.toString(mulSum(oneLineInput));
    }

    private static int mulSum(String input) {
        var pattern = Pattern.compile("mul\\((\\d+),(\\d+)\\)");
        var matcher = pattern.matcher(input);
        var result = 0;
        while (matcher.find()) {
            result += parseInt(matcher.group(1)) * parseInt(matcher.group(2));
        }
        return result;
    }

    @Override
    public String secondStar(String input) {
        var oneLineInput = input.replaceAll("\n", "");
        return Integer.toString(mulSum(oneLineInput.replaceAll("don't\\(\\).*?do\\(\\)", "X")));
    }

    public static void main(String[] args) {
        new Day03().run("input.in");
    }
}
