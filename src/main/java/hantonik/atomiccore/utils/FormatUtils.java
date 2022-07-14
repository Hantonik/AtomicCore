package hantonik.atomiccore.utils;

@Deprecated(forRemoval = true)
public final class FormatUtils {
    @Deprecated(forRemoval = true)
    public static String formatTime(long time, TimeUnit unit, TimeUnit precision, boolean only) {
        if (time == 0)
            return 0 + "t";

        var output = new StringBuilder();

        var helper = time;

        if (unit == TimeUnit.SECONDS)
            helper = time * 20;
        else if (unit == TimeUnit.MINUTES)
            helper = time * 20 * 60;
        else if (unit == TimeUnit.HOURS)
            helper = time * 20 * 60 * 60;
        else if (unit == TimeUnit.DAYS)
            helper = time * 20 * 60 * 60 * 24;

        if (helper % 20 > 0 || precision == TimeUnit.TICKS) {
            if (precision == TimeUnit.TICKS)
                output.insert(0, helper + "t ");
            else
                output.insert(0, helper % 20 + "t ");
        }

        if (helper / 20 >= 1 && precision != TimeUnit.TICKS) {
            helper = helper / 20;

            if (helper % 60 > 0) {
                if (precision == TimeUnit.SECONDS)
                    output.insert(0, helper + "s ");
                else
                    output.insert(0, helper % 60 + "s ");
            }

            if (helper / 60 >= 1 && precision != TimeUnit.SECONDS) {
                helper = helper / 60;

                if (helper % 60 > 0) {
                    if (precision == TimeUnit.MINUTES)
                        output.insert(0, helper + "m ");
                    else
                        output.insert(0, helper % 60 + "m ");
                }

                if (helper / 60 >= 1 && precision != TimeUnit.MINUTES) {
                    helper = helper / 60;

                    if (helper % 24 > 0) {
                        if (precision == TimeUnit.HOURS)
                            output.insert(0, helper + "h ");
                        else
                            output.insert(0, helper % 24 + "h ");
                    }

                    if (helper / 24 >= 1 && precision != TimeUnit.HOURS) {
                        output.insert(0, helper / 24 + "d ");

                        if (only) {
                            if (output.chars().anyMatch(p -> p == 't'))
                                return output.deleteCharAt(output.length() - 1).delete(output.indexOf(" "), output.lastIndexOf("t") + 1).toString();
                            if (output.chars().anyMatch(p -> p == 's'))
                                return output.deleteCharAt(output.length() - 1).delete(output.indexOf(" "), output.lastIndexOf("s") + 1).toString();
                            if (output.chars().anyMatch(p -> p == 'm'))
                                return output.deleteCharAt(output.length() - 1).delete(output.indexOf(" "), output.lastIndexOf("m") + 1).toString();
                            if (output.chars().anyMatch(p -> p == 'h'))
                                return output.deleteCharAt(output.length() - 1).delete(output.indexOf(" "), output.lastIndexOf("h") + 1).toString();
                        }
                    } else {
                        if (only) {
                            if (output.chars().anyMatch(p -> p == 't'))
                                return output.deleteCharAt(output.length() - 1).delete(output.indexOf(" "), output.lastIndexOf("t") + 1).toString();
                            if (output.chars().anyMatch(p -> p == 's'))
                                return output.deleteCharAt(output.length() - 1).delete(output.indexOf(" "), output.lastIndexOf("s") + 1).toString();
                            if (output.chars().anyMatch(p -> p == 'm'))
                                return output.deleteCharAt(output.length() - 1).delete(output.indexOf(" "), output.lastIndexOf("m") + 1).toString();
                        }
                    }
                } else {
                    if (only) {
                        if (output.chars().anyMatch(p -> p == 't'))
                            return output.deleteCharAt(output.length() - 1).delete(output.indexOf(" "), output.lastIndexOf("t") + 1).toString();
                        if (output.chars().anyMatch(p -> p == 's'))
                            return output.deleteCharAt(output.length() - 1).delete(output.indexOf(" "), output.lastIndexOf("s") + 1).toString();
                    }
                }
            } else {
                if (only) {
                    if (output.chars().anyMatch(p -> p == 't'))
                        return output.deleteCharAt(output.length() - 1).delete(output.indexOf(" "), output.lastIndexOf("t") + 1).toString();
                }
            }
        }

        return output.deleteCharAt(output.length() - 1).toString();
    }

    @Deprecated(forRemoval = true)
    public static String format(long l, UniversalUnit unit, UniversalUnit precision, String suffix, boolean only) {
        if (l == 0)
            return 0 + " " + suffix;

        var output = new StringBuilder();

        var helper = l * unit.getMultiplier();

        boolean contains = output.toString().contains(" " + suffix);

        if (helper % 1000 > 0 || precision == UniversalUnit.NONE) {
            if (precision == UniversalUnit.NONE)
                output.insert(0, helper + " " + suffix + " ");
            else
                output.insert(0, helper % 1000 + " " +  suffix + " ");
        }

        if (helper / 1000 >= 1 && precision != UniversalUnit.NONE) {
            helper = helper / 1000;

            if (helper % 1000 > 0) {
                if (precision == UniversalUnit.KILO)
                    output.insert(0, helper + " " + UniversalUnit.KILO.getSymbol() + suffix + " ");
                else
                    output.insert(0, helper % 1000 + " " + UniversalUnit.KILO.getSymbol() + suffix + " ");
            }

            if (helper / 1000 >= 1 && precision != UniversalUnit.KILO) {
                helper = helper / 1000;

                if (helper % 1000 > 0) {
                    if (precision == UniversalUnit.MEGA)
                        output.insert(0, helper + " " + UniversalUnit.MEGA.getSymbol() + suffix + " ");
                    else
                        output.insert(0, helper % 1000 + " " + UniversalUnit.MEGA.getSymbol() + suffix + " ");
                }

                if (helper / 1000 >= 1 && precision != UniversalUnit.MEGA) {
                    helper = helper / 1000;

                    if (helper % 1000 > 0) {
                        if (precision == UniversalUnit.GIGA)
                            output.insert(0, helper + " " + UniversalUnit.GIGA.getSymbol() + suffix + " ");
                        else
                            output.insert(0, helper % 1000 + " " + UniversalUnit.GIGA.getSymbol() + suffix + " ");
                    }

                    if (helper / 1000 >= 1 && precision != UniversalUnit.GIGA) {
                        output.insert(0, helper / 1000 + " " + precision.getSymbol() + suffix + " ");

                        if (only) {
                            if (contains)
                                return output.deleteCharAt(output.length() - 1).delete(output.indexOf(" ", output.indexOf(" ") + 1), output.lastIndexOf(" " + suffix) + suffix.length() + 1).toString();
                            if (output.toString().contains(" " + UniversalUnit.KILO.getSymbol() + suffix))
                                return output.deleteCharAt(output.length() - 1).delete(output.indexOf(" ", output.indexOf(" ") + 1), output.lastIndexOf(" " + UniversalUnit.KILO.getSymbol() + suffix) + (UniversalUnit.KILO.getSymbol() + suffix).length()).toString();
                            if (output.toString().contains(" " + UniversalUnit.MEGA.getSymbol() + suffix))
                                return output.deleteCharAt(output.length() - 1).delete(output.indexOf(" ", output.indexOf(" ") + 1), output.lastIndexOf(" " + UniversalUnit.MEGA.getSymbol() + suffix) + (UniversalUnit.MEGA.getSymbol() + suffix).length()).toString();
                            if (output.toString().contains(" " + UniversalUnit.TERA.getSymbol() + suffix))
                                return output.deleteCharAt(output.length() - 1).delete(output.indexOf(" ", output.indexOf(" ") + 1), output.lastIndexOf(" " + UniversalUnit.TERA.getSymbol() + suffix) + (UniversalUnit.TERA.getSymbol() + suffix).length()).toString();
                        }
                    } else {
                        if (only) {
                            if (contains)
                                return output.deleteCharAt(output.length() - 1).delete(output.indexOf(" ", output.indexOf(" ") + 1), output.lastIndexOf(" " + suffix) + suffix.length() + 1).toString();
                            if (output.toString().contains(" " + UniversalUnit.KILO.getSymbol() + suffix))
                                return output.deleteCharAt(output.length() - 1).delete(output.indexOf(" ", output.indexOf(" ") + 1), output.lastIndexOf(" " + UniversalUnit.KILO.getSymbol() + suffix) + (UniversalUnit.KILO.getSymbol() + suffix).length()).toString();
                            if (output.toString().contains(" " + UniversalUnit.MEGA.getSymbol() + suffix))
                                return output.deleteCharAt(output.length() - 1).delete(output.indexOf(" ", output.indexOf(" ") + 1), output.lastIndexOf(" " + UniversalUnit.MEGA.getSymbol() + suffix) + (UniversalUnit.MEGA.getSymbol() + suffix).length()).toString();
                        }
                    }
                } else {
                    if (only) {
                        if (contains)
                            return output.deleteCharAt(output.length() - 1).delete(output.indexOf(" ", output.indexOf(" ") + 1), output.lastIndexOf(" " + suffix) + suffix.length() + 1).toString();
                        if (output.toString().contains(" " + UniversalUnit.KILO.getSymbol() + suffix))
                            return output.deleteCharAt(output.length() - 1).delete(output.indexOf(" ", output.indexOf(" ") + 1), output.lastIndexOf(" " + UniversalUnit.KILO.getSymbol() + suffix) + (UniversalUnit.KILO.getSymbol() + suffix).length()).toString();
                    }
                }
            } else {
                if (only) {
                    if (contains)
                        return output.deleteCharAt(output.length() - 1).delete(output.indexOf(" ", output.indexOf(" ") + 1), output.lastIndexOf(suffix) + suffix.length() + 1).toString();
                }
            }
        }

        return output.deleteCharAt(output.length() - 1).toString();
    }

    @Deprecated(forRemoval = true)
    public enum UniversalUnit {
        NONE("" , 1),
        KILO("k", 1_000),
        MEGA("M", 1_000_000),
        GIGA("G", 1_000_000_000),
        TERA("T", 1_000_000_000_000L);

        private final String symbol;
        private final long multiplier;

        UniversalUnit(String symbol, long multiplier) {
            this.symbol = symbol;
            this.multiplier = multiplier;
        }

        @Deprecated(forRemoval = true)
        public long getMultiplier() {
            return this.multiplier;
        }

        @Deprecated(forRemoval = true)
        public String getSymbol() {
            return this.symbol;
        }
    }

    @Deprecated(forRemoval = true)
    public enum TimeUnit {
        TICKS,
        SECONDS,
        MINUTES,
        HOURS,
        DAYS
    }
}
