package pl.kaq.aoc.y15.day21;

public record Stats(int hp, int damage, int armor) {
    public Stats dealDamage(int dealDamage) {
        return new Stats(hp - dealDamage, damage, armor);
    }

    public boolean dead() {
        return hp <= 0;
    }
}
