package pl.kaq.aoc.y16.day03;

import pl.kaq.Solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.Integer.parseInt;

record Triangle(int a, int b, int c) {

}

public class Day03 extends Solution {
    @Override
    public String firstStar(String input) {
        return Long.toString(parseTriangles(input).stream().filter(this::isTrianglePossible).count());
    }

    private boolean isTrianglePossible(Triangle triangle) {
        var sides = new ArrayList<>(List.of(triangle.a(), triangle.b(), triangle.c()));
        Collections.sort(sides);
        return sides.get(0) + sides.get(1) > sides.get(2);
    }

    private List<Triangle> parseTriangles(String input) {
        return Arrays.stream(input.split("\n")).map(this::parseTriangle).toList();
    }

    private Triangle parseTriangle(String triangleStr) {
        var split = triangleStr.trim().split("\\s+");
        return new Triangle(parseInt(split[0].strip()), parseInt(split[1].strip()), parseInt(split[2].strip()));
    }

    @Override
    public String secondStar(String input) {
        return Long.toString(parseTrianglesGroupedBy3(input).stream().filter(this::isTrianglePossible).count());
    }

    private List<Triangle> parseTrianglesGroupedBy3(String input) {
        var triangles = parseTriangles(input);
        var newTriangles = new ArrayList<Triangle>();
        for (var i = 0; i < triangles.size(); i += 3) {
            newTriangles.add(new Triangle(triangles.get(i).a(), triangles.get(i + 1).a(), triangles.get(i + 2).a()));
            newTriangles.add(new Triangle(triangles.get(i).b(), triangles.get(i + 1).b(), triangles.get(i + 2).b()));
            newTriangles.add(new Triangle(triangles.get(i).c(), triangles.get(i + 1).c(), triangles.get(i + 2).c()));
        }
        return newTriangles;
    }


    public static void main(String[] args) {
        new Day03().run("input.in");
    }
}
