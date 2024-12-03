package pl.kaq.aoc.y15.day19;

import pl.kaq.Solution;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class Day19 extends Solution {
    @Override
    public String firstStar(String input) {
        var replacementsAndMolecule = parseInput(input);
        var distinctMolecules = moleculeReplacements(
                replacementsAndMolecule.replacementRules(),
                replacementsAndMolecule.molecule()
        );
        return Integer.toString(distinctMolecules.size());
    }

    private Set<String> moleculeReplacements(List<ReplacementRule> replacementRules, String molecule) {
        return replacementRules.stream()
                .flatMap(rule -> rule.replacements(molecule).stream())
                .collect(Collectors.toSet());
    }

    private ReplacementRulesAndMolecule parseInput(String input) {
        var split = input.split("\n\n");
        var rules = Arrays.stream(split[0].split("\n"))
                .map(ReplacementRule::from)
                .toList();
        var molecule = split[1];
        return new ReplacementRulesAndMolecule(rules, molecule);
    }

    @Override
    public String secondStar(String input) {
        var replacementsAndMolecule = parseInput(input);
        var rules = replacementsAndMolecule.replacementRules().stream()
                .map(ReplacementRule::reverse)
                .toList();
        var recursiveSearch = new RecursiveSearch(rules, "e");
        return Integer.toString(recursiveSearch.search(replacementsAndMolecule.molecule()));
    }

    public static void main(String[] args) {
        new Day19().run("input.in");
    }
}
