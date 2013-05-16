package com.googlecode.charpa.web.util;

import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.joda.time.Period;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Formats
 */
public class FormatUtils {
    /**
     * 01:02:03
     */
    private static PeriodFormatter PERIOD_FORMATTER = new PeriodFormatterBuilder()
            .printZeroRarelyFirst()
            .printZeroAlways()
            .minimumPrintedDigits(2)
            .appendHours()
            .appendSuffix(":")
            .appendMinutes()
            .appendSuffix(":")
            .appendSeconds()
            .toFormatter();

    /**
     * Formats period to 00:00:00
     * @param aPeriod period
     * @return formatted string
     */
    public static String formatPeriod(Period aPeriod) {
        return aPeriod!=null ? PERIOD_FORMATTER.print(aPeriod) : "-";
    }

    /**
     * Formats to yyyy.MM.dd HH:mm:ss
     * @param aDate date
     * @return formatted string
     */
    public static String formatDateTime(Date aDate) {
        final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        return aDate!=null ? dateTimeFormat.format(aDate) : "-";
    }
}
