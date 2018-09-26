package com.giophub.commons.utils;

/**
 * @author Giovanni PERTA
 * @since initial version
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


public class TimeConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeConverter.class);

    private static final String MILLISEC = "ms";
    private static final String SECONDS  = "ss";
    private static final String MINUTES  = "mm";
    private static final String HOURS    = "hh";
    private static final String DAYS     = "dd";


    /**
     * Given an input time, the method returns the time converted in milli-seconds.
     *
     * @param timeExp
     * Accepted values are: {delay time} {format}. Where
     * {deley time} is an integer value.
     * {format} can be: ms for milli-seconds, ss for seconds, mm for minutes, hh for hours, dd for days.
     * Example: 30 ss, means a delay of 30 seconds.
     *
     * @return time value converted in milli-seconds.
     */
    public static long convert(String timeExp) {
        String unit = null;
        long time = 0L;
        try {
            unit = timeExp.substring(timeExp.length() - 2, timeExp.length()).trim();
            time = Long.valueOf(timeExp.substring(0, timeExp.length() - 2).trim());
            LOGGER.debug("Input value: {}. Parsed values: time: {}, unit: {}", timeExp, time, unit);
        } catch (Exception e) {
            LOGGER.error("Invalid UNIT TIME input parameter");
        }

        if      (unit.equalsIgnoreCase(SECONDS)) time = TimeUnit.SECONDS.toMillis(time);
        else if (unit.equalsIgnoreCase(MINUTES)) time = TimeUnit.MINUTES.toMillis(time);
        else if (unit.equalsIgnoreCase(HOURS))   time = TimeUnit.HOURS.toMillis(time);
        else if (unit.equalsIgnoreCase(DAYS))    time = TimeUnit.DAYS.toMillis(time);
        else if (unit.equalsIgnoreCase(MILLISEC))time = TimeUnit.MILLISECONDS.toMillis(time);
        LOGGER.debug("Conversion result: {} milli-sec", time);

        return time;
    }


    /*// test
    public static void main(String[] args) {
        System.out.println("converto to millisec = " + TimeConverter.convert("10 DD"));
    }*/
}
