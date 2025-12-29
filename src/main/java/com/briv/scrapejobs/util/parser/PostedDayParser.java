package com.briv.scrapejobs.util.parser;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostedDayParser {

    private static final Pattern RELATIVE_PATTERN =
            Pattern.compile("(\\d+)\\s+(day|week|month)s?\\s+ago", Pattern.CASE_INSENSITIVE);

    public static LocalDate parse(String text) {
        if (text == null || text.isBlank()) return null;

        String normalized = text.trim().toLowerCase();
        LocalDate today = LocalDate.now();

        // handle special cases
        switch (normalized) {
            case "today":
                return today;
            case "yesterday":
                return today.minusDays(1);
        }

        // handle patterns like "3 days ago", "2 weeks ago", "1 month ago"
        Matcher matcher = RELATIVE_PATTERN.matcher(normalized);
        if (matcher.matches()) {
            int value = Integer.parseInt(matcher.group(1));
            String unit = matcher.group(2);

            switch (unit) {
                case "day":
                    return today.minusDays(value);
                case "week":
                    return today.minusWeeks(value);
                case "month":
                    return today.minusMonths(value);
            }
        }

        // unknown format → return null
        return null;
    }
}
