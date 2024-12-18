package pl.kaq.aoc.y24.day16;

import pl.kaq.Solution;
import pl.kaq.model.Board;
import pl.kaq.model.Direction;
import pl.kaq.model.Position;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.Function;
import java.util.stream.Collectors;

class DefaultMap<K, V> {
    private final Map<K, V> map = new HashMap<>();
    private final V defaultValue;

    DefaultMap(V defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void set(K key, V value) {
        map.put(key, value);
    }

    public V get(K key) {
        return map.getOrDefault(key, defaultValue);
    }

    public Map<K, V> getBackingMap() {
        return map;
    }
}

record DijkstraResult(Map<DirectedPosition, Long> distance) {

}

record PricedDirectedPosition(DirectedPosition position, int price) {

}

record DirectedPosition(Position position, Direction direction) {

    public List<PricedDirectedPosition> pricedAround() {
        return List.of(
                new PricedDirectedPosition(new DirectedPosition(direction.move(position), direction), 1),
                new PricedDirectedPosition(new DirectedPosition(position, direction.onLeft()), 1000),
                new PricedDirectedPosition(new DirectedPosition(position, direction.onRight()), 1000),
                new PricedDirectedPosition(new DirectedPosition(position, direction.onLeft().onLeft()), 2000)
        );

    }

    public List<PricedDirectedPosition> pricedAroundReversed() {
        return List.of(
                new PricedDirectedPosition(new DirectedPosition(direction.moveBack(position), direction), 1),
                new PricedDirectedPosition(new DirectedPosition(position, direction.onLeft()), 1000),
                new PricedDirectedPosition(new DirectedPosition(position, direction.onRight()), 1000),
                new PricedDirectedPosition(new DirectedPosition(position, direction.onLeft().onLeft()), 2000)
        );
    }
}

public class Day16 extends Solution {

    @Override
    public String firstStar(String input) {
        var board = board(input);
        var start = board.find('S');
        var end = board.find('E');
        var dijkstra = dijkstra(board, new DirectedPosition(start, Direction.RIGHT), DirectedPosition::pricedAround);
        return Long.toString(dijkstra.get(new DirectedPosition(end, Direction.UP)));
    }

    private Map<DirectedPosition, Long> dijkstra(
            Board board,
            DirectedPosition start,
            Function<DirectedPosition, List<PricedDirectedPosition>> aroundFunction
    ) {
        var distance = new DefaultMap<DirectedPosition, Long>(Long.MAX_VALUE);
        var notWalls = board.positions().stream()
                .filter(pos -> board.at(pos) != '#')
                .flatMap(pos -> Arrays.stream(Direction.values()).map(direction -> new DirectedPosition(pos, direction)))
                .toList();
        notWalls.forEach(position -> distance.set(position, Long.MAX_VALUE));
        distance.set(start, 0L);

        var queue = new PriorityQueue<>(Comparator.comparing(distance::get));
        queue.addAll(notWalls);
        var available = new HashSet<>(notWalls);

        while (!queue.isEmpty()) {
            var current = queue.poll();
            available.remove(current);
            var around = aroundFunction.apply(current).stream()
                    .filter(pricedPosition -> available.contains(pricedPosition.position()))
                    .filter(pricedPosition -> board.onBoard(pricedPosition.position().position()))
                    .filter(pricedPosition -> board.at(pricedPosition.position().position()) != '#')
                    .toList();
            around.forEach(position -> {
                var dist = distance.get(current) + position.price();
                if (dist < distance.get(position.position())) {
                    distance.set(position.position(), dist);
                    queue.remove(position.position());
                    queue.add(position.position());
                }
            });
        }

        return distance.getBackingMap();
    }


    @Override
    public String secondStar(String input) {
        var board = board(input);
        var start = board.find('S');
        var end = board.find('E');
        var fromTheStart = dijkstra(board, new DirectedPosition(start, Direction.RIGHT), DirectedPosition::pricedAround);
        var fromTheEnd = dijkstra(board, new DirectedPosition(end, Direction.UP), DirectedPosition::pricedAroundReversed);
        var score = fromTheEnd.get(new DirectedPosition(start, Direction.RIGHT));
        return Long.toString(board.positions().stream()
                .filter(position -> board.at(position) != '#')
                .flatMap(pos -> Arrays.stream(Direction.values()).map(direction -> new DirectedPosition(pos, direction)))
                .filter(directedPosition -> fromTheStart.get(directedPosition) + fromTheEnd.get(directedPosition) == score)
                .map(DirectedPosition::position)
                .collect(Collectors.toSet())
                .size());
    }


    public static void main(String[] args) {
        new Day16().run("input.in");
    }
}
