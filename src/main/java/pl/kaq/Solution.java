package pl.kaq;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public abstract class Solution {
    abstract String firstStar(String input);

    abstract String secondStar(String input);

    private String input(String fileName) {
        var classloader = Thread.currentThread().getContextClassLoader();
        var inputPath = this.getClass().getSimpleName().toLowerCase() + "/" + fileName;
        var inputFileUrl = classloader.getResource(inputPath);

        if (inputFileUrl == null) {
            throw new IllegalStateException("Unable to read %s".formatted(inputPath));
        }

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

    void run(String fileName) {
        var input = input(fileName);
        System.out.println(firstStar(input));
        System.out.println(secondStar(input));
    }
}
