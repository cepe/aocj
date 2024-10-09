package pl.kaq;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import pl.kaq.model.TestCase;
import pl.kaq.model.Output;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

public abstract class TestBase {
    private final Solution solution;

    public TestBase(Solution solution) {
        this.solution = solution;
    }

    @TestFactory
    public Stream<DynamicTest> tests() {
        final var firstStarTestCases = testCases().stream()
                .map(this::makeFirstStarTest);
        final var secondStarTestCases = testCases().stream()
                .map(this::makeSecondStarTest);
        return Stream.concat(firstStarTestCases, secondStarTestCases);
    }

    private DynamicTest makeFirstStarTest(TestCase testCase) {
        return makeTest(
                testCase.inputName() + " - first star",
                testCase.input(),
                testCase.output().firstStar(),
                solution::firstStar
        );
    }

    private DynamicTest makeSecondStarTest(TestCase testCase) {
        return makeTest(
                testCase.inputName() + " - second star",
                testCase.input(),
                testCase.output().secondStar(),
                solution::secondStar
        );
    }

    private DynamicTest makeTest(String testName, String input, String output, Function<String, String> function) {
        return DynamicTest.dynamicTest(
                testName,
                () -> assertSoftly(softly -> softly.assertThat(function.apply(input)).isEqualTo(output))
        );
    }

    private List<TestCase> testCases() {
        final var testCasesDirectory = testCasesDirectory();

        final var inputFiles = testCasesDirectory.listFiles(file -> file.getName().endsWith("in"));

        if (inputFiles == null) {
            throw new IllegalStateException("File %s is not directory".formatted(testCasesDirectory.getPath()));

        }
        return Arrays.stream(inputFiles)
                .map(inputFile -> {
                    final var input = fileContent(inputFile);
                    var outputFile = outputFile(inputFile);
                    var output = parseOutput(fileContent(outputFile));
                    return new TestCase(inputFile.getName(), input, output);
                })
                .toList();
    }

    private Output parseOutput(String content) {
        final var split = content.split("\n");
        assert split.length == 2;
        return new Output(split[0], split[1]);
    }

    private File outputFile(File inputFile) {
        final var outputFilePath = inputFile.getPath().replace(".in", ".out");
        return new File(outputFilePath);
    }

    private String fileContent(File file) {
        try (var lines = Files.lines(file.toPath())) {
            return lines.collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File testCasesDirectory() {
        var classloader = Thread.currentThread().getContextClassLoader();
        var directoryUrl = classloader.getResource(directory());

        if (directoryUrl == null) {
            throw new IllegalStateException();
        }

        return new File(directoryUrl.getPath());
    }

    private String directory() {
        return solution.getClass().getSimpleName().toLowerCase();
    }

}
