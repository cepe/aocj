package pl.kaq.aoc.y15.day21;

import java.util.List;

public record Hero(Stats stats, List<Item> items) {

    public Stats finalStats() {
        var itemStats = itemStats();
        return new Stats(stats.hp(), stats.damage() + itemStats.damage(), stats.armor() + itemStats.armor());
    }

    Stats itemStats() {
        var damage = items.stream()
                .map(Item::damage)
                .mapToInt(v -> v)
                .sum();
        var armor = items.stream()
                .map(Item::armor)
                .mapToInt(v -> v)
                .sum();
        return new Stats(0, damage, armor);
    }

}
