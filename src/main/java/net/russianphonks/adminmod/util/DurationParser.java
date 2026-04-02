package net.russianphonks.adminmod.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DurationParser {
    private static final Pattern TOKEN = Pattern.compile("(\\d+)([smhdw])", Pattern.CASE_INSENSITIVE);

    private DurationParser() {
    }

    public static long parseMillis(String input) {
        if (input == null || input.isBlank()) {
            return -1;
        }

        String normalized = input.trim().toLowerCase();
        Matcher matcher = TOKEN.matcher(normalized);

        long total = 0;
        int consumed = 0;
        while (matcher.find()) {
            if (matcher.start() != consumed) {
                return -1;
            }

            long value = Long.parseLong(matcher.group(1));
            char unit = matcher.group(2).charAt(0);
            consumed = matcher.end();

            switch (unit) {
                case 's' -> total += value * 1000L;
                case 'm' -> total += value * 60_000L;
                case 'h' -> total += value * 3_600_000L;
                case 'd' -> total += value * 86_400_000L;
                case 'w' -> total += value * 604_800_000L;
                default -> {
                    return -1;
                }
            }
        }

        if (consumed != normalized.length()) {
            return -1;
        }

        return total > 0 ? total : -1;
    }

    public static String formatRemaining(long millis) {
        if (millis <= 0) {
            return "0s";
        }

        long seconds = millis / 1000;
        long weeks = seconds / 604800;
        seconds %= 604800;
        long days = seconds / 86400;
        seconds %= 86400;
        long hours = seconds / 3600;
        seconds %= 3600;
        long minutes = seconds / 60;
        seconds %= 60;

        StringBuilder sb = new StringBuilder();
        appendPart(sb, weeks, "w");
        appendPart(sb, days, "d");
        appendPart(sb, hours, "h");
        appendPart(sb, minutes, "m");
        appendPart(sb, seconds, "s");
        return sb.toString();
    }

    private static void appendPart(StringBuilder sb, long value, String suffix) {
        if (value <= 0) {
            return;
        }
        sb.append(value).append(suffix);
    }
}
