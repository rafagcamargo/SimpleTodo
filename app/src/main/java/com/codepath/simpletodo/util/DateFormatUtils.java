package com.codepath.simpletodo.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatUtils {

    public static String getFormattedDate(Date date) {
        String format = "EEE, LLL dd, yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
        return simpleDateFormat.format(date);
    }

    public static String getFormattedDate(long milliseconds) {
        Date date = new Date(milliseconds);
        return getFormattedDate(date);
    }
}
