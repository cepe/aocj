package pl.kaq.aoc.y24.day11;

import pl.kaq.Solution;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Long.parseLong;

record CountRequest(String num, int blinks) {
}

class StoneCounter {

    private final Map<CountRequest, Long> memo = new HashMap<>();

    long count(CountRequest request) {
        if (request.blinks() == 0) {
            return 1L;
        }
        if (memo.containsKey(request)) {
            return memo.get(request);
        }
        var num = request.num();
        if (num.equals("0")) {
            var request1 = new CountRequest("1", request.blinks() - 1);
            memo.put(request1, count(request1));
            return memo.get(request1);
        }
        if (num.length() % 2 == 0) {
            var request1 = new CountRequest(num.substring(0, num.length() / 2), request.blinks() - 1);
            var request2 = new CountRequest(Long.toString(parseLong(num.substring(num.length() / 2))), request.blinks() - 1);
            memo.put(request1, count(request1));
            memo.put(request2, count(request2));
            return memo.get(request1) + memo.get(request2);
        }
        var request1 = new CountRequest(Long.toString(parseLong(request.num()) * 2024), request.blinks() - 1);
        memo.put(request1, count(request1));
        return memo.get(request1);
    }

}

public class Day11 extends Solution {
    @Override
    public String firstStar(String input) {
        var counter = new StoneCounter();
        return Long.toString(Arrays.stream(input.split(" "))
                .mapToLong(stone -> counter.count(new CountRequest(stone, 25)))
                .sum());
    }

    @Override
    public String secondStar(String input) {
        var counter = new StoneCounter();
        return Long.toString(Arrays.stream(input.split(" "))
                .mapToLong(stone -> counter.count(new CountRequest(stone, 75)))
                .sum());
    }

    public static void main(String[] args) {
        new Day11().run("input.in");
    }
}