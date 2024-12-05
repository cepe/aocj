package pl.kaq.aoc.y24.day05;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static java.util.Collections.swap;

public record RuleSet(List<Rule> rules) {
    public boolean inOrder(Update update) {
        return inOrder(update.pages());
    }

    private boolean inOrder(List<Integer> pages) {
        var ruleSet = new HashSet<>(rules);
        for (var page = 0; page < pages.size(); page++) {
            for (var pageAfter = page + 1; pageAfter < pages.size(); pageAfter++) {
                if (ruleSet.contains(new Rule(pages.get(pageAfter), pages.get(page)))) {
                    return false;
                }
            }
        }
        return true;
    }

    public Update fix(Update update) {
        var ruleSet = new HashSet<>(rules);
        var pages = new ArrayList<>(update.pages());
        var current = 0;
        while (current < pages.size() - 1) {
            var broken = false;
            for (var page = current + 1; page < pages.size(); page++) {
                if (ruleSet.contains(new Rule(pages.get(page), pages.get(current)))) {
                    swap(pages, page, current);
                    broken = true;
                    break;
                }
            }
            if (!broken) {
                current += 1;
            }
        }
        return new Update(pages);
    }
}
