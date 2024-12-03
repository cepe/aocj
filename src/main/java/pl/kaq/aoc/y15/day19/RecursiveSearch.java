package pl.kaq.aoc.y15.day19;

import java.util.List;

public class RecursiveSearch {
    private final List<ReplacementRule> rules;
    private final String targetMolecule;
    private int steps = 100_000_000;

    public RecursiveSearch(List<ReplacementRule> rules, String targetMolecule) {
        this.rules = rules;
        this.targetMolecule = targetMolecule;
    }

    public int search(String molecule) {
        search(molecule, 0);
        return steps;
    }

    private void search(String molecule, int steps) {
        if (molecule.equals(targetMolecule)) {
            this.steps = Math.min(this.steps, steps);
            return;
        }
        for (var rule : rules) {
            var molecules = rule.replacements(molecule);
            for (var m : molecules) {
                search(m, steps + 1);
            }
        }
    }
}
