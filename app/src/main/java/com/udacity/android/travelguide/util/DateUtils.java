package com.udacity.android.travelguide.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat HEADER_FORMAT = new SimpleDateFormat("dd/MM/yyyy - E");

    public static String dateAsString(int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return FORMAT.format(cal.getTime());
    }

    public static String getDuration(String startDate, String endDate) {
        return String.format("%s - %s", startDate, endDate);
    }

    public static String dateAsString(String date) {
        try {
            Date parsedDate = FORMAT.parse(date);
            return HEADER_FORMAT.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Date fromString(String date) {
        try {
            return FORMAT.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }
}
