package pl.kaq.aoc.y24.day07;

public class LongUtils {
    public static Long mul(Long a, Long b) {
        return a * b;
    }

    public static Long concat(Long a, Long b) {
        return Long.parseLong(a.toString() + b.toString());
    }
}
