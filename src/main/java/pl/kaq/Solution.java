package pl.kaq;

import pl.kaq.model.Board;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class Solution {
    public abstract String firstStar(String input);

    public abstract String secondStar(String input);

    public void run(String fileName) {
        var input = input(fileName);
        System.out.println(firstStar(input));
        System.out.println(secondStar(input));
    }

    public void runFirstStar(String fileName) {
        var input = input(fileName);
        System.out.println(firstStar(input));
    }

    public void runSecondStar(String fileName) {
        var input = input(fileName);
        System.out.println(secondStar(input));
    }


    public Board board(String input) {
        return new Board(parseBoard(input));
    }

    private char[][] parseBoard(String input) {
        final var stringList = Arrays.stream(input.split("\n")).toList();
        if (stringList.isEmpty()) {
            return new char[0][0];
        }

        int rows = stringList.size();
        int cols = stringList.stream().mapToInt(String::length).max().orElse(0);

        char[][] charArray = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            String str = stringList.get(i);
            for (int j = 0; j < str.length(); j++) {
                charArray[i][j] = str.charAt(j);
            }

            for (int j = str.length(); j < cols; j++) {
                charArray[i][j] = ' ';
            }
        }

        return charArray;
    }



    private String input(String fileName) {
        final var inputFileUrl = getUrl(fileName);

        try {
            final var path = Path.of(inputFileUrl.toURI());
            try (var lines = Files.lines(path)) {
                return lines.collect(Collectors.joining("\n"));
            } catch (Exception e) {
                throw new IllegalStateException("Failed to load: %s".formatted(path));
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private URL getUrl(String fileName) {
        var classloader = Thread.currentThread().getContextClassLoader();
        final Class<? extends Solution> solutionClass = this.getClass();
        final var packageParts = solutionClass.getPackageName().split("\\.");

        var inputDirectory = packageParts[packageParts.length - 2] + "/" + packageParts[packageParts.length - 1];

        var inputPath = inputDirectory + "/" + fileName;
        var inputFileUrl = classloader.getResource(inputPath);

        if (inputFileUrl == null) {
            throw new IllegalStateException("Unable to read %s".formatted(inputPath));
        }
        return inputFileUrl;
    }
}
