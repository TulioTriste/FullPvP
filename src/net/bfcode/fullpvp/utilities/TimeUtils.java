package net.bfcode.fullpvp.utilities;

import org.apache.commons.lang.time.DurationFormatUtils;
import java.util.concurrent.TimeUnit;
import java.text.DecimalFormat;

public class TimeUtils
{
    public static DecimalFormat getDecimalFormat() {
        return new DecimalFormat("0.0");
    }
    
    public static long parse(final String input) {
        if (input == null || input.isEmpty()) {
            return -1L;
        }
        long result = 0L;
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < input.length(); ++i) {
            final char c = input.charAt(i);
            if (Character.isDigit(c)) {
                number.append(c);
            }
            else {
                final String str;
                if (Character.isLetter(c) && !(str = number.toString()).isEmpty()) {
                    result += convert(Integer.parseInt(str), c);
                    number = new StringBuilder();
                }
            }
        }
        return result;
    }
    
    private static long convert(final int value, final char unit) {
        switch (unit) {
            case 'y': {
                return value * TimeUnit.DAYS.toMillis(365L);
            }
            case 'M': {
                return value * TimeUnit.DAYS.toMillis(30L);
            }
            case 'd': {
                return value * TimeUnit.DAYS.toMillis(1L);
            }
            case 'h': {
                return value * TimeUnit.HOURS.toMillis(1L);
            }
            case 'm': {
                return value * TimeUnit.MINUTES.toMillis(1L);
            }
            case 's': {
                return value * TimeUnit.SECONDS.toMillis(1L);
            }
            default: {
                return -1L;
            }
        }
    }
    
    public static class IntegerCountdown
    {
        public static String setFormat(final Integer value) {
            final int remainder = value * 1000;
            final int seconds = remainder / 1000 % 60;
            final int minutes = remainder / 60000 % 60;
            final int hours = remainder / 3600000 % 24;
            return String.valueOf((hours > 0) ? String.format("%02d:", hours) : "") + String.format("%02d:%02d", minutes, seconds);
        }
    }
    
    public static class LongCountdown
    {
        public static String setFormat(final Long value) {
            if (value < TimeUnit.MINUTES.toMillis(1L)) {
                return String.valueOf(TimeUtils.getDecimalFormat().format(value / 1000.0)) + "s";
            }
            return DurationFormatUtils.formatDuration((long)value, String.valueOf((value >= TimeUnit.HOURS.toMillis(1L)) ? "HH:" : "") + "mm:ss");
        }
    }
}
