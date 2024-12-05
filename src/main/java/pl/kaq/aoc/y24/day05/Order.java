package pl.kaq.aoc.y24.day05;

import java.util.List;

public record Order(List<Rule> rules, List<Update> updates) {
}
