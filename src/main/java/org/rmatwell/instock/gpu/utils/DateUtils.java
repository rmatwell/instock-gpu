package org.rmatwell.instock.gpu.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

/**
 * @author Richard Atwell
 */
public class DateUtils {

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    public static String dateTodayToString(){
        LocalDate localDate = LocalDate.now();
        return localDate.toString();
    }

}
