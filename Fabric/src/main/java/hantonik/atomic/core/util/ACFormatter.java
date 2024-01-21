package hantonik.atomic.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ACFormatter {
    private static final DecimalFormat PATTERN = new DecimalFormat("#.##");
    private static final List<String> PREFIXES = List.of("", "k", "M", "G", "T");

    public static String formatTime(long ticks) {
        var formattedTime = new StringBuilder();

        var seconds = (ticks / 20) % 60;
        var minutes = (ticks / 20 / 60) % 60;
        var hours = (ticks / 20 / 60 / 60) % 60;

        ticks %= 20;

        if (hours > 0)
            formattedTime.append(hours).append("h ");

        if (minutes > 0)
            formattedTime.append(minutes).append("m ");

        if (seconds > 0 && hours <= 0)
            formattedTime.append(seconds).append("s ");

        if (seconds <= 0 && minutes <= 0 && hours <= 0)
            formattedTime.append(ticks).append("t ");

        return formattedTime.toString().trim();
    }

    public static String formatPrefix(long input) {
        for (var prefix : PREFIXES) {
            if (input >= 1000)
                input /= 1000;
            else
                return PATTERN.format(input) + " " + prefix;
        }

        return PATTERN.format(input) + " " + PREFIXES.get(PREFIXES.size() - 1);
    }
}
