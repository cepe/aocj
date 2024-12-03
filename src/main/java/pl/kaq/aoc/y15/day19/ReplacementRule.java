package pl.kaq.aoc.y15.day19;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public record ReplacementRule(String from, String to) {
    public static ReplacementRule from(String ruleLine) {
        var split = ruleLine.split(" => ");
        return new ReplacementRule(split[0], split[1]);
    }

    public ReplacementRule reverse() {
        return new ReplacementRule(to, from);
    }

    public List<String> replacements(String string) {
        var result = new ArrayList<String>();

        var pattern = Pattern.compile(from);
        var matcher = pattern.matcher(string);
        while (matcher.find()) {
            var start = matcher.start();
            var end = matcher.end();
            result.add(string.substring(0, start) + to + string.substring(end));
        }

        return result;
    }
}
