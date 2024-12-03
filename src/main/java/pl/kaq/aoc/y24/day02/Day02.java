package pl.kaq.aoc.y24.day02;

import pl.kaq.Solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day02 extends Solution {
    @Override
    public String firstStar(String input) {
        return Long.toString(input.lines().map(this::toIntList).filter(this::isSafe).count());
    }

    private boolean isSafe(List<Integer> integers) {
        return (isDescending(integers) || isAscending(integers)) && hasSafeDistance(integers);
    }

    private boolean isDescending(List<Integer> integers) {
        var previous = Integer.MAX_VALUE;
        for (var integer : integers) {
            if (integer > previous) {
                return false;
            }
            previous = integer;
        }
        return true;
    }

    private boolean isAscending(List<Integer> integers) {
        var previous = Integer.MIN_VALUE;
        for (var integer : integers) {
            if (integer < previous) {
                return false;
            }
            previous = integer;
        }
        return true;
    }

    private boolean hasSafeDistance(List<Integer> integers) {
        for (var i = 1; i < integers.size(); i++) {
            var abs = Math.abs(integers.get(i) - integers.get(i - 1));
            if (abs == 0 || abs > 3) {
                return false;
            }
        }
        return true;
    }

    private List<Integer> toIntList(String line) {
        return Arrays.stream(line.split("\\s+")).map(Integer::parseInt).toList();
    }

    @Override
    public String secondStar(String input) {
        return Long.toString(input.lines().map(this::toIntList).filter(this::isSafeRemovingOne).count());
    }

    private boolean isSafeRemovingOne(List<Integer> integers) {
        return sublistsWithOneRemoved(integers).stream().anyMatch(this::isSafe);
    }

    private List<List<Integer>> sublistsWithOneRemoved(List<Integer> integers) {
        var sublists = new ArrayList<List<Integer>>();
        for (var i = 0; i < integers.size(); i++) {
            var sublist = new ArrayList<>(integers);
            sublist.remove(i);
            sublists.add(sublist);
        }
        return sublists;
    }

    public static void main(String[] args) {
        new Day02().run("input.in");
    }


}
