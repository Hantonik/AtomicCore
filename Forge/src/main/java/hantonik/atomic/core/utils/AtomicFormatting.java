package hantonik.atomic.core.utils;

import java.text.DecimalFormat;

public final class AtomicFormatting {
    public static String formatTime(long time) {
        var formattedTime = new StringBuilder();

        var ticks = time % 20;
        var seconds = (time / 20) % 60;
        var minutes = ((time / 20) % 3600) / 60;
        var hours = (time / 20) / 3600;

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

    public static String format(long input) {
        var pattern = new DecimalFormat("#.##");
        String[] suffixes = {" ", " k", " M", " G", " T"};

        for (String suffix : suffixes) {
            if (input >= 1000)
                input /= 1000;
            else
                return pattern.format(input) + suffix;
        }

        return pattern.format(input) + suffixes[suffixes.length - 1];
    }
}
