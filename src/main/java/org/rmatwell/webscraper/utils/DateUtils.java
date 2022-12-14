package org.rmatwell.webscraper.utils;

import java.time.LocalDate;

/**
 * @author Richard Atwell
 */
public class DateUtils {

    public static String dateTodayToString(){
        LocalDate localDate = LocalDate.now();
        return localDate.toString();
    }
}
