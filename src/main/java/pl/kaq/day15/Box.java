package pl.kaq.day15;

import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

class Box {
    private final LinkedHashMap<String, Integer> lenses = new LinkedHashMap<>();
    private final int boxNumber;

    public Box(int boxNumber) {
        this.boxNumber = boxNumber;
    }

    public void minus(String label) {
        lenses.remove(label);
    }

    public void addOrUpdate(String label, Integer focalLength) {
        lenses.put(label, focalLength);
    }

    public int focalPower() {
        var sum = new AtomicInteger();
        var position = new AtomicInteger(1);

        lenses.forEach((key, value) -> {
            final var delta = (boxNumber + 1) * position.get() * value;
            sum.addAndGet(delta);
            position.getAndIncrement();
        });

        return sum.get();
    }

    @Override
    public String toString() {
        return lenses.toString();
    }
}
