package pl.kaq.aoc.y15.day21;

import pl.kaq.Solution;

import java.util.ArrayList;
import java.util.List;


public class Day21 extends Solution {
    @Override
    public String firstStar(String input) {
        var enemy = new Hero(parseStats(input), List.of());
        return Integer.toString(itemSets().stream()
                .filter(itemSet -> playerWins(new Hero(new Stats(100, 0, 0), itemSet), enemy))
                .map(this::setValue)
                .mapToInt(v -> v)
                .min()
                .orElseThrow());
    }

    public boolean playerWins(Hero player, Hero enemy) {
        var playerStats = player.finalStats();
        var enemyStats = enemy.finalStats();

        while (true) {
            var dealDamage = calculateDamage(playerStats, enemyStats);
            enemyStats = enemyStats.dealDamage(dealDamage);
            if (enemyStats.dead()) {
                return true;
            }

            dealDamage = calculateDamage(enemyStats, playerStats);
            playerStats = playerStats.dealDamage(dealDamage);
            if (playerStats.dead()) {
                return false;
            }
        }
    }

    private int calculateDamage(Stats attacker, Stats defender) {
        return Math.max(1, attacker.damage() - defender.armor());
    }

    private int setValue(List<Item> items) {
        return items.stream()
                .mapToInt(Item::cost)
                .sum();
    }

    private List<List<Item>> itemSets() {
        var itemSets = new ArrayList<List<Item>>();
        var weapons = List.of(
                new Item(8, 4, 0), new Item(10, 5, 0), new Item(25, 6, 0), new Item(40, 7, 0),
                new Item(74, 8, 0)
        );
        var armors = List.of(
                new Item(0, 0, 0), new Item(13, 0, 1), new Item(31, 0, 2), new Item(53, 0, 3), new Item(75, 0, 4),
                new Item(102, 0, 5)
        );
        var rings = List.of(
                new Item(0, 0, 0), new Item(0, 0, 0), new Item(25, 1, 0), new Item(50, 2, 0), new Item(100, 3, 0),
                new Item(20, 0, 1), new Item(40, 0, 2), new Item(80, 0, 3)
        );
        for (var weapon : weapons) {
            for (var armor : armors) {
                for (var r1 = 0; r1 < rings.size(); r1++) {
                    for (var r2 = r1 + 1; r2 < rings.size(); r2++) {
                        itemSets.add(List.of(weapon, armor, rings.get(r1), rings.get(r2)));
                    }
                }
            }
        }

        return itemSets;
    }

    private Stats parseStats(String input) {
        var lines = input.split("\n");
        var hp = Integer.parseInt(lines[0].split(": ")[1]);
        var damage = Integer.parseInt(lines[1].split(": ")[1]);
        var armor = Integer.parseInt(lines[2].split(": ")[1]);
        return new Stats(hp, damage, armor);
    }

    @Override
    public String secondStar(String input) {
        var enemy = new Hero(parseStats(input), List.of());
        return Integer.toString(itemSets().stream()
                .filter(itemSet -> !playerWins(new Hero(new Stats(100, 0, 0), itemSet), enemy))
                .map(this::setValue)
                .mapToInt(v -> v)
                .max()
                .orElseThrow());
    }

    public static void main(String[] args) {
        new Day21().run("input.in");
    }
}
