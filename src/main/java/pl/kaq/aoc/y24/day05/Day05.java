package pl.kaq.aoc.y24.day05;

import pl.kaq.Solution;

import java.util.Arrays;

import static java.lang.Integer.parseInt;
import static java.util.function.Predicate.not;

public class Day05 extends Solution {
    @Override
    public String firstStar(String input) {
        var order = parseOrder(input);
        var ruleSet = new RuleSet(order.rules());
        return Integer.toString(order.updates().stream()
                .filter(ruleSet::inOrder)
                .map(this::middleElement)
                .mapToInt(v -> v)
                .sum());
    }

    private Integer middleElement(Update update) {
        var pages = update.pages();
        return pages.get(pages.size() / 2);
    }

    private Order parseOrder(String input) {
        var split = input.split("\n\n");
        var rules = Arrays.stream(split[0].split("\n")).map(this::parseRule).toList();
        var updates = Arrays.stream(split[1].split("\n")).map(this::parseUpdate).toList();
        return new Order(rules, updates);
    }

    private Update parseUpdate(String updateStr) {
        return new Update(Arrays.stream(updateStr.split(",")).mapToInt(Integer::parseInt).boxed().toList());
    }

    private Rule parseRule(String ruleStr) {
        var split = ruleStr.split("\\|");
        return new Rule(parseInt(split[0]), parseInt(split[1]));
    }

    @Override
    public String secondStar(String input) {
        var order = parseOrder(input);
        var ruleSet = new RuleSet(order.rules());
        return Integer.toString(order.updates().stream()
                .filter(not(ruleSet::inOrder))
                .map(ruleSet::fix)
                .map(this::middleElement)
                .mapToInt(v -> v)
                .sum());
    }

    public static void main(String[] args) {
        new Day05().run("input.in");
    }
}