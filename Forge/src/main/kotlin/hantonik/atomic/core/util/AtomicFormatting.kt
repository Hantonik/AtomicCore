package hantonik.atomic.core.util

import java.text.DecimalFormat

object AtomicFormatting {
    fun formatTime(time: Long): String {
        val formattedTime = StringBuilder()

        val ticks = time % 20
        val seconds = (time / 20) % 60
        val minutes = (time / 20 / 60) % 60
        val hours = (time / 20 / 60 / 60) % 60

        if (hours > 0)
            formattedTime.append(hours).append("h ")

        if (minutes > 0)
            formattedTime.append(minutes).append("m ")

        if (seconds > 0 && hours <= 0)
            formattedTime.append(seconds).append("s ")

        if (seconds <= 0 && minutes <= 0 && hours <= 0)
            formattedTime.append(ticks).append("t ")

        return formattedTime.trim().toString()
    }

    fun format(input: Long): String {
        val pattern = DecimalFormat("#.##")
        val suffixes = arrayOf(" ", " k", " M", " G", " T")

        var output = input

        for (suffix in suffixes) {
            if (output >= 1000)
                output /= 1000
            else
                return pattern.format(output) + suffix
        }

        return pattern.format(output) + suffixes[suffixes.size - 1]
    }
}