package com.myfutureapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {

    public static String convertyyyymmddhhmmsstoddmmyyyy(String date) {

        String convertedDate = null;
        try {
            SimpleDateFormat inputDateFormaty = new SimpleDateFormat(AppConstants.DATEFORMATFROMSERVER,Locale.getDefault());
            inputDateFormaty.setTimeZone(TimeZone.getTimeZone("IST"));
            Date date1 = inputDateFormaty.parse(date);
            SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.DATE_FORMAT_DMY_SLASH,Locale.getDefault());
            convertedDate = sdf.format(date1);
        } catch (ParseException e) {

        }
        return convertedDate;
    }

    public static String convertDateIntoUtc(String day,String mnth,String year) {

        String convertedDate = null;
        try {
            SimpleDateFormat inputDateFormaty = new SimpleDateFormat(day+"-"+mnth+"-"+year);
            inputDateFormaty.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date1 = inputDateFormaty.parse(day+"-"+mnth+"-"+year);
            SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.DATE_FORMAT_DMY_SLASH,Locale.getDefault());
            convertedDate = sdf.format(date1);
        } catch (ParseException e) {

        }
        return convertedDate;
    }

    public static int yearCalender(long msecs){
        GregorianCalendar cal = new GregorianCalendar();

        cal.setTime(new Date(msecs));

        return cal.get(Calendar.YEAR);
    }
    public static String mnthName(long msecs) {
        GregorianCalendar cal = new GregorianCalendar();

        cal.setTime(new Date(msecs));

        int dow = cal.get(Calendar.MONTH);

        switch (dow) {
            case Calendar.JANUARY:
                return "Jan";
            case Calendar.FEBRUARY:
                return "Feb";
            case Calendar.MARCH:
                return "Mar";
            case Calendar.APRIL:
                return "Apr";
            case Calendar.MAY:
                return "May";
            case Calendar.JUNE:
                return "Jun";
            case Calendar.JULY:
                return "Jul";
            case Calendar.AUGUST:
                return "Aug";
            case Calendar.SEPTEMBER:
                return "Sept";
            case Calendar.OCTOBER:
                return "Oct";
            case Calendar.NOVEMBER:
                return "Nov";
            case Calendar.DECEMBER:
                return "Dec";
        }

        return "Unknown";
    }
}
