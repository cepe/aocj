package pl.kaq.aoc.y15.day20;

import pl.kaq.Solution;

import java.util.HashMap;

public class Day20 extends Solution {

    @Override
    public String firstStar(String input) {
        var limit = Integer.parseInt(input) / 10;

        var house = 1;
        while (true) {
            var score = 0;
            for (var elv = 1; elv <= house; elv++) {
                if (house % elv == 0) {
                    score += elv;
                }
            }
            if (score >= limit) {
                break;
            }
            house += 1;
        }
        return Integer.toString(house);
    }

    @Override
    public String secondStar(String input) {
        var limit = Integer.parseInt(input);
        var houses = new HashMap<Integer, Integer>();
        var elv = 1;
        while (true) {
            var presents = houses.getOrDefault(elv, 0);
            houses.put(elv, presents + elv * 11);
            if (houses.get(elv) >= limit) {
                break;
            }
            for (var visit = 2; visit <= 50; visit++) {
                presents = houses.getOrDefault(elv * visit, 0);
                houses.put(elv * visit, presents + elv * 11);
            }
            elv += 1;
        }
        return Integer.toString(elv);
    }

    public static void main(String[] args) {
        new Day20().run("input.in");
    }
}
